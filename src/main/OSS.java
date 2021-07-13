package main;

import graphics.GraphicsDriver;

public class OSS {
	
	
	public static final double VERSION = 1.0;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try { new OSS(); } 
		catch (Exception e) { e.printStackTrace(); }
	}

	/**
	 * Create the application.
	 */
	public OSS() {	
		//TEST WITH https://github.com/exeloar/OSSTest.git
		initialize();
		runOSS();
	}

	/**
	 * Setup the program
	 */
	private void initialize() { 
		GraphicsDriver.initializeGraphics(); 
	}
	
	private void runOSS() {
		while(true) {
		
			
			try{ Thread.sleep(10); } catch(Exception e) {} //TODO: This is non-FPS based way to draw
			//GraphicsDriver.repaint();
		}		
	}
}
