import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
	private static final String DEFAULT_HOST = "atlas.dsv.su.se";
	private static final int DEFAULT_PORT = 4848;
	private OutputThread output = null;
	private InputThread input = null;
	private DrawGUI gui;

	private void run(String host, int port) {
		try (Socket socket = new Socket(host, port)) {
			gui = new DrawGUI(this);
			output = new OutputThread(socket);
			input = new InputThread(socket, this);
			while (true) {
			}
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

	public void convertData(Point point) {
		gui.drawPoint(point);
	}

	public void addMessage(String message) {
		gui.addChatMessage(message);
	}

	public void sendPoint(Point point) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(out);
		oos.writeObject(point);
		sendData(out.toByteArray());
	}

	public void sendMessage(String message) {
		sendData(message.getBytes());
	}

	private void sendData(byte[] data) {
		output.sendData(data);
	}
}
