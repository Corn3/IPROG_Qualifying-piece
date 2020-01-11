import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

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
					
					Point point = convertToPoint(data);
					if(point == null) {
						String message = new String(data);
						client.addMessage(message);
					} else {
						client.convertData(point);
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Point convertToPoint(byte[] data) throws IOException, ClassNotFoundException {
		Point point = null;
		try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
				ObjectInputStream ois = new ObjectInputStream(bis);) {
			point = (Point)ois.readObject();
		} catch(java.io.EOFException eof) {
			return point;
		}
		return point;
	}
}
