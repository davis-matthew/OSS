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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;

public class Functions {
	
	//TODO: https://jflex.de/
	
	
	/*
	*	Given Link, this will download the repository using
	*   the user's git to repos/reponame/ 
	*/
	public static void DownloadGithubRepo(URL repoLink) {
		try {
			//Create repos folder if it does not exist
			Files.createDirectories(Paths.get(System.getProperty("user.dir") + "/repos/"));
			
			Process p;
			
			System.out.println("Downloading Repo");
			
			//Windows
			if(System.getProperty("os.name").startsWith("Windows")) {
				p = Runtime.getRuntime().exec("cmd /c git clone " + repoLink, null, new File(System.getProperty("user.dir") + "/repos/"));
			}
			
			//Unix
			else {
				p = Runtime.getRuntime().exec("sh -c git clone " + repoLink, null, new File(System.getProperty("user.dir") + "/repos/"));
			}
			
			try {
				p.waitFor(); //hold until process terminates
			} 
			catch (InterruptedException e) {}
			
			System.out.println("Done Downloading Repo");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String response = "", line = "";
			while((line = reader.readLine()) != null) {
				response += line + "\n";
			}
			System.out.println(response);
			
			//TODO: if an error occurs (authentication, github repo doesn't exist, etc.) then handle it
			//TODO: wait for dl to complete? Async issues
		} 
		catch (IOException e) { e.printStackTrace(); }
	}
	
	
	/*
	*	After selecting a folder and clicking parse, this
	*   will create the master comment file
	*/
	static final String[] regexPatterns = new String[] {
			"\\/\\*(\\*(?!\\/)|[^*])*\\*\\/",     //Matches /*Comment*/
			"/\\*.*?\\*/"     // Matches //Comment
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
					CommentStyleSet test = languageMap[fileExtensionToCommentStyle.get(file.getName().substring(file.getName().lastIndexOf(".")))]; 
					files.add(file);
				}
				catch(Exception e) { System.out.println("Unhandled language/extension type of file "+file.getName()); }
			}
		}
	}
	public static void ParseCommentsOfRepo(final File repo) {
		PropagateFilesOfRepo(repo);
		for(final File file : files) {
			System.out.println("Parsing "+file.getName()+" at "+repo.getAbsolutePath());
			parseFile(file);
		}
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
							file.getName(),
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
	}
	
	/*
	*	This method creates the master comment file [repo]_comment.txt in XML format
	*/
	public static void CreateMasterCommentFile(String repoName) {
		try { 
			BufferedWriter masterFile = new BufferedWriter(new FileWriter(new File(repoName+"_comments.txt"))); 
			masterFile.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
			for(Comment comment : comments) {
				masterFile.write(comment.toString()+"\n");
			} 		
			masterFile.close();
		}
		catch(IOException e) {}
	}
	
	/*
	*	Given the master comment file (with changes)
	*   this will fix the comments in each individual file
	*/
	public static void ApplyChangesToRepo(final File repo) {
		comments.clear();
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(true);
			factory.setIgnoringElementContentWhitespace(false);
			
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			File commentFile = new File(repo.getPath()+"/"+repo.getName()+"_comments.txt");
			
			Document doc = builder.parse(commentFile);
			NodeList commentList = doc.getElementsByTagName("Comment");
			
			for(int i = 0; i<commentList.getLength(); i++) {
				Element commentElement = (Element) commentList.item(i);
				comments.add(
					new Comment(
						commentElement.getElementsByTagName("File").item(0).getTextContent(),
						Integer.parseInt(commentElement.getElementsByTagName("Start").item(0).getTextContent()),
						Integer.parseInt(commentElement.getElementsByTagName("End").item(0).getTextContent()),
						commentElement.getElementsByTagName("Text").item(0).getTextContent()
					)
				);
			}
			
			Collections.sort(comments,new CommentSort());
			
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
				
				content = content.substring(0,start+offset)
						+ newContent
						+ content.substring(end+offset);
				
				offset += newContent.length() - (end - start);
			
			}
			
		}
		catch (ParserConfigurationException e) {} //TODO: consider throws instead of try/catch 
		catch (SAXException e) {} 
		catch (IOException e) {}
		
		
		
	}
	
	
	
	
}
