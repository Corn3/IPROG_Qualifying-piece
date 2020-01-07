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
					Object o = convertToObject(data);
					if (o.equals(null)) {

					} else {
						client.convertData(o);
					}
					// 2 seperate storage objects, one for text and one for drawing.
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Object convertToObject(byte[] data) throws IOException, ClassNotFoundException {
		Object o = null;
		try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
				ObjectInputStream ois = new ObjectInputStream(bis);) {
			o = ois.readObject();
		}
		return o;
	}
}
