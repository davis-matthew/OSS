import java.awt.EventQueue;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try { Main OSS = new Main();} 
				catch (Exception e) { e.printStackTrace(); }
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {	
		initialize();
		runOSS(); // potentially unnecessary... actions handled by button press handlers
	}

	
	/**
	 * Setup the program
	 */
	private void initialize() { 
		initializeGraphics(); 
	}
	
	/**
	 * Initialize the OSS Hud
	 */
	private void initializeGraphics() {
		//Put your graphics code here
	}
	
	/**
	 * Runs the main spell checking program
	 */
	private void runOSS() {
		//Testing
		try {
			Functions.DownloadGithubRepo(new URL("https://github.com/davis-matthew/twitchirc.git"));
		} 
		catch (MalformedURLException e) { e.printStackTrace(); }
		try {
			Thread.sleep(8000); //FIXME: NOT A GOOD WAY TO DO THIS AT ALL
		} 
		catch (InterruptedException e) { e.printStackTrace(); }
		
		
		Functions.ParseCommentsOfRepo(new File(System.getProperty("user.dir") + "/repos/twitchirc/"));
		
		Functions.ShrinkAndSortComments();
		Functions.CreateMasterCommentFile("twitchirc");
		Functions.ApplyChangesToRepo(new File("twitchirc"));
	}
}
