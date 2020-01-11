
public class SQLThread implements Runnable {

	private static final String DEFAULT_DB = "db_19762992";
	private static final String DEFAULT_COMPUTER = "atlas.dsv.su.se";
	private static final String USR_NAME = "usr_19762992";
	private static final String USR_PWD = "762992";

	private volatile boolean alive = true;
	private boolean checkOnly;
	private String userName;
	private DBHandler dbHandler = new DBHandler();
	private Thread thread;
	
	public SQLThread(boolean checkOnly, String userName) {
		this.checkOnly = checkOnly;
		this.userName = userName;
		
	}
	
	public void stop() {
		alive = false;
	}
	
	public void start() {
		this.start();
	}

	@Override
	public void run() {
		while(alive) {
			if(checkOnly == false) {
				
			} else {
				
			}
		}
	}

}
