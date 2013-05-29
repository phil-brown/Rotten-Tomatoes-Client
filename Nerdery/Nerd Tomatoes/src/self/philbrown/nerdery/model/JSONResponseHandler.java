package self.philbrown.nerdery.model;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import self.philbrown.AbLE.AbLEUtil;

/**
 * Handle an HttpResponse as a JSON Object. 
 * @author Phil Brown
 */
public class JSONResponseHandler implements ResponseHandler<JSONObject> 
{

	/** The Object generated by this handler */
	private JSONObject obj;
	
	/** The callback that is run when this handler completes its task of parsing the {@link HttpResponse} */
	private Callback callback;
	
	/**
	 * Constructor
	 */
	public JSONResponseHandler() 
	{
		this.obj = null;
		this.callback = null;
	}
	
	/**
	 * Constructor
	 * @param callback the {@link Callback} for this handler. This is useful because it
	 * allows the calling class to get the returned {@link JSONArray}.
	 */
	public JSONResponseHandler(Callback callback)
	{
		this.obj = null;
		this.callback = callback;
	}
	
	/** 
	 * Set the {@link Callback} for this handler. This is useful because it
	 * allows the calling class to get the returned {@link JSONArray}.
	 */
	public void setCallback(Callback callback) 
	{
		this.callback = callback;
	}
	
	@Override
	public JSONObject handleResponse(HttpResponse response) throws ClientProtocolException, IOException 
	{
		StatusLine statusLine = response.getStatusLine();
		if (statusLine.getStatusCode() >= 300)
        {
        	AbLEUtil.err("HTTP Response Error %d:%s.", statusLine.getStatusCode(), statusLine.getReasonPhrase());
        	return null;
        }

        HttpEntity entity = response.getEntity();
        if (entity == null) 
        	return null;
        
        try 
        {
        	this.obj = new JSONObject(EntityUtils.toString(entity));
        	
        	if (this.callback != null)
        		callback.invoke(this.obj);
			return this.obj;
		} 
        catch (ParseException e) 
        {
			e.printStackTrace();
			return null;
		} 
        catch (JSONException e) 
        {
			e.printStackTrace();
			return null;
		}
	}
	
	/** Callback for this {@link JSONResponseHandler} */
	public interface Callback 
	{
		/** 
		 * Return the JSON Array
		 * @param obj the {@code JSONArray} that the handler has finished receiving and parsing.
		 */
		public void invoke(JSONObject obj);
		
	}

}
