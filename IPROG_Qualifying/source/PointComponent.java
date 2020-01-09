import java.awt.*;

import javax.swing.JComponent;


public class PointComponent extends JComponent {
	
	private static final int DEFAULT_SIZE = 10;
	private static final Color DEFAULT_COLOR = Color.BLUE;
	
	private int x;
	private int y;
	private Color color;
	private int height, width;
	
	public PointComponent(int x, int y) {
		this(x, y, DEFAULT_COLOR, DEFAULT_SIZE);
	}
	
	public PointComponent(int x, int y, int size) {
		this(x, y, DEFAULT_COLOR, size);
	}
	
	public PointComponent(int x, int y, Color color) {
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
	
	public PointComponent(int x, int y, Color color, int size) {
		this.x = x - (size / 2);
		this.y = y - (size / 2);
		this.color = color;
		this.height = size;
		this.width = size;
		int halfSize = size / 2;
		setBounds(x - halfSize, y - halfSize, size, size);
		setPreferredSize(new Dimension(size, size));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(color);
		g.fillOval(0, 0, width, height);
	}
	
}