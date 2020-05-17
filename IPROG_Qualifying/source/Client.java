import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class Client {
	private static final String DEFAULT_HOST = ""; // Change this to a host name.
	private static final int DEFAULT_PORT = 0; // Change this to a port number.
	
	private String userName = "";
	
	private OutputThread output = null;
	private DrawGUI gui;

	/**
	 * Sets up the program, by starting off a socket connection
	 * to the specified host-port combination, and setting up
	 * inputthread and the gui.
	 * 
	 * 
	 * @param host the host name to connect to.
	 * @param port the host port to connect to.
	 */
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

	/**
	 * Passes all points to the gui to be drawn.
	 * 
	 * @param points the list of points to be drawn on this
	 * clients gui.
	 */
	public void convertData(CopyOnWriteArrayList<Point> points) {
		for(Point point : points)
			gui.drawPoint(point);
	}

	/**
	 * Passes the message on to the gui, to be displayed
	 * to this client.
	 * <p>
	 * Checks if the message retrieved is from this client,
	 * and disregards it if it is.
	 * 
	 * @param message the text to be displayed to this
	 * client.
	 */
	public void addMessage(String message) {
		if(message.contains(":")) {
			int i = message.indexOf(":");
			String sender = message.substring(0, i);
			if(sender.equals(userName))
				return;
		}
		gui.addChatMessage(message);
	}
	
	/**
	 * Passes the clear command to the gui if the
	 * boolean command is true.
	 * 
	 * @param clear the boolean command to allow
	 * complete clear of draw area.
	 */
	public void clearScreenData(boolean clear) {
		if(clear == true)
			gui.clearScreen();
	}

	/**
	 * Sends all points to all connected clients, by first converting the points
	 * to a byte array data.
	 * 
	 * @param points the list of points to be drawn on this
	 * clients gui.
	 */
	public void sendPoint(CopyOnWriteArrayList<Point> points) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(out);
		oos.writeObject(points);
		output.sendData(false, out.toByteArray());
	}

	/**
	 * Sends the text to all connected clients by
	 * passing it to a output thread.
	 * 
	 * @param message the text to be sent
	 */
	public void sendMessage(String message) {
		output.sendData(false, message.getBytes());
	}
	
	/**
	 * Sends the command to clear
	 * the draw area for all connected
	 * clients.
	 * 
	 */
	public void sendClearArea() {
		output.sendData(true, null);
	}
}
