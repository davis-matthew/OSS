import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Collections;

public class Functions {
	
	//TODO: https://jflex.de/
	
	
	/*
	*	Given Link, this will download the repository using
	*   the user's git to repos/reponame/ 
	*/
	public static void GenerateCommentFile(URL repo) {
		String[] repoNameParse = repo.toString().split("/");
		String name = repoNameParse[repoNameParse.length-1];
		name = name.substring(0,name.indexOf(".git"));
		
		DownloadGithubRepo(repo);
		
		//FIXME: Hi! The problem of not downloading can be in part solved by adding a 4th button. 
		//Do that first and we should avoid errors
		ParseCommentsOfRepo(new File(System.getProperty("user.dir") + "/repos/" +name));
		ShrinkAndSortComments();
		CreateMasterCommentFile(name);
	}
	
	/*
	*	Given Link, this will download the repository using
	*   the user's git to repos/reponame/ 
	*/
	public static void DownloadGithubRepo(URL repoLink) {
		
		JOptionPane pane = new JOptionPane();
		
		String repoName = repoLink.toString().substring(repoLink.toString().lastIndexOf("/")+1,repoLink.toString().indexOf(".git"));
		String gitCommand = "clone "+repoLink;
		
		//Create repos folder if it does not exist
		if(Files.notExists(Paths.get(System.getProperty("user.dir") + "/repos/" + repoName))) { 
			try { Files.createDirectories(Paths.get(System.getProperty("user.dir") + "/repos/"));} 
			catch (IOException e) { e.printStackTrace(); }
		}
		else
		{
			// display the showInputDialog to see how the user wants to handle if the repos directory already exists
	        String[] gitOptions = new String[] {"git reset --hard (restore to what's on github)", "git pull (pull changes and keep other edits)"};
			
	        String s = (String)JOptionPane.showInputDialog(
					pane,
					"Select Option to Proceed:",
					"Repo Already Exists!",
					JOptionPane.WARNING_MESSAGE,
					null,
					gitOptions,
					"");
	        
			switch(s) {
				case("git reset --hard (restore to what's on github)"):{ gitCommand = "reset --hard "; break; }
				case("git pull (pull changes and keep other edits)"):{ gitCommand = "pull "; break; }
				default:{ return; }
			}		
			int choices = JOptionPane.showOptionDialog(null, 
			      "Are you Sure?", 
			      "Directory May Be Overwritten!", 
			      JOptionPane.YES_NO_OPTION,  
			      JOptionPane.WARNING_MESSAGE, 
			      null, null, null);
			
			if (choices == JOptionPane.NO_OPTION) { return; }
				
		}
 
		System.out.println("Downloading Repo " + repoLink + " into " + System.getProperty("user.dir") + "/repos/");

		String shellCommand = "sh -c ";
		if(System.getProperty("os.name").startsWith("Windows")) { shellCommand = "cmd /c "; }
		shellCommand += "cd "+System.getProperty("user.dir") + "/repos/"+repoName+" && ";	
		
		try { 
			System.out.println("Running: "+shellCommand+"git "+gitCommand);
			Process p = Runtime.getRuntime().exec(shellCommand+"git "+gitCommand, null, new File(System.getProperty("user.dir") + "/repos/"));
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String response = "", line = "";
			while((line = reader.readLine()) != null) {
				response += line + "\n";
			}
			System.out.println(response);
			p.waitFor(); //hold until process terminates
		} 
		catch (InterruptedException | IOException e) { e.printStackTrace(); }
			
		System.out.println("Done Downloading Repo");
	}
		
	
	
	


	/*
	*	After selecting a folder and clicking parse, this
	*   will create the master comment file
	*/
	static final String[] regexPatterns = new String[] {
			"\\/\\*(\\*(?!\\/)|[^*])*\\*\\/",     //Matches /*Comment*/
			"\\/\\/.*"     // Matches //Comment
	};
	static final CommentStyleSet[] languageMap = new CommentStyleSet[]{ //Refer to the enclosed table "Comment Styles" for the styles & support
			new CommentStyleSet(
					"C, C++, Go, Java, JavaScript, C#, PHP, Rust",										 
					new int[] {0,1}            
			)
	};
	
	
	static final Map<String,Integer> fileExtensionToCommentStyle = Map.of(
			".c", 		0,
			".cpp", 	0,
			".h",		0,
			".java",	0,
			".go",		0,
			".js",		0,
			".cs",		0,
			".rs",		0,
			".php",		0
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
			
			Matcher[] mat = new Matcher[style.regexPatterns.length];
			
			for(int i =0;i<mat.length;i++) {
				mat[i] = Pattern.compile(regexPatterns[style.regexPatterns[i]]).matcher(content);
			}
			
			// Every Comment
			for(Matcher m : mat) {
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
		for(int i = 0; i<comments.size(); i++) {
			for(int j = i+1; j<comments.size(); j++) {
				if(comments.get(i).getFile().equals(comments.get(j).getFile()) && comments.get(i).overlaps(comments.get(j))) {
					if(comments.get(i).length() > comments.get(j).length()) { comments.remove(j); }
					else { comments.remove(i); }
					i=0;j=1; //TODO: This is probably not a good solution to the broken iterator problem.
				}
			}
		}
		Collections.sort(comments,new CommentSort());
		System.out.println("Done Optimizing Comments");
	}
	
	/*
	*	This method creates the master comment file [repo]_comment.txt in XML format
	*/
	public static void CreateMasterCommentFile(String repoName) {
		System.out.println("Creating Comment File");
		try { 
			BufferedWriter masterFile = new BufferedWriter(new FileWriter(new File(repoName+"_comments.txt"))); 
			for(Comment comment : comments) {
				masterFile.write(comment.toString()+"\n");
			} 		
			masterFile.close();
		}
		catch(IOException e) {}
		System.out.println("Created File "+repoName+"_comments.txt");
	}
	
	/*
	*	Given the master comment file (with changes)
	*   this will fix the comments in each individual file
	*/
	public static void ApplyChangesToRepo(final File repo) {
		System.out.println("Applying Changes");
		comments.clear();
		try{
			Scanner input = new Scanner(new File(repo.getName()+"_comments.txt"));
			
			//{File: filename.ext, Start: ##, End: ##}
			//	Text
			//	Text2
			//
			//
			//{File: etc..}
			
			String info = "";
			while(input.hasNextLine()) {
				info = input.nextLine();
				System.out.println(info);
				if(info.length()!=0) {
					
					String file;
					int start, end;
					String text = "";
					
					file = info.substring(info.indexOf(" ")+1,info.indexOf(","));
					info = info.substring(info.indexOf(",")+2);
					start = Integer.parseInt(info.substring(info.indexOf(" ")+1,info.indexOf(",")));
					info = info.substring(info.indexOf(",")+2);
					end = Integer.parseInt(info.substring(info.indexOf(" ")+1,info.indexOf("}")));
					
					
					endofcomment:
					while(true) {
						info = input.nextLine();
						if(info.length()==0) { break endofcomment; }
						text += info.substring(2) +"\n"; //substring 2 gets rid of tabs
					}
					text.substring(0,text.length()-1); //removes last \n... not necessarily smart.
					
					comments.add(new Comment(file,start,end,text));
				}				
			}
			input.close();
		}
		catch (Exception e) { e.printStackTrace(); }
		
		Collections.sort(comments,new CommentSort());
		String contentBefore;
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
				
				System.out.println("Applying change to "+comments.get(i).toString());
				System.out.println("start+offset = "+(start+offset));
				
				contentBefore = content;
				content = content.substring(0,start+offset)
						+ newContent
						+ content.substring(end+offset);
				
				System.out.println("contentBefore:");
				System.out.println(contentBefore);
				System.out.println("contentAfter:");
				System.out.println(content);
				
				offset += newContent.length() - (end - start + 1); //end - start + 1 ??? TODO: this is broken, or perhaps the file format parser has issues
				System.out.println("Generated offset of "+offset);
			}
		}
		catch(Exception e) { e.printStackTrace(); }
		
		System.out.println("Done Applying Changes");
	}
	
	
	
	
}
