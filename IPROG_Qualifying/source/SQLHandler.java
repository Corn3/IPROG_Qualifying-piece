
public class SQLHandler {

	// INSERT
	public String insertIntoDB(String userName, String index) {
		String record = "INSERT INTO userInformation (user_name, user_id) " + "VALUES ('" + userName
				+ "', '" + index + "')";
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

}
