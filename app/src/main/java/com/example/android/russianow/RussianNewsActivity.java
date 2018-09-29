package com.example.android.russianow;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class RussianNewsActivity extends AppCompatActivity implements LoaderCallbacks<ArrayList<Novosti>> {

/**
 * Constant value for the Novosti (Russian for 'News') loader ID.
 */
    private static final int NOV_LOADER_ID = 1;

    /** URL for the Guardian API that will be filtered for Russia */
    private static final String NOVOSTI_REQUEST_URL = "https://content.guardianapis.com/search?q=russia&api-key=011c78b6-cb58-46b2-ac61-5a89e6eff784&show-fields=thumbnail&show-tags=contributor";

    private NovAdapter mAdapter;

    /** TextView that is displayed when the Novosti list is empty */

    private ImageView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.novosti_main);

        //Find a reference to the {@link ListView} in the layout
        ListView NovListView = (ListView) findViewById(R.id.news);

        //Creation of an EmptyStateTextView and setting it on nov_empty_view
        mEmptyStateTextView = findViewById(R.id.empty_view);
        NovListView.setEmptyView(mEmptyStateTextView);

        //Creation of a new adapter that takes an empty list of news stories
        mAdapter = new NovAdapter(this, new ArrayList<Novosti>());
        NovListView.setAdapter(mAdapter);

        /*
        Set an item click listener on the ListView, which sends an intent to the device's default browser
        to view Russian-related news stories.
        */

        NovListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //find the relevant story, pass the URL into a URI, create an intent, and send that intent to launch
                //a new websiteIntent activity
                Novosti currentNews = mAdapter.getItem(position);
                Uri NewsUri = Uri.parse(currentNews.getmUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, NewsUri);
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        /*
        If there is a network connection, fetch data then
        get a reference to the LoaderManager to interact with loaders
        but display an error and hide the 'loading' device if not.
        */
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NOV_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_circle);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setImageResource(R.drawable.obshibka);
        }
    }

    @Override
    public Loader<ArrayList<Novosti>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String orderBy;
        orderBy = sharedPrefs.getString(getString(R.string.preferences_sector_order_by_key), getString(R.string.settings_order_by_default));
        String section;
        section = sharedPrefs.getString(getString(R.string.preferences_sector_key), getString(R.string.sector_default));


        Uri baseUri = Uri.parse(NOVOSTI_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();;


        if (!section.equals("")) {
        if (!section.equals(getString(R.string.sector_default_value))) {
            uriBuilder.appendQueryParameter("section", section);
        }
    }

        uriBuilder.appendQueryParameter("order-by", orderBy);

        return new NovostiLoader(this, uriBuilder.toString());
}
    @Override
    public void onLoadFinished(Loader<ArrayList<Novosti>> loader, ArrayList<Novosti> news) {
        mAdapter.clear();
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
        /*
        Set empty state text to display an image of former President Dmitri Medvedev saying "новостей нет."
        if there are no news stories from Russia, which is very unlikely.
        Hide loading indicator because the data has been loaded
        */
        mEmptyStateTextView.setImageResource(R.drawable.medvnov);
        View loadingIndicator = findViewById(R.id.loading_circle);
        loadingIndicator.setVisibility(View.GONE);
    }

    @Override
    // Loader reset, to clear out existing data.
    public void onLoaderReset(Loader<ArrayList<Novosti>> loader) {
        mAdapter.clear();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nov_actions) {
            Intent settingsIntent = new Intent(this, SettingsActivityRU.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
