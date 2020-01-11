import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class UserForm extends JPanel {

	private JTextField userField = new JTextField(20);
	private JPanel[] rows = new JPanel[2];
	private JPanel cards;

	public UserForm(JPanel cards) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.cards = cards;

		for (int i = 0; i < 2; i++) {
			rows[i] = new JPanel();
		}

		rows[0].add(new JLabel("UserName: "));
		rows[0].add(userField);
		add(rows[0]);

		JButton createButton = new JButton("Create User");
		JButton previousButton = new JButton("Login");
		rows[1].add(createButton);
		rows[1].add(previousButton);
		add(rows[1]);
		createButton.addActionListener(new CreateListener());
		previousButton.addActionListener(new PreviousListener());
	}

	public String getUserName() {
		return userField.getText();
	}

	private class PreviousListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			CardLayout cl = (CardLayout) (cards.getLayout());
			cl.previous(cards);
		}
	}
	
	private class CreateListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			if(userField.getText() != "") {
				String name = getUserName();
			}
			else {
				//Error message: empty userName.
			}

			//Check database for name.
			//if name does exist
			if(0 == 1) {
				//Error message: Name exists already.
			}
			else {
				//Create new user and login.
			}
				
		}
	}

}
