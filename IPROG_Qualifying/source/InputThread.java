import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class InputThread extends Thread {

	private Boolean alive;
	private Socket socket;

	public InputThread(Socket socket) {
		this.socket = socket;
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
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
