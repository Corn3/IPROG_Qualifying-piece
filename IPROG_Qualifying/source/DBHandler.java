
public class DBHandler {

	// INSERT
	public String insertIntoDB(String userName) {
		String record = "INSERT INTO userInformation (user_name) " + "VALUES (" + userName
				+ "')";
		return record;
	}
	
	// SELECT
	public String checkForUserName(String userName) {
		String record = 
				"SELECT userName" +
				"FROM userInformation" +
				"WHERE userName = user_name";
		return record;
	}

}
