import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class LoginForm extends JPanel {

	private JTextField userField = new JTextField(20);
	private JPanel[] rows = new JPanel[2];
	private JPanel cards;
	private DBHandler dbHandler = new DBHandler();
	private boolean exists = false;
	private LoginWindow loginWindow;

	public LoginForm(JPanel cards, LoginWindow loginWindow) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.cards = cards;
		this.loginWindow = loginWindow;

		for (int i = 0; i < 2; i++) {
			rows[i] = new JPanel();
		}

		rows[0].add(new JLabel("UserName: "));
		rows[0].add(userField);
		add(rows[0]);

		JButton loginButton = new JButton("Login");
		JButton newUserButton = new JButton("New User");
		rows[1].add(loginButton);
		rows[1].add(newUserButton);
		add(rows[1]);
		loginButton.addActionListener(new LoginListener());
		newUserButton.addActionListener(new NextListener());
	}

	public String getUserName() {
		return userField.getText();
	}
	
	public boolean getExists() {
		return exists;
	}

	private class NextListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			userField.setText("");
			CardLayout cl = (CardLayout) (cards.getLayout());
			cl.next(cards);
		}
	}

	private class LoginListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			String name = userField.getText();
			if(name.equals("")) {
				JOptionPane.showMessageDialog(LoginForm.this, "The entered userName can't be empty.", "Empty username",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			// Check database for name.
			// if name does exist
			exists = dbHandler.checkAvailability(true, name);
			if (exists == false) {
				JOptionPane.showMessageDialog(LoginForm.this, "The entered username doesn't exist.", "Wrong username",
						JOptionPane.ERROR_MESSAGE);
			} else {
				loginWindow.setLoggedIn(true);
			}
		}
	}

}
