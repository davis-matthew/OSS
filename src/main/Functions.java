package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import data.Comment;
import data.CommentSort;
import graphics.GraphicsDriver;
import util.Utils;


public class Functions {
	
	//TODO: https://jflex.de/
	
	/*
	*	Given Link, this will download the repository using
	*   the user's git to repos/acctName/reponame/ = repox
	*/
	public static void DownloadGithubRepo(URL repoLink) {
		
		//https://github.com/serso/android-calculatorpp.git
				//System.out.println("repoLink " + repoName);
		//FIXME: git reset && pull need to cd into the repo, clone does not.
		String acctName = repoLink.toString().split("/")[repoLink.toString().split("/").length-2];
		String repoName = repoLink.toString().substring(repoLink.toString().lastIndexOf("/")+1,repoLink.toString().indexOf(".git"));
		String gitCommand = "clone "+repoLink;
		boolean alreadyExists = false;
		
		//Create repos folder if it does not exist
		if(Files.notExists(Paths.get(System.getProperty("user.dir") + "/repos/" + acctName))) { 
			try { Files.createDirectories(Paths.get(System.getProperty("user.dir") + "/repos/" + acctName));} 
			catch (IOException e) { e.printStackTrace(); }
		}
		
		if(Files.exists(Paths.get(System.getProperty("user.dir") + "/repos/" + acctName + "/"+repoName))) {
			
			alreadyExists = true;
			
			
			// display the showInputDialog to see how the user wants to handle if the repos directory already exists
	        String[] gitOptions = new String[] {"git reset --hard (restore to what's on github)", "git pull (pull changes and keep other edits)"};
			
	        String s = (String)JOptionPane.showInputDialog(
					GraphicsDriver.frame,
					"Select Option to Proceed:",
					"Repo Already Exists!",
					JOptionPane.WARNING_MESSAGE,
					null,
					gitOptions,
					"");
	        //FIXME: null doesn't like this
			switch(s) {
				case("git reset --hard (restore to what's on github)"):{ gitCommand = "reset --hard "; break; }
				case("git pull (pull changes and keep other edits)"):{ gitCommand = "pull "; break; }
				default:{ return; }
			}		
			int choices = JOptionPane.showOptionDialog(
				  GraphicsDriver.frame, 
			      "Are you Sure?", 
			      "Directory May Be Overwritten!", 
			      JOptionPane.YES_NO_OPTION,  
			      JOptionPane.WARNING_MESSAGE, 
			      null, null, null);
			
			if (choices == JOptionPane.NO_OPTION) { return; }
		}	
 
		System.out.println("Downloading Repo " + repoLink + " into " + System.getProperty("user.dir") + "/repos/" + acctName + "/");

		String shellCommand = "sh -c ";
		if(System.getProperty("os.name").startsWith("Windows")) { shellCommand = "cmd /c "; }
		shellCommand += "cd "+System.getProperty("user.dir") + "/repos/" + acctName +"/";	
		if(alreadyExists) { shellCommand += repoName; }
		shellCommand += " && ";
		
		try { 
			System.out.println("Running: "+shellCommand+"git "+gitCommand);
			Process p = Runtime.getRuntime().exec(shellCommand+"git "+gitCommand, null, new File(System.getProperty("user.dir") + "/repos/" + acctName + "/"));
			
			Scanner output = new Scanner(p.getErrorStream());
			
			System.out.println("Output from Github:");
			System.out.println("------------");
			while(true) {
				while(output.hasNextLine()) {
					System.out.println("\t"+output.nextLine());
				}
					
				if(!p.isAlive()) { break; }
			}
			output.close();
			System.out.println("------------");
		} 
		catch (IOException e) { e.printStackTrace(); }
		if(Files.exists(Paths.get(System.getProperty("user.dir") + "/repos/"+ acctName + "/" + repoName ))) { 
			System.out.println("Done Downloading Repo " +repoLink+ " into " + System.getProperty("user.dir") + "/repos/" + acctName + "/");
		}
		else {
			System.out.println("Download Failed");
		}	
	}
	
	private static String getUsername(File f) {
		return f.getParentFile().getName();
	}
	
	/*
	*	Given Link, this will download the repository using
	*   the user's git to repos/acctName/reponame/ 
	*/
	public static void GenerateCommentFile(File repo) {
		String userName = getUsername(repo);
		String repoName = repo.getName();
		

		ParseCommentsOfRepo(new File(System.getProperty("user.dir") + "/repos/" + userName+"/"+repoName));
		ShrinkAndSortComments();
		CreateMasterCommentFile(userName, repoName);
	}
			
	/*
	*	After selecting a folder and clicking parse, this
	*   will create the master comment file
	*/
	static final String[] regexPatterns = new String[] {
			"\\/\\*(\\*(?!\\/)|[^*])*\\*\\/",     //Matches /*Comment*/
			"\\/\\/.*",     // Matches //Comment
			
			"RAWTEXT" //Always keep this last in the list. Will take the entire file as one large comment
	};
	static final CommentStyleSet[] languageMap = new CommentStyleSet[]{ //Refer to the enclosed table "Comment Styles" for the styles & support
			new CommentStyleSet(
					"C, C++, Go, Java, JavaScript, C#, PHP, Rust",
					new int[] {0,1}            
			),
			new CommentStyleSet(
					"Markdown, Txt",
					new int[] {regexPatterns.length-1}
			)
	};
	
	static final Map<String,Integer> fileExtensionToCommentStyle = Map.ofEntries(
			Map.entry(".c", 	0),
			Map.entry(".cpp", 	0),
			Map.entry(".h", 	0),
			Map.entry(".java", 	0),
			Map.entry(".go", 	0),
			Map.entry(".js", 	0),
			Map.entry(".cs", 	0),
			Map.entry(".rs", 	0),
			Map.entry(".php", 	0),
			Map.entry(".md", 	languageMap.length-1),
			Map.entry(".txt", 	languageMap.length-1)
	);
	static class CommentStyleSet{
		String name;
		int[] regexPatterns; 
		CommentStyleSet(String n, int[] regex){
			name = n;
			regexPatterns = regex;
		}
	}
	
	//For parallelization of search, consider:
	
	ConcurrentLinkedQueue<File> filePointers = new ConcurrentLinkedQueue<File>();
	void searchProducer(final File repo){
		ArrayList<File> directories = new ArrayList<File>();
		directories.add(repo);
		while(directories.size() != 0) {
			for(File file : directories.get(0).listFiles())
			{
				if (file.isDirectory()) {
					directories.add(file);
				}
				else {
					try{
						CommentStyleSet test = languageMap[fileExtensionToCommentStyle.get(file.getName().substring(file.getName().lastIndexOf(".")))]; //FIXME: probably not a good way to do it
						filePointers.add(file);
					}
					catch(Exception e) { System.out.println("Unhandled language/extension type of file "+file.getName()); }
				}
			}
		}
	}
	
	int availableThreads = Runtime.getRuntime().availableProcessors()-1;
	ArrayList<File> filePointers2 = new ArrayList<File>();
	Semaphore semaFullSlots = new Semaphore(0);
	Semaphore semaEmptySlots = new Semaphore(availableThreads);
	Lock m = new ReentrantLock();
	void searchProducer2(final File repo){
		ArrayList<File> directories = new ArrayList<File>();
		ArrayList<File> files = new ArrayList<File>();
		directories.add(repo);
		while(directories.size() != 0) {
			for(File file : directories.get(0).listFiles())
			{
				if (file.isDirectory()) {
					directories.add(file);
				}
				else {
					try{
						CommentStyleSet test = languageMap[fileExtensionToCommentStyle.get(file.getName().substring(file.getName().lastIndexOf(".")))]; //FIXME: probably not a good way to do it
						files.add(file);
					}
					catch(Exception e) { System.out.println("Unhandled language/extension type of file "+file.getName()); }
				}
			}
			try { semaEmptySlots.acquire();} 
			catch (InterruptedException e) {}
			m.lock();
			for(File f : files) { filePointers2.add(f); }
			m.unlock();
			semaFullSlots.release(files.size());
			files.clear();
		}
	}
	void searchConsumer() {
		while(true) {
			try {
				semaFullSlots.acquire();
			} 
			catch (InterruptedException e) {}
			m.lock();
			File f = filePointers2.get(0);
			m.unlock();
			semaEmptySlots.release(1);
			parseFile(f);
		}
	}
	/*
	 * Queue Q;
	 * Mutex m;
	 * Semaphore semaFullSlots = {0, B};
	 * Semaphore semaEmptySlots = {B, B};
	
	//Producer
	Producer() {
	 while (true) {
		CreateObjectToPush();
		semaEmptySlots.Wait();
		m.Lock();
		Q.add (x);
		m.Unlock();
		semaFullSlots.Release(1);
	 }
	}
	
	//Consumer
	Consumer() {
 	 while (true) {
		semaFullSlots.Wait ();
		m.Lock();
		x = Q.pop();
		m.Unlock();
		semaEmptySlots.Release(1);
		DoStuffWithPoppedObject();
  	 }
	}
	
	Repo Thread pushes file pointers -> Regex Checker Threads push comments and file info -> Collection thread makes one master file of all comments
	
	*/
	static ArrayList<File> files = new ArrayList<File>();
	static ArrayList<Comment> comments = new ArrayList<Comment>();
	
	public static void PropagateFilesOfRepo(final File repo) {
		for (final File file : repo.listFiles()) {
			if (file.isDirectory()) {
				PropagateFilesOfRepo(file);
			} 
			else {
				try{
					CommentStyleSet test = languageMap[fileExtensionToCommentStyle.get(file.getName().substring(file.getName().lastIndexOf(".")))]; //FIXME: probably not a good way to do it
					files.add(file);
				}
				catch(Exception e) { System.out.println("Unhandled language/extension type of file "+file.getName()); }
			}
		}
	}
	public static void ParseCommentsOfRepo(final File repo) {
		System.out.println("Starting Parsing Files");
		PropagateFilesOfRepo(repo);
		for(final File file : files) {
			System.out.println("Parsing "+file.getName()+" at "+repo.getAbsolutePath());
			parseFile(file);
		}
		System.out.println("Done Parsing Files");
	}
	
	public static void parseFile(final File file) {
		try{
			//Find Regex Patterns for language
			//Search for those patterns in the file
			//Find the longest pattern for each comment (do not keep sub-matches)
			//Generate the information required to push the modified comment text later.
			
			//Example
			//hello.cpp
			/*
			
				/* This function returns 0. * /
				int main(){
				
					return 0; //Returns 0
				}
				
				//This is what a /*comment* / looks like in c++		
			
			*/
			
			//.cpp -> inline //, block /* */
			
			//Found 
			//1.	/* This function returns 0. * /
			//2.	//Returns 0
			//3.	/*comment* /
			//4.	//This is what a /*comment* / looks like in c++
			
			//Keep 1,2,4. Discard 3.
			
			//hello.cpp : {line 2 col 1 : {/* This function returns 0. * /}, line 5 col 12 : {//Returns 0}, line 8 col 1 : {//This is what a /*comment* / looks like in c++}
			
			
			
			CommentStyleSet style = languageMap[fileExtensionToCommentStyle.get(file.getName().substring(file.getName().lastIndexOf(".")))]; //gets the comment style
			String content = Files.readString(file.getAbsoluteFile().toPath()); //TODO: absolute path / relative path?
			
			if(regexPatterns[style.regexPatterns[0]].equals("RAWTEXT")) {
				comments.add(new Comment(file.getPath(),0,content.length(),content));
				return;
			}
				
			Matcher[] mat = new Matcher[style.regexPatterns.length];
			
			for(int i =0;i<mat.length;i++) {
				mat[i] = Pattern.compile(regexPatterns[style.regexPatterns[i]]).matcher(content);
			}
			
			// Every Comment
			for(Matcher m : mat) {
				try{
					while(m.find()) { 
						comments.add(
							new Comment(
								file.getPath(),
								m.start(),
								m.end(),
								m.group()
							)
						);
					}
				}
				catch(StackOverflowError e) {
					e.printStackTrace();
					return; //Skip this file
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	*	This method shrinks the amount of comments to put in the master file
	*/
	public static void ShrinkAndSortComments() {
		System.out.println("Optimizing Comments");
		Collections.sort(comments,new CommentSort());
		for(int i = 0; i<comments.size(); i++) {
			for(int j = i+1; j<comments.size(); j++) {
				if(!comments.get(i).getFile().equals(comments.get(j).getFile())) {break;}
				if(comments.get(i).overlaps(comments.get(j))) {
					if(comments.get(i).length() > comments.get(j).length()) { comments.remove(j); }
					else { comments.remove(i); }
					i=0;j=1; //TODO: This is probably not a good solution to the broken iterator problem.
				}
			}
		}
		System.out.println("Done Optimizing Comments");
	}
	
	/*
	*	This method creates the master comment file [repo]_comment.txt
	*/
	public static void CreateMasterCommentFile(String userName, String repoName) {
		System.out.println("Creating New Comment File");
		try { 
			//Check if comments file already exists
			File f = new File(System.getProperty("user.dir") +"/"+ userName+"_"+repoName + "_comments.txt"); 
			System.out.println("Printing out f "+ f);
			  if(f.exists()) {
				// create a jframe
				   
				  int choices = JOptionPane.showOptionDialog(
						  GraphicsDriver.frame,
					      "The Comment file " + f + " already exists.\n Regenerating will delete it. ", 
					      "The Comment File Already Exists!", 
					      JOptionPane.OK_CANCEL_OPTION,  
					      JOptionPane.WARNING_MESSAGE, 
					      null, null, null);
					
					if (choices == JOptionPane.CANCEL_OPTION) { return; }
					
					//If OK is selected then delete file
					
					f.delete();    
					System.out.println(f + " is deleted!");
					
			  }
				
			
			BufferedWriter masterFile = new BufferedWriter(new FileWriter(f)); 
			for(Comment comment : comments) {
				masterFile.write(comment.toString()+"\n\n");
			} 		
			masterFile.close();
		}
		catch(IOException e) {}
		System.out.println("Created File "+userName+"_"+repoName+"_comments.txt");
	}
	
	/*
	*	Given the master comment file (with changes)
	*   this will fix the comments in each individual file
	*/
	public static void ApplyChangesToRepo(final File repo) {
		System.out.println("Applying Changes");
		File commentsFile = new File(getUsername(repo)+"_"+repo.getName()+"_comments.txt");
		comments.clear();
		try{
			Scanner input = new Scanner(commentsFile).useDelimiter("");
			String info = "";
			while(input.hasNextLine()) {
				info = input.nextLine();
				if(info.length()!=0) {
					System.out.println(info);
					String file;
					int start, end;
					String text = "";
					
					file = info.substring(info.indexOf(" ")+1,info.indexOf(","));
					info = info.substring(info.indexOf(",")+2);
					start = Integer.parseInt(info.substring(info.indexOf(" ")+1,info.indexOf(",")));
					info = info.substring(info.indexOf(",")+2);
					end = Integer.parseInt(info.substring(info.indexOf(" ")+1,info.indexOf("}")));
					
					ArrayList<Character> commentChars = new ArrayList<Character>();
					while(true) {
						char c;
						try{c = input.next().charAt(0);}catch(Exception e) {break;}
						if(c== '\7') { break; }
						commentChars.add(c);
					}
					
					/*
					 * if(commentChars.get(0) == '/' && commentChars.get(1) == '/') { text +=
					 * "//"+input.nextLine()+"\n"; } else { while(true) {
					 * commentChars.add(input.next().charAt(0));
					 * if(commentChars.get(commentChars.size()-2)=='*' &&
					 * commentChars.get(commentChars.size()-2)=='/') { break; } } while(true) {
					 * commentChars.add(input.next().charAt(0));
					 * if(commentChars.get(commentChars.size()-1)=='\n') { break; } } for(char c :
					 * commentChars) { text += c; } }
					 */
					for(char c : commentChars) { text += c; }
					text = text.substring(0,text.length()-2); //removes last \n
					
					comments.add(new Comment(file,start,end,text));
				}				
			}
			input.close();
		}
		catch (Exception e) { Utils.missingFile(); return; }
		
		Collections.sort(comments,new CommentSort());
		//String contentBefore;
		
		try {
			String fileName = "", content = "", newContent = ""; 
			int offset = 0, start, end;
			for(int i = 0; i<comments.size();i++) {
				if(!comments.get(i).getFile().equals(fileName)) {
					if(!fileName.equals("")) {
						Files.writeString(Path.of(fileName),content);
					}
					
					fileName = comments.get(i).getFile();
					offset = 0;
					content = Files.readString(Path.of(fileName));
				}
				start = comments.get(i).getStart();
				end = comments.get(i).getEnd();
				newContent = comments.get(i).getComment();
				
				/*
				 * System.out.println("Applying change to "+comments.get(i).toString());
				 * System.out.println("start+offset = "+(start+offset));
				 */
				
				//contentBefore = content;
				
				content = content.substring(0,start+offset)
						+ newContent
						+ content.substring(end+offset);
				/*
				 * System.out.println("contentBefore:\n--------");
				 * System.out.println(contentBefore);
				 * System.out.println("--------\ncontentAfter:\n--------");
				 * System.out.println(content+"\n--------");
				 */
				
				offset += newContent.length() - (end - start);
				/*
				 * System.out.println("Generated offset of "+offset);
				 * System.out.println("Other offset "+(content.length()-contentBefore.length()))
				 * ;
				 */
			}
			Files.writeString(Path.of(fileName),content); //Last file changes
			if(!commentsFile.delete()) { //TODO: reform comments file instead of delete? 
				System.out.println("Unable to delete comments file. Do not use this outdated file");
			}
		}
		catch(Exception e) { e.printStackTrace(); }
		
		System.out.println("Done Applying Changes");
	}
	
	
	
	
}
