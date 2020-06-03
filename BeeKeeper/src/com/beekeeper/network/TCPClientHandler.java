package com.beekeeper.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClientHandler implements Runnable {

	private Socket socket;
	private NetworkManager serverManager;
	
	private boolean connectionClosed = false;
	
	private PrintWriter writer;
	private BufferedInputStream reader;

	public TCPClientHandler(Socket client, NetworkManager serverManager) {
		socket = client;
		this.serverManager = serverManager;
	}

	@Override
	public void run() {

		while(!connectionClosed && !socket.isClosed())
		{
			try {
				
				writer = new PrintWriter(socket.getOutputStream());
				reader = new BufferedInputStream(socket.getInputStream());
				
				String request;
				try {
					request = read();
				} catch (Exception e) {
					//TODO make this better
					connectionClosed = true;
					e.printStackTrace();
					return;
				}
				
				switch(request)
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
						writer.write("FrUp");
						break;
						
					case "FrDOWN":
						System.out.println("Droping the frame");
						//serverManager.createUDPClientHandler(socket.getInetAddress());
						writer.write("FrDOWN");
						break;
						
					case "FrHIT":
						System.out.println("Hiting the frame");
						//serverManager.createUDPClientHandler(socket.getInetAddress());
						writer.write("FrHIT");
						break;
				}				
				
				System.out.println("Sending " + request);
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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

}

