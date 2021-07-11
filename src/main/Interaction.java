package main;

import java.io.File;
import java.net.URL;

import util.Utils;

public class Interaction {
	
	public static final void downloadRepo() {
		URL githubURL;
		if((githubURL = Utils.getURL()) != null) {
			Functions.DownloadGithubRepo(githubURL);
		}
	}
	
	public static final void generateComments() {
		File repo;
		if((repo = Utils.selectRepository()) != null) {
			Functions.GenerateCommentFile(repo);
		}
	}
	
	public static final void applyCorrections() {
		File repo;
		if((repo = Utils.selectRepository()) != null) {
			Functions.ApplyChangesToRepo(repo);
		}
	}
	
	public static final void exitOSS() {
		System.exit(0);
	}
}
