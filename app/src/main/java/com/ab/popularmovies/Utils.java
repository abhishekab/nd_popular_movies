package com.ab.popularmovies;

import android.content.Context;
import android.net.Uri;

/**
 * Created by q4J1X056 on 27-07-2016.
 */
public class Utils {


    public static String getCompletePosterPath(Context context,String posterPath)
    {
        Uri.Builder builder= Uri.parse(context.getString(R.string.path_base_poster_moviedb))
                                .buildUpon()
                                .appendPath(context.getString(R.string.size_poster_moviedb))
                                .appendEncodedPath(posterPath);

        return builder.toString();


    }


    public  static String getYouTubeIconPath(String video_id)
    {
        return "http://img.youtube.com/vi/"+video_id+"/default.jpg";

    }

    public static Uri getYoutubeUriFromId(String id)
    {
        return Uri.parse("http://m.youtube.com/watch?v="+id);
    }
}
