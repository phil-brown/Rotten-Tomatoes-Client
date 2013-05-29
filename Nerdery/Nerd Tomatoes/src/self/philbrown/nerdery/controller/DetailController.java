package self.philbrown.nerdery.controller;

import java.util.List;
import java.util.Map;

import self.philbrown.AbLE.AbLEActivity;
import self.philbrown.AbLE.AbLEUtil;
import self.philbrown.nerdery.R;
import self.philbrown.nerdery.model.Constants;
import self.philbrown.nerdery.model.Movie;
import self.philbrown.nerdery.model.PhotoGetter;
import self.philbrown.nerdery.store.TwitterCredentialsStore;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the Controller class for the Movie Detail Activity. Once control is delegated, this 
 * Object handles inserting relevant data into the view, as well as user interaction.
 * 
 * @author Phil Brown
 * @since April 2013
 * 
 * @see self.philbrown.nerdery.DetailActivity
 *
 */
public class DetailController
{
	/**
	 * Provides access to the parent Activity
	 */
	private AbLEActivity context;
	/**
	 * Log tag
	 */
	public static final String TAG = "Controller";
	
	/**
	 * Twitter API Key. This is retrieved from meta-data in the Android Manifest.
	 */
	private String twitterKey;
	
	/**
	 * Overlay that displays the twitter login webview
	 */
	private View twitterLogin;
	/**
	 * Overlay that displays the view from which a user can tweet to Twitter
	 */
	private View tweetView;
	/**
	 * Text Editor for writing the tweet shown in {@link #tweetView}
	 */
	private EditText tweetEditor;
	
	/**
	 * Constructor
	 * @param context
	 */
	public DetailController(AbLEActivity context)
	{
		this.context = context;
	}
	
	/**
	 * Called by the Activity to delegate control to this controller
	 * @param contentView the inflated view
	 */
	public void takeControl(View contentView)
	{
		try
		{
			ApplicationInfo app = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			Bundle metaData = app.metaData;
			twitterKey = metaData.getString(context.getString(R.string.twitter_api_key));
			
		}
		catch (Throwable t)
		{
			throw new NullPointerException("Invalid project setup. Must include API Key in Manifest Application Meta-Data!");
		}
		
		twitterLogin = context.findViewById(R.id.overlay_twitter);
		tweetView = context.findViewById(R.id.tweeter);
		tweetEditor = (EditText) context.findViewById(R.id.tweet_editor);
		
		Intent intent = context.getIntent();
        Bundle b = intent.getExtras();
        Movie movie = (Movie) b.getParcelable("movie");
        contentView.setBackgroundColor(b.getInt("background"));
        Log.d(TAG, movie.toString());
        
        tweetEditor.setText("Checking out the ratings for #" + movie.getTitle().replace(" ", "") + " on #NerdTomatoes (Freshness: " + movie.getRatings().criticsScore + "%)! " + movie.getLinks().get("alternate"));
        
        //set view components
        TextView title = (TextView) contentView.findViewById(R.id.movie_title);
        title.setText(movie.getTitle());
        
        FrameLayout imageParent = (FrameLayout) contentView.findViewById(R.id.placeholder);
        PhotoGetter getter = new PhotoGetter(context, imageParent, movie.getPosters().get("original"));
        getter.setCallback(new PhotoGetter.Callback() 
        {
			@Override
			public void onPostExecute(ImageView imageView) 
			{
				if (imageView != null)
					imageView.setScaleType(ScaleType.CENTER_INSIDE);
			}
		});
        getter.execute();
        
        TextView synopsis = (TextView) contentView.findViewById(R.id.synopsis);
        String _synopsis = movie.getSynopsis();
        if (_synopsis == null)
        {
        	_synopsis = "No synopsis available";
        }
        else
        {
        	if (_synopsis.isEmpty())
        	{
        		_synopsis = "No synopsis available";
        	}
        	else if (_synopsis.startsWith("SYNOPSIS:"))
        	{
        		if (_synopsis.equals("SYNOPSIS:"))
        		{
        			_synopsis = "No synopsis available";
        		}
        		else
        			_synopsis = _synopsis.substring(10);
        	}
        }
        synopsis.setText(_synopsis);
        
        TextView cast = (TextView) contentView.findViewById(R.id.cast);
        List<Map<String, Object>> actors = movie.getAbridgedCast();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < actors.size(); i++)
        {
        	Map<String, Object> actor = actors.get(i);
        	String name = (String) actor.get("name");
        	Object temp = actor.get("characters");
        	if (temp != null)
        	{
        		String characters = AbLEUtil.buildCommaSeparatedString((Object[]) temp);
        		builder.append(AbLEUtil.format("%s as %s\n", name, characters));
        		if (i != actors.size()-1)
        			builder.append("\n");
        		
        	}
        	else
        	{
        		//not all actors have listed characters, so display those anyway.
        		builder.append(name);
        		if (i != actors.size()-1)
        			builder.append("\n");
        	}
        }
        cast.setText(builder.toString());
        
        TextView footer = (TextView) contentView.findViewById(R.id.footer);
        String rating = movie.getMpaaRating();
        int freshness = movie.getRatings().criticsScore;
        int runtime = movie.getRuntime();
        int hr = (int)(runtime/60);
        int min = runtime - (hr*60);

        footer.setText(AbLEUtil.format("Rated %s ¥ Freshness: %d%% ¥ Runtime: %dhr %dmin", rating, freshness, hr, min));

	}
	
	/**
	 * Called when the user presses the back button
	 * @return
	 */
	public boolean onBackButtonPressed()
	{
		boolean absorbed = false;
		//hide the twitter login view
		if (twitterLogin.getVisibility() == View.VISIBLE)
		{
			twitterLogin.setVisibility(View.GONE);
			absorbed = true;
		}
		//hide the tweet view
		if (tweetView.getVisibility() == View.VISIBLE)
		{
			tweetView.setVisibility(View.GONE);
			absorbed = true;
		}
		//otherwise, go to the previous activity, and provide a nice transition
		if (!absorbed)
		{
			context.finish();
			context.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		}
		return true;
	}
	
	/**
	 * Called when the user presses the menu button. For this application, the menu button
	 * will allow the user to post a Tweet about the movie review. If the user has not yet
	 * logged in, it will also allow him or her to authorize the app to read and write tweets.
	 * @return always {@code true}
	 */
	public boolean onMenuPressed()
	{
		TwitterCredentialsStore store = TwitterCredentialsStore.sharedStore(context);
		//TODO: show dialog for tweeting, which will be covered by the webview
		if (store.getToken() == null || store.getSecret() == null)
		{
			twitterLogin.setVisibility(View.VISIBLE);
		}
		
		//for now, let's just send a tweet
		else
		{
			tweetView.setVisibility(View.VISIBLE);
			
		}
		
		return true;
	}
	
	/**
	 * Called when the screen orientation changes
	 * @param orientation
	 */
	public void onOrientationChanged(int orientation)
	{
		if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) 
		{
			context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}
	}
	
	/**
	 * Handle clicks registered in XML layout files.
	 * @param v
	 */
	public void onClick(View v)
	{
		switch(v.getId())
		{
		case R.id.btn_submit : {
			TwitterCredentialsStore store = TwitterCredentialsStore.sharedStore(context);
			AccessToken a = new AccessToken(store.getToken(), store.getSecret());
			Twitter twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(twitterKey, Constants.consumerSecret);
			twitter.setOAuthAccessToken(a);
			try {
				twitter.updateStatus(tweetEditor.getText().toString());
				Toast.makeText(context, "Tweet Posted!", Toast.LENGTH_LONG).show();
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			tweetView.setVisibility(View.GONE);
			break;
		}
		case R.id.btn_cancel : {
			tweetView.setVisibility(View.GONE);
			break;
		}
		default : 
			break;
		}
	}

}
