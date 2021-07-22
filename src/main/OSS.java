package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

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
		checkVersion();
		GraphicsDriver.initializeGraphics(); 
	}
	
	private void checkVersion() {
		double upversion;
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(new URL("https://open-source-spellchecker.github.io/currentversion/").openConnection().getInputStream()));
			upversion = Double.parseDouble(input.readLine().substring(17));// Current Version: ##
			input.close();
			
			if (VERSION == upversion) {
				System.out.println("OSS is Up-To-Date");
			}
			else {
				System.out.println("OSS is Out-Of-Date");
			}
		}
		// If can't connect, say offline
		catch (Exception e) {
			System.out.println("Cannot check OSS version, offline mode");
			e.printStackTrace();
		}
	}
	
	private void runOSS() {
		while(true) {
		
			
			try{ Thread.sleep(10); } catch(Exception e) {} //TODO: This is non-FPS based way to draw
			//GraphicsDriver.repaint();
		}		
	}
}
