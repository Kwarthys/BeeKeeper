package com.beekeeper.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import com.beekeeper.controller.MainControllerServices;

public class TCPClientReceiverHandler implements Runnable {

	private Socket socket;
	private NetworkManager serverManager;
	
	private boolean connectionClosed = false;
	
	private PrintWriter writer;
	private BufferedInputStream reader;

	private MainControllerServices services;

	public TCPClientReceiverHandler(Socket client, NetworkManager serverManager, MainControllerServices services) {
		socket = client;
		this.serverManager = serverManager;
		this.services = services;
	}

	@Override
	public void run() {

		while(!connectionClosed && !socket.isClosed())
		{
			try {
				
				writer = new PrintWriter(socket.getOutputStream());
				reader = new BufferedInputStream(socket.getInputStream());
				
				ClientRequest request;
				try {
					request = new ClientRequest(read());
					
					System.out.println("TREATING");
					
					switch(request.header)
					{
						case "STARTUDP":
							System.out.println("Creating udp");
							serverManager.createUDPClientHandler(socket.getInetAddress());
							writer.write("Started");
							break;
							
						case "CLOSE":
							connectionClosed = true;
							serverManager.closeUDPClientHandler(socket.getInetAddress());
							writer.write("ADIEU");
							break;
							
						case "FrUP":
							System.out.println("Lifting a frame");
							//serverManager.createUDPClientHandler(socket.getInetAddress());
							writer.write(request.data[0] + "UP");
							services.liftFrame(Integer.valueOf(request.data[0]));
							break;
							
						case "FrDOWN":
							System.out.println("Droping the frame");
							//serverManager.createUDPClientHandler(socket.getInetAddress());
							writer.write(request.data[0] + " DOWN at " + request.data[1]);
							services.putFrame(Integer.valueOf(request.data[0]), Integer.valueOf(request.data[1]), Boolean.valueOf(request.data[2]));
							break;
							
						case "FrHIT":
							System.out.println("Hiting the frame");
							//serverManager.createUDPClientHandler(socket.getInetAddress());
							writer.write("FrHIT");
							break;
							
						case "FFWD":
							System.out.println("Asking a fast forward");
							//serverManager.createUDPClientHandler(socket.getInetAddress());
							writer.write("FastForward" + request.data[0]);
							services.setNumberOfSecondsToGoFast(Integer.valueOf(request.data[0]));
							break;
							
						case "Deaths":
							ArrayList<Integer> deads = services.getTheDead();
							if(deads.size() > 0)
							{
								StringBuffer sb = new StringBuffer();
								sb.append("DEATHS -1 ");
								for(Integer id : deads)
								{
									sb.append(" ");
									sb.append(id);
								}
								System.out.println("sending " + sb.toString());
								writer.write(sb.toString());			
							}
							break;
							
						case "Contacts":
							StringBuffer sb = new StringBuffer();
							sb.append("CONTACTS -1");
							
							//long nano = System.nanoTime();
							
							HashMap<Integer, Integer> contactsQtt = services.getAgentContacts();
							
							for(Integer key : contactsQtt.keySet())
							{
								String futureString = " " + key + " " + contactsQtt.get(key);
								
								if(sb.length() + futureString.length() > 8192)//8192
								{
									while(sb.length() < 8192)
									{
										sb.append(" ");
									}
									//System.out.println("Sending " + sb.toString().substring(0, Math.min(sb.length(), 30)) + "length:" + sb.length());
									writer.write(sb.toString());
									sb = new StringBuffer();
									sb.append("CONTACTS -1");
								}
								
								sb.append(futureString);
							}
							
							//map has beel locked for less than 2ms
							services.freeLockAgentContacts();
							
							//System.out.println("Map got locked for " + (float)((System.nanoTime() - nano)/1000)/1000 + "ms.");
							
							writer.write(sb.toString());	
							//System.out.println("Sending " + sb.toString().substring(0, Math.min(sb.length(), 30)) + "length:" + sb.length());				
							
							
							break;
					}				
					
					System.out.println("replying to " + request.header);
					writer.flush();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("TCPDONE");
	}
	
	public void stop()
	{
		try {
			this.socket.close();
			System.out.println("TCP Dead");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String read() throws Exception{      
		String response = "";
		int stream;
		byte[] b = new byte[4096];
		stream = reader.read(b);
		response = new String(b, 0, stream);
		return response;
	}
	
	private class ClientRequest
	{
		String header = null;
		String[] data = null;
		
		public ClientRequest(String request)
		{
			header = request.split(" ")[0];
			if(header.compareTo(request) == 0)
			{
				return;
			}
			data = request.split(" ", 2)[1].split(" ");
		}
	}

}

