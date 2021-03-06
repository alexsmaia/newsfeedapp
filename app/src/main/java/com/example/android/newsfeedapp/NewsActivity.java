package com.example.android.newsfeedapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    private static final String LOG_TAG = NewsActivity.class.getName();

    /** URL for News data from the The Guardin dataset */
    private static final String API_REQUEST_URL =
            "https://content.guardianapis.com/search";

    /**
     * Constant value for the News loader ID.
     */
    private static final int NEWS_LOADER_ID = 1;

    /** Adapter for the list of News */
    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of News as input
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        newsListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, and send an intent to a web browser
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                News currentNews = mAdapter.getItem(position);

                // Convert the String URL into a URI object
                Uri newsUri = Uri.parse(currentNews.getUrl());

                // Create a new intent to view the complete News URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader.
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View progressBar = findViewById(R.id.loading_spinner);
            progressBar.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }


    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        Log.e(LOG_TAG, " Create Loader ");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Get News Section Preference
        String newsSection = sharedPrefs.getString(
                getString(R.string.settings_news_section_key),
                getString(R.string.settings_news_section_default));

        // Get Order By Preference
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        // Get News Number Display Preference
        String newsNumber = sharedPrefs.getString(
                getString(R.string.settings_news_number_display_key),
                getString(R.string.settings_news_number_display_default));

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(API_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `format=geojson`
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("section", newsSection);
        uriBuilder.appendQueryParameter("show-fields", "all");
        uriBuilder.appendQueryParameter("page-size", newsNumber);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("api-key", BuildConfig.THE_GUARDIAN_API_KEY);

        // Create a new loader for the given URL
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsArticles) {

        Log.e(LOG_TAG, " Load Finish ");

        // Hide loader after load finished
        View progressBar = findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link News} articles, add them to the adapter's
        if (newsArticles != null && !newsArticles.isEmpty()) {
            mAdapter.addAll(newsArticles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

        Log.e(LOG_TAG, " Loader Reset ");

        // Loader reset, so we can clear out our existing data.
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
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
