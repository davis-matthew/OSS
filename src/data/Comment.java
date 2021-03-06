package data;

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
	
	public int length() 					{ return endLocation - startLocation; }
	
	public boolean overlaps(Comment other) {
		//No pairs of comments should ever get to this function unless they are completely not overlapping
		//	or one is completely contained in another. 
		return 
				(startLocation >= other.getStart() && endLocation <= other.getEnd()) ||
				(startLocation <= other.getStart() && endLocation >= other.getEnd());
		
	}
	
	public String toString() {
		return "\7{File: "+fileName+", Start: "+startLocation+", End: "+endLocation+"}\7\n"+ comment;
	}
}
