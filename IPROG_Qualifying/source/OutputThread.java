import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class OutputThread extends Thread {

	private boolean alive, awake;
	private Socket socket;
	private byte[] data = null;
	private boolean clear = false;

	public OutputThread(Socket socket) {
		this.socket = socket;
		alive = true;
		awake = true;
		start();
	}

	@Override
	public void run() {
		try {
			BufferedOutputStream buffer = new BufferedOutputStream(socket.getOutputStream());
			ObjectOutputStream output = new ObjectOutputStream(buffer);
			while (alive) {
				if (data == null && clear == true) {
					output.writeObject(clear);
					output.reset();
					output.flush();
					clear = false;
				}
				else if (data != null && clear != true) {
					// 2 seperate storage objects, one for text and one for drawing.
					Storage storage = new Storage(data);
					output.writeObject(storage);
					output.reset();
					output.flush();
					data = null;
				}
				Thread.sleep(50);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendData(boolean clear, byte[] data) {
		this.clear = clear;
		this.data = data;
	}

}
