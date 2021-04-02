import java.awt.EventQueue;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Main {

	private Pane pane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.pane.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		/*pane = new Pane();
			pane.maxHeight	(Double.NEGATIVE_INFINITY);
			pane.maxWidth	(Double.NEGATIVE_INFINITY);
			pane.minHeight	(Double.NEGATIVE_INFINITY);
			pane.minWidth	(Double.NEGATIVE_INFINITY);
			pane.prefHeight	(400.0);
			pane.prefWidth	(600.0);
		
			Rectangle background = new Rectangle();
				background.setArcHeight		(5.0);
				background.setArcWidth		(5.0);
				background.setHeight		(400.0);
				background.setStroke		(Color.BLACK);
				background.setStrokeType	(StrokeType.INSIDE);
				background.setWidth			(600.0);
				LinearGradient backgroundGradient = new LinearGradient(
					0.0, 											//startX
					0.0, 											//startY
					1.0, 											//endX
					1.0, 											//endY
					true, 											//proportional
					CycleMethod.NO_CYCLE,							//CycleMethod
					new Stop[] {									//Stops
							new Stop(
									0.0,							//offset
									Color.web(
											"0x8122BC",				//color code; RGB = (129, 34, 188)
											1.0						//opacity
									)			
							), 
							new Stop(
									1.0,
									Color.web(
											"0x9A5D5B",				//RGB = (154, 93, 91)
											1.0)
							)
					}
				);	
				background.setFill(backgroundGradient);
		
			Rectangle titleOutline = new Rectangle();
				titleOutline.setArcHeight	(5.0);
				titleOutline.setArcWidth	(5.0);
				titleOutline.setHeight		(53.0);
				titleOutline.setLayoutX		(27.0);
				titleOutline.setLayoutY		(12.0);
				titleOutline.setStroke		(Color.BLACK);
				titleOutline.setStrokeType	(StrokeType.INSIDE);
				titleOutline.setWidth			(538.0);
				LinearGradient titleOutlineGradient = new LinearGradient(
					0.0, 											//startX
					0.0, 											//startY
					1.0, 											//endX
					1.0, 											//endY
					true, 											//proportional
					CycleMethod.NO_CYCLE,							//CycleMethod
					new Stop[] {									//Stops
							new Stop(
									0.0,							//offset
									Color.web(
											"0x8122BC",				//color code; RGB = (129, 34, 188)
											1.0						//opacity
									)			
							), 
							new Stop(
									1.0,
									Color.web(
											"0xDA1C7B",				//RGB = (218, 28, 123)
											0.58
									)
							)
					}
				);	
				titleOutline.setFill(titleOutlineGradient);
			
			Rectangle outerButtonBoundary = new Rectangle();
				outerButtonBoundary.setArcHeight	(5.0);
				outerButtonBoundary.setArcWidth		(5.0);
				outerButtonBoundary.setFill(
						Color.web(
								"0x175C9C",							//RGB = (23, 92, 156)
								0.36
						)
				);
				outerButtonBoundary.setHeight		(200.0);
				outerButtonBoundary.setLayoutX		(14.0);
				outerButtonBoundary.setLayoutY		(101.0);
				outerButtonBoundary.setStroke		(Color.BLACK);
				outerButtonBoundary.setStrokeType	(StrokeType.INSIDE);
				outerButtonBoundary.setWidth		(344.0);
				
			Rectangle innerButtonBoundary = new Rectangle();
				outerButtonBoundary.setArcHeight	(5.0);
				outerButtonBoundary.setArcWidth		(5.0);
				outerButtonBoundary.setFill(
						Color.web(
								"0x1F93FF",							//RGB = (31, 147, 255)
								0.56
						)
				);
				outerButtonBoundary.setHeight		(178.0);
				outerButtonBoundary.setLayoutX		(27.0);
				outerButtonBoundary.setLayoutY		(112.0);
				outerButtonBoundary.setStroke		(Color.BLACK);
				outerButtonBoundary.setStrokeType	(StrokeType.INSIDE);
				outerButtonBoundary.setWidth		(61.0);
			
			Button generate = new Button();
				generate.setLayoutX					(99.0);
				generate.setLayoutY					(138.0);
				generate.setMnemonicParsing			(false);
				generate.setPrefHeight				(31.0);
				generate.setPrefWidth				(238.0);
				generate.setText					("Generate Comment File");
				generate.setTextAlignment			(TextAlignment.CENTER);
				
			Button applychanges = new Button();
				applychanges.setLayoutX				(99.0);
				applychanges.setLayoutY				(185.0);
				applychanges.setMnemonicParsing		(false);
				applychanges.setPrefHeight			(31.0);
				applychanges.setPrefWidth			(238.0);
				applychanges.setText				("Apply Spelling Corrections");
				applychanges.setTextAlignment		(TextAlignment.CENTER);
				
			Button quit = new Button();
				quit.setLayoutX						(99.0);
				quit.setLayoutY						(235.0);
				quit.setMnemonicParsing				(false);
				quit.setPrefHeight					(31.0);
				quit.setPrefWidth					(238.0);
				quit.setText						("Quit Open Source Spellchecker");
				quit.setTextAlignment				(TextAlignment.CENTER);
				
			Label titleText = new Label();
				titleText.setLayoutX				(44.0);
				titleText.setLayoutY				(14.0);
				titleText.setText					("Open Source Spellchecker (OSS)");
				titleText.setFont(
						new Font(36.0)
				);
			
		pane.getChildren().addAll(
				background,
				titleOutline,
				outerButtonBoundary,
				innerButtonBoundary,
				generate,
				applychanges,
				quit,
				titleText
		);*/

		/*	frame.setResizable(false);
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		pane.setTitle("OSS - Open Source Spellchecker");
		try {
			frame.setIconImage(ImageIO.read(new File("assets/icon.png")));
			GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
			groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
					.addGap(0, 794, Short.MAX_VALUE)
			);
			groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
					.addGap(0, 571, Short.MAX_VALUE)
			);
			frame.getContentPane().setLayout(groupLayout);
		} 
		catch (IOException e) {	e.printStackTrace(); }
		
		
		*/
		
		//Testing
		try {
			Functions.DownloadGithubRepo(new URL("https://github.com/davis-matthew/twitchirc.git"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Functions.ParseCommentsOfRepo(new File(System.getProperty("user.dir") + "/repos/twitchirc/"));
		
		Functions.ShrinkAndSortComments();
		Functions.CreateMasterCommentFile("twitchirc");
		Functions.ApplyChangesToRepo(new File("twitchirc"));
	}
}
