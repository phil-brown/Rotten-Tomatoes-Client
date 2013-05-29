package self.philbrown.nerdery.layout;

import self.philbrown.AbLE.AbLEActivity;
import self.philbrown.AbLE.AbLEUtil;
import self.philbrown.AbLE.annotations.Layout;
import self.philbrown.AbLE.annotations.Variable;
import self.philbrown.nerdery.R;
import self.philbrown.nerdery.model.Constants;
import self.philbrown.nerdery.store.TwitterCredentialsStore;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Twitter Overlay AbLE Layout
 * @author Phil Brown
 *
 */
@Layout(viewClass="android.widget.RelativeLayout", params={"fill_parent", "fill_parent"})
public class TwitterOverlay 
{
	public static int id = R.id.overlay_twitter;
		
	public static int visibility = View.GONE;
	
	public static int backgroundColor = Color.parseColor("#AA000000");
	
	public static void onLayoutComplete(AbLEActivity context, View v)
	{
		int[] size = new int[2];
		AbLEUtil.getScreenSize(context, size);
		v.setPadding(size[0]/8, size[1]/8, size[0]/8, size[1]/8);
		ViewGroup.LayoutParams params = v.getLayoutParams();
		params.width = size[0];
		params.height = size[1];
		v.setLayoutParams(params);
	}
	
	@Layout(viewClass="android.webkit.WebView", params={"fill_parent", "fill_parent"})
	public static class Web
	{
		public static int id = R.id.web;
		
		@Variable
		public static String twitterConsumerKey = null;
		@Variable
		public static Twitter twitter = null;
		
		/**
		 * This method shows off an interesting characteristic of AbLE - the hybrid nature of Layout files.
		 * This method is invoked on all views loaded by AbLE (if it exists), and can perform tasks
		 * usually performed in a Controller. In this case, it handles the Twitter OAuth page request.
		 * The request callback is then handled in the DetailController.
		 * @param context
		 * @param v
		 */
		public static void onLayoutComplete(final AbLEActivity context, View v)
		{

			try
			{
				WebView w = (WebView) v;
				w.getSettings().setJavaScriptEnabled(true);
				
				ApplicationInfo app = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
				Bundle metaData = app.metaData;
				twitterConsumerKey = metaData.getString(context.getString(R.string.twitter_api_key));

				ConfigurationBuilder builder = new ConfigurationBuilder();
	            builder.setOAuthConsumerKey(twitterConsumerKey);
	            builder.setOAuthConsumerSecret(Constants.consumerSecret);
	            Configuration configuration = builder.build();
	 
	            TwitterFactory factory = new TwitterFactory(configuration);
				twitter = factory.getInstance();
	            
				RequestToken req = twitter.getOAuthRequestToken(Constants.callbackURL);
				
				w.setWebViewClient( new WebViewClient()
		        {
		            @Override
		            public boolean shouldOverrideUrlLoading(WebView view, String url)
		            {
		                if( url.contains(Constants.callbackURL))
		                {
		                	try
		                	{
		                		Uri uri = Uri.parse( url );
			                    String oauthVerifier = uri.getQueryParameter( "oauth_verifier" );
			                    context.findViewById(R.id.overlay_twitter).setVisibility(View.GONE);
			                    TwitterCredentialsStore store = TwitterCredentialsStore.sharedStore(context);
			                    if (store.getToken() == null)
			                    {
			                    	//first time authenticating
				                    context.findViewById(R.id.tweeter).setVisibility(View.VISIBLE);
			                    }
			                    AccessToken ac = twitter.getOAuthAccessToken(oauthVerifier);
			                    store.setToken(ac.getToken());
			                    store.setSecret(ac.getTokenSecret());
			                    return true;
		                	}
		                    catch (Throwable t)
		                    {
		                    	t.printStackTrace();
		                    }
		                }
		                return false;
		            }
		        });
		        w.loadUrl(req.getAuthenticationURL());
			}
			catch (Throwable t)
			{
				t.printStackTrace();
			}

		}
	}
}
