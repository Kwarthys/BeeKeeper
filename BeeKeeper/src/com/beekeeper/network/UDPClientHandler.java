package com.beekeeper.network;

import java.awt.Point;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.agent.Agent;
import com.beekeeper.model.comb.Comb;
import com.beekeeper.model.comb.cell.CellContent;

public class UDPClientHandler implements Runnable {

	private volatile boolean running = true;

	private InetAddress inetAddress;
	private DatagramSocket udpServer;

	private int index = -1;

	private static final int maxAgentCount = 30000;
	private static final int sendRate = 100;

	private MainControllerServices services;

	public void stop()
	{
		running = false;
	}

	public UDPClientHandler(InetAddress inetAddress, DatagramSocket udpServer, MainControllerServices services) {
		this.inetAddress = inetAddress;
		this.udpServer = udpServer;
		this.services = services;
	}

	@Override
	public void run() {
		System.out.println("UDP Spam started to " + inetAddress);
		while(running)
		{
			try
			{
				/*** FRAMECONTENT ***/
				if(++index%20 == 0)
				{
					sendFramesContent(udpServer);
					index = 0;
				}
				
				/*** FORAGERS ***/
				sendForagerDatagram(udpServer);

				/*** FRAMES ***/
				sendAdultsDatagram(udpServer);


			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		


			try {
				Thread.sleep(sendRate);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("UDP Spam stopped to " + inetAddress);
	}

	private void sendForagerDatagram(DatagramSocket udpServer) throws IOException
	{
		ArrayList<Integer> foragers = services.getForagers();
		if(foragers.size() == 0)return;
		StringBuffer foragerData = new StringBuffer();
		foragerData.append("FORAGERS -1 ");
		foragerData.append(foragers.get(0));
		for(int i = 1; i < foragers.size(); ++i)
		{
			foragerData.append(" ");
			foragerData.append(foragers.get(i));
		}
		byte[] data = foragerData.toString().getBytes();
		System.out.println("Sending data:" + data.length + " || " + foragerData.toString());
		DatagramPacket p = new DatagramPacket(data, data.length, inetAddress, 4244);
		udpServer.send(p);
	}

	private void sendAdultsDatagram(DatagramSocket udpServer) throws IOException
	{
		ArrayList<Comb> combs = services.getCombs();
		for(Comb c : combs)
		{
			if(c.getAgents().size() != 0)
			{
				int pieces = ((c.getAgents().size()-1) / maxAgentCount) +1;
				System.out.println("Will have to send " + pieces + " piece(s)");
				for(int i = 0; i < pieces; ++i)
				{
					/* AGENTS */
					StringBuffer combBuffer = new StringBuffer();
					combBuffer.append("AGENTS ");
					combBuffer.append(c.ID);
					combBuffer.append(" ");
					boolean first = true;
					for(int ia = i*maxAgentCount; ia - i*maxAgentCount < maxAgentCount && ia - i*maxAgentCount < c.getAgents().size(); ++ia)
					{
						Agent a = c.getAgents().get(ia);
						Point point = a.getPosition();
						if(first)
						{
							first = false;
						}
						else
						{							
							combBuffer.append(" ");
						}
						combBuffer.append(a.getID());
						combBuffer.append(" ");
						combBuffer.append(point.x);
						combBuffer.append(" ");
						combBuffer.append(point.y);
					}

					byte[] data = combBuffer.toString().getBytes();
					System.out.println("Sending data:" + i + " " + data.length + " || " + combBuffer.toString());
					DatagramPacket p = new DatagramPacket(data, data.length, inetAddress, 4244);
					udpServer.send(p);
				}
			}
		}
	}

	private void sendFramesContent(DatagramSocket udpServer) throws IOException
	{
		ArrayList<Comb> combs = services.getCombs();
		for(Comb c : combs)
		{
			int combSize = c.getDimension().height * c.getDimension().width;

			StringBuffer combBuffer = new StringBuffer();
			combBuffer.append("CONTENT ");
			combBuffer.append(c.ID);

			for(int i = 0; i < combSize; ++i)
			{
				if(c.getCell(i).content != CellContent.empty)
				{
					combBuffer.append(" ");
					combBuffer.append(i);
					combBuffer.append(" ");
					int contentCode = getCodeForContent(c.getCell(i).content);
					combBuffer.append(contentCode);

					short nextData;

					if(c.getCell(i).content == CellContent.brood)
					{
						nextData = 150; //As old
					}
					else
					{
						nextData = 150; //As full
					}
					combBuffer.append(" ");
					combBuffer.append(nextData);
				}
			}

			byte[] data = combBuffer.toString().getBytes();
			System.out.println("Sending data:" + data.length + " || " + combBuffer.toString());
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

}

