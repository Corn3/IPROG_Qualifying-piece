import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class DrawGUI extends JFrame {
	
	private static final int DEFAULT_WIDTH = 1500;
	private static final int DEFAULT_HEIGHT = 1000;
	private static final String PROGRAM_NAME = "Draw Client";
	
	private DrawClient client;
	
	private JPanel drawPanel = new JPanel();
	
	public DrawGUI(DrawClient client) {
		super(PROGRAM_NAME);
		this.client = client;

		this.add(drawPanel);
		MouseListener me = new MouseListener();
		drawPanel.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		drawPanel.setBackground(Color.WHITE);
		drawPanel.setLayout(null);
		drawPanel.addMouseMotionListener(me);
		
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 * Paints the point on the given coordinates from
	 * point. Converts the coordinates from a double to an
	 * int that is used to create a component.
	 * 
	 * @param point the Point containing coordinates to be
	 * painted on screen.
	 */
	public void drawPoint(Point point) {
		int x = (int)point.getX();
		int y = (int)point.getY();
		PointComponent pointComp = new PointComponent(x, y);
		drawPanel.add(pointComp);
		pointComp.repaint();
		pointComp.validate();
	}
	
	public class MouseListener extends MouseAdapter {
		
		@Override
		public void mouseDragged(MouseEvent me) {
			int x = me.getX();
			int y = me.getY();
			PointComponent pointComp = new PointComponent(x, y);
			drawPanel.add(pointComp);
			pointComp.repaint();
			client.sendPoint(new Point(x, y));
		}
	}
	
}
