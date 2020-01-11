
public class SQLThread extends Thread {

	private static final String DEFAULT_DB = "db_19762992";
	private static final String DEFAULT_COMPUTER = "atlas.dsv.su.se";
	private static final String USR_NAME = "usr_19762992";
	private static final String USR_PWD = "762992";

	private boolean alive;
	private String userName;
	private DBHandler dbHandler = new DBHandler();

	public SQLThread(String userName) {
		this.userName = userName;
		alive = true;
		this.start();
	}

	@Override
	public void run() {
		while (alive) {
			try {
				while (!userName.equals(null)) {

				}
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
