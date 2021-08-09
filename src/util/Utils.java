package util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
		
		//String acctName = repoList.toString().split("/")[repoList.toString().split("/").length-2];
		
		for(File userName : new File(System.getProperty("user.dir") + "/repos/" ).listFiles()) {
			if(userName.isDirectory()) {
				for(File repo : userName.listFiles()) {
					if(repo.isDirectory()) {
						for(File subFile : repo.listFiles()) {
							//don't include in list if there is not .git file
							if(subFile.getName().equals(".git") && subFile.isDirectory()) {
								repoList.add(userName.getName()+"/"+repo.getName());
							}
						}
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
		return Arrays.toString((E[]) list.toArray());
	}
}
