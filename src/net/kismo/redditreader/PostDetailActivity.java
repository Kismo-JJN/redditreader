package net.kismo.redditreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

/**
 * An activity representing a single Post detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link PostListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link PostDetailFragment}.
 */
public class PostDetailActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_detail);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			
			Intent i = getIntent();
			Bundle b = i.getExtras();
			b = getIntent().getExtras();

			PostDetailFragment fragment = new PostDetailFragment();
			fragment.setArguments(b);
			
			getSupportFragmentManager().beginTransaction()
				.add(R.id.post_detail_container, fragment).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {

			NavUtils.navigateUpTo(this,
					new Intent(this, PostListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
