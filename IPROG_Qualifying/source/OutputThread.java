import java.io.*;
import java.net.Socket;

public class OutputThread extends Thread {

	private boolean alive = true;
	private Socket socket;
	
	public OutputThread(Socket socket) {
		this.socket = socket;
		start();
	}

	@Override
	public void run() {
		try {
			BufferedOutputStream buffer = new BufferedOutputStream(socket.getOutputStream());
			ObjectOutputStream output = new ObjectOutputStream(buffer);
			while (alive) {
				Storage storage = new Storage(bytes);
				output.reset();
				output.flush();
				Thread.sleep(50);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
