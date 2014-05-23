package net.kismo.redditreader;

import org.json.JSONArray;
import org.json.JSONObject;
import net.kismo.redditreader.adapters.PostsListAdapter;
import net.kismo.redditreader.data.Post;
import net.kismo.redditreader.data.PostsList;
import net.kismo.redditreader.utils.DialogUtil;
import net.kismo.redditreader.utils.NetworkUtil;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

/**
 * An activity representing a list of Posts. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link PostDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link PostListFragment} and the item details (if present) is a
 * {@link PostDetailFragment}.
 * <p>
 * This activity also implements the required {@link PostListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class PostListActivity extends FragmentActivity implements
		PostListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	
	private static final String TAG = "RedditReader";
	private static final int ACCESS_ERROR = 0;
	private static final int ACCESS_SUCCESS = 1;
	
	private PostsListAdapter postsAdapter;
	private PostsList mPosts;
	
	private String mBeforeKey = "";
	private String mAfterKey = "";
	
    //private ProgressDialog mProgressDialog = null;
    private Runnable viewPosts= new Runnable(){
		@Override
		public void run() {
			try {
				getPosts();
			} catch (Exception e) {
				e.printStackTrace();
				handler.sendEmptyMessage(ACCESS_ERROR);
			} finally {
				handler.sendEmptyMessage(ACCESS_SUCCESS);
			}			
		}			
	};
    
    private Handler handler = new Handler(new Handler.Callback() {		
		@Override
		public boolean handleMessage(Message msg) {
			int msgValue;
			
			msgValue= msg.what;
			switch (msgValue){
			case ACCESS_ERROR:
				Toast.makeText(PostListActivity.this, getString(R.string.msg_error_loading_posts), Toast.LENGTH_SHORT).show();
				break;
			case ACCESS_SUCCESS:				
				LoadResults();
				
				DialogUtil.hideProgressDialog();           
			}			
			return false;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_list);	
		
		if (findViewById(R.id.post_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((PostListFragment) getSupportFragmentManager().findFragmentById(
					R.id.post_list)).setActivateOnItemClick(true);
		}
		
		ThreadGetPosts();		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_reset:
	        	mBeforeKey="";
	        	mAfterKey="";
	        	ClearDetails();

	        	ThreadGetPosts();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * Callback method from {@link PostListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(Post p) {		
		Bundle arguments = new Bundle();
		PostsList temp = new PostsList();
		temp.add(p);
		arguments.putParcelable(PostDetailFragment.KEY_POSTS, temp);		
		
		if (mTwoPane) {
			// It's a tablet, so update the detail fragment			
			// Pass the selected Post object to the detail fragment	
			
			Bundle b = new Bundle();
			b.putParcelable(PostDetailFragment.KEY_POSTS, arguments);

			PostDetailFragment fragment = new PostDetailFragment();
			fragment.setArguments(b);
			
			getSupportFragmentManager().beginTransaction()
				.replace(R.id.post_detail_container, fragment).commit();			

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.			
			Intent detailIntent = new Intent(this, PostDetailActivity.class);
			detailIntent.putExtra(PostDetailFragment.KEY_POSTS, arguments);
			startActivity(detailIntent);
		}
	}	
	
    private void ThreadGetPosts() {
    	final Thread thread = new Thread(null, viewPosts, "ViewPosts");
    	
    	DialogUtil.showProgressDialog(PostListActivity.this, getString(R.string.msg_please_wait), true,
                new DialogInterface.OnCancelListener(){
        	@Override
            public void onCancel(DialogInterface dialog) {
            	thread.interrupt();
                finish();
            	}
    		}  
    	);
    	
    	thread.start();    	
    }  	
	
    private void getPosts() throws Exception {
    	try { 
    		GetPostList();
        } catch (Exception e) {
            throw e;
        }    	
    }	
	
    private void LoadResults(){
		PostListFragment postsList = ((PostListFragment) getSupportFragmentManager().findFragmentById(
				R.id.post_list));	            
		postsAdapter= new PostsListAdapter(PostListActivity.this, R.layout.posts_row, mPosts);
		
		ListView lv = postsList.getListView();		
		View header = getLayoutInflater().inflate(R.layout.header, lv, false);
	    View footer = getLayoutInflater().inflate(R.layout.footer,  lv, false);

	    if (mBeforeKey!=null && (mBeforeKey.length()>0)){	
	    	if (lv.getHeaderViewsCount()<1){
	    		lv.addHeaderView(header, null, true);
	    	}
			View prev = findViewById(R.id.btn_prev);
		    prev.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {	
					mAfterKey = "";
					ClearDetails();
					ThreadGetPosts();
				}});
	    }
	    
	    if (mAfterKey!=null && (mAfterKey.length()>0)){
	    	if (lv.getFooterViewsCount()<1){
	    		lv.addFooterView(footer, null, true);
	    	}
		    View next = findViewById(R.id.btn_next);
		    next.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v) {
					mBeforeKey="";
					ClearDetails();
					ThreadGetPosts();					
				}});			    
	    }
		lv.setAdapter(postsAdapter);    	
    }
    
    private void ClearDetails(){
		PostDetailFragment fragment = (PostDetailFragment) getSupportFragmentManager().findFragmentById(R.id.post_detail_container);
		if (fragment!=null){
			getSupportFragmentManager().beginTransaction()
				.remove(fragment).commit();
		}		
    }
    
	private void GetPostList(){
		PostsList resultList = new PostsList();
		
		String result = "";
        try { 
      	  	NetworkUtil request = new NetworkUtil();	
			
			String url = getString(R.string.reddit_json_url);
			if (mAfterKey.length()>0){
				url = url + "?after=" + mAfterKey;
			} else if (mBeforeKey.length()>0){
				url = url + "?after=" + mBeforeKey;
			}
			
			result = request.get(url);
			
			JSONObject jsonObject = new JSONObject(result);

	        if (jsonObject.has("data")){
	        	JSONObject jsonData = jsonObject.getJSONObject("data");
	        	
	        	mAfterKey= jsonData.getString("after");
	        	mBeforeKey = jsonData.getString("before");
	        	if (mAfterKey!=null && mAfterKey.equalsIgnoreCase("null")){mAfterKey="";}
	        	if (mBeforeKey!=null && mBeforeKey.equalsIgnoreCase("null")){mBeforeKey="";}
	        	
	        	JSONArray jsonChildren = jsonData.getJSONArray("children");
			    Log.i(TAG, "Number of entries " + jsonChildren.length());
			    
			    for (int i=0;i<jsonChildren.length(); i++){
			    	JSONObject jsonChild = (jsonChildren.getJSONObject(i)).getJSONObject("data");;

			    	String idPost = jsonChild.getString("id");
			    	String title = jsonChild.getString("title");
			    	int score = jsonChild.getInt("score");
			    	int numComments = jsonChild.getInt("num_comments");
			    	String thumbnailUrl = jsonChild.getString("thumbnail");
			    	boolean isSelf = jsonChild.getBoolean("is_self");
			    	String postUrl = jsonChild.getString("url");
			    	String selfText = jsonChild.getString("selftext");
			    	String authorName = jsonChild.getString("author");
			    	int createTime = jsonChild.getInt("created");			    	
			    	
			    	Post p = new Post();
			    	p.setIdPost(idPost);
			    	p.setTitle(title);
			    	p.setScore(score);
			    	p.setNumComments(numComments);
			    	p.setThumbnailUrl(thumbnailUrl);
			    	p.setIsSelf(isSelf);
			    	p.setPostUrl(postUrl);
			    	p.setSelfText(selfText);
			    	p.setAuthorName(authorName);
			    	p.setCreateTime(createTime);
			    	
			    	resultList.add(p);
			    }	
	        }			
        } 
        catch (Exception e) {
          e.printStackTrace();            
        }         
        mPosts = resultList;
	}
}
