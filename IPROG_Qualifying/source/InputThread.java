import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class InputThread extends Thread {

	private Boolean alive;
	private Socket socket;
	private Client client;

	public InputThread(Socket socket, Client client) {
		this.socket = socket;
		this.client = client;
		alive = true;
		start();
	}

	/**
	 * Overrides the superclass Threads run method. Runs this thread and then
	 * constantly listens for data from the connected server. When data is available
	 * this method opens a new ObjectInputStream and converts the data into a
	 * Storage object.
	 * <p>
	 * Depending on if the data recieved is a boolean, a text message or a Point object
	 * the data is passed on to the client in all cases to different methods.
	 * 
	 * @see Thread
	 */
	@Override
	public void run() {
		while (alive) {
			try {
				if (socket.getInputStream().available() != 0) {
					ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
					Object o = in.readObject();
					if (o instanceof Storage) {
						Storage storage = (Storage)o;
						byte[] data = storage.getData();
						CopyOnWriteArrayList<Point> points = convertToPoints(data);
						
						if (points != null) {
							client.convertData(points);
						} else {
							String message = new String(data);
							client.addMessage(message);
						}
					} else {
						boolean clear = (boolean)o;
						client.clearScreenData(clear);
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Tries to convert the given data into a Point object.
	 * <p>
	 * If any error occurs then the data is of type String and null is returned instead.
	 * 
	 * @param data the bytes to be converted.
	 * @return a Point object
	 * @throws IOException if the streams are unable to be created.
	 * @throws ClassNotFoundException if the given class doesn't exists.
	 */
	private CopyOnWriteArrayList convertToPoints(byte[] data) throws IOException, ClassNotFoundException {
		CopyOnWriteArrayList<Point> points = null;
		try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
				ObjectInputStream ois = new ObjectInputStream(bis);) {
			points = (CopyOnWriteArrayList) ois.readObject();
		} catch (java.io.StreamCorruptedException sce) {
			return null;
		} catch (java.io.EOFException eof) {
			return null;
		}
		return points;
	}
}
