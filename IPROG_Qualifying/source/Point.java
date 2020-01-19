import java.awt.Color;
import java.io.Serializable;
/**
 * Base class used for transferring data to other clients.
 * <p>
 * Can be seen as abit redundant with "PointComponent", but
 * was required to be used when sending data accross the socket.
 * 
 * @author Robin
 *
 */
public class Point implements Serializable {
	
	private static final int DEFAULT_SIZE = 10;
	private static final Color DEFAULT_COLOR = Color.BLUE;
	
	private int x;
	private int y;
	private Color color;
	private int height, width;
	
	public Point(int x, int y) {
		this(x, y, DEFAULT_COLOR, DEFAULT_SIZE);
	}
	
	public Point(int x, int y, int size) {
		this(x, y, DEFAULT_COLOR, size);
	}
	
	public Point(int x, int y, Color color) {
		this(x, y, color, DEFAULT_SIZE);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Color getColor() {
		return color;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public Point(int x, int y, Color color, int size) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.height = size;
		this.width = size;
	}

}
