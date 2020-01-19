import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class LoginForm extends JPanel {

	private JTextField userField = new JTextField(20);
	private JTextField passwordField = new JTextField(20);
	private JPanel[] rows = new JPanel[3];
	private JPanel cards;
	private DBHandler dbHandler = new DBHandler();
	private boolean exists = false;
	private LoginWindow loginWindow;

	public LoginForm(JPanel cards, LoginWindow loginWindow) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.cards = cards;
		this.loginWindow = loginWindow;

		for (int i = 0; i < 3; i++) {
			rows[i] = new JPanel();
		}

		rows[0].add(new JLabel("UserName: "));
		rows[0].add(userField);
		add(rows[0]);

		rows[1].add(new JLabel("Password: "));
		rows[1].add(passwordField);
		add(rows[1]);
		
		JButton loginButton = new JButton("Login");
		JButton newUserButton = new JButton("New User");
		rows[2].add(loginButton);
		rows[2].add(newUserButton);
		add(rows[2]);
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
			passwordField.setText("");
			CardLayout cl = (CardLayout) (cards.getLayout());
			cl.next(cards);
		}
	}

	private class LoginListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			String name = userField.getText();
			if(name.equals("")) {
				JOptionPane.showMessageDialog(LoginForm.this, "The entered username can't be empty.", "Empty username",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (passwordField.getText().equals("")) {
				JOptionPane.showMessageDialog(LoginForm.this, "The entered password can't be empty.", "Illegal password",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			exists = dbHandler.checkAvailability(true, name, passwordField.getText());
			if (exists == false) {
				JOptionPane.showMessageDialog(LoginForm.this, "The entered username doesn't exist.", "Wrong username",
						JOptionPane.ERROR_MESSAGE);
			} else {
				if(dbHandler.checkPassword(name, passwordField.getText()) == true) {
					dbHandler.changeLogin(true, userField.getText());
					loginWindow.setLoggedIn(true);
				} else {
					JOptionPane.showMessageDialog(LoginForm.this, "The entered password is wrong.", "Wrong password",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		}
	}

}
