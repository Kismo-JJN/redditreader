package net.kismo.redditreader.data;

public class Post {
	private String idPost;
	private String title;
	private int score;
	private int numComments;
	private String thumbnailUrl;
	private boolean isSelf;
	private String postUrl;
	
	private String selfText;
	private String authorName;
	private int created; // UTC code
	
	public String getIdPost(){	return idPost;}	
	public String getTitle(){return title;}
	public int getScore(){return score;}
	public int getNumComments(){return numComments;}
	public String getThumbnailUrl(){return thumbnailUrl;}
	public boolean getIsSelf(){return isSelf;}
	public String getPostUrl(){return postUrl;}	
	public String getSelfText(){return selfText;}
	public String getAuthorName(){return authorName;}
	public int getCreateTime(){return created;}
	
	public void setIdPost(String value){ this.idPost = value;}	
	public void setTitle(String value){this.title = value;}	
	public void setScore(int value){this.score = value;}
	public void setNumComments(int value){this.numComments = value;}
	public void setThumbnailUrl(String value){this.thumbnailUrl = value;}
	public void setIsSelf(boolean value){this.isSelf = value;}
	public void setPostUrl(String value){this.postUrl = value;}
	public void setSelfText(String value){this.selfText = value;}
	public void setAuthorName(String value){this.authorName = value;}
	public void setCreateTime(int value){this.created = value;}	
}
