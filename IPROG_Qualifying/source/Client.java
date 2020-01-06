import java.io.IOException;
import java.net.Socket;

public class Client {
	private static final String DEFAULT_HOST = "atlas.dsv.su.se";
	private static final int DEFAULT_PORT = 4848;
	private OutputThread out = null;

	private void run(String host, int port) {
		try(Socket socket = new Socket(host, port)) {
			new DrawGUI(this);
			while (true) {}
		} catch (IOException ioe) {
			System.err.println("Couldn't connect to the specified socket.");
		}
	}

	public static void main(String[] args) {
		String host = DEFAULT_HOST;
		Integer port = DEFAULT_PORT;
		if (args.length > 0)
			host = args[0];
		if (args.length == 2)
			port = Integer.parseInt(args[1]);

		new Client().run(host, port);
	}
}
