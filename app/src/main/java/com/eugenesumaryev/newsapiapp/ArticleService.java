package com.eugenesumaryev.newsapiapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by eugenesumaryev on 1/5/18.
 *
 */

public class ArticleService {

    public interface OnDataChangedListener {

        public void onDataChanged(ArrayList<Article> articleList);
    }

    private OnDataChangedListener onDataChangedListener;

    private ArrayList<Article> allArticles;

    public static String TAG = "Refresh";

    Activity activity;

    public ArticleService(Activity _activity) {

        //allArticles = _list;
       //res = _res;
        activity = _activity;

        onDataChangedListener = (OnDataChangedListener)activity;
    }

    public void populateArticles(){

        int objCount;
        Bitmap _bitmap = null;
        allArticles = new ArrayList<Article>();


        URL url;
        try {
            String articlesFeed = activity.getString(R.string.articles_feed);
            url = new URL(articlesFeed);

            URLConnection connection;
            connection = url.openConnection();

            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = httpConnection.getInputStream();

                // Parse the articles feed.
                JSNParser parser = new JSNParser();
                JSONObject obj = (JSONObject) parser.readJSONObject(in);


                JSONArray articleArray = (JSONArray)obj.get( "articles");

                //for each article in array
                for (objCount = 0; objCount < articleArray.length(); objCount++){

                    JSONObject articleObject = (JSONObject) articleArray.get(objCount);

                    String _urlImage = articleObject.getString("urlToImage");
                    String _title = articleObject.getString("title");
                    String _urlLink  = articleObject.getString("url");

                    try {
                        InputStream inStream = new URL(_urlImage).openStream();
                        _bitmap = BitmapFactory.decodeStream(inStream);
                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }


                    final Article article = new Article(_bitmap, _title, _urlLink);

                    allArticles.add(article);
                }

            }

        } catch (MalformedURLException e) {
            Log.d(TAG, "MalformedURLException", e);
        } catch (IOException e) {
            Log.d(TAG, "IOException", e);
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception", e);
        } finally {
        }


/*

        for (int i = 0; i<4; i++){
            Article someArticle = new Article(_bitmap, "someTitle"+i, "someArticle");
            allArticles.add(someArticle);
        }

*/


        onDataChangedListener.onDataChanged(allArticles);


    }




}
