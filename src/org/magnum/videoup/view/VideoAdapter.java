package org.magnum.videoup.view;

import java.util.ArrayList;
import java.util.zip.Inflater;

import org.magnum.videoup.client.R;
import org.magnum.videoup.client.Video;
import org.magnum.videoup.client.R.drawable;
import org.magnum.videoup.client.R.id;
import org.magnum.videoup.client.R.layout;

import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VideoAdapter extends BaseAdapter implements OnClickListener {
	
	private Context mContext;
	private ArrayList<Video> mVideoList; 
	private LikeListener mLikeListener; 
	
	interface LikeListener{
		public void like(long id, boolean likeState);
	}
	
	
	public VideoAdapter(Context context, LikeListener l, ArrayList<Video> videoList){
		mContext = context; 
		mVideoList = videoList; 
		mLikeListener = l; 
	}
	
	public void updateVideo(Video v){
		for(int i = 0; i < mVideoList.size(); i++){
			Video video = mVideoList.get(i);
			if (video.getId() == v.getId()){
				video.setLikeState(v.getLikeState());
				video.setLikes(v.getLikes());
				notifyDataSetChanged();
				break; 	
			}
		}
	}
	
	public void likeVideo(long id){
		for(Video v: mVideoList){
			if (v.getId() == id){
				v.addLike();
				notifyDataSetChanged();
				break; 				
			}
		}	
	}

	public void unlikeVideo(long id){
		for(Video v: mVideoList){
			if (v.getId() == id){
				v.resetLike();
				notifyDataSetChanged();
				break; 				
			}
		}
	}	
	
	public void changeData(ArrayList<Video> data){
		mVideoList.clear();
		mVideoList.addAll(data);
		notifyDataSetChanged();		
	}
	
	public void addVideo(Video v){
		mVideoList.add(v);
		notifyDataSetChanged();
	}
	

	@Override
	public int getCount() {
		return mVideoList.size();
	}

	@Override
	public Object getItem(int position) {
		return mVideoList.get(position);
		
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		Video video = (Video)mVideoList.get(position);
		
		if (convertView == null){
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.video_list_item, parent, false);	
			
			viewHolder = new ViewHolder();	
			viewHolder.videoName = (TextView)convertView.findViewById(R.id.video_name);
			viewHolder.videoOwner = (TextView)convertView.findViewById(R.id.video_owner);
			viewHolder.likeCounter = (TextView)convertView.findViewById(R.id.likes);
			viewHolder.likeIcon = (ImageView)convertView.findViewById(R.id.like_icon);
			viewHolder.likeBut = (LinearLayout)convertView.findViewById(R.id.like_layout);
			viewHolder.likeBut.setTag(position);
			viewHolder.likeBut.setOnClickListener(this);
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.videoName.setText(video.getName());
		viewHolder.videoOwner.setText(video.getUser());
		String amountLikes = Long.toString(video.getLikes());
		viewHolder.likeCounter.setText(amountLikes);
		
		int iconSrc = (video.getLikeState())? R.drawable.like1: R.drawable.like0;
		viewHolder.likeIcon.setImageResource(iconSrc);

		//Log.d("Adapter", "name: " + video.getName() + " state: " + Boolean.toString(video.getLikeState()));
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView videoName;
		TextView videoOwner;
		TextView likeCounter;
		ImageView likeIcon; 
		LinearLayout likeBut;
	}


	@Override
	public void onClick(View v) {
		Video video = mVideoList.get((int)v.getTag());
		mLikeListener.like(video.getId(), video.getLikeState());
	}

}
