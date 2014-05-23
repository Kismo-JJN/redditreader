package net.kismo.redditreader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import net.kismo.redditreader.data.Post;
import net.kismo.redditreader.data.PostsList;

/**
 * A fragment representing a single Post detail screen. This fragment is either
 * contained in a {@link PostListActivity} in two-pane mode (on tablets) or a
 * {@link PostDetailActivity} on handsets.
 */
public class PostDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String KEY_POSTS = "redditposts";
	private static final String TAG = "RedditReader";

	private PostsList mPostContainer;
	private Post mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public PostDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent i = getActivity().getIntent();
		Bundle b = i.getExtras();
		if (getArguments()!=null && getArguments().containsKey(KEY_POSTS)) {
			b = getArguments().getParcelable(KEY_POSTS);
			mPostContainer = b.getParcelable(KEY_POSTS);
			mItem = mPostContainer.get(0);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_post_detail,
				container, false);

		// Load the content of the post in the detail screen
		if (mItem != null) {
			TextView title = (TextView) rootView.findViewById(R.id.post_title);
			TextView detailText = (TextView) rootView.findViewById(R.id.post_detail);
			Button url = (Button) rootView.findViewById(R.id.btn_url);
			title.setText(mItem.getTitle());
			if (mItem.getIsSelf()){
				
				if (mItem.getSelfText()!=null && mItem.getSelfText().length()>0){
					detailText.setText(mItem.getSelfText());
				} else {
					detailText.setText(getString(R.string.msg_end_of_line));
				}
				url.setVisibility(View.GONE);
			} else { // we are dealing with an external link
				detailText.setText(getString(R.string.msg_external_url) + "\n" + mItem.getPostUrl());
				url.setVisibility(View.VISIBLE);
				url.setOnClickListener(new View.OnClickListener() {			
					@Override
					public void onClick(View v) {
						Log.i(TAG, "LAUNCH BROWSER CLICKED.");						
						String url = mItem.getPostUrl();
						
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(url));
						startActivity(i);						
					}
				}); 
			}
		}

		return rootView;
	}
}
