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

	@Override
	public void run() {
		while (alive) {
			try {
				if (socket.getInputStream().available() != 0) {
					ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
					Storage storage = (Storage) in.readObject();
					byte[] data = storage.getData();
					CopyOnWriteArrayList<Point> points = convertToPoints(data);
					if(points == null) {
						String message = new String(data);
						client.addMessage(message);
					} else {
						client.convertData(points);
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private CopyOnWriteArrayList convertToPoints(byte[] data) throws IOException, ClassNotFoundException {
		CopyOnWriteArrayList<Point> points = null;
		try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
				ObjectInputStream ois = new ObjectInputStream(bis);) {
			points = (CopyOnWriteArrayList)ois.readObject();
		} catch(java.io.StreamCorruptedException sce) {
			return null;
		}
		catch(java.io.EOFException eof) {
			return null;
		}
		return points;
	}
}
