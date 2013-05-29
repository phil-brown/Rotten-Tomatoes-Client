package self.philbrown.nerdery.store;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Basic {@link SharedPreferences} wrapper for storing and retrieving the Twitter token and secret.
 * @author Phil Brown
 *
 */
public class TwitterCredentialsStore 
{
	/** singleton instance of {@code TwitterCredentialsStore} */
	private static TwitterCredentialsStore self;
	
	/** Provides access the SharedPreferences */
	private SharedPreferences prefs;
	
	/** Allow changes to be written and saved to {@link #prefs}. */
	private SharedPreferences.Editor editor;

	/**
	 * Constructor. This is private, since this is a Singleton
	 * @param context used to create save and retrieve data to and from application storage
	 */
	private TwitterCredentialsStore(Context context)
	{
		prefs = context.getSharedPreferences("Twitter_Creds", Context.MODE_PRIVATE);
		editor = prefs.edit();
	}
	
	/**
	 * Singleton accessor
	 * @param context used to create save and retrieve data to and from application storage
	 * @return the singleton instance of this data store
	 */
	public static TwitterCredentialsStore sharedStore(Context context)
	{
		if (self == null)
		{
			if (context == null)
			{
				throw new NullPointerException("Context must not be null!");
			}
			self = new TwitterCredentialsStore(context);
		}
		return self;
	}
	
	/**
	 * Set the Twitter Token
	 * @param token
	 */
	public void setToken(String token)
	{
		editor.putString("token", token);
		editor.commit();
	}
	
	/**
	 * Get the Twitter Token
	 * @return the saved token, or {@code null} if it does not exist.
	 */
	public String getToken()
	{
		return prefs.getString("token", null);
	}
	
	/**
	 * Set the Twitter Token Secret
	 * @param secret
	 */
	public void setSecret(String secret)
	{
		editor.putString("secret", secret);
		editor.commit();
	}
	
	/**
	 * Get the Twitter Token Secret
	 * @return the saved token secret, or {@code null} if it does not exist.
	 */
	public String getSecret()
	{
		return prefs.getString("secret", null);
	}
	
	
}
