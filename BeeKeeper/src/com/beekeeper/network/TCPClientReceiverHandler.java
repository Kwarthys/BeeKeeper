package com.beekeeper.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.beekeeper.controller.MainControllerServices;
import com.beekeeper.model.comb.Comb;

public class TCPClientReceiverHandler implements Runnable {

	private Socket socket;
	private NetworkManager serverManager;
	
	private boolean connectionClosed = false;
	
	private PrintWriter writer;
	private BufferedInputStream reader;

	private volatile MainControllerServices services;

	public TCPClientReceiverHandler(Socket client, NetworkManager serverManager, MainControllerServices services) {
		socket = client;
		this.serverManager = serverManager;
		this.services = services;
		
		serverManager.registerTCPHandler(this);
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
					
					//System.out.println("TREATING");
					
					StringBuffer sb;
					
					switch(request.header)
					{
						case "STARTUDP":
							System.out.println("Creating udp");
							serverManager.createUDPClientHandler(socket.getInetAddress());
							sb = new StringBuffer();
							sb.append("Started -1");
							for(Comb c : services.getCombs())
							{
								sb.append(" ");
								sb.append(c.ID);
							}
							writer.write(sb.toString());
							break;
							
						case "CLOSE":
							connectionClosed = true;
							serverManager.closeUDPClientHandler(socket.getInetAddress());
							writer.write("ADIEU");
							break;
							
						case "RESTART":
							sb = new StringBuffer();
							sb.append("Restarted -1");
							for(Comb c : services.getCombs())
							{
								sb.append(" ");
								sb.append(c.ID);
							}
							writer.write(sb.toString());
							services.askRestart();
							break;
							
						case "FrUP":
							//System.out.println("Lifting a frame");
							writer.write("Frame " + request.data[0] + " UP");
							services.liftFrame(Integer.valueOf(request.data[0]));
							break;
							
						case "FrDOWN":
							//System.out.println("Droping the frame");
							writer.write(request.data[0] + " DOWN at " + request.data[1]);
							services.putFrame(Integer.valueOf(request.data[0]), Integer.valueOf(request.data[1]), Boolean.valueOf(request.data[2]));
							break;
							
						case "FrHIT":
							//System.out.println("Hiting the frame");
							writer.write("Frame " + request.data[0] + " hit");
							services.hitFrame(Integer.valueOf(request.data[0]));
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
								sb = new StringBuffer();
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
							
						default:
							writer.write(request.header + " unknown");
							break;
					}				
					
					System.out.println("replying to " + request.header);
					writer.flush();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			} catch (IOException e) {
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

	public void registerControllerServices(MainControllerServices services)
	{
		this.services = services;
	}

}

