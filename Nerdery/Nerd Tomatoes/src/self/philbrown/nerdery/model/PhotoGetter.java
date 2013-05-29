package self.philbrown.nerdery.model;

import java.io.InputStream;
import java.lang.ref.WeakReference;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import self.philbrown.AbLE.AbLEActivity;
import self.philbrown.AbLE.AbLEUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commonsware.cwac.task.AsyncTaskEx;

/**
 * Asynchronously retrieves a Bitmap Image from a given URL
 * @author Phil Brown
 */
public class PhotoGetter extends AsyncTaskEx<Void, Void, ImageView>
{
	/** parent view for the pending imageView */
	private ViewGroup parent;
	
	/** indicator parent view. A RelativeLayout is used to center the indicator in the view */
	private RelativeLayout progressContainer;
	
	/** The spinner to show while the image is downloading */
	private ProgressBar indicator;
	
	/** The URL from which to download the bitmap image */
	private String url;
	
	/** Provides access to the current Activity */
	private AbLEActivity context;
	
	/** Optional callback that is invoked after the task has completed */
	private Callback callback;
	
	/** The view into which the retrieved bitmap is inflated */
	private ImageView imageView;
	
	/** can be used to manually set the image size */
	private int[] size;
	
	/**
	 * Constructor
	 * @param context current Activity context. Used to create layout elements
	 * @param parent <em>optional</em> {@code ViewGroup} in which to add the ImageView once 
	 * downloaded. If no parent is provided, a Callback should be set using 
	 * {@link #setCallback(Callback)}.
	 * @param url the url from which to download the image
	 */
	public PhotoGetter(AbLEActivity context, ViewGroup parent, String url)
	{
		this.parent = parent;
		this.url = url;
		if (parent != null)
		{
			progressContainer = new RelativeLayout(context);
			indicator = new ProgressBar(context);
		}
		
		this.context = context;
	}
	
	public void setCallback(Callback callback)
	{
		this.callback = callback;
	}
	
	/**
	 * Set the dimensions. This is useful when no parent view is set.
	 * @param height
	 * @param width
	 */
	public void setSize(int width, int height)
	{
		size = new int[]{width, height};
	}
	
	@Override
	protected void onPreExecute()
	{
		if (parent == null)
		{
			return;
		}
		
		progressContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		
		indicator.setIndeterminate(true);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		indicator.setLayoutParams(params);
		progressContainer.addView(indicator);
		
		parent.setPadding(0, 2, 0, 2);
		parent.addView(progressContainer);
		
	}
	
	@Override
	protected void onPostExecute(ImageView result)
	{
		if (parent != null)
		{
			parent.removeAllViews();
			
			if (result != null)
			{
				result.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
				parent.addView(result);
			}
			else
			{
				//There was an error. Show an error message instead of an image
				RelativeLayout error = new RelativeLayout(context);
				error.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
				TextView text = new TextView(context);
				text.setText("IMAGE NOT AVAILABLE");
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.CENTER_IN_PARENT);
				text.setLayoutParams(params);
				text.setTextColor(Color.GRAY);
				error.addView(text);
				parent.addView(error);
			}
		}
		
		if (callback != null)
			callback.onPostExecute(imageView);
	}

	@Override
	protected ImageView doInBackground(Void... arg0) 
	{
		imageView = new ImageView(context);
		
		try
		{
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
	        HttpResponse responseGet = httpclient.execute(get);  
	        HttpEntity entity = responseGet.getEntity();  
	        if (entity != null)
	        {
	        	
	        	InputStream is = entity.getContent();
	        	BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inSampleSize = 1;
				opt.inPurgeable = true;
				opt.inInputShareable = false;
				if (parent != null)
				{
					opt.outHeight = parent.getHeight();
					opt.outWidth = parent.getWidth();
				}
				else if (size != null)
				{
					opt.outHeight = size[1];
					opt.outWidth = size[0];
				}
				
				WeakReference<Bitmap> bitmap = new WeakReference<Bitmap>(BitmapFactory.decodeStream(is, new Rect(0,0,0,0), opt));
				
				if (bitmap == null || bitmap.get() == null)
				{
					AbLEUtil.err("Bitmap could not be decoded!");
					return null;
				}
				
				if (bitmap.get().isRecycled())
				{
					AbLEUtil.err("Bitmap has already been recycled!");
					return null;
				}
		        imageView.setImageBitmap(bitmap.get());
		        is.close();
		        
		        return imageView;
		     	   
	        }	
		}
		catch (Throwable t) 
		{
			AbLEUtil.err("Download error!");
			t.printStackTrace();
		}
		return null;
	}
	
	/** 
	 * Allows a caller to register a callback to be notified when the execution of this task
	 * has completed.
	 */
	public interface Callback
	{
		/**
		 * Called after the task has completed.
		 */
		public void onPostExecute(ImageView imageView);
	}
}