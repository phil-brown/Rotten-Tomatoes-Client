package self.philbrown.nerdery.layout;

import self.philbrown.AbLE.AbLEActivity;
import self.philbrown.AbLE.AbLEUtil;
import self.philbrown.AbLE.annotations.Embed;
import self.philbrown.AbLE.annotations.Layout;
import self.philbrown.AbLE.annotations.Setter;
import self.philbrown.AbLE.annotations.Variable;
import self.philbrown.nerdery.R;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * This {@code FrameLayout} defines the content displayed in DetailActivity.
 * @author Phil Brown
 */
@Layout
public class DetailView 
{
	@Layout(viewClass="android.widget.ScrollView")
	public static class ScrollView 
	{
		public static int fadingEdgeLength = 0;
		
		public static int backgroundColor = Color.WHITE;
		
		@Layout(viewClass="android.widget.RelativeLayout", params={"fill_parent", "fill_parent"})
		public static class Content
		{
			
			@Layout(viewClass="android.widget.TextView")
			public static class Title
			{
				public static int id = R.id.movie_title;
				
				@Variable
				public static int dip10 = (int) AbLEActivity.convertDipToPixels(10);
				
				@Setter
				public static Object padding = new Object[]{0, dip10, 0, dip10};
				
				@Variable
				public static RelativeLayout.LayoutParams params;
				
				static
				{
					params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.CENTER_HORIZONTAL);
					params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				}
				
				public static ViewGroup.LayoutParams layoutParams = params;
				
				public static float textSize = AbLEActivity.convertDipToPixels(56f);
				public static int textColor = Color.BLACK;
				
			}
			
			@Layout
			public static class ImageContainer
			{
				public static int id = R.id.placeholder;
				
				@Variable
				public static int dip5 = (int) AbLEActivity.convertDipToPixels(5);
				
				@Setter
				public static Object padding = new Object[]{dip5, 0, dip5, 0};
				
				@Variable
				public static RelativeLayout.LayoutParams params;
				
				static
				{
					params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.CENTER_HORIZONTAL);
					params.addRule(RelativeLayout.BELOW, Title.id);
				}
				
				public static ViewGroup.LayoutParams layoutParams = params;
				
				public static void onLayoutComplete(AbLEActivity context, View v)
				{
					ViewGroup.LayoutParams params = v.getLayoutParams();
					int[] size = new int[2];
					AbLEUtil.getScreenSize(context, size);
					params.height = size[1]/2;
					v.setLayoutParams(params);
				}
			}
			
			@Layout(viewClass="android.widget.TextView", padding={15})
			public static class SynopsisHeader
			{
				
				public static int id = R.id.label_synopsis;

				public static CharSequence text = "Synopsis";
				
				public static int textColor = Color.BLACK;
				
				@Setter
				public static Object textSize = new Object[]{TypedValue.COMPLEX_UNIT_SP, 36f};
				
				@Setter
				public static Object typeface = new Object[]{Typeface.DEFAULT, Typeface.BOLD};

				@Variable
				public static RelativeLayout.LayoutParams params;
				
				static
				{
					params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					params.addRule(RelativeLayout.BELOW, ImageContainer.id);
				}
				
				public static ViewGroup.LayoutParams layoutParams = params;
			}
			
			@Layout(viewClass="android.widget.TextView", padding={15, 5, 15, 5})
			public static class Synopsis
			{

				public static int id = R.id.synopsis;
				
				public static int textColor = Color.BLACK;
				
				@Setter
				public static Object textSize = new Object[]{TypedValue.COMPLEX_UNIT_SP, 18f};

				@Variable
				public static RelativeLayout.LayoutParams params;
				
				static
				{
					params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					params.addRule(RelativeLayout.BELOW, SynopsisHeader.id);
				}
				
				public static ViewGroup.LayoutParams layoutParams = params;
			}
			
			@Layout(viewClass="android.widget.TextView", padding={15, 20, 15, 5})
			public static class CastHeader
			{
				
				public static int id = R.id.label_cast;

				public static CharSequence text = "Cast";
				
				public static int textColor = Color.BLACK;
				
				@Setter
				public static Object textSize = new Object[]{TypedValue.COMPLEX_UNIT_SP, 36f};
				
				@Setter
				public static Object typeface = new Object[]{Typeface.DEFAULT, Typeface.BOLD};

				@Variable
				public static RelativeLayout.LayoutParams params;
				
				static
				{
					params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					params.addRule(RelativeLayout.BELOW, Synopsis.id);
				}
				
				public static ViewGroup.LayoutParams layoutParams = params;
			}
			
			@Layout(viewClass="android.widget.TextView", padding={15, 5, 15, 5})
			public static class Cast
			{

				public static int id = R.id.cast;
				
				public static int textColor = Color.BLACK;
				
				@Setter
				public static Object textSize = new Object[]{TypedValue.COMPLEX_UNIT_SP, 18f};

				@Variable
				public static RelativeLayout.LayoutParams params;
				
				static
				{
					params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					params.addRule(RelativeLayout.BELOW, CastHeader.id);
				}
				
				public static ViewGroup.LayoutParams layoutParams = params;
			}
			
			@Layout(viewClass="android.view.View")
			public static class HorizontalLine
			{
				public static int id = R.id.horizontal_line;
				
				public static int backgroundColor = Color.BLACK;
				
				@Variable
				public static RelativeLayout.LayoutParams params;
				
				static
				{
					params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 3);
					params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					params.addRule(RelativeLayout.BELOW, Cast.id);
					params.leftMargin = 15;
					params.rightMargin = 15;
					params.topMargin = 5;
					params.bottomMargin = 5;
				}
				
				public static ViewGroup.LayoutParams layoutParams = params;
			}
			
			@Layout(viewClass="android.widget.TextView", padding={15, 5, 15, 5})
			public static class Footer
			{
				public static int id = R.id.footer;
				
				public static int textColor = Color.BLACK;
				
				@Variable
				public static RelativeLayout.LayoutParams params;
				
				static
				{
					params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
					params.addRule(RelativeLayout.CENTER_HORIZONTAL);
					params.addRule(RelativeLayout.BELOW, HorizontalLine.id);
				}
				
				public static ViewGroup.LayoutParams layoutParams = params;
				
				
			}
		}
	}
	
		
	@Variable
	public static View.OnTouchListener listener = new View.OnTouchListener()
	{
		@Override
		public boolean onTouch(View v, MotionEvent event) 
		{
			//prevents touches from going through to the scrollview
			return true;
		}
	};
	
	@Embed(layout="self.philbrown.nerdery.layout.TweetView")
	public static class Tweet
	{
		public static View.OnTouchListener onTouchListener = listener;
	}
	
	@Embed(layout="self.philbrown.nerdery.layout.TwitterOverlay")
	public static class Web
	{
		public static View.OnTouchListener onTouchListener = listener;
	}
		
}
