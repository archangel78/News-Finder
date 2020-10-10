package com.example.newsreport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<NewsReports> {

    public NewsAdapter(Context context, List<NewsReports> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link NewsReports} object located at this position in the list
        NewsReports currentNewsArticle = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID news_title

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.news_title);
        // Get the Title from the current NewsReports object and
        // set this text on the name TextView
        nameTextView.setText(currentNewsArticle.getTitle());

        // Find the TextView in the list_item.xml layout with the ID news_section
        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.news_section);
        sectionTextView.setText(currentNewsArticle.getSectionName());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.news_date);
        dateTextView.setText(currentNewsArticle.getDatePublished());

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.news_author);
        authorTextView.setText(currentNewsArticle.getAuthor());
        // Return the whole list item layout (containing 4 TextViews)
        // so that it can be shown in the ListView
        return listItemView;
    }
}
