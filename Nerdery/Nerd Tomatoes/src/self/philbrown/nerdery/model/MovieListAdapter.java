package self.philbrown.nerdery.model;

import java.util.List;

import self.philbrown.AbLE.AbLEActivity;
import self.philbrown.AbLE.annotations.AnnotatedLayoutInflater;
import self.philbrown.nerdery.DetailActivity;
import self.philbrown.nerdery.R;
import self.philbrown.nerdery.layout.Cell;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * List adapter for displaying movie info in the list view
 * @author Phil Brown
 *
 */
public class MovieListAdapter extends ArrayAdapter<Movie>
{
	/**
	 * Background color of every other cell
	 */
	private static int customColor = Color.parseColor("#F2F2F2");
	
	/**
	 * Used to access resources and controllers
	 */
	private AbLEActivity context;
	
	/**
	 * List of movies to display
	 */
	private List<Movie> list;
	
	/**
	 * Constructor
	 * @param context
	 * @param list
	 */
	public MovieListAdapter(AbLEActivity context, List<Movie> list)
	{
		super(context, 0, list);
		this.context = context;
		this.list = list;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		View view;
        if (convertView == null) 
        {
        	view = AnnotatedLayoutInflater.inflate(context, Cell.class, null);
        	view.setTag("");
        }
        else
        {
        	view = convertView;
        }
        
        //set background color
        if (position % 2 == 0)
        {
        	view.setBackgroundColor(Color.WHITE);
        }
        else
        {
        	view.setBackgroundColor(customColor);
        }
        
        final Movie m = list.get(position);
        
        //handle clicks by opening the detail activity
        view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, DetailActivity.class);
				//pass the movie at this position to the DetailActivity
				intent.putExtra("movie", m);
				//also pass the cell background color, which will be used to set the background color of the detail view
				intent.putExtra("background", (position % 2 == 0 ? Color.WHITE : customColor));
				context.startActivity(intent);
				context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
        
        //title
        TextView tv = (TextView) view.findViewById(R.id.movie_title);
        tv.setText(m.getTitle());
        
        //thumbnail image
        FrameLayout imageParent = (FrameLayout) view.findViewById(R.id.imageparent);
        ViewParent vp = m.getThumbnail().getParent();
        if (!view.getTag().equals(m.getId()))
        {
        	
        	if (vp != null)
        	{
        		((ViewGroup) vp).removeView(m.getThumbnail());
        	}
        	imageParent.addView(m.getThumbnail());
        }
        else if (vp == null)
        {
        	imageParent.addView(m.getThumbnail());
        }
        
        //used to determine if a reused cell needs to swap change
        view.setTag(m.getId());
        
        //rating image
        ImageView rating = (ImageView) view.findViewById(R.id.rating);
        if (m.getMpaaRating().equalsIgnoreCase("G"))
        {
        	rating.setBackgroundResource(R.drawable.g);
        }
        else if (m.getMpaaRating().equalsIgnoreCase("PG"))
        {
        	rating.setBackgroundResource(R.drawable.pg);
        }
        else if (m.getMpaaRating().equalsIgnoreCase("PG-13"))
        {
        	rating.setBackgroundResource(R.drawable.pg_13);
        }
        else if (m.getMpaaRating().equalsIgnoreCase("R"))
        {
        	rating.setBackgroundResource(R.drawable.r);
        }
        else if (m.getMpaaRating().equalsIgnoreCase("NC-17"))
        {
        	rating.setBackgroundResource(R.drawable.nc_17);
        }
        
        //score
        ProgressBar score = (ProgressBar) view.findViewById(R.id.critic_score);
        score.setProgress(m.getRatings().criticsScore);
        
		return view;
	}
}
