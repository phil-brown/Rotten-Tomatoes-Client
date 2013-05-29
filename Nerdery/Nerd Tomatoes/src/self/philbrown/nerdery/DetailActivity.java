package self.philbrown.nerdery;

import self.philbrown.AbLE.AbLEActivity;
import self.philbrown.nerdery.controller.DetailController;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

/**
 * This Activity is displayed to the user when a list item is selected from the main view. Its 
 * layout file is configured in the Android Manifest, and handled by AbLE, my Annotation-based 
 * Layout Engine.
 * 
 * This layout shows more details about the selected Movie, including its title, movie poster,
 * synopsis, abridged cast, rating, freshness, and length. Pressing the menu button will also allow
 * the user to login to Twitter and post a Tweet about the movie.
 * 
 * @author Phil Brown
 * @since April 2013
 * 
 * @see self.philbrown.nerdery.layout.DetailView
 * @see self.philbrown.nerdery.controller.DetailController
 */
public class DetailActivity extends AbLEActivity
{
	/**
	 * Provides access to the controller used with this Activity
	 */
	private DetailController controller;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	//sometimes, controllers can be used within a single-activity layout file, so the order
    	//of these calls is best practice for AbLE
    	controller = new DetailController(this);
        super.onCreate(savedInstanceState);
        controller.takeControl(contentView);
    }
    
    /**
     * Allows outside classes to get access to the controller
     * @return this Activity's Controller
     */
    public DetailController getController()
    {
    	return controller;
    }
    
    /**
     * Pass screen orientation changes (only configuration change that is registered in the Android
     * Manifest) to the Controller.
     */
    @Override
	public void onConfigurationChanged (Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		controller.onOrientationChanged(newConfig.orientation);
	}
    
    /**
     * Pass back-button clicks to the controller
     */
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			if(controller.onBackButtonPressed())
			{
				return true;
			}
		}
		else if (keyCode == KeyEvent.KEYCODE_MENU)
		{
			if(controller.onMenuPressed())
			{
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
    
    /**
     * Pass clicks registered in XML to the controller
     * @param v the clicked view
     */
    public void onClick(View v)
    {
    	controller.onClick(v);
    }
}
