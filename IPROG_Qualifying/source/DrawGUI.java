import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DrawGUI extends JFrame {

	private static final int DEFAULT_WIDTH = 1500;
	private static final int DEFAULT_HEIGHT = 1000;
	private static final String PROGRAM_NAME = "Draw Client";
	private static final int DEFAULT_BUTTON_WIDTH = 30;
	private static final int DEFAULT_BUTTON_HEIGHT = 25;
	private static final int UTIL_AREA_HEIGHT = DEFAULT_HEIGHT / 30;
	private static final int CHAT_PANEL_HEIGHT = DEFAULT_WIDTH / 5;
	private static final int CHAT_AREA_HEIGHT = (CHAT_PANEL_HEIGHT / 2) + (CHAT_PANEL_HEIGHT / 3);
	private static final int MAX_SLIDER = 200;
	private static final int MIN_SLIDER = 5;
	
	private Color[] colors = new Color[13];
	private JButton[] colorButtons = new JButton[13];
	private Color paintColor = Color.BLUE;

	private Client client;
	
	private JLabel currentColorText = new JLabel("Current color: ");
	private JLabel currentPointSizeText = new JLabel("" + MIN_SLIDER);

	private JPanel drawPanel = new JPanel();
	private JPanel chatPanel = new JPanel();
	private JPanel utilPanel = new JPanel();
	private JPanel sizePanel = new JPanel();
	private JPanel colorPanel = new JPanel();
	
	private JSlider sizeSlider = new JSlider(MIN_SLIDER, MAX_SLIDER, MIN_SLIDER);
	private int size = sizeSlider.getValue();
	
	private JTextArea chatArea = new JTextArea();
	private JTextField chatField = new JTextField();

	public DrawGUI(Client client) {
		super(PROGRAM_NAME);
		this.client = client;
		this.getRootPane().setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.blue));
		
		add(utilPanel, BorderLayout.NORTH);
		utilPanel.setLayout(new BorderLayout());
		utilPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH, UTIL_AREA_HEIGHT));
		addColorArea();
		addSizeArea();
		
		addDrawArea();
		
		addChatArea();

		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void addSizeArea() {
		utilPanel.add(sizePanel, BorderLayout.EAST);
		sizePanel.add(currentPointSizeText);
		sizeSlider.setMinorTickSpacing(5);
		sizeSlider.setMajorTickSpacing(10);
		sizeSlider.setPaintTicks(true);
		sizePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		sizePanel.add(sizeSlider);
		sizeSlider.addChangeListener(new SlideListener());
		
	}
	
	private void addColorArea() {
		utilPanel.add(colorPanel, BorderLayout.WEST);
		currentColorText.setText("Selected Color: " + getColorName(paintColor));
		utilPanel.add(currentColorText);
		initColors();
		handleButtons();
	}
	
	private void addDrawArea() {
		MouseListener me = new MouseListener();
		drawPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		drawPanel.setBackground(Color.WHITE);
		drawPanel.setLayout(null);
		drawPanel.addMouseMotionListener(me);
		JScrollPane scroll = new JScrollPane(drawPanel);
		this.add(scroll);
	}
	
	private void addChatArea() {
		chatPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH, CHAT_PANEL_HEIGHT));
		this.add(chatPanel, BorderLayout.SOUTH);
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
		chatArea.setPreferredSize(new Dimension(DEFAULT_WIDTH, CHAT_AREA_HEIGHT));
		chatArea.setEditable(false);
		chatField.addKeyListener(new ChatListener());
		chatPanel.add(chatArea);
		chatPanel.add(chatField);
	}

	private void initColors() {
		colors[0] = Color.BLACK;
		colors[1] = Color.BLUE;
		colors[2] = Color.CYAN;
		colors[3] = Color.DARK_GRAY;
		colors[4] = Color.GRAY;
		colors[5] = Color.GREEN;
		colors[6] = Color.LIGHT_GRAY;
		colors[7] = Color.MAGENTA;
		colors[8] = Color.ORANGE;
		colors[9] = Color.PINK;
		colors[10] = Color.RED;
		colors[11] = Color.WHITE;
		colors[12] = Color.YELLOW;
	}
	
	private String getColorName(Color color) {
		if(color == Color.BLACK)
			return "Black";
		else if(color == Color.BLUE)
			return "Blue";
		else if(color == Color.CYAN)
			return "Cyan";
		else if(color == Color.DARK_GRAY)
			return "Dark Gray";
		else if(color == Color.GRAY)
			return "Gray";
		else if(color == Color.GREEN)
			return "Green";
		else if(color == Color.LIGHT_GRAY)
			return "Light Gray";
		else if(color == Color.MAGENTA)
			return "Magenta";
		else if(color == Color.ORANGE)
			return "Orange";
		else if(color == Color.PINK)
			return "Pink";
		else if(color == Color.RED)
			return "Red";
		else if(color == Color.WHITE)
			return "White";
		else if(color == Color.YELLOW)
			return "Yellow";
		else {
			//Not implemented that color.
			return "No such color";
		}
	}

	private void handleButtons() {
		for (int i = 0; i < colorButtons.length; i++) {
			initColorButton(i);
			setButtonColor(i);
			setButtonSize(i);
			addButtonListener(i);
			addButtonToPanel(i);
		}
	}

	private void initColorButton(int i) {
		colorButtons[i] = new JButton();
	}
	
	private void addButtonListener(int i) {
		colorButtons[i].addActionListener(new ButtonListener());
	}
	
	private void setButtonSize(int i) {
		colorButtons[i].setPreferredSize(new Dimension(DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT));
	}

	private void setButtonColor(int i) {
		colorButtons[i].setBackground(colors[i]);
		colorButtons[i].setForeground(colors[i]);
	}

	private void addButtonToPanel(int i) {
		colorPanel.add(colorButtons[i]);
	}

	

	public void drawPoint(Point point) {
		PointComponent pointComp = new PointComponent(point.getX(), point.getY(), point.getColor(), point.getHeight());
		drawPanel.add(pointComp);
		pointComp.repaint();
		pointComp.validate();
	}
	
	public void addChatMessage(String message) {
		chatArea.append(message + "\n");
	}
	
	public class SlideListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent che) {
			size = sizeSlider.getValue();
			currentPointSizeText.setText("" + size);
		}
	}
	
	public class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ave) {
			if(ave.getSource() instanceof JButton) {
				JButton colorButton = (JButton)ave.getSource();
				paintColor = colorButton.getBackground();
				currentColorText.setText("Selected Color: " + getColorName(paintColor));
			}
		}
		
	}
	
	public class ChatListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent kev) {
			if(kev.getKeyCode() == KeyEvent.VK_ENTER) {
				String message = chatField.getText();
				addChatMessage(message);
				chatField.setText("");
				client.sendMessage(message);
			}
		}
		
	}
	
	public class MouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent me) {
			Point point = createPoint(me);
			try {
				client.sendPoint(point);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void mouseDragged(MouseEvent me) {
			Point point = createPoint(me);
			try {
				client.sendPoint(point);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private Point createPoint(MouseEvent me) {
			int x = me.getX();
			int y = me.getY();
			Point point = new Point(x, y, paintColor, size);
			drawPoint(point);
			return point;
		}
	}

}
