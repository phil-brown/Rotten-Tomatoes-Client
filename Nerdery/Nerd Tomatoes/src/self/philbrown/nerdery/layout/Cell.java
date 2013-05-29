package self.philbrown.nerdery.layout;

import self.philbrown.AbLE.AbLEActivity;
import self.philbrown.AbLE.AbLEUtil;
import self.philbrown.AbLE.annotations.Layout;
import self.philbrown.AbLE.annotations.Setter;
import self.philbrown.AbLE.annotations.Variable;
import self.philbrown.AbLE.annotations.XMLLayout;
import self.philbrown.nerdery.R;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * This class is used by AbLE to inflate a view to be used as a cell in the movies list view.
 * @author Phil Brown
 *
 */
@Layout(viewClass="android.widget.RelativeLayout")
public class Cell
{
	@Variable
	public static AbsListView.LayoutParams params;
	
	static
	{
		//This fixes a class cast exception that occurs with ListViews when the layout params of a cell are already set
		params = new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
	}
	
	/**
	 * This sets the layout params to be of type {@code AbsListView.LayoutParams}, which fixes a 
	 * {@code ClassCastException} that occurs for programmatically-created {@code ListView} cells.
	 */
	public static ViewGroup.LayoutParams layoutParams = params;
	
	/** The default background color is white */
	public static int backgroundColor = Color.WHITE;
	
	/**
	 * This {@code FrameLayout} contains the image thumbnail, and its background and padding
	 * are designed to give the image a <em>shadow</em> effect.
	 */
	@Layout(padding={1,1,4,4})
	public static class Image
	{
		public static int id = R.id.imageparent;
		
		public static int backgroundResource = R.drawable.shadow;
		
		@Variable
		public static RelativeLayout.LayoutParams params;
		
		static
		{
			params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		}
		
		public static ViewGroup.LayoutParams layoutParams = params;

	}
	
	/**
	 * This {@code LinearLayout} contains everything to the right of the thumbnail.
	 */
	@Layout(viewClass="android.widget.LinearLayout")
	public static class Body
	{
		public static int orientation = LinearLayout.VERTICAL;
		
		@Variable
		public static RelativeLayout.LayoutParams params;
		
		static
		{
			params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.RIGHT_OF, Image.id);
			params.addRule(RelativeLayout.CENTER_VERTICAL);
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params.leftMargin = (int) AbLEActivity.convertDipToPixels(10);
		}
		
		public static ViewGroup.LayoutParams layoutParams = params;
		
		@Layout(viewClass="android.widget.RelativeLayout")
		public static class MovieInfo
		{
			
			@Layout(viewClass="android.widget.TextView")
			public static class Text 
			{
				public static int id = R.id.movie_title;
				
				public static int textColor = Color.BLACK;
				
				@Setter
				public static Object textSize = new Object[]{TypedValue.COMPLEX_UNIT_SP, 24f};
				
				@Setter
				public static Object typeface = new Object[]{Typeface.DEFAULT, Typeface.BOLD};
				
				@Variable
				public static RelativeLayout.LayoutParams params;
				
				static
				{
					params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
					params.rightMargin = (int) AbLEActivity.convertDipToPixels(5);
					params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					params.addRule(RelativeLayout.CENTER_VERTICAL);
				}
				
				public static ViewGroup.LayoutParams layoutParams = params;
				
				public static void onLayoutComplete(AbLEActivity context, View v)
				{
					TextView tv = (TextView) v;
					if (tv.getTextSize() < 14) 
					{
						tv.setTextSize(14);
					}
					
					Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/TheanoDidot-Regular.ttf");
					tv.setTypeface(tf);
				}
				
			}
			
			@Layout(viewClass="android.widget.ImageView")
			public static class Rating 
			{
				public static int id = R.id.rating;
				
				@Variable
				public static RelativeLayout.LayoutParams params;
				
				static
				{
					params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.RIGHT_OF, Text.id);
					params.addRule(RelativeLayout.CENTER_VERTICAL);
				}
				
				public static ViewGroup.LayoutParams layoutParams = params;
			}
		}
		
		/**
		 * This Progress Bar is inflated from an XML resource
		 */
		@XMLLayout(resourceID="progress")
		public static class Score
		{
			/**
			 * Once the view has been laid out, this method is called to update its dimensions
			 * @param context
			 * @param v
			 */
			public static void onLayoutComplete(AbLEActivity context, View v)
			{
				//set the width of the progress bar to half the screen width
				int[] size = new int[2];
				AbLEUtil.getScreenSize(context, size);
				ViewGroup.LayoutParams params = v.getLayoutParams();
				params.width = size[0]/2;
				v.setLayoutParams(params);
			}
		}
		
		
	}
			
	
}