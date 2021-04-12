import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
		/* Create Panels */
		ImageIcon rIcon = new ImageIcon("Comments.jpg");
		ImageIcon bIcon = new ImageIcon("Spellcheck.jpg");
		ImageIcon gIcon = new ImageIcon("Goodbye.jpg");
		
		JLabel rLabel = new JLabel();
		JLabel bLabel = new JLabel();
		JLabel gLabel = new JLabel();
		
		JButton rButton = new JButton();
		rButton.setBounds(100, 100, 250, 100);
		rButton.setFont(new Font("MV Boli",Font.PLAIN,14));
		rButton.setFocusable(false);
		rButton.setText("Generate Comments File");
		rButton.addActionListener(e ->System.out.println("Creating comments file"));
		
		JButton bButton = new JButton();
		bButton.setBounds(100, 100, 250, 100);
		bButton.setFont(new Font("MV Boli",Font.PLAIN,14));
		bButton.setFocusable(false);
		bButton.setText("Apply Spelling Corrections");
		bButton.addActionListener(e ->System.out.println("Applying spelling corrections"));
		
		
		JButton gButton = new JButton();
		gButton.setBounds(100, 100, 250, 100);
		gButton.setFont(new Font("MV Boli",Font.PLAIN,14));
		gButton.setFocusable(false);
		gButton.setText("Exit Spellchecker");
		gButton.addActionListener(e -> System.exit(0));
		
		rLabel.setIcon(rIcon);
		
		bLabel.setIcon(bIcon);
		//bLabel.setText("Apply Spelling Corrections");
		
		
		gLabel.setIcon(gIcon);
		//gLabel.setText("Exit Spellchecker");
		
		
		JPanel redPanel = new JPanel();
		redPanel.setBackground(Color.red);
		redPanel.setBounds(0,0,250,250);
		
		JPanel bluePanel = new JPanel();
		//blabel.setText("Apply Spelling Corrections");
		bLabel.setVerticalAlignment(JLabel.BOTTOM);
		bLabel.setHorizontalAlignment(JLabel.CENTER);
		bluePanel.setBackground(Color.blue);
		bluePanel.setBounds(250,0,250,250);
		
		JPanel greenPanel = new JPanel();
		//glabel.setText("Exit Open Source Spellchecker");
		bLabel.setVerticalAlignment(JLabel.BOTTOM);
		bLabel.setHorizontalAlignment(JLabel.CENTER);
		greenPanel.setBackground(Color.green);
		greenPanel.setBounds(0,250,500,250);
		//greenPanel.setLayout(new BorderLayout());
		//greenPanel.setLayout(null);
		//gLabel.setIcon(gIcon);
		//greenPanel.add(gLabel);
		
		
		JFrame frame = new JFrame();
		
		frame.setTitle("MOSS");
		frame.setSize(1020,620);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ImageIcon image = new ImageIcon("MOSS.jpg");
	
		frame.setIconImage(image.getImage());
		frame.getContentPane().setBackground(new Color(123,50,250));
		frame.setLayout(null);
		frame.setVisible(true);
		frame.add(redPanel);
		redPanel.add(rLabel);
		redPanel.add(rButton);
		frame.add(bluePanel);
		bluePanel.add(bLabel);
		bluePanel.add(bButton);
		frame.add(greenPanel);
		greenPanel.add(gLabel);
		greenPanel.add(gButton);
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
