import java.awt.CardLayout;

import javax.swing.*;

public class LoginWindow extends JFrame {
	
	private LoginForm lf;
	private UserForm uf;
	private boolean loggedIn = false;
	
	public LoginWindow() {
		super("Login");
		
		JPanel cards = new JPanel(new CardLayout());
		lf = new LoginForm(cards, this);
		uf = new UserForm(cards, this);
		cards.add(lf);
		cards.add(uf);
		this.add(cards);
		
		setSize(400, 150);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public boolean loggedIn() {
		return loggedIn;
	}
	
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	
	public String getUserName() {
		String userName = null;
		if(!lf.getUserName().equals(""))
			userName = lf.getUserName();
		else if(!uf.getUserName().equals(""))
			userName = uf.getUserName();
		return userName;
			
	}
	
}
