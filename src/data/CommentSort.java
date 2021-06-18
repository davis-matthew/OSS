package data;

import java.util.Comparator;

public class CommentSort implements Comparator<Comment>
{
    // File then position
    public int compare(Comment a, Comment b)
    {
    	int fileComp = a.getFile().compareTo(b.getFile());
    	if(fileComp!=0) { return fileComp; }
    	return b.getStart() - a.getStart();
    }
}