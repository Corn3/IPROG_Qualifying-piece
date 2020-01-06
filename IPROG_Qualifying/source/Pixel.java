import java.awt.Color;

public class Pixel {
	private static final int DEFAULT_SIZE = 10;
	private static final Color DEFAULT_COLOR = Color.BLUE;
	
	private int x;
	private int y;
	private Color color;
	private int size;
	
	public Pixel(int x, int y, Color color, int size) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.size = size;
	}
	
	public Pixel(int x, int y) {
		this(x, y, DEFAULT_COLOR, DEFAULT_SIZE);
	}
	
	public Pixel(int x, int y, int size) {
		this(x, y, DEFAULT_COLOR, size);
	}
	
	public Pixel(int x, int y, Color color) {
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

	public int getSize() {
		return size;
	}
	
	

}
