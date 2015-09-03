package org.magnum.videoup.client;

import java.util.Collection;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * This interface defines an API for a VideoSvc. The
 * interface is used to provide a contract for client/server
 * interactions. The interface is annotated with Retrofit
 * annotations so that clients can automatically convert the
 * 
 * 
 * @author jules
 *
 */
public interface VideoSvcApi {
	
	public static final String ID_PARAMETER = "id";
	
	public static final String PASSWORD_PARAMETER = "password";

	public static final String USERNAME_PARAMETER = "username";

	public static final String TITLE_PARAMETER = "title";
	
	public static final String DURATION_PARAMETER = "duration";
	
	public static final String TOKEN_PATH = "/oauth/token";
	
	// The path where we expect the VideoSvc to live
	public static final String VIDEO_SVC_PATH = "/video";

	// The path to search videos by title
	public static final String VIDEO_TITLE_SEARCH_PATH = VIDEO_SVC_PATH + "/search/findByName";
	
	// The path to search videos by title
	public static final String VIDEO_DURATION_SEARCH_PATH = VIDEO_SVC_PATH + "/search/findByDurationLessThan";
	
	public static final String GET_VIDEO_PATH = "/video/{id}";
	
	public static final String LIKE_VIDEO_PATH = "/video/{id}/like";
	
	public static final String UNLIKE_VIDEO_PATH = "/video/{id}/unlike";
	
	public static final String LIKEDBY_PATH = "/video/{id}/likedby";
	
	@GET(VIDEO_SVC_PATH)
	public Collection<Video> getVideoList();
	
	@POST(VIDEO_SVC_PATH)
	public Video addVideoMetaData(@Body Video v);
	
	@GET(VIDEO_TITLE_SEARCH_PATH)
	public Collection<Video> findByTitle(@Query(TITLE_PARAMETER) String title);
	
	@GET(VIDEO_DURATION_SEARCH_PATH)
	public Collection<Video> findByDurationLessThan(@Query(DURATION_PARAMETER) String title);
	
	@POST(LIKE_VIDEO_PATH)
	public Response likeVideo(@Path(ID_PARAMETER) long id);
	
	@POST(UNLIKE_VIDEO_PATH)
	public Response unlikeVideo(@Path(ID_PARAMETER) long id);
	
	@GET(GET_VIDEO_PATH)
	public Video getVideo(@Path(ID_PARAMETER) long id);
	
	@GET(LIKEDBY_PATH) 
	public Collection<String> getLikedByList(@Path(ID_PARAMETER) long id);
	
}
