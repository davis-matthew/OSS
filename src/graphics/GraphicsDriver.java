package graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.Interaction;
import main.OSS;

public class GraphicsDriver {
	
	public static JFrame frame;
	
	/**
	 * Initialize the OSS Hud
	 */
	public static final void initializeGraphics() {
		/* Create Panels and Buttons */
		
		//JLabel rLabel0 = new JLabel("Select a Button:");
		JLabel rLabel0 = new JLabel("Select from the Following Options:",JLabel.CENTER);
		JLabel rLabel1 = new JLabel();
		JLabel rLabel2 = new JLabel();
		JLabel rLabel3 = new JLabel();
		JLabel rLabel4 = new JLabel();
		
		JPanel mPanel = new JPanel();
		mPanel.setLayout(new BoxLayout(mPanel, BoxLayout.Y_AXIS));

		//Button to download Comments File from github
		
		JButton rButton1 = new JButton();
		rLabel0.setBounds(140,50,300,50);
		rLabel0.setFont(new Font("MV Boli",Font.PLAIN,16));
		rLabel0.setForeground(Color.WHITE);
		
		JButton rButton4 = new JButton();
		rButton1.setBounds(140, 100, 300, 50);
		rButton1.setFont(new Font("MV Boli",Font.PLAIN,14));
		rButton1.setFocusable(false);
		rButton1.setText("Download Github Repo");
		mPanel.add(rButton1, BorderLayout.CENTER);
		rButton1.addActionListener(e -> Interaction.downloadRepo());
		
		
		//Button to generate Comments File for analysis
		
		JButton rButton2 = new JButton();
		rButton2.setBounds(140, 150, 300,50);
		rButton2.setFont(new Font("MV Boli",Font.PLAIN,14));
		rButton2.setFocusable(false);
		rButton2.setText("Analyze/Generate Comments ");
		mPanel.add(rButton2, BorderLayout.CENTER);
		//mPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		rButton2.addActionListener(e -> Interaction.generateComments());
		
		
		//Button to Apply Spelling Corrections and Propogate changes
		
		JButton rButton3 = new JButton();
		rButton3.setBounds(140, 200, 300, 50);
		rButton3.setFont(new Font("MV Boli",Font.PLAIN,14));
		rButton3.setFocusable(false);
		rButton3.setText("Apply Spelling Corrections");
		mPanel.add(rButton3, BorderLayout.CENTER);
		rButton3.addActionListener(e -> Interaction.applyCorrections());
		
		
		//JButton gButton = new JButton();
		rButton4.setBounds(140, 250, 300, 50);
		rButton4.setFont(new Font("MV Boli",Font.PLAIN,14));
		rButton4.setFocusable(false);
		rButton4.setText("Exit Spellchecker");
		mPanel.add(rButton4, BorderLayout.CENTER);
		rButton4.addActionListener(e -> Interaction.exitOSS());
		
		
		
		frame = new JFrame();
		
		//================================
		
		/*JButton button = new JButton("Button1");
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(button);

		button = new JButton("Button2");
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(button);

		button = new JButton("Button3");
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(button);

		add(panel);*/
		
		//====================================
		
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		mPanel.setLayout(new BorderLayout());
		//mPanel.setLayout(new BoxLayout());
		mPanel.setBackground(Color.BLUE);
		mPanel.setBounds(0,0,600,400); 
		frame.setTitle("OSS - OpenSource Spellchecker (v "+OSS.VERSION+")");
		frame.setSize(600,400);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
	
		//frame.setIconImage(image.getImage());
		frame.getContentPane().setBackground(new Color(123,50,250));
		frame.setLayout(null);
		frame.setVisible(true);
		
	
		frame.add(mPanel);
		mPanel.add(rLabel0);
		mPanel.add(rLabel1);
		mPanel.add(rLabel2);
		mPanel.add(rLabel3);
		mPanel.add(rLabel4);

		
		
	}
}
