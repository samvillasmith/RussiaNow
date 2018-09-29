package com.example.android.russianow;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Loads a list of stories by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class NovostiLoader extends AsyncTaskLoader<ArrayList<Novosti>> {

    /**
     * Query URL
     */

    private String mUrl;

    /**
     * Constructs a new {@link NovostiLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public NovostiLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    /**
     * Behind the scenes, a loadInBackground thread begins.
     */
    @Override
    public ArrayList<Novosti> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of Russian related news stories.
        ArrayList<Novosti> novosti = QueryNov.fetchNewsdata(mUrl, getContext());
        return novosti;
    }

}