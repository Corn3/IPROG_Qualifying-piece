import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.*;

public class DrawGUI extends JFrame {
	
	private static final int DEFAULT_WIDTH = 1500;
	private static final int DEFAULT_HEIGHT = 1000;
	private static final String PROGRAM_NAME = "Draw Client";
	
	private Client client;
	
	private JPanel drawPanel = new JPanel();
	
	public DrawGUI(Client client) {
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
	
	public class MouseListener extends MouseAdapter {
		
		@Override
		public void mouseDragged(MouseEvent me) {
			int x = me.getX();
			int y = me.getY();
			PointComponent pointComp = new PointComponent(x, y);
			drawPoint(pointComp);
			try {
				client.sendPoint(pointComp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void drawPoint(PointComponent pointComp) {
		drawPanel.add(pointComp);
		pointComp.repaint();
		pointComp.validate();
	}
	
}
