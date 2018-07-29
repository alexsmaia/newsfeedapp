package com.example.android.newsfeedapp;

/**
 *
 * An {@link News} object contains information related to a single news
 */

public class News {

    /** Title of the News */
    private String mTitle;

    /** Section of the News */
    private String mSection;

    /** Author of the News */
    private String mAuthor;

    /** Date Published of the News */
    private String mDate;

    /** Website URL of the News */
    private String mUrl;

    /**
     * Create a new {@link News} object.
     *
     * @param title is the title of the news
     * @param section is the section the news belong to
     * @param title is the author of the news
     * @param date is the time in milliseconds the news has published
     * @param url is the website URL to read the news
     */
    public News(String title, String section, String author, String date, String url) {
        mTitle = title;
        mSection = section;
        mAuthor = author;
        mDate = date;
        mUrl = url;
    }

    /**
     * Returns the title of the news.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Returns the section of the news.
     */
    public String getSection() {
        return mSection;
    }

    /**
     * Returns the author of the news.
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Returns the author of the news.
     */
    public String getDate() {
        return mDate;
    }

    /**
     * Returns the website URL to find the complete article of the news.
     */
    public String getUrl() { return mUrl; }

}
