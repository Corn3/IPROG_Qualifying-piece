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

	/**
	 * Checks if the specified name is available or not in the given database and returns the case if it exists
	 * or not.
	 * <p>
	 * Attempts to start a connection with this database and if any error occurs, a exception will be caught.
	 * 
	 * 
	 * @param checkOnly specifies if the database should only be checked for the name or if the name should be 
	 * added aswell.
	 * @param userName the name to be checked for and added if checkOnly is marked false.
	 * @param userPassword the password to be added if checkOnly marked false.
	 * @return if the name exists or not.
	 */
	public boolean checkAvailability(boolean checkOnly, String userName, String userPassword) {
		try {
			connectToDB();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		boolean exists = checkEntryFromDB(userName);

		if (checkOnly == false && exists == false) {
			addEntryToDB(userName, userPassword);
		}
		return exists;
	}

	/**
	 * Checks if the specified password is correct for the given username.
	 * <p>
	 * If the username and password combo exists then true is returned, for all other cases false is returned.
	 * 
	 * @param userName the name to be checked for.
	 * @param password the password to be checked for.
	 * @return a boolean which indicates if the combination exists.
	 */
	public boolean checkPassword(String userName, String password) {
		try {
			ResultSet rs = (ResultSet) stmt.executeQuery(sqlHandler.checkForCorrectPassword(userName, password));
			if (rs.next())
				return true;
			else
				return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Creates a new index for the user entry in the database, by executing the SQL
	 * command returned from the Database handler.
	 * <p>
	 * If the command succeeds a index String is returned, if unsuccessful this
	 * method throws a SQLException.
	 * 
	 * @return a String representing the user id for the database
	 * @throws SQLException
	 *             is thrown if it fails to execute the given index query.
	 */
	private String getNewId() throws SQLException {
		ResultSet rs = (ResultSet) stmt.executeQuery(sqlHandler.newIndex());
		rs.next();
		return rs.getString(1);

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
		stmt = (Statement) dbConnection.createStatement();
	}

	/**
	 * Pushes the name and password combination together with a new unique ID to the database
	 * for adding.
	 * <p>
	 * If an error occurs a SQLException is caught and none of the data is added.
	 * 
	 * @param userName the name to be added.
	 * @param userPassword the password to be added.
	 */
	private void addEntryToDB(String userName, String userPassword) {
		try {
			stmt.executeUpdate(sqlHandler.insertIntoDB(userName, userPassword, getNewId()));
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Checks for the existant of the specified user, by calling an sql query to determine the
	 * name's existant in the database.
	 * <p>
	 * If an error occurs this method returns false or if the user didn't exist. If the user exists
	 * this method returns true.
	 * 
	 * @param userName the name to be checked for.
	 * @return a boolean value to indicate if the name exists.
	 */
	private boolean checkEntryFromDB(String userName) {
		try {
			ResultSet rs = (ResultSet) stmt.executeQuery(sqlHandler.checkForUserName(userName));
			if (rs.next())
				return true;
			else
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * Sets the login of this user to the given value.
	 * <p>
	 * If this is a new instance this class, then this method connects to the
	 * database, which might cause an error and an exception will be caught.
	 * 
	 * @param loggedIn the status of the user to be changed to.
	 * @param userName the username to have its status changed.
	 */
	public void changeLogin(boolean loggedIn, String userName) {
		try {
			if (stmt == null)
				connectToDB();
			stmt.executeUpdate(sqlHandler.setLoggedIn(loggedIn, userName));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
