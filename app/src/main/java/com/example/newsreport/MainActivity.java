package com.example.newsreport;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsReports>> {
    /**
     * Constant value for the NewsReport loader ID. We can choose any integer.
     */
    private int loader_id = 1;
    /**
     * Adapter for the list of NewsReports
     */
    private NewsAdapter mAdapter;
    //Url that requests news reports with tag "CORONA"
    private static final String GUARDIAN_DEFAULT_URL = "https://content.guardianapis.com/search?q=corona&show-references=author&api-key=test";
    private String guardianFinalUrl = GUARDIAN_DEFAULT_URL;;

    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of NewsReports as input
        mAdapter = new NewsAdapter(this, new ArrayList<NewsReports>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);
        updateUi();

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected news report
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current news report that was clicked on
                NewsReports currentNewsReport = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri NewsReportseUri = Uri.parse(currentNewsReport.getUrl());

                // Create a new intent to view the news URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, NewsReportseUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
        Button search = findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View loadingIndicator = findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.VISIBLE);
                mAdapter.clear();
                EditText searchBar = findViewById(R.id.search_edit_text);
                String searchedText = searchBar.getText().toString();
                //Replaces " " (Spaces) with %20AND%20, so that multiple words can get searched
                searchedText.replaceAll(" ", "%20AND%20");
                guardianFinalUrl = "https://content.guardianapis.com/search?q="+searchedText+"&show-references=author"+"&api-key=test";
                //Creates a new loader id for each search so that new results get updated
                loader_id++;
                updateUi();
            }
        });

    }

    public void updateUi() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getSupportLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(loader_id, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }

    @NonNull
    @Override
    public Loader<List<NewsReports>> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewsReportLoader(this, guardianFinalUrl);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsReports>> loader, List<NewsReports> data) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No News Reports found."
        mEmptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous data
        mAdapter.clear();

        // If there is a valid list of {@link NewsReports}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsReports>> loader) {
        // Loader reset, so we can clear out our existing data.
        guardianFinalUrl = GUARDIAN_DEFAULT_URL;
        mAdapter.clear();
    }

}