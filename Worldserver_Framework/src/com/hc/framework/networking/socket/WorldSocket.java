package com.hc.framework.networking.socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import com.hc.framework.annotations.Command;
import com.hc.framework.commands.CommandMgr;
import com.hc.framework.logger.LogType;
import com.hc.framework.logger.Logger;

public class WorldSocket {
	private final String ip;
	private final int port;
	private Socket socket;
	private final String name;
	private boolean isConnected;
	private final CommandMgr commandMgr;
	
	public WorldSocket(String ip, int port, String name) {
		this.ip = ip;
		this.port = port;
		this.name = name;
		isConnected = false;
		
		// Initiate the commandMgr
		commandMgr = new CommandMgr(this, WorldSocket.class);
		commandMgr.loadChatCommands();
	}
	
	public void connect() {
		try {
			// Open socket.
			Logger.printMessage(LogType.CONSOLE_INFO, "Opening connection to: " + ip + ":" + port);
			socket = new Socket(ip, port);
			
			// Set that we are connected
			Logger.printMessage(LogType.CONSOLE_INFO, "Connected to : " + socket.getRemoteSocketAddress());
			isConnected = true;
			
			// Announce the server that we entered the pool.
			sendMessage("SocketName:" + name);
			sendMessage(name + " Entered the pool.");
		} catch (IOException ex) {
			Logger.printMessage(LogType.CONSOLE_INFO, "Could not connect to: " + ip + ":" + port);
		}
		
		// Start receiving messages from the server.
		receiveMessage();
	}
	
	private void receiveMessage() {
		while(isConnected) {
			try {
				InputStream inFromServer = socket.getInputStream();
				DataInputStream in = new DataInputStream(inFromServer);
				String input = in.readUTF();

				Logger.printMessage(LogType.CONSOLE_INFO, "Data received from server: " + input);
				
				// Manage the commands that we receive.
				commandMgr.parse(input);
			} catch (IOException ex) {
				Logger.printMessage(LogType.CONSOLE_INFO, "Could not accept packet data.");
			}
		}
	}
	
	public void sendMessage(String message) {
		try {
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF(message);
		} catch (IOException e) {
			Logger.printMessage(LogType.CONSOLE_ERROR, "Could not write the data to the remote socket : " + socket.getRemoteSocketAddress());
		}
	}
	
	@Command(name="close", description="Closes the socket")
	public void handleShutDown(String[] args) {
		isConnected = false;
	}

	public Socket getSocket() {
		return socket;
	}
}
