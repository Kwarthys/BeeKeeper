package com.beekeeper.network;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import com.beekeeper.controller.MainControllerServices;

public class NetworkManager {

	public final int TCPport = 4241;
	public final int UDPport = 4243;

	private ServerSocket serverTCPSocket;
	private DatagramSocket serverUDPSocket;
	
	private Thread tcpClientThread;

	private HashMap<InetAddress, UDPClientHandler> udpConnected = new HashMap<>();
	private ArrayList<TCPClientReceiverHandler> tcpHandlers = new ArrayList<>();

	private MainControllerServices controlerServices;
	
	private volatile boolean running = true;
	
	public void registerControllerServices(MainControllerServices services)
	{
		this.controlerServices = services;
	}
	
	public NetworkManager()
	{
		this(null);
	}

	public NetworkManager(MainControllerServices controlerServices)
	{
		this.controlerServices = controlerServices;
				
		try {
			serverTCPSocket = new ServerSocket(TCPport);
			serverTCPSocket.setSoTimeout(5000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			serverUDPSocket = new DatagramSocket(UDPport);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		openTCP();
	}

	public void createUDPClientHandler(InetAddress inetAddress)
	{
		System.out.println("UDP starting");
		if(!udpConnected.containsKey(inetAddress))
		{
			UDPClientHandler c = new UDPClientHandler(inetAddress, serverUDPSocket, controlerServices);
			Thread t = new Thread(c);
			udpConnected.put(inetAddress, c);
			t.setName("UDPSpammerFor" + inetAddress);
			t.start();
		}
		System.out.println("UDP Started");
	}

	public void closeUDPClientHandler(InetAddress inetAddress)
	{
		UDPClientHandler r = udpConnected.remove(inetAddress);

		if(r != null)
		{
			r.stop();
		}
	}

	public void openTCP()
	{
		System.out.println("creating TCP");
		tcpClientThread = new Thread(new Runnable() {			
			@Override
			public void run()
			{
				while(running)
				{
					try	{
						if(!serverTCPSocket.isClosed())
						{
							Socket client = serverTCPSocket.accept();
	
							Thread clientThread = new Thread(new TCPClientReceiverHandler(client, NetworkManager.this, controlerServices));
							//client.setSoTimeout(5000);
							clientThread.start();
						}

					} catch (SocketTimeoutException e) {
						//System.out.println("TimeOut");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		tcpClientThread.setName("TCPHandlerThread");
		tcpClientThread.start();
		System.out.println("TCP created");
	}
	
	public void registerTCPHandler(TCPClientReceiverHandler tcpc)
	{
		tcpHandlers.add(tcpc);
	}


	public void closing() {
		System.out.println("Closing servers");
		tcpHandlers.forEach((tcpc) -> tcpc.stop());
		running = false;
		for(UDPClientHandler udpc : udpConnected.values())
		{
			udpc.stop();
		}
		udpConnected.clear();
	}

}
