package org.magnum.videoup.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import org.magnum.videoup.client.DataLoader;
import org.magnum.videoup.client.R;
import org.magnum.videoup.client.TaskCallback;
import org.magnum.videoup.client.Video;
import org.magnum.videoup.client.R.id;
import org.magnum.videoup.client.R.layout;
import org.magnum.videoup.client.R.menu;
import org.magnum.videoup.client.R.string;
import org.magnum.videoup.utils.VideoMediaStoreUtils;
import org.magnum.videoup.view.VideoAdapter.LikeListener;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class VideoListActivity extends Activity implements TaskCallback<ArrayList<Video>>, LikeListener {

	public final static String TAG = "VideoListActivity";

	private final int REQUEST_GET_VIDEO = 1;

	private ListView mVideoList;
	private VideoAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_list);
		mVideoList = (ListView) findViewById(R.id.videoList);

		FragmentManager fm = getFragmentManager();
		DataLoader dataLoader = (DataLoader) fm.findFragmentByTag(TAG);
		if (dataLoader == null) {
			dataLoader = new DataLoader();
			fm.beginTransaction().add(dataLoader, TAG).commit();
		}

		mAdapter = new VideoAdapter(this, this, new ArrayList<Video>());
		mVideoList.setAdapter(mAdapter);
		mVideoList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Video v = (Video) mAdapter.getItem(position);
				Intent intent = DetailActivity.makeIntent(VideoListActivity.this, v.getId());
				startActivity(intent);

			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

		FragmentManager fm = getFragmentManager();
		DataLoader dataLoader = (DataLoader) fm.findFragmentByTag(TAG);
		mAdapter.changeData(dataLoader.getVideos());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.video_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_add_video) {
			openGallery();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Uri videoUri = null;

		if (resultCode == Activity.RESULT_OK) {

			if (requestCode == REQUEST_GET_VIDEO) {
				videoUri = data.getData();
			}

			if (videoUri != null) {
				Toast.makeText(this, R.string.uploading_video, Toast.LENGTH_SHORT).show();

				FragmentManager fm = getFragmentManager();
				DataLoader dataLoader = (DataLoader) fm.findFragmentByTag(TAG);
				if (dataLoader != null) {
					Video v = VideoMediaStoreUtils.getVideo(this, videoUri);
					dataLoader.addVideoMetaData(v);
				}
			}
		}

		if (videoUri == null) {
			Toast.makeText(this, R.string.uploading_error, Toast.LENGTH_SHORT).show();
		}
	}

	private void openGallery() {
		final Intent videoGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
		videoGalleryIntent.setType("video/*");
		videoGalleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

		// Verify the intent will resolve to an Activity.
		if (videoGalleryIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(videoGalleryIntent, REQUEST_GET_VIDEO);
		}

	}

	@Override
	public void taskCallback(int com, ArrayList<Video> data, Exception e) {
        long id; 
		
		if (data != null) {
			
			switch (com) {
			case DataLoader.COM_GET_VIDEOS:
				mAdapter.changeData(data);
				break;

			case DataLoader.COM_LIKE_VIDEO:
				mAdapter.likeVideo(data.get(0).getId());
				//mAdapter.changeData(data);
				break;

			case DataLoader.COM_UNLIKE_VIDEO:
				mAdapter.unlikeVideo(data.get(0).getId());
				//mAdapter.changeData(data);
				break;

			case DataLoader.COM_ADD_VIDEO:
				mAdapter.changeData(data);
				break;

			default:

			}
		}

	}

	@Override
	public void like(long id, boolean likeState) {

		FragmentManager fm = getFragmentManager();
		DataLoader dataLoader = (DataLoader) fm.findFragmentByTag(TAG);
		if (likeState == false) {
			Log.d(TAG, "like video");
			dataLoader.likeVideo(id);
		} else {
			Log.d(TAG, "unlike video");
			dataLoader.unlikeVideo(id);
		}
	}

}
