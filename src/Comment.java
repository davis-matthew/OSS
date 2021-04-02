import java.util.Comparator;

public final class Comment {
	private String fileName;
	private int startLocation; //TODO: maybe make these longs
	private int endLocation;
	private String comment;
	
	public Comment(String file, int start, int end, String comment) {
		setFile(file);
		setStart(start);
		setEnd(end);
		setComment(comment);
	}
	
	public String getFile() 				{ return fileName; }
	public void setFile(String file) 		{ fileName = file; }

	public int getStart()	 				{ return startLocation; }
	public void setStart(int startPos) 		{ startLocation = startPos; }
	
	public int getEnd()	 					{ return endLocation; }
	public void setEnd(int endPos) 			{ endLocation = endPos; }
	
	public String getComment() 				{ return comment; }
	public void setComment(String comment) 	{ this.comment = comment; }
	
	public int length() { return endLocation - startLocation; }
	
	public boolean overlaps(Comment other) {
		//No pairs of comments should ever get to this function unless they are completely not overlapping
		//	or one is completely contained in another. 
		return 
				(startLocation >= other.getStart() && endLocation <= other.getEnd()) ||
				(startLocation <= other.getStart() && endLocation >= other.getEnd());
		
	}
	
	public String toString() {
		return "<Comment>\n"
				+ "\t<File>\n\t\t"+fileName+"\n\t</File>\n"
				+ "\t<Start>\n\t\t"+startLocation+"\n\t</Start>\n"
				+ "\t<End>\n\t\t"+endLocation+"\n\t</End>\n"
				+ "\t<Text>\n\t\t"+comment.replaceAll("\n","\n\t\t")+"\n\t</Text>\n"
		+ "</Comment>";
	}
	
	
	
	
}
class CommentSort implements Comparator<Comment>
{
    // File then position
    public int compare(Comment a, Comment b)
    {
    	int fileComp = a.getFile().compareTo(b.getFile());
    	
    	if(fileComp!=0) { return fileComp; }
    	return b.getStart() - a.getStart();
    }
}
