package com.example.android.wikirace;

import android.app.PendingIntent;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by David Jung on 2016-02-17.
 *
 * Creates queries to work with Wikipedia API
 */
public class WikiQueries {
    public static final String apiArticleTitlesIds = "https://en.wikipedia.org/w/api.php?action=query&format=json&list=random&utf8=1&rnnamespace=0&rnlimit=2";
    public static final String apiArticleUrl = "https://en.wikipedia.org/w/api.php?action=query&prop=info&inprop=url&pageids=";

    public static List<Article> getTwoRandomArticles() {
        ArrayList<Article> articles = new ArrayList<>();

        String jsonString = jsonStringFromUrl(apiArticleTitlesIds);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONObject jsonQueryObject = (JSONObject) jsonObject.get("query");
            JSONArray jsonRandomArray = (JSONArray) jsonQueryObject.get("random");

            for(int i = 0; i < jsonRandomArray.length(); i++) {
                JSONObject jsonArticle = (JSONObject) jsonRandomArray.get(i);
                String title = jsonArticle.getString("title");
                Long id = jsonArticle.getLong("id");
                String url = getArticleUrl(id);
                articles.add(new Article(title,id,url));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return articles;
    }


    public static String getArticleUrl(long id) throws JSONException{
        String articleUrl = null;

        String url = apiArticleUrl + Long.toString(id) + "&format=json&utf8=1";
        String jsonString = jsonStringFromUrl(url);

        JSONObject jsonObject = (JSONObject) new JSONObject(jsonString);
        JSONObject jsonQueryObject = (JSONObject) jsonObject.get("query");
        JSONObject jsonPagesObject = (JSONObject) jsonQueryObject.get("pages");
        JSONObject jsonArticleObject = (JSONObject) jsonPagesObject.get(Long.toString(id));
        articleUrl = jsonArticleObject.getString("fullurl");
        Log.i("Article URL",articleUrl);
        return articleUrl;
    }



    public static String jsonStringFromUrl(String urlText) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String jsonStirng = null;

        try {
            URL url = new URL(urlText);

            //Create request for Wikipedia
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Read input stream into a string
            InputStream inStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inStream == null) {
                //Don't do anything if the there is no input
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inStream));

            String line;

            while((line = reader.readLine()) != null){
                //Add a new line to make debugging easier and it won't affect json parsing
                buffer.append(line + '\n');
            }

            if(buffer.length() == 0) {
                return null;
            }

            jsonStirng = buffer.toString();

            Log.v("JSON String Result",jsonStirng);
        } catch (IOException e) {
            Log.e("Error", "IO");
            return null;
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("Error", "Error closing stream",e);
                }
            }
        }

        return jsonStirng;
    }
}
