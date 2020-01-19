import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class UserForm extends JPanel {

	private JTextField userField = new JTextField(20);
	private JTextField passwordField = new JTextField(20);
	private JPanel[] rows = new JPanel[3];
	private JPanel cards;
	private DBHandler dbHandler = new DBHandler();
	private boolean exists = true;
	private LoginWindow loginWindow;

	/**
	 * Constructs a panel used for the login window.
	 * <p>
	 * Adds a username part, a password part and 2 buttons for 
	 * logging in and swithing to the LoginForm.
	 * 
	 * @param cards the panel that holds this panel form together with
	 * LoginForm.
	 * @param loginWindow the window to hold this Class together with 
	 * LoginForm.
	 */
	public UserForm(JPanel cards, LoginWindow loginWindow) {
		this.loginWindow = loginWindow;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.cards = cards;

		for (int i = 0; i < 3; i++) {
			rows[i] = new JPanel();
		}

		rows[0].add(new JLabel("UserName: "));
		rows[0].add(userField);
		add(rows[0]);

		rows[1].add(new JLabel("Password: "));
		rows[1].add(passwordField);
		add(rows[1]);

		JButton createButton = new JButton("Create User");
		JButton previousButton = new JButton("<--");
		rows[2].add(createButton);
		rows[2].add(previousButton);
		add(rows[2]);
		createButton.addActionListener(new CreateListener());
		previousButton.addActionListener(new PreviousListener());
	}

	public String getUserName() {
		return userField.getText();
	}

	public boolean getExists() {
		return exists;
	}

	private class PreviousListener implements ActionListener {
		/**
		 * Triggers an event which causes any written fields in
		 * this form to be reset and then switches to the LoginForm.
		 * 
		 * @param ae the triggered event which occurs when the
		 * "<-" button is clicked.
		 */
		@Override
		public void actionPerformed(ActionEvent ae) {
			CardLayout cl = (CardLayout) (cards.getLayout());
			userField.setText("");
			passwordField.setText("");
			cl.previous(cards);
		}
	}

	private class CreateListener implements ActionListener {
		/**
		 * Triggers an event which causes the specified textfields to be converted into a username and password.
		 * <p>
		 * If either the username or password is empty an error message is displayed.
		 * If not then the availability for this given information is checked and logs the user in if the username was
		 * unique.
		 * 
		 * @param ae the triggered event which occurs when the "Create user" button is clicked.
		 */
		@Override
		public void actionPerformed(ActionEvent ae) {
			String name = null;
			if (userField.getText().equals("") || userField.getText().contains(":")) {
				JOptionPane.showMessageDialog(UserForm.this,
						"The entered username can't be empty or contains illegal characters (:).", "Illegal username",
						JOptionPane.ERROR_MESSAGE);
				return;
			} else {
				name = getUserName();
			}

			if (passwordField.getText().equals("")) {
				JOptionPane.showMessageDialog(UserForm.this, "The entered password can't be empty.", "Illegal password",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			exists = dbHandler.checkAvailability(false, name, passwordField.getText());
			if (exists == true) {
				JOptionPane.showMessageDialog(UserForm.this, "The entered username already exist.", "Username exist",
						JOptionPane.ERROR_MESSAGE);
			} else {
				dbHandler.changeLogin(true, userField.getText());
				loginWindow.setLoggedIn(true);
			}

		}
	}

}
