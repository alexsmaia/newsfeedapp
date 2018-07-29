package com.example.android.newsfeedapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Constructs a new {@link News}
     *
     * @param context of the app
     * @param newsArticles is the list of news from the guardian Api
     */
    public NewsAdapter(Context context, List<News> newsArticles) {
        super(context, 0, newsArticles);
    }

    /**
     * Return a list item view that display the News
     *
     * @param position The position in the list of the list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        // Get the {@link News} object located at this position in the list
        News currentNews = getItem(position);

        // Find the TextView with view ID title_text_view
        TextView titleView = (TextView) listItemView.findViewById(R.id.title_text_view);
        // Display the title of the current news in the TextView
        titleView.setText(currentNews.getTitle());

        // Find the TextView with view ID section_text_view
        TextView sectionView = (TextView) listItemView.findViewById(R.id.section_text_view);
        // Display the title of the current news in the TextView
        sectionView.setText(currentNews.getSection());

        // Find the TextView with view ID date_text_view
        TextView dateView = (TextView) listItemView.findViewById(R.id.date_text_view);
        // Display the title of the current news in the TextView
        dateView.setText(currentNews.getDate());

        // Find the TextView with view ID author_text_view
        TextView authorView = (TextView) listItemView.findViewById(R.id.author_text_view);
        // Display the title of the current news in the TextView
        authorView.setText(currentNews.getAuthor());

        // Return the list item view
        return listItemView;
    }

}
