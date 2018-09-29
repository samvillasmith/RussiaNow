package com.example.android.russianow;

public class Novosti {
    private String mTitle;
    private String mRegion;
    private String mUrl;
    private String mTimeStamp;
    private String mAuthor;

    /**
     * This constructs a new {@link Novosti} object.
     *
     * @param Region is the region or sphere of life where Russia is mentioned or influencing.
     * @param Title is the title of the article
     * @param Author is the contributor to the article, if available.
     * @param Url is the website URL to go to regarding the entire story.
     * @param TimeStamp is the date and time, along with hours and minutes in Zulu (local) time.
     */

    public Novosti(String Title, String Region, String Url, String Author, String TimeStamp) {
        mRegion = Region;
        mTitle = Title;
        mAuthor = Author;
        mUrl = Url;
        mTimeStamp = TimeStamp;
    }

    /**
     * Returns the time of the story in Zulu time.
     */

    public String getmDateTime() {
        return mTimeStamp;
    }

    /**
     * Returns the region or sphere of life that Russia has influenced.
     */

    public String getmSection() {

        return mRegion;
    }

    /**
     * Returns the title of the article.
     */
    public String getmTitle() {

        return mTitle;
    }

    /**
     * Returns the name of the author.
     */

    public String getmAuthor() {
        return mAuthor;
    }

    /**
     * Returns the website URL to find more information about the news article.
     */

    public String getmUrl() {

        return mUrl;
    }
}
