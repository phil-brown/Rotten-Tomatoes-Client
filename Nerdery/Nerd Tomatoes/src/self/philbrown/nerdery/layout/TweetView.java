package self.philbrown.nerdery.layout;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import self.philbrown.AbLE.AbLEActivity;
import self.philbrown.AbLE.AbLEUtil;
import self.philbrown.AbLE.annotations.XMLLayout;
import self.philbrown.nerdery.R;

/**
 * View for sending a tweet. Provides a Text Editor, and cancel and submit buttons.
 * @author Phil Brown
 *
 */
@XMLLayout(resourceID="tweet_view")
public class TweetView 
{
	public static int id = R.id.tweeter;
	
	public static int backgroundColor = Color.parseColor("#AA000000");
	
	public static int visibility = View.GONE;
	
	public static void onLayoutComplete(AbLEActivity context, View v)
	{
		int[] size = new int[2];
		AbLEUtil.getScreenSize(context, size);
		v.setPadding(size[0]/10, size[1]/4, size[0]/10, size[1]/4);
		ViewGroup.LayoutParams params = v.getLayoutParams();
		params.width = size[0];
		params.height = size[1];
		v.setLayoutParams(params);
	}
}
