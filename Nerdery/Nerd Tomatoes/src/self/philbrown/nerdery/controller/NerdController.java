package self.philbrown.nerdery.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import self.philbrown.AbLE.AbLEActivity;
import self.philbrown.AbLE.AbLEUtil;
import self.philbrown.core.net.AsyncHttpRequest;
import self.philbrown.nerdery.R;
import self.philbrown.nerdery.model.JSONResponseHandler;
import self.philbrown.nerdery.model.Movie;
import self.philbrown.nerdery.model.MovieListAdapter;
import self.philbrown.nerdery.model.MoviesListParser;
import self.philbrown.nerdery.model.PhotoGetter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import eu.erikw.PullToRefreshListView;

/**
 * This is the Controller class for the Main Activity. Once control is delegated, this 
 * Object handles inserting relevant data into the view, as well as user interaction.
 * 
 * @author Phil Brown
 * @since April 2013
 * 
 * @see self.philbrown.nerdery.MainActivity
 *
 */
public class NerdController
{
	/**
	 * Provides access to the parent Activity
	 */
	private AbLEActivity context;
	
	/**
	 * The Main List view contains a pull-to-refresh mechanism that, when triggered, will get the 
	 * latest top 10 box office earnings movies from Rotten Tomatoes, and display them in the list.
	 */
	private PullToRefreshListView list;
	
	/**
	 * This is the list of movies that are displayed in the {@link #list}.
	 */
	private List<Movie> movies;
	
	/**
	 * This adapter provides a way to inflate each movie as a cell with relevant, unique UI
	 */
	private MovieListAdapter adapter;
	
	/**
	 * Log tag
	 */
	public static final String TAG = "Controller";
	
	/**
	 * Rotten Tomatoes API Key. This is retrieved from meta-data in the Android Manifest.
	 */
	private String rtKey;
	
	/**
	 * Constructor
	 * @param context
	 */
	public NerdController(AbLEActivity context)
	{
		this.context = context;
		
		movies = new ArrayList<Movie>();
		adapter = new MovieListAdapter(context, movies);
		
	}

	/**
	 * Take control of the application
	 * @param contentView the inflated view
	 */
	public void takeControl(View contentView)
	{
		
		try
		{
			ApplicationInfo app = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			Bundle metaData = app.metaData;
			rtKey = metaData.getString(context.getString(R.string.rotten_tomatoes_api_key));
		}
		catch (Throwable t)
		{
			//api key is null - meaning project was not correctly set up
			throw new NullPointerException("Invalid project setup. Must include API Key in Manifest Application Meta-Data!");
		}
		
		list = (PullToRefreshListView) context.findViewById(R.id.listview);
		list.setAdapter(adapter);
		
		refresh();
	}
	
	/**
	 * Updates the data in the list
	 */
	public void refresh()
	{
		JSONResponseHandler handler = new JSONResponseHandler();
		handler.setCallback(new JSONResponseHandler.Callback(){

			int counter = 0;
			
			@Override
			public void invoke(JSONObject json) 
			{
				//saveJSON(json);
				try
				{
					final List<Movie> newMovies = MoviesListParser.parse(json.getJSONArray("movies"));
					movies.clear();
					
					counter = 0;
					for (final Movie m : newMovies)
					{
						Log.d(TAG, m.toString());
						PhotoGetter getter = new PhotoGetter(context, null, m.getPosters().get("thumbnail"));
						getter.setCallback(new PhotoGetter.Callback() {

							@Override
							public void onPostExecute(ImageView imageView) {
								m.setThumbnail(imageView);
								counter++;
								movies.add(m);
								if (counter == newMovies.size()) 
								{
									adapter.notifyDataSetChanged();
									if (list.isRefreshing())
										list.onRefreshComplete();
								}
							}
							
						});
						getter.execute();
					}
					
				}
				catch (Throwable t)
				{
					t.printStackTrace();
				}
				
				
				
			}
			
		});
		String url = AbLEUtil.buildString("http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json?apikey=", rtKey, "&limit=10");
		//This core library class allows a developer to create GET or POST HTTP requests, and execute it in an AsyncTask.
		//The callback is then invoked in onPostExecute().
		AsyncHttpRequest request = AsyncHttpRequest.initGETRequest(url, handler);
		try 
		{
			request.execute();
		} 
		catch (Throwable t) 
		{
			if (list.isRefreshing())
				list.onRefreshComplete();
			t.printStackTrace();
		}
		
	}
	
	/**
	 * Called when the user presses the back button
	 * @return
	 */
	public boolean onBackButtonPressed()
	{
		return false;
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
		
	}
	
	/**
	 * This can be used for debugging the content received from the URL by saving it to the
	 * Android filesystem.
	 * @param json the JSON Object to write into a file.
	 * @param filename the name of the file in which to save {@code json}.
	 */
	public void saveJSON(JSONObject json, String filename)
	{
		//output to external storage to look over
		String storageDir = Environment.getExternalStorageDirectory().toString();
		String mainDirName = AbLEUtil.buildString(storageDir, "/", context.getPackageName());
		
		File logFile = new File(AbLEUtil.buildString(mainDirName, "/", filename));
		//make the parent directory if it does not exist
		logFile.getParentFile().mkdirs();
		try {
			
			FileOutputStream os = new FileOutputStream(logFile, false);
			os.write(json.toString(4).getBytes());
			os.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
