import com.hc.framework.networking.listener.WorldListener;

public class Main {
	public static void main(String[] args) {
		WorldListener listener = new WorldListener(3724);
		listener.startListener();
	}
}
