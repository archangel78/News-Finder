package com.example.newsreport;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class NewsReportLoader extends AsyncTaskLoader<List<NewsReports>> {
    private String mUrl;
    /**
     * Constructs a new {@link NewsReports}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public NewsReportLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }
    @Nullable
    @Override
    public List<NewsReports> loadInBackground() {
        List<NewsReports> newsReports = QueryUtils.fetchNews(mUrl);
        return newsReports;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
