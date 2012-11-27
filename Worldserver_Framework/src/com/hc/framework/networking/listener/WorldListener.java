package com.hc.framework.networking.listener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import com.hc.framework.annotations.Command;
import com.hc.framework.commands.CommandMgr;
import com.hc.framework.logger.LogType;
import com.hc.framework.logger.Logger;
import com.hc.framework.utils.StringUtils;

public class WorldListener {
	private boolean isListening;
	private final int port;
	private ServerSocket worldserver;
	private final CommandMgr commandMgr;
	
	// Collection off all the client threads
	private final ArrayList<ClientThread> clientThreads;
	
	public WorldListener(int port) {
		commandMgr = new CommandMgr(this, WorldListener.class);
		commandMgr.loadChatCommands();
		
		isListening = false;
		this.port = port;
		clientThreads = new ArrayList<ClientThread>();
		
		try {
			worldserver = new ServerSocket(port);
		} catch (IOException e) {
			Logger.printMessage(LogType.CONSOLE_INFO, "Could not listen on port : " + port);
			System.exit(-1);
		}
		
		isListening = true;
	}
	
	public void startListener() {
		// Open new Class.
		Logger.printMessage(LogType.CONSOLE_INFO, "Waiting for the system components to enter.");
		Logger.printMessage(LogType.CONSOLE_COMMAND);
		waitForInput();
		waitForConnectionsOrData();
	}
	
	private void waitForInput() {
		new Thread() {
			@Override
			public void run() {
				Scanner scanner = new Scanner(System.in);
				String line;
				
				// Check for connections the whole time
				while((line = scanner.nextLine()) != null) {
					try {
						Thread.sleep(1);
						
						commandMgr.parse(line);
						Logger.printMessage(LogType.CONSOLE_COMMAND);
					} catch (InterruptedException e) {
						Logger.printMessage(LogType.CONSOLE_ERROR, "Socket sleep thread was interrupted.");
					}
				}
			}
		}.start();
	}
	
	private void waitForConnectionsOrData() {
		new Thread() {
			@Override
			public void run() {
				try {
					while(isListening)  {
						// Accept sockets
						Socket client = worldserver.accept();
						ClientThread thread = new ClientThread(client);
						addClientThread(thread);
						thread.start();
						// Start thread that accepts data from this socket
						//waitForData(client);
						Logger.printMessage(LogType.CONSOLE_INFO, "New socked opened from: " + client.getRemoteSocketAddress());
					}
				} catch (IOException e) {
					Logger.printMessage(LogType.CONSOLE_ERROR, "Could not accept incoming connection.");
				}
			}
		}.start();
	}
	
	public ServerSocket getServerSocket() {
		return worldserver;
	}
	
	public void addClientThread(ClientThread thread) {
		clientThreads.add(thread);
	}
	
	public void removeThread(ClientThread thread) {
		if (thread != null) {
			thread.sendMessage("close");
			thread.setIsStarted(false);
			clientThreads.remove(thread);
		} else {
			Logger.printMessage(LogType.CONSOLE_INFO, "Socket could not be removed.");
		}
	}
	
	public ClientThread findClientThreadByName(String name) {
		for (ClientThread c : clientThreads) {
			if (c.getSocketName().equals(name)) {
				return c;
			}
		}
		
		return null;
	}
	
	@Command(name="sendall", description="Sends a message to all the connected clients usage: sendall message")
	public void sendMessageToAllClients(String[] message) {
		for (ClientThread c : clientThreads) {
			c.sendMessage(StringUtils.joinArray(message));
		}
	}
	
	@Command(name="closeSocket", description="Closes the remote socket connections usage: closeSocket SOCKETNAME")
	public void closeSocket(String[] args) {
		ClientThread thread = findClientThreadByName(args[0]);
		removeThread(thread);
	}
	
	@Command(name="help", description="Displays help")
	public void handleHelp(String[] args) {
		Logger.printMessage(LogType.CONSOLE_INFO, commandMgr.getDescription(args[1]));
	}
	
	class ClientThread extends Thread {
		private final Socket socket;
		private String socketName;
		private boolean isRunning;
		
		public ClientThread(Socket socket) {
			this.socket = socket;
			socketName = "";
			isRunning = true;
		}
		
		@Override
		public void run() {
			try {
				InputStream inFromServer = socket.getInputStream();
				DataInputStream in = new DataInputStream(inFromServer);

				String message;
				// keep getting messages, also make sure the thread is started.
				while (((message = in.readUTF()) != null) && isRunning) {
					String args[] = message.split(":");
					if (args[0].equalsIgnoreCase("SocketName")) {
						socketName = args[1];
					}
					Logger.printMessage(LogType.CONSOLE_INFO, message);
				}
				
			} catch (IOException ex) {
				Logger.printMessage(LogType.CONSOLE_INFO, "Socket closed connection");
				
				// Delete this thread!
				removeThread(this);
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
		
		public void setIsStarted(boolean state) {
			isRunning = state;
		}
		
		public Socket getSocket() {
			return socket;
		}
		
		public String getSocketName() {
			return socketName;
		}
	}
}
