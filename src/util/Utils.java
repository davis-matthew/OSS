package util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JOptionPane;

import graphics.GraphicsDriver;

public class Utils {
	public static final URL getURL() {
		String url = "";
		URL githubURL;
		while(true) {
			try {
				url = ((String)JOptionPane.showInputDialog(
						GraphicsDriver.frame,
		                "Enter a github URL:",
		                "Download GitHub Repos", 
		                JOptionPane.PLAIN_MESSAGE,
		                null,
		                null,
		                ""
		        ));
				
				// Cancel button
				if(url == null) { return null; }
				
				githubURL = new URL(url);
				return githubURL;
			}
			catch(MalformedURLException e) {
				JOptionPane.showMessageDialog(
						GraphicsDriver.frame,
		                "Invalid URL",
		                "Generate Comments File", 
		                JOptionPane.ERROR_MESSAGE,
		                null
		        );
			}
		}
	}
	
	public static final File selectRepository() {
		ArrayList<String> repoList = new ArrayList<String>();
		
		for(File repo : new File(System.getProperty("user.dir") + "/repos/").listFiles()) {
			if(repo.isDirectory()) {
				for(File subfile : repo.listFiles()) {
					//don't include in list if there is not .git file
					if(subfile.getName().equals(".git") && subfile.isDirectory()) {
						repoList.add(repo.getName());
					}
				}
			}
		}
		
		String s = (String)JOptionPane.showInputDialog(
				GraphicsDriver.frame,
				"Select Repository:",
				"Get the Repo for Comment Files:",
				JOptionPane.PLAIN_MESSAGE,
				null,
				repoList.toArray(),
				"");
		
		// Cancel button
		if(s == null) { return null; }
		return new File(System.getProperty("user.dir") + "/repos/"+s);
	}
	
	public static final void missingFile() {
		JOptionPane.showMessageDialog(
				GraphicsDriver.frame,
                "Comments file is missing, please use the \"Generate Comments File\" button",
                "File Not Found", 
                JOptionPane.ERROR_MESSAGE,
                null
        );
	}
	
	public final static <E> String listToString(Collection<E> list) {
		return arrayToString((E[]) list.toArray());
	}

	public final static <E> String arrayToString(E[] array) {
		String listStr = "{";
		for(int i =0;i<array.length;i++) {
			listStr+= array[i];
			if(i!=array.length-1) {
				listStr+=", ";
			}
		}
		listStr+="}";
		return listStr;
	}
}
