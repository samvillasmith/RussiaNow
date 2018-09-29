package com.example.android.russianow;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving news data from the Guardian.
 */

public final class QueryNov {



    /** Tag for the log messages */
    public static final String LOG_TAG = RussianNewsActivity.class.getSimpleName();

    static Context mContext;


    public static ArrayList<Novosti> fetchNewsdata(String requestUrl, Context context) {
        mContext = context;
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, mContext.getString(R.string.HTTPSError), e);
        }
        ArrayList<Novosti> russiaNow = QueryNov.extractStories(jsonResponse);
        return russiaNow;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            // If the request was successful (response code 200),
            // then read the input stream and parse the response.

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, mContext.getString(R.string.ErrorCode) + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, mContext.getString(R.string.JSONError1), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static URL createUrl(String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, mContext.getString(R.string.ErrorWithUrl), exception);
            return null;
        }
        return url;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    /**
     * Query the Guardian API and return a list of {@link Novosti} objects.
     */
    public static ArrayList<Novosti> extractStories(String NovostiJSon) {
        ArrayList<Novosti> RussiaNow = new ArrayList<>();
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(NovostiJSon)) {
            return null;
        }
        try {
            // Try to parse the JSON response string. If there's a problem with the JSON
            // is formatted, a JSONException exception object will be thrown.
            // Catch the exception so the app doesn't crash, and print the error message to the logs.
            JSONObject novostiJson;
            JSONObject novostiResponse;
            JSONArray novResults;
            novostiJson = new JSONObject(NovostiJSon);
            if (novostiJson.has(mContext.getString(R.string.response))) {
                novostiResponse = novostiJson.getJSONObject(mContext.getResources().getString(R.string.response));

                /*
                My logic in this is that if some attribute, such as title, is not present, then
                do nothing. However, if any of these attributes are present, then execute what's
                in the else portion of the code and add that attribute to our news list.
                */

                if (novostiResponse.has(mContext.getString(R.string.results))) {
                    novResults = novostiResponse.getJSONArray(mContext.getString(R.string.results));
                    int i = 0;
                    while (true) {
                        if (!(i < novResults.length())) {
                            break;
                        }
                        String regionName;
                        regionName = "";
                        String storyAuthor;
                        storyAuthor = "";
                        JSONObject breakingRussianNews;
                        breakingRussianNews = novResults.getJSONObject(i);
                        String articleTitle;
                        articleTitle = "";
                        if (!breakingRussianNews.has(mContext.getString(R.string.webTitle))) {
                        } else {
                            articleTitle = breakingRussianNews.getString(mContext.getString(R.string.webTitle));
                        }
                        if (!breakingRussianNews.has(mContext.getString(R.string.sectionName))) {
                        } else
                            regionName = breakingRussianNews.getString(mContext.getString(R.string.sectionName));
                        String storyLink;
                        storyLink = "";
                        if (!breakingRussianNews.has(mContext.getString(R.string.webUrl))) {
                        } else {
                            storyLink = breakingRussianNews.getString(mContext.getString(R.string.webUrl));
                        }
                        String timeStamp;
                        timeStamp = "";
                        if (!breakingRussianNews.has(mContext.getString(R.string.webPublicationDate))) {
                        } else {
                            timeStamp = breakingRussianNews.getString(mContext.getString(R.string.webPublicationDate));
                        }

                        if (!breakingRussianNews.has(mContext.getString(R.string.fields))) {
                        } else {
                            breakingRussianNews.getJSONObject(mContext.getString(R.string.fields));
                        }
                        if (!breakingRussianNews.has(mContext.getString(R.string.tags))) {
                        } else {
                            JSONArray tagsArray = breakingRussianNews.getJSONArray(mContext.getString(R.string.tags));
                            if (tagsArray.length() != 0) {
                                JSONObject tags;
                                tags = tagsArray.getJSONObject(0);
                                if (!tags.has(mContext.getString(R.string.webTitle))) {
                                } else {
                                    timeStamp = tags.getString(mContext.getString(R.string.webTitle));
                                }
                            }
                        }
                        Novosti Pravda;
                        Pravda = new Novosti(articleTitle, regionName, storyLink, timeStamp, storyAuthor);
                        RussiaNow.add(Pravda);
                        i++;
                    }
                }
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception and rint a log message.
            Log.e(mContext.getString(R.string.QueryUtils), mContext.getString(R.string.ProblemWithResults), e);
        }
        return RussiaNow;
    }

}
