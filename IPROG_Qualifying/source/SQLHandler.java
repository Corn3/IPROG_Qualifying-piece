
public class SQLHandler {

	// INSERT
	public String insertIntoDB(String userName, String userPassword, String index) {
		String record = "INSERT INTO userInformation (user_name, user_password, user_id) " + "VALUES ('" + userName
				+ "', '" + userPassword + "', '" + index + "')";
		return record;
	}
	
	//CREATE UNIQUE INDEX
		public String newIndex() {
			String index = "SELECT COUNT(*)"
					+ " FROM userInformation";
			return index;
		}
	
	// SELECT
	public String checkForUserName(String userName) {
		String record = 
				"SELECT user_name " +
				"FROM userInformation " + 
				"WHERE user_name = '" + userName + "'";
		return record;
	}
	
	//UPDATE login status
	public String setLoggedIn(boolean loggedIn, String userName) {
		String record = 
				"UPDATE userInformation "
				+ "set user_logged_in = " + loggedIn +
				" WHERE user_name = '" + userName + "'";
		return record;
	}
	
	//Check correct password
	public String checkForCorrectPassword(String userName, String password){
		String record = 
				"SELECT user_name " +
				"FROM userInformation " + 
				"WHERE user_name = '" + userName + "' AND user_password = '" + password + "'";
		return record;
	}

}
