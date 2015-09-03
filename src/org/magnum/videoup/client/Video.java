package org.magnum.videoup.client;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.base.Objects;


/**
 * A simple object to represent a video and its URL for viewing.
 * 
 * @author jules
 * 
 */
public class Video {

	private String user;
	private long id;
	private String name;
	private String url;
	private long duration;
	private long likes;
	private boolean likeState;
	private Collection<String> likedByList;

	public Video() {
	}

	public Video(String name, String url, long duration) {
		super();
		this.id = 0; 
		this.name = name;
		this.url = url;
		this.duration = duration;
		this.likes = 0; 
		this.likeState = false; 
	}
	
	public String getUser(){
		return user; 
	}
	
	public void setUser(String user){
		this.user = user; 
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public long getLikes() {
		return likes;
	}
	
	public void setLikes(long likes) {
		this.likes = likes;
	}
	
	public void addLike(){
		if (!likeState){
		   likes++;
		   likeState = true;
		}
	}
	
	public void resetLike(){
		if (likeState){
			if (likes > 0){
				likes--;
			}
			likeState = false; 
		}
	}
	
	public boolean getLikeState(){
		return likeState;
	}
	
	public void setLikeState(boolean value){
		likeState = value; 
	}
	
	public void setLikedByList(Collection<String> list){
		likedByList = list; 
	}
	
	public Collection getLikeByList(){
		return likedByList;
	}

	/**
	 * Two Videos will generate the same hashcode if they have exactly the same
	 * values for their name, url, and duration.
	 * 
	 */
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(name, url, duration);
	}

	/**
	 * Two Videos are considered equal if they have exactly the same values for
	 * their name, url, and duration.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Video) {
			Video other = (Video) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(name, other.name)
					&& Objects.equal(url, other.url)
					&& duration == other.duration;
		} else {
			return false;
		}
	}

}
