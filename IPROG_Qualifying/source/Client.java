import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class Client {
	private static final String DEFAULT_HOST = "atlas.dsv.su.se";
	private static final int DEFAULT_PORT = 4848;
	
	private String userName = "";
	
	private OutputThread output = null;
	private DrawGUI gui;

	private void run(String host, int port) {
		try (Socket socket = new Socket(host, port)) {
			LoginWindow loginWindow = new LoginWindow();
			while(userName.equals("")) {
				if(loginWindow.loggedIn() == false) {
					Thread.sleep(30);
				} else {
					userName = loginWindow.getUserName();
					loginWindow.dispose();
				}
			}
			gui = new DrawGUI(userName, this);
			output = new OutputThread(socket);
			new InputThread(socket, this);
			while (true) {
			}
		} catch (IOException ioe) {
			System.err.println("Couldn't connect to the specified socket.");
		} catch (InterruptedException e) {
			e.printStackTrace();
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

	public void convertData(CopyOnWriteArrayList<Point> points) {
		for(Point point : points)
			gui.drawPoint(point);
	}

	public void addMessage(String message) {
		if(message.contains(":")) {
			int i = message.indexOf(":");
			String sender = message.substring(0, i);
			if(sender.equals(userName))
				return;
		}
		gui.addChatMessage(message);
	}
	
	public void clearScreenData(boolean clear) {
		if(clear == true)
			gui.clearScreen();
	}

	public void sendPoint(CopyOnWriteArrayList<Point> points) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(out);
		oos.writeObject(points);
		output.sendData(false, out.toByteArray());
	}

	public void sendMessage(String message) {
		output.sendData(false, message.getBytes());
	}
	
	public void sendClearArea() {
		output.sendData(true, null);
	}
}
