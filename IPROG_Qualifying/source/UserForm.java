import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class UserForm extends JPanel {

	private JTextField userField = new JTextField(20);
	private JPanel[] rows = new JPanel[2];
	private JPanel cards;
	private DBHandler dbHandler = new DBHandler();
	private boolean exists = true;
	private LoginWindow loginWindow;

	public UserForm(JPanel cards, LoginWindow loginWindow) {
		this.loginWindow = loginWindow;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.cards = cards;

		for (int i = 0; i < 2; i++) {
			rows[i] = new JPanel();
		}

		rows[0].add(new JLabel("UserName: "));
		rows[0].add(userField);
		add(rows[0]);

		JButton createButton = new JButton("Create User");
		JButton previousButton = new JButton("<--");
		rows[1].add(createButton);
		rows[1].add(previousButton);
		add(rows[1]);
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
			cl.previous(cards);
		}
	}

	private class CreateListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			String name = null;
			if (userField.getText() == "") {
				JOptionPane.showMessageDialog(UserForm.this, "The entered userName can't be empty.", "Empty username",
						JOptionPane.ERROR_MESSAGE);
				return;
			} else {
				name = getUserName();
			}

			exists = dbHandler.checkAvailability(false, name);
			if (exists == true) {
				JOptionPane.showMessageDialog(UserForm.this, "The entered username already exist.", "Username exist",
						JOptionPane.ERROR_MESSAGE);
			} else {
				loginWindow.setLoggedIn(true);
			}

		}
	}

}
