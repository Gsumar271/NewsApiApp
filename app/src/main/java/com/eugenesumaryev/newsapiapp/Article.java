package com.newsapiapp;

import android.graphics.Bitmap;

/**
 * Created on 1/2/18.
 */

public class Article {

    private String title;
    private String urlArticle;
    private Bitmap bitmap;



    public Article (Bitmap _bitmap, String _title, String _urlArticle){
    
        bitmap = _bitmap;
        title = _title;
        urlArticle = _urlArticle;
    }


    public Bitmap getBitmap() { return bitmap; }

    public String getTitle() {
        return title;
    }

    public String getUrlArticle() {
        return urlArticle;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", urlArticle='" + urlArticle + '\'' +
                '}';
    }
}
