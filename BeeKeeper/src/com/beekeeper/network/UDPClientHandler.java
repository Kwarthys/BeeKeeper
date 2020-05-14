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

public class UDPClientHandler implements Runnable {

	private volatile boolean running = true;

	private InetAddress inetAddress;
	private DatagramSocket udpServer;
	
	//private int index = -1;

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
				//Data get format and send
				
				/*** FORAGERS ***/
				ArrayList<Integer> foragers = services.getForagers();
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
				
				/*** FRAMES ***/
				ArrayList<Comb> combs = services.getCombs();
				for(Comb c : combs)
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
						
						data = combBuffer.toString().getBytes();
						System.out.println("Sending data:" + i + " " + data.length + " || " + combBuffer.toString());
						p = new DatagramPacket(data, data.length, inetAddress, 4244);
						udpServer.send(p);
					}
				}
				
				/*
				int pieces = ((model.getSize()-1) / maxCubesSize) +1;
				System.out.println("Will have to send " + pieces + " piece(s)");
				for(int i = 0; i < pieces && running; ++i)
				{
					String sdata = i*maxCubesSize + " " + model.getPartialString(i*maxCubesSize, maxCubesSize);
					
					data = sdata.getBytes();
					System.out.println("Every " + sendRate + "ms. Sending data:" + i + " " + data.length + " || " + sdata);
					DatagramPacket p = new DatagramPacket(data, data.length, inetAddress, 4244);
					udpServer.send(p);
					//System.out.println("Sent");				
				}
				*/
				
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

}

