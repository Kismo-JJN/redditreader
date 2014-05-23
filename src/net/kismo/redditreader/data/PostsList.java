package net.kismo.redditreader.data;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class PostsList extends ArrayList<Post> implements Parcelable{
	private static final long serialVersionUID = 1L;
	
	public PostsList(){
		// empty constructor
	}
	
	public PostsList(Parcel in){
		readFromParcel(in);
	}
	
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		@Override
		public PostsList createFromParcel(Parcel in) {
			return new PostsList(in);
		}

		@Override
		public Object[] newArray(int size) {
			return null;
		}	
	};

	private void readFromParcel(Parcel in){
		this.clear();
		
		// First read the list size
		int size = in.readInt();
		
		// Order for reading parcel is fundamental
		for (int i=0;i<size;i++){
			Post p = new Post();
			
			p.setIdPost(in.readString());
			p.setTitle(in.readString());
			p.setScore(in.readInt());
			p.setNumComments(in.readInt());
			p.setThumbnailUrl(in.readString());
			p.setIsSelf(in.readInt() != 0);
			p.setPostUrl(in.readString());
			p.setSelfText(in.readString());
			p.setAuthorName(in.readString());
			p.setCreateTime(in.readInt());
			
			this.add(p);
		}		
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags){
		int size = this.size();
		
		dest.writeInt(size);
		for (int i=0; i<size; i++){
			Post p = this.get(i);
			
			dest.writeString(p.getIdPost());
			dest.writeString(p.getTitle());
			dest.writeInt(p.getScore());
			dest.writeInt(p.getNumComments());
			dest.writeString(p.getThumbnailUrl());
			dest.writeInt((p.getIsSelf() ? 1 : 0));
			dest.writeString(p.getPostUrl());
			dest.writeString(p.getSelfText());
			dest.writeString(p.getAuthorName());
			dest.writeInt(p.getCreateTime());			
		}
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
