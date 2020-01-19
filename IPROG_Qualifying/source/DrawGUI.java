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

	/**
	 * Constructs a new DrawGUI object that creates a new gui. Here a color area, size area,
	 * clear button, draw area and chat area gets added.
	 * 
	 * @param userName specifies the name of the user, using this client.
	 * @param client the client which this DrawGUI runs with.
	 */
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
	/**
	 * Adds a button to the utility panel, which allows for
	 * completely clearing the draw area.
	 * 
	 */
	private void addClearArea() {
		JButton clearButton = new JButton("Clear");
		utilPanel.add(clearPanel);
		clearPanel.add(clearButton);
		clearButton.addActionListener(new ClearListener());
	}

	// Size area
	/**
	 * Adds a size slider to the utility panel which allows
	 * for changing the size of the drawn point.
	 * 
	 */
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
	/**
	 * Adds a color bar that allows for changing the drawn points color.
	 * 
	 */
	private void addColorArea() {
		utilPanel.add(colorPanel, BorderLayout.WEST);
		initColors();
		currentColorText.setText("Selected Color: " + getColorName(paintColor));
		utilPanel.add(currentColorText);
		handleButtons();
	}

	// Draw area
	/**
	 * Adds a draw area to this window, which allows for adding components in the
	 * of points.
	 * <p>
	 * This draw area is added to a scroll, which allows for bigger draw areas.
	 * 
	 */
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
	/**
	 * Adds a chat area to this window, which allows for sending text messages to
	 * others connected to the connection.
	 * <p>
	 * The received messages are added to a chat area which displays all messages.
	 */
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

	/**
	 * Adds all predefined colors from the
	 * class {@link Color} to a data list
	 * for easier access, for other methods.
	 */
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

	/**
	 * Fetches the color name of the specified
	 * color.
	 * <p>
	 * If the color exists a String representing
	 * the color name is returned.
	 * 
	 * @param color the specified color.
	 * @return the name of the color.
	 */
	private String getColorName(Color color) {
		if (color == colors[0])
			return "Black";
		else if (color == colors[1])
			return "Blue";
		else if (color == colors[2])
			return "Cyan";
		else if (color == colors[3])
			return "Dark Gray";
		else if (color == colors[4])
			return "Gray";
		else if (color == colors[5])
			return "Green";
		else if (color == colors[6])
			return "Light Gray";
		else if (color == colors[7])
			return "Magenta";
		else if (color == colors[8])
			return "Orange";
		else if (color == colors[9])
			return "Pink";
		else if (color == colors[10])
			return "Red";
		else if (color == colors[11])
			return "White";
		else if (color == colors[12])
			return "Yellow";
		else {
			// Not implemented that color.
			return "No such color";
		}
	}

	/**
	 * Creates the settings for the color buttons, which
	 * the buttons are placed accordingly.
	 * 
	 */
	private void handleButtons() {
		for (int i = 0; i < colorButtons.length; i++) {
			initColorButton(i);
			setButtonColor(i);
			setButtonSize(i);
			addButtonListener(i);
			addButtonToPanel(i);
		}
	}

	/**
	 * Creates a new color button to be 
	 * placed.
	 * 
	 * @param i specifies the index.
	 */
	private void initColorButton(int i) {
		colorButtons[i] = new JButton();
	}

	/**
	 * Makes the the specified button intractable by adding a 
	 * actionlistener.
	 * 
	 * @param i specifies the index.
	 */
	private void addButtonListener(int i) {
		colorButtons[i].addActionListener(new ButtonListener());
	}

	/**
	 * Changes the size of the specified button to be that of a default value.
	 * 
	 * @param i specifies the index.
	 */
	private void setButtonSize(int i) {
		colorButtons[i].setPreferredSize(new Dimension(DEFAULT_BUTTON_WIDTH, DEFAULT_BUTTON_HEIGHT));
	}

	/**
	 * Changes the color of this specified button
	 * to be the same of the colors list.
	 * 
	 * @param i specifies the index.
	 */
	private void setButtonColor(int i) {
		colorButtons[i].setBackground(colors[i]);
		colorButtons[i].setForeground(colors[i]);
	}

	/**
	 * Adds the specified finished button
	 * to a color and displayed to the user.
	 * 
	 * @param i specifies the index.
	 */
	private void addButtonToPanel(int i) {
		colorPanel.add(colorButtons[i]);
	}

	/**
	 * Adds this specific point to the draw area, by first converting it into a component.
	 * <p>
	 * if a component already exists on the given coordinates, this component is added over it.
	 * 
	 * @param point the specific point to be added.
	 */
	public void drawPoint(Point point) {
		PointComponent pointComp = new PointComponent(point.getX(), point.getY(), point.getColor(), point.getHeight());
		if (drawPanel.getComponentAt(point.getX(), point.getY()) != null) {
			drawPanel.add(pointComp, 1);
		} else
			drawPanel.add(pointComp, 0);
		pointComp.repaint();
		pointComp.validate();
	}

	/**
	 * Prints out the text received from a user by 
	 * adding it to the chat area.
	 * 
	 * @param message the text to be added.
	 */
	public void addChatMessage(String message) {
		chatArea.append(message + "\n");
	}

	/**
	 * Removes all components from
	 * the draw area.
	 * 
	 */
	public void clearScreen() {
		drawPanel.removeAll();
		repaint();
		validate();
	}

	public class ClearListener implements ActionListener {
		/**
		 * Triggers an event which causes draw area to be cleared and sends the
		 * clear command to other clients.
		 * 
		 * @param ae the triggered event which occurs when clearbutton is pushed.
		 */
		@Override
		public void actionPerformed(ActionEvent ae) {
			client.sendClearArea();
			clearScreen();
		}
	}

	public class SlideListener implements ChangeListener {
		/**
		 * Triggers an event which causes the size of
		 * the drawn point to be changed.
		 * 
		 * @param che the triggered event which occurs
		 * when the size slider is changed.
		 */
		@Override
		public void stateChanged(ChangeEvent che) {
			size = sizeSlider.getValue();
			currentPointSizeText.setText("" + size);
		}
	}

	public class ButtonListener implements ActionListener {
		/**
		 * Triggers an event which causes the color of
		 * the drawn point to be changed.
		 * 
		 * @param ave the triggered event which occurs
		 * when one of the color buttons are clicked.
		 */
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
		/**
		 * Triggers an event which causes the typed text in
		 * the chat area to be sent to all connected clients.
		 * 
		 * @param kev the triggered event which occurs
		 * when the enter button is clicked.
		 */
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

		/**
		 * Triggers an event which causes a point component
		 * to be draw on the draw area, and sent to all
		 * connected clients.
		 * <p>
		 * Send 1 point to all connected clients.
		 * 
		 * @param me the triggered event which occurs
		 * when clicked on the draw area.
		 */
		@Override
		public void mouseClicked(MouseEvent me) {
			Point point = createPoint(me.getX(), me.getY());
			points.add(point);
			try {
				client.sendPoint(points);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Triggers an event which causes a point component
		 * to be draw on the draw area, and sent to all
		 * connected clients.
		 * <p>
		 * Sends a maximum specified amount of points
		 * to all connected clients.
		 * 
		 * @param me the triggered event which occurs
		 * when clicked on the draw area.
		 */
		@Override
		public void mouseDragged(MouseEvent me) {
			Point point = createPoint(me.getX(), me.getY());
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

		/**
		 * Triggers an event which causes a point component
		 * to be draw on the draw area, and sent to all
		 * connected clients.
		 * <p>
		 * The sent points equal to the current stored points.
		 * 
		 * @param me the triggered event which occurs
		 * when clicked on the draw area.
		 */
		@Override
		public void mouseReleased(MouseEvent me) {
			try {
				client.sendPoint(points);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Creates a new Point objects with the specified
		 * coordinates.
		 * 
		 * @param x one of the coordinates on the draw area.
		 * @param y one of the coordinates on the draw area.
		 * @return the point.
		 */
		private Point createPoint(int x, int y) {
			Point point = new Point(x, y, paintColor, size);
			drawPoint(point);
			return point;
		}
	}

	public class WinListener extends WindowAdapter {
		/**
		 * Specified what the window should do when the close button is pressed.
		 * <p>
		 * A window dialog is brought up for options that asks if the user want to quit.
		 * A message is sent to all connected clients that this client has disconnected and sets a logged off state
		 * in the database.
		 * 
		 */
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
