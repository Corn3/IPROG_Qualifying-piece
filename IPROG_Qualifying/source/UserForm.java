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
		@Override
		public void actionPerformed(ActionEvent ae) {
			CardLayout cl = (CardLayout) (cards.getLayout());
			userField.setText("");
			passwordField.setText("");
			cl.previous(cards);
		}
	}

	private class CreateListener implements ActionListener {
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
