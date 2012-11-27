import com.hc.framework.networking.socket.WorldSocket;

public class Main {
	private static final String SERVERIP = "127.0.0.1";
	private static final int SERVERPORT = 3724;
	private static WorldSocket client;
	
	public static void main(String[] args) {
		client = new WorldSocket(SERVERIP, SERVERPORT, "CHAT");
		client.connect();
	}
}
