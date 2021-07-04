package main;

import graphics.GraphicsDriver;

public class OSS {
	
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
	}

	/**
	 * Setup the program
	 */
	private void initialize() { 
		GraphicsDriver.initializeGraphics(); 
	}
}
