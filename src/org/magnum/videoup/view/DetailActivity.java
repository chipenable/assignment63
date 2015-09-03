package org.magnum.videoup.view;

import java.util.ArrayList;
import java.util.Collection;

import org.magnum.videoup.client.DataLoader;
import org.magnum.videoup.client.R;
import org.magnum.videoup.client.TaskCallback;
import org.magnum.videoup.client.Video;
import org.magnum.videoup.client.R.id;
import org.magnum.videoup.client.R.layout;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends Activity implements TaskCallback<ArrayList<Video>>{

	  private final String TAG = "DetailActivity";
	  private static final String VIDEO_ID = "id";
	  
	  private long mId; 
	  
	  private TextView mVideoName;
	  private TextView mVideoOwner;
	  private TextView mVideoDuration; 
	  private TextView mLikedBy;

      public static Intent makeIntent(Context context, long videoId){
    	  Intent intent = new Intent(context, DetailActivity.class);
    	  intent.putExtra(VIDEO_ID, videoId);
    	  return intent;
      }
      
      @Override
      protected void onCreate(Bundle savedInstanceState){
    	  super.onCreate(savedInstanceState);
    	  setContentView(R.layout.activity_details);
    	  
    	  mId = 0; 
    	  
    	  Intent intent = getIntent();
    	  if (intent != null){
    		 mId = intent.getLongExtra(VIDEO_ID,  0);
    	  }
    	  
    	  if (mId == 0){
    		  finish(); 
    	  }
    	  
    	  
    	  mVideoName = (TextView)findViewById(R.id.video_name);
    	  mVideoOwner = (TextView)findViewById(R.id.video_owner);
    	  mVideoDuration = (TextView)findViewById(R.id.video_duration);
    	  mLikedBy = (TextView)findViewById(R.id.liked_by);
    	  
          FragmentManager fm = getFragmentManager();
          DataLoader dataLoader = (DataLoader) fm.findFragmentByTag(TAG);
          if (dataLoader == null) {
              dataLoader = new DataLoader();
              fm.beginTransaction().add(dataLoader, TAG).commit();
          }
           	  
      }

	@Override
	protected void onResume() {
		super.onResume();
		FragmentManager fm = getFragmentManager();
        DataLoader dataLoader = (DataLoader) fm.findFragmentByTag(TAG);
		dataLoader.getVideo(mId);
	}

	@Override
	public void taskCallback(int com, ArrayList<Video> result, Exception e) {
		
		if (result != null){
			if (com == DataLoader.COM_GET_VIDEO){	
				Video v = result.get(0); 
				mVideoName.setText(v.getName());
				mVideoOwner.setText(v.getUser());
				mVideoDuration.setText(Long.toString(v.getDuration()));
				
				FragmentManager fm = getFragmentManager();
		        DataLoader dataLoader = (DataLoader) fm.findFragmentByTag(TAG);
				dataLoader.getLikedByList(v.getId());
			}
			else if (com == DataLoader.COM_GET_LIKED_BY_LIST){
				Video v = result.get(0); 
				Collection<String> list = v.getLikeByList();
				if (list.size() > 0){
				   mLikedBy.setText(list.toString());
				}
			}
		}
		
	}
      

      
      
      


}
