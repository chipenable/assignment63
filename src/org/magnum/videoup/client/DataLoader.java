package org.magnum.videoup.client;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.magnum.videoup.utils.VideoMediaStoreUtils;

import retrofit.RestAdapter;
import retrofit.client.Response;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class DataLoader extends Fragment {

	public static final String TAG = "DataLoader";

	public static final int COM_GET_VIDEOS = 1;
	public static final int COM_GET_VIDEO = 2;
	public static final int COM_LIKE_VIDEO = 3;
	public static final int COM_UNLIKE_VIDEO = 4;
	public static final int COM_ADD_VIDEO = 5;
	public static final int COM_GET_LIKED_BY_LIST = 6; 
	

	public static final int HTTP_OK = 200;
	public static final int HTTP_NOT_FOUND = 404;
	public static final int HTTP_HTTP_BAD_REQUEST = 400;

	private TaskCallback<ArrayList<Video>> mCallbackFunc = null;

	/***************************************************************/

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallbackFunc = (TaskCallback<ArrayList<Video>>) activity;
		Log.d(TAG, "onAttach");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbackFunc = null;
		Log.d(TAG, "onDetach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	public void likeVideo(final long id) {
		final VideoSvcApi svc = VideoSvc.getOrShowLogin(getActivity());
		CallableTask.invoke(COM_LIKE_VIDEO, new Callable<ArrayList<Video>>() {

			@Override
			public ArrayList<Video> call() throws Exception {

				Video v;
				Response response = svc.likeVideo(id);
				if (response.getStatus() == HTTP_OK) {
					v = svc.getVideo(id);
					if (v != null) {
						ArrayList<Video> list = new ArrayList<Video>();
						list.add(v);
						return list;
					}
					
					/*Collection<Video> videoCollection = svc.getVideoList();
                	ArrayList<Video> list = new ArrayList<Video>();
					list.addAll(videoCollection);
					return list;*/
				}
				return null;
			}

		}, mCallbackFunc);
	}

	public void unlikeVideo(final long id) {
		final VideoSvcApi svc = VideoSvc.getOrShowLogin(getActivity());
		CallableTask.invoke(COM_UNLIKE_VIDEO, new Callable<ArrayList<Video>>() {

			@Override
			public ArrayList<Video> call() throws Exception {

				Video v;
				Response response = svc.unlikeVideo(id);
				if (response.getStatus() == HTTP_OK) {
					v = svc.getVideo(id);
					if (v != null) {
						ArrayList<Video> list = new ArrayList<Video>();
						list.add(v);
						return list;
					}
					/*Collection<Video> videoCollection = svc.getVideoList();
                	ArrayList<Video> list = new ArrayList<Video>();
					list.addAll(videoCollection);
					return list;*/
				}
				return null;
			}

		}, mCallbackFunc); 

	}

	public void addVideoMetaData(final Video v) {
		final VideoSvcApi svc = VideoSvc.getOrShowLogin(getActivity());
		CallableTask.invoke(COM_ADD_VIDEO, new Callable<ArrayList<Video>>() {

			@Override
			public ArrayList<Video> call() throws Exception {
                Video video = svc.addVideoMetaData(v);
                if (video != null) {
                	Collection<Video> videoCollection = svc.getVideoList();
                	ArrayList<Video> list = new ArrayList<Video>();
					list.addAll(videoCollection);
					return list;
				}
                return null;
			}
			
		}, mCallbackFunc);
	}

	public void getVideo(final long id){
    	if (id == 0) return; 
    	
    	final VideoSvcApi svc = VideoSvc.getOrShowLogin(getActivity());
    	CallableTask.invoke(COM_GET_VIDEO, new Callable<ArrayList<Video>>() {

			@Override
			public ArrayList<Video> call() throws Exception {
				Video v = svc.getVideo(id);
				if (v != null){
				   ArrayList<Video> list =  new ArrayList<Video>();
				   list.add(v);
				   Log.d(TAG, "ok");
				   return list;
				}
				return null;
			}
		}, mCallbackFunc);    	
    	
    }

	public ArrayList<Video> getVideos() {
			ArrayList<Video> list = new ArrayList<Video>();
			final VideoSvcApi svc = VideoSvc.getOrShowLogin(getActivity());
			CallableTask.invoke(COM_GET_VIDEOS, new Callable<ArrayList<Video>>() {

				@Override
				public ArrayList<Video> call() throws Exception {
					Collection<Video> videoCollection = svc.getVideoList();
					ArrayList<Video> list = new ArrayList<Video>();
					list.addAll(videoCollection);
					return list;
				}

			}, mCallbackFunc);
			
		return list;
	}

	
	public void getLikedByList(final long id){
		
		final VideoSvcApi svc = VideoSvc.getOrShowLogin(getActivity());
		CallableTask.invoke(COM_GET_LIKED_BY_LIST, new Callable<ArrayList<Video>>() {

			@Override
			public ArrayList<Video> call() throws Exception {
				Collection<String> list = svc.getLikedByList(id);
				if (list != null){
				   Video v = new Video();
				   v.setLikedByList(list);
				   ArrayList<Video> videoList =  new ArrayList<Video>();
				   videoList.add(v);
				   return videoList; 	
				}
				return null;
			}
			
			
		}, mCallbackFunc);
		
	}
	
}
