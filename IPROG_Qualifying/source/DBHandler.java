import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class DBHandler {

	private static final String DEFAULT_DB = "db_19762992";
	private static final String DEFAULT_COMPUTER = "atlas.dsv.su.se";
	private static final String USR_NAME = "usr_19762992";
	private static final String USR_PWD = "762992";

	private SQLHandler sqlHandler = new SQLHandler();
	private Connection dbConnection;
	private Statement stmt = null;
	
	public boolean checkAvailability(boolean checkOnly, String userName) {
		try {
			connectToDB();
			stmt = (Statement) dbConnection.createStatement();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean exists = checkEntryFromDB(userName);
		
		if(checkOnly == false && exists == false) {
			addEntryToDB(userName);
		}
		return exists;
	}

	/**
	 * Establishes a connection to the given database by first getting the name and
	 * the host computer which it runs on. This information is then translated into
	 * an URL, which is used to connect to this address.
	 * 
	 * @throws InstantiationException
	 *             if this class fails to instantiate the connection to the middle
	 *             program.
	 * @throws IllegalAccessException
	 *             is thrown when this class doesn't have access to the definition
	 *             to the middle program.
	 * @throws ClassNotFoundException
	 *             is thrown when the class is not defined or found.
	 * @throws SQLException
	 *             thrown when this method is unable to establish a connection with
	 *             the database.
	 */
	private void connectToDB()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		String computer = DEFAULT_COMPUTER;
		String db_name = DEFAULT_DB;
		String url = "jdbc:mysql://" + computer + "/" + db_name;
		dbConnection = (Connection) DriverManager.getConnection(url, USR_NAME, USR_PWD);
	}
	
	private void addEntryToDB(String userName) {
		try {
			stmt.executeUpdate(sqlHandler.insertIntoDB(userName));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private boolean checkEntryFromDB(String userName) {
		try {
			ResultSet rs = (ResultSet) stmt.executeQuery(sqlHandler.checkForUserName(userName));
			if(rs.next())
				return true;
			else
				return false;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
		
	}

}
