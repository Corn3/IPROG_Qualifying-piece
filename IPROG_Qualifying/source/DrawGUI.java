import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

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
	private static final int MAX_DATA_SIZE = 10;

	private String userName;

	private Color[] colors = new Color[13];
	private JButton[] colorButtons = new JButton[13];
	private CopyOnWriteArrayList<Point> points = new CopyOnWriteArrayList<Point>();

	private Color paintColor = Color.BLUE;

	private Client client;
	private DBHandler dbHandler = new DBHandler();

	private JLabel currentColorText = new JLabel("Current color: ");
	private JLabel currentPointSizeText = new JLabel("" + MIN_SLIDER);
	private JLayeredPane drawPanel = new JLayeredPane();
	private JPanel chatPanel = new JPanel();
	private JPanel utilPanel = new JPanel();
	private JPanel sizePanel = new JPanel();
	private JPanel colorPanel = new JPanel();
	private JPanel clearPanel = new JPanel();

	private JSlider sizeSlider = new JSlider(MIN_SLIDER, MAX_SLIDER, MIN_SLIDER);
	private int size = sizeSlider.getValue();

	private JTextArea chatArea = new JTextArea();
	private JTextField chatField = new JTextField();

	public DrawGUI(String userName, Client client) {
		super(PROGRAM_NAME);
		this.userName = userName;
		this.client = client;
		this.getRootPane().setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.blue));

		add(utilPanel, BorderLayout.NORTH);
		utilPanel.setLayout(new BorderLayout());
		utilPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH, UTIL_AREA_HEIGHT));
		addColorArea();
		addSizeArea();
		addClearArea();

		addDrawArea();

		addChatArea();

		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WinListener());
	}

	// Clear area
	private void addClearArea() {
		JButton clearButton = new JButton("Clear");
		utilPanel.add(clearPanel);
		clearPanel.add(clearButton);
		clearButton.addActionListener(new ClearListener());
	}

	// Size area
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

	// Color area
	private void addColorArea() {
		utilPanel.add(colorPanel, BorderLayout.WEST);
		currentColorText.setText("Selected Color: " + getColorName(paintColor));
		utilPanel.add(currentColorText);
		initColors();
		handleButtons();
	}

	// Draw area
	private void addDrawArea() {
		MouseListener me = new MouseListener();
		drawPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		drawPanel.setOpaque(true);
		drawPanel.setBackground(Color.WHITE);
		drawPanel.setLayout(null);
		drawPanel.addMouseListener(me);
		drawPanel.addMouseMotionListener(me);
		JScrollPane scroll = new JScrollPane(drawPanel);
		this.add(scroll);
	}

	// Chat area
	private void addChatArea() {
		chatPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH, CHAT_PANEL_HEIGHT));
		this.add(chatPanel, BorderLayout.SOUTH);
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
		chatArea.setPreferredSize(new Dimension(DEFAULT_WIDTH, CHAT_AREA_HEIGHT));
		chatArea.setEditable(false);
		chatField.addKeyListener(new ChatListener());
		JScrollPane scroll = new JScrollPane(chatArea);
		chatPanel.add(scroll);
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
		if (color == Color.BLACK)
			return "Black";
		else if (color == Color.BLUE)
			return "Blue";
		else if (color == Color.CYAN)
			return "Cyan";
		else if (color == Color.DARK_GRAY)
			return "Dark Gray";
		else if (color == Color.GRAY)
			return "Gray";
		else if (color == Color.GREEN)
			return "Green";
		else if (color == Color.LIGHT_GRAY)
			return "Light Gray";
		else if (color == Color.MAGENTA)
			return "Magenta";
		else if (color == Color.ORANGE)
			return "Orange";
		else if (color == Color.PINK)
			return "Pink";
		else if (color == Color.RED)
			return "Red";
		else if (color == Color.WHITE)
			return "White";
		else if (color == Color.YELLOW)
			return "Yellow";
		else {
			// Not implemented that color.
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
		if (drawPanel.getComponentAt(point.getX(), point.getY()) != null) {
			drawPanel.add(pointComp, 1);
		} else
			drawPanel.add(pointComp, 0);
		pointComp.repaint();
		pointComp.validate();
	}

	public void addChatMessage(String message) {
		chatArea.append(message + "\n");
	}

	public void clearScreen() {
		drawPanel.removeAll();
		repaint();
		validate();
	}

	public class ClearListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			client.sendClearArea();
			clearScreen();
		}
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
			if (ave.getSource() instanceof JButton) {
				JButton colorButton = (JButton) ave.getSource();
				paintColor = colorButton.getBackground();
				currentColorText.setText("Selected Color: " + getColorName(paintColor));
			}
		}

	}

	public class ChatListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent kev) {
			if (kev.getKeyCode() == KeyEvent.VK_ENTER) {
				String message = chatField.getText();
				if (message.equals(""))
					return;
				else
					message = userName + ": " + message;

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
			points.add(point);
			try {
				client.sendPoint(points);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void mouseDragged(MouseEvent me) {
			Point point = createPoint(me);
			points.add(point);
			try {
				if (points.size() == MAX_DATA_SIZE) {
					client.sendPoint(points);
					points.clear();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void mouseReleased(MouseEvent me) {
			try {
				client.sendPoint(points);
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

	public class WinListener extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			int confirm = JOptionPane.showOptionDialog(null, "Are you sure you wish to close the chat?",
					"Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (confirm == 0) {
				dbHandler.changeLogin(false, userName);
				client.sendMessage(userName + " has left the chatroom.");
				System.exit(0);
			}
		}
	}

}
