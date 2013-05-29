package self.philbrown.nerdery;

import self.philbrown.AbLE.AbLEActivity;
import self.philbrown.nerdery.controller.NerdController;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

/**
 * This is the first Activity that is displayed when the application starts. Its layout file
 * is configured in the Android Manifest, and handled by AbLE, my Annotation-based Layout Engine.
 * 
 * This layout contains a Pull-To-Refresh ListView, which is populated with the top 10 Box Office
 * Earnings movies list from Rotten Tomatoes.
 * 
 * @author Phil Brown
 * @since April 2013
 * 
 * @see self.philbrown.nerdery.layout.MainLayout
 * @see self.philbrown.nerdery.controller.NerdController
 */
public class MainActivity extends AbLEActivity {
    
	/**
	 * Provides access to the controller used with this Activity
	 */
	private NerdController controller;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	//sometimes, controllers can be used within a single-activity layout file, so the order
    	//of these calls is best practice for AbLE
    	controller = new NerdController(this);
        super.onCreate(savedInstanceState);
        controller.takeControl(contentView);
    }
    
    /**
     * Allows outside classes to get access to the controller
     * @return this Activity's Controller
     */
    public NerdController getController()
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