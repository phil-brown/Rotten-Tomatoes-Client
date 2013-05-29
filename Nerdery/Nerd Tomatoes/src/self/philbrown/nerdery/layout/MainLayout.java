package self.philbrown.nerdery.layout;

import self.philbrown.AbLE.AbLEActivity;
import self.philbrown.AbLE.annotations.Layout;
import self.philbrown.AbLE.annotations.ORIENTATION;
import self.philbrown.AbLE.annotations.XMLLayout;
import self.philbrown.nerdery.MainActivity;
import self.philbrown.nerdery.controller.NerdController;
import android.graphics.Color;
import android.view.View;
import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

/**
 * Main AbLE Layout. AbLE is an open source Layout engine for Android that I created. It is
 * available on <a href="https://github.com/phil-brown/AbLE">GitHub</a>.
 * @author Phil Brown
 *
 */
@Layout(params = {"match_parent", "match_parent"}, 
        orientation = {ORIENTATION.portrait, ORIENTATION.portraitUpsideDown})
public class MainLayout 
{
	public static int backgroundColor = Color.BLACK;
	
	@XMLLayout(resourceID="main")
	public static class List
	{
		/**
		 * Once the ListView is added to the layout, set its refreshListener to call a method
		 * in the controller class.
		 * @param context
		 * @param v
		 */
		public static void onLayoutComplete(AbLEActivity context, View v)
		{
			final NerdController controller = ((MainActivity) context).getController();
			((PullToRefreshListView) v).setOnRefreshListener(new OnRefreshListener(){

				@Override
				public void onRefresh() 
				{
					controller.refresh();
				}
				
			});
		}
	}
}
