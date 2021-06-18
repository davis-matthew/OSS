import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Main {

	private static JFrame frame;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try { Main OSS = new Main();} 
		catch (Exception e) { e.printStackTrace(); }
	}

	/**
	 * Create the application.
	 */
	public Main() {	
		//TEST WITH https://github.com/exeloar/OSSTest.git
		initialize();
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
		JPanel redPanel = new JPanel();
		
		//Button to download Comments File from github
		JButton rButton1 = new JButton();
		//rButton1.setBounds(100, 100, 250, 100);
		rButton1.setBounds(0, 5, 200, 50);
		rButton1.setFont(new Font("MV Boli",Font.PLAIN,14));
		rButton1.setFocusable(false);
		rButton1.setText("Download Github Repo");
		redPanel.add(rButton1, BorderLayout.WEST);
		rButton1.addActionListener(e -> downloadRepos());
		
		bLabel.setVerticalAlignment(JLabel.BOTTOM);
		bLabel.setHorizontalAlignment(JLabel.CENTER);
		//Button to generate Comments File for analysis
		JButton rButton2 = new JButton();
		//rButton2.setBounds(100, 100, 250, 100);
		rButton2.setBounds(0, 60, 240, 50);
		rButton2.setFont(new Font("MV Boli",Font.PLAIN,14));
		rButton2.setFocusable(false);
		rButton2.setText("Analyze/Generate Comments ");
		redPanel.add(rButton2, BorderLayout.WEST);
		redPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		rButton2.addActionListener(e -> generateComments());
		
		
		JButton bButton = new JButton();
		bButton.setBounds(100, 100, 250, 100);
		bButton.setFont(new Font("MV Boli",Font.PLAIN,14));
		bButton.setFocusable(false);
		bButton.setText("Apply Spelling Corrections");
		bButton.addActionListener(e -> spellingCorrections());
		
		
		JButton gButton = new JButton();
		gButton.setBounds(100, 100, 250, 100);
		gButton.setFont(new Font("MV Boli",Font.PLAIN,14));
		gButton.setFocusable(false);
		gButton.setText("Exit Spellchecker");
		gButton.addActionListener(e -> exitOSS());
		rLabel.setIcon(rIcon);
		
		bLabel.setIcon(bIcon);
		//bLabel.setText("Apply Spelling Corrections");
		
		
		gLabel.setIcon(gIcon);
		//gLabel.setText("Exit Spellchecker");
		
		
		//JPanel redPanel = new JPanel();
		redPanel.setLayout(new BorderLayout());
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
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		
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
		
		
		
		frame.add(bluePanel);
		bluePanel.add(bLabel);
		bluePanel.add(bButton);
		frame.add(greenPanel);
		greenPanel.add(gLabel);
		greenPanel.add(gButton);
	}
	
	public static final void generateComments() {
		String s = "";
		while(s.equals("")) {
			s = (String)JOptionPane.showInputDialog(
	                frame,
	                "Enter a File Location: ",
	                "Analyze/Generate Comments File", 
	                JOptionPane.PLAIN_MESSAGE,
	                null,
	                null,
	                ""
	        );
			
			/* Cancel button returns user to Generate Comments selection */
			
			if(s == null) { return; }
				
				
			/*System.out.println(JOptionPane.showConfirmDialog(frame, JOptionPane.OK_CANCEL_OPTION););*/
			
				
			try { Functions.GenerateCommentFile(new URL(s)); } 
			
			catch (MalformedURLException e) {
				s = "";
				/* URL Entry is invalid */
				JOptionPane.showMessageDialog(
		                frame,
		                "Invalid File location!",
		                "Analyze/Generate Comments File", 
		                JOptionPane.ERROR_MESSAGE,
		                null
		        );				
			}
		}
	}
	
	public static final void downloadRepos() {
		String s = "";
		while(s.equals("")) {
			s = (String)JOptionPane.showInputDialog(
	                frame,
	                "Enter a github URL:",
	                "Download GitHub Repos", 
	                JOptionPane.PLAIN_MESSAGE,
	                null,
	                null,
	                ""
	        );
			
			/* Cancel button returns user to Download Repos selection */
			
			if(s == null) { return; }
				
				
			/*System.out.println(JOptionPane.showConfirmDialog(frame, JOptionPane.OK_CANCEL_OPTION););*/
			
			//Needs to be modified for Download Repos
			
			try { Functions.DownloadGithubRepo(new URL(s)); } 
			catch (MalformedURLException e) {
				s = "";
				/* URL Entry is invalid */
				JOptionPane.showMessageDialog(
		                frame,
		                "Invalid URL",
		                "Generate Comments File", 
		                JOptionPane.ERROR_MESSAGE,
		                null
		        );				
			}
		}
	}
	
	
	
	public static final void downloadComments() {
		String s = "";
		while(s.equals("")) {
			s = (String)JOptionPane.showInputDialog(
	                frame,
	                "Enter a URL:",
	                "Download Comments File", 
	                JOptionPane.PLAIN_MESSAGE,
	                null,
	                null,
	                ""
	        );
			
			/* Cancel button returns user to Generate Comments selection */
			
			if(s == null) { return; }
				
				
			/*System.out.println(JOptionPane.showConfirmDialog(frame, JOptionPane.OK_CANCEL_OPTION););*/
			
				
			try { Functions.GenerateCommentFile(new URL(s)); } 
			catch (MalformedURLException e) {
				s = "";
				/* URL Entry is invalid */
				JOptionPane.showMessageDialog(
		                frame,
		                "Invalid URL",
		                "Generate Comments File", 
		                JOptionPane.ERROR_MESSAGE,
		                null
		        );				
			}
		}
	}
	
	
	public static final void spellingCorrections() {
		
		
		//Functions.ApplyChangesToRepo(new File(System.getProperty("user.dir") + "/repos/OSSTest")); //FIXME: FOR TESTS
		
		//Ask user to pick the repo to apply corrections to (dropdown)
		
		File reposFolder = new File(System.getProperty("user.dir") + "/repos/");
		ArrayList<String> arrayList = new ArrayList<String>();
		
		for(File repo : reposFolder.listFiles()) {
			if(repo.isDirectory())
			{ 
				for(File subfile : repo.listFiles()) {
					if(subfile.getName().equals(".git") && subfile.isDirectory()) {     //don't include in list if there is not .git file 		
						arrayList.add(repo.getName());
					}
				}
			}
			
			try { Functions.ApplyChangesToRepo(reposFolder); } 
			catch (Exception e) {
				String s = "";
				/* URL Entry is invalid */
				JOptionPane.showMessageDialog(
		                frame,
		                "Invalid URL",
		                "Generate Comments File", 
		                JOptionPane.ERROR_MESSAGE,
		                null
		        );				
			}
		}
		
		
		
		String s = (String)JOptionPane.showInputDialog(
				frame,
				"Select Repo:",
				"Get the Repo for Comment Files:",
				JOptionPane.PLAIN_MESSAGE,
				null,
				arrayList.toArray(),
				"");
		
		/* Cancel button returns user to Apply Spelling Corrections selection */
		if(s == null) { return;}
		
		
		
		Functions.ApplyChangesToRepo(new File(System.getProperty("user.dir") + "/repos/"+s));
		System.out.println(s);
		
	}
	
	public static final void exitOSS() {
		System.exit(0);
	}
}
