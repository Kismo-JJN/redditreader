package net.kismo.redditreader.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import net.kismo.redditreader.R;
import net.kismo.redditreader.data.Post;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PostsListAdapter extends ArrayAdapter<Post> {

	private List<Post> data;
	private View rowView;
	
	public PostsListAdapter(Context context, int resource, List<Post> data) {
		super(context, R.layout.posts_row, data);
		this.data = data;
	}

	public int getCount() {
		return (data != null) ? data.size() : 0;
	}

	public Post getItem(int position) {
		return data.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Activity activity = (Activity) getContext();
		rowView = convertView;
		PostsListViewCache viewCache = null;

		// Get the cached view
		if (rowView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			rowView = inflater.inflate(R.layout.posts_row, null);
			viewCache = new PostsListViewCache(rowView);
			rowView.setTag(viewCache);

		} else {
			viewCache = (PostsListViewCache) rowView.getTag();
		}

		Post redditPost = getItem(position);
		
		// Score
		TextView postScore = viewCache.getScore();
		postScore.setText(String.valueOf(redditPost.getScore()));		

		// Post Title
		TextView postTitle = viewCache.getPostTitle();
		postTitle.setText(redditPost.getTitle());
		
		// submitted info
		TextView postTime = viewCache.getTime();
		
		String submittedTime = getSubmitTime(redditPost.getCreateTime());
		postTime.setText(submittedTime);
		
		TextView postAuthor = viewCache.getAuthorName();
		postAuthor.setText(redditPost.getAuthorName());
		
		// Thumbnail
		ImageView postThumbnail = viewCache.getThumbnail();
		if (redditPost.getThumbnailUrl().length()>0){
			UrlImageViewHelper.setUrlDrawable(postThumbnail, redditPost.getThumbnailUrl());
			postThumbnail.setVisibility(View.VISIBLE);
		} else {
			postThumbnail.setVisibility(View.GONE);
		}
		
		// comments
		TextView postComments = viewCache.getNumComments();
		String comments = ((redditPost.getNumComments()==1?"1 comment": (redditPost.getNumComments() + " comments")));
		postComments.setText(comments);
		
		return rowView;
	}
	
	private String getSubmitTime(int utc){
		String result="";		
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SZ", Locale.getDefault());
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date currentDate = new Date();
		
		long difference = currentDate.getTime() - utc; 
		long x = difference / 1000;
		long hours = x % 24;
		
		result = "submitted " + ((hours==1)?"1 hour": (hours + " hours")) + " ago by ";
		return result;
	}
}
