package net.kismo.redditreader.adapters;

import net.kismo.redditreader.R;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PostsListViewCache {
	private View rowView;

	private TextView title;
	private TextView score;
	private TextView numComments;
	private TextView authorName;
	private TextView time;
	
	private ImageView thumbnail;

	public PostsListViewCache(View rowView) {
		this.rowView = rowView;
	}

	public TextView getPostTitle() {
		if (title == null) {
			title = (TextView) rowView.findViewById(R.id.postTitle);
		}
		return title;
	}
	
	public TextView getScore() {
		if (score == null) {
			score = (TextView) rowView.findViewById(R.id.postScore);
		}
		return score;
	}	
	
	public TextView getNumComments() {
		if (numComments == null) {
			numComments = (TextView) rowView.findViewById(R.id.postCommentsCount);
		}
		return numComments;
	}	
	
	public TextView getAuthorName() {
		if (authorName == null) {
			authorName = (TextView) rowView.findViewById(R.id.postSubmittedByUser);
		}
		return authorName;		
	}
	
	public TextView getTime() {
		if (time == null) {
			time = (TextView) rowView.findViewById(R.id.postSubmittedTime);
		}
		return time;
	}
	
	public ImageView getThumbnail() {
		if (thumbnail == null) {
			thumbnail = (ImageView)  rowView.findViewById(R.id.postThumbnail);
		}
		return thumbnail;
	}

}
