package org.magnum.videoup.view;

import java.util.Collection;
import java.util.concurrent.Callable;

import org.magnum.videoup.client.CallableTask;
import org.magnum.videoup.client.R;
import org.magnum.videoup.client.TaskCallback;
import org.magnum.videoup.client.Video;
import org.magnum.videoup.client.VideoSvc;
import org.magnum.videoup.client.VideoSvcApi;
import org.magnum.videoup.client.R.id;
import org.magnum.videoup.client.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


/**
 * 
 * This application uses ButterKnife. AndroidStudio has better support for
 * ButterKnife than Eclipse, but Eclipse was used for consistency with the other
 * courses in the series. If you have trouble getting the login button to work,
 * please follow these directions to enable annotation processing for this
 * Eclipse project:
 * 
 * http://jakewharton.github.io/butterknife/ide-eclipse.html
 * 
 */
public class LoginScreenActivity extends Activity {

	protected EditText mUserName;
	protected EditText mPassword;
	protected EditText mServer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);

		mUserName = (EditText) findViewById(R.id.userName);
		mPassword = (EditText) findViewById(R.id.password);
		mServer = (EditText) findViewById(R.id.server);
	}

	public void loginButton(View view) {

		final String user = mUserName.getText().toString();
		final String pass = mPassword.getText().toString();
		final String server = mServer.getText().toString();

		final VideoSvcApi svc = VideoSvc.init(server, user, pass);

		CallableTask.invoke(0, new Callable<Collection<Video>>() {

			@Override
			public Collection<Video> call() throws Exception {
				return svc.getVideoList();
			}

		},

		new TaskCallback<Collection<Video>>() {

			@Override
			public void taskCallback(int com, Collection<Video> result, Exception e) {
				if (e == null) {
					startActivity(new Intent(LoginScreenActivity.this, VideoListActivity.class));
				} else {
					Log.e(LoginScreenActivity.class.getName(), "Error logging in via OAuth.", e);
					Toast.makeText(LoginScreenActivity.this,
							"Login failed, check your Internet connection and credentials.", Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

}
