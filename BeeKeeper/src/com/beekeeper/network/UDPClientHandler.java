package com.beekeeper.network;

import java.awt.Point;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.agent.AgentStateSnapshot;
import com.beekeeper.model.agent.AgentType;
import com.beekeeper.model.comb.Comb;
import com.beekeeper.model.comb.cell.CellContent;
import com.beekeeper.network.netutils.NetBalancer;
import com.beekeeper.network.netutils.NetBalancerCallBack;
import com.beekeeper.parameters.ModelParameters;

public class UDPClientHandler implements Runnable {

	private volatile boolean running = true;

	private InetAddress inetAddress;
	//private DatagramSocket udpServer;
	private static final int maxAgentCount = 10000;
	private static final int sendRate = 100;
	
	private ArrayList<NetBalancer> functions = new ArrayList<>();
	
	private DecimalFormat format = new DecimalFormat("#.###");
	
	private volatile MainControllerServices services;

	public void stop()
	{
		running = false;
	}

	public UDPClientHandler(InetAddress inetAddress, DatagramSocket udpServer, MainControllerServices services) {
		this.inetAddress = inetAddress;
		//this.udpServer = udpServer;
		this.services = services;
		
		functions.add(new NetBalancer(new NetBalancerCallBack() {
			
			@Override
			public void call() {
				try {
					sendForagerDatagram(udpServer);
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
		}, 1,0,true));
		
		functions.add(new NetBalancer(new NetBalancerCallBack() {
			
			@Override
			public void call() {
				try {
					sendAdultsPositions(udpServer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 1,0,true));
		
		functions.add(new NetBalancer(new NetBalancerCallBack() {
			
			@Override
			public void call() {
				try {
					sendAdultStates(udpServer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 3,0,false));
		
		functions.add(new NetBalancer(new NetBalancerCallBack() {
			
			@Override
			public void call() {
				try {
					sendFramesContent(udpServer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 3,1,true));
		
		/*
		functions.add(new NetBalancer(new NetBalancerCallBack() {
			
			@Override
			public void call() {
				try {
					sendAgentContacts(udpServer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 3,2, false));
		*/
	}

	@Override
	public void run() {
		System.out.println("UDP Spam started to " + inetAddress);
		while(running)
		{
			services.waitForTimeStep();
			
			boolean ff = services.isFastForward();
			
			for(int i = 0; i < functions.size(); ++i)
			{
				NetBalancer n = functions.get(i);
				if(!(ff && n.affectedByCondition))
				{
					n.call();
				}

				try {
					Thread.sleep(sendRate);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
		}
		System.out.println("UDP Spam stopped to " + inetAddress);
	}

	private void sendForagerDatagram(DatagramSocket udpServer) throws IOException
	{
		ArrayList<Integer> foragers = services.getForagers();
		if(foragers.size() == 0)
		{
			//System.out.println("noForagers to send");
			return;
		}
		StringBuffer foragerData = new StringBuffer();
		foragerData.append("FORAGERS -1 ");
		foragerData.append(foragers.get(0));
		for(int i = 1; i < foragers.size(); ++i)
		{
			foragerData.append(" ");
			foragerData.append(foragers.get(i));
		}
		byte[] data = foragerData.toString().getBytes();
		//System.out.println("Sending data:" + data.length + " || " + foragerData.toString());
		DatagramPacket p = new DatagramPacket(data, data.length, inetAddress, 4244);
		udpServer.send(p);
	}

	private void sendAdultsPositions(DatagramSocket udpServer) throws IOException
	{
		ArrayList<Comb> combs = services.getCombs();
		/*
		for(Comb c : combs)
		{
			MyUtils.showList(c.getAgents());
		}
		 */
		for(Comb c : combs)
		{
			if(c.getAgents().size() != 0)
			{
				int pieces = ((c.getAgents().size()-1) / maxAgentCount) +1;
				//System.out.println("Will have to send " + pieces + " piece(s)");
				for(int i = 0; i < pieces; ++i)
				{
					/* AGENTS */
					StringBuffer combBuffer = new StringBuffer();
					combBuffer.append("AGENTS ");
					combBuffer.append(c.ID);
					//System.out.println("Working on agents from C" + c.ID);
					for(int ia = i*maxAgentCount; ia - i*maxAgentCount < maxAgentCount && ia - i*maxAgentCount < c.getAgents().size(); ++ia)
					{

						Agent a = c.getAgents().get(ia);
						if(a.getBeeType() != AgentType.BROOD_BEE)
						{
							/*
							if(a.getPosition() == null)
							{
								System.out.println(a.getStringName() + " detected on C" + c.ID + " on null doing " + ((WorkingAgent)a).getTaskName() + " isInside? " + a.isInside());
							}
							 */
							Point point = a.getPosition();
							combBuffer.append(" ");
							combBuffer.append(a.getID());
							combBuffer.append(" ");
							combBuffer.append(point.x);
							combBuffer.append(" ");
							combBuffer.append(point.y);
						}
					}

					byte[] data = combBuffer.toString().getBytes();
					//System.out.println("Sending data:" + i + " " + data.length + " || " + combBuffer.toString());
					DatagramPacket p = new DatagramPacket(data, data.length, inetAddress, 4244);
					udpServer.send(p);
				}
			}
		}
	}

	private void sendAdultStates(DatagramSocket udpServer) throws IOException
	{
		int maxAgentCount = 2000;

		ArrayList<AgentStateSnapshot> agents = services.getAllAdults();

		int numberOfChunks = ((agents.size()-1) / maxAgentCount) +1;

		//System.out.println("will send " + numberOfChunks + " chunk(s) " + agents.size());

		int lastLoopIndex = 0;

		for(int chunk = 0; chunk < numberOfChunks; chunk++)
		{
			StringBuffer combBuffer = new StringBuffer();
			combBuffer.append("STATES ");
			combBuffer.append(services.getCurrentTimeStep());

			int iagent;

			for(iagent = lastLoopIndex; iagent - lastLoopIndex < maxAgentCount && iagent < agents.size(); ++iagent)
			{
				AgentStateSnapshot snap = agents.get(iagent);
				combBuffer.append(" ");
				combBuffer.append(snap.agentID);
				combBuffer.append(" ");
				combBuffer.append((int)(snap.agentAge / ModelParameters.secondToTimeStepCoef));
				combBuffer.append(" ");
				combBuffer.append(((int)(snap.jhAmount * 100)) / 100.0);
				combBuffer.append(" ");
				combBuffer.append(format.format(snap.exchangedPheromones));
				combBuffer.append(" ");
				combBuffer.append(snap.taskName);
			}

			lastLoopIndex = iagent;
			//System.out.println("lastLoopIndex " + lastLoopIndex + " " + agents.size() + " " + maxAgentCount);

			byte[] data = combBuffer.toString().getBytes();
			//System.out.println("Sending data:" + chunk + " " + data.length + " || " + combBuffer.toString().substring(0, Math.min(combBuffer.length(), 50)) + "...");
			DatagramPacket p = new DatagramPacket(data, data.length, inetAddress, 4244);
			udpServer.send(p);
		}
	}

	/* MOVED IN AGENTSTATES
	private void sendAgentContacts(DatagramSocket udpServer) throws IOException
	{
		StringBuffer sb = new StringBuffer();
		sb.append("CONTACTS -1");

		
		//long nano = System.nanoTime();
		HashMap<Integer, Double> contactsQtt = services.getAgentContacts();

		for(Integer key : contactsQtt.keySet())
		{
			//System.out.println(contactsQtt.get(key) + " -> " + format.format(contactsQtt.get(key)));
			String futureString = " " + key + " " + format.format(contactsQtt.get(key));

			if(sb.length() + futureString.length() > 20000)
			{
				byte[] data = sb.toString().getBytes();
				//System.out.println("Sending data:" + data.length + " || " + sb.toString());
				DatagramPacket p = new DatagramPacket(data, data.length, inetAddress, 4244);
				udpServer.send(p);

				sb = new StringBuffer();
				sb.append("CONTACTS -1");
			}

			sb.append(futureString);
		}

		//map has been locked for less than 2ms
		services.freeLockAgentContacts();
		//System.out.println("Map got locked for " + (float)((System.nanoTime() - nano)/1000)/1000 + "ms.");
		
		
		
		
		
		

		byte[] data = sb.toString().getBytes();
		//System.out.println("Sending data:" + data.length + " || " + sb.toString());
		DatagramPacket p = new DatagramPacket(data, data.length, inetAddress, 4244);
		udpServer.send(p);

	}
	*/
	
	
	private void sendFramesContent(DatagramSocket udpServer) throws IOException
	{
		ArrayList<Comb> combs = services.getCombs();
		for(Comb c : combs)
		{
			int combSize = c.getDimension().height * c.getDimension().width;

			StringBuffer combBuffer = new StringBuffer();
			combBuffer.append("CONTENT ");
			combBuffer.append(c.ID);
			
			int cellSent = 0;

			for(int i = 0; i < combSize; ++i)
			{
				if(c.getCell(i).content != CellContent.empty)
				{
					combBuffer.append(" ");
					combBuffer.append(i);
					combBuffer.append(" ");
					int contentCode = getCodeForContent(c.getCell(i).content);
					combBuffer.append(contentCode);

					int nextData;

					if(c.getCell(i).content == CellContent.brood)
					{
						nextData = (int)((float)((c.getCell(i).inside.getRealAge() * 1.0 / ModelParameters.timestepLarvaPop)) * 255);
					}
					else
					{
						nextData = 200; //As near full
					}
					combBuffer.append(" ");
					combBuffer.append(nextData);
					
					cellSent++;
				}
			}
			
			if(cellSent == 0)
			{
				combBuffer.append(" -1");
				//System.out.println("Empty comb detected");
			}

			byte[] data = combBuffer.toString().getBytes();
			//System.out.println("Sending data:" + data.length + " || " + combBuffer.toString());
			DatagramPacket p = new DatagramPacket(data, data.length, inetAddress, 4244);
			udpServer.send(p);
		}
	}

	private int getCodeForContent(CellContent c)
	{
		switch(c)
		{
		case food:
			return 0;
		case brood:
			return 1;
		case empty:
		default:
			return -1;
		}
	}

	public void registerControllerServices(MainControllerServices services)
	{
		this.services = services;
	}

}

