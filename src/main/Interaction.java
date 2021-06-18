package main;

import java.io.File;
import java.net.URL;

import util.Utils;

public class Interaction {
	
	public static final void downloadRepo() {
		URL githubURL = Utils.getURL();
		if(githubURL != null) {
			Functions.DownloadGithubRepo(githubURL);
		}
	}
	
	public static final void generateComments() {
		File repo = Utils.selectRepository();
		if(repo != null) {
			Functions.GenerateCommentFile(repo);
		}
	}
	
	public static final void applyCorrections() {
		File repo = Utils.selectRepository();
		if(repo != null) {
			Functions.ApplyChangesToRepo(repo);
		}
	}
	
	public static final void exitOSS() {
		System.exit(0);
	}
}
