package com.eugenesumaryev.newsapiapp;

import android.graphics.Bitmap;

/**
 * Created by eugenesumaryev on 1/2/18.
 */

public class Article {

    private String urlImage;
    private String title;
    private String urlArticle;
    private Bitmap bitmap;



    public Article (Bitmap _bitmap, String _title, String _urlArticle){
       // urlImage = _urlImage;
        bitmap = _bitmap;

        title = _title;
        urlArticle = _urlArticle;
    }


    public Bitmap getBitmap() { return bitmap; }

    public String getUrlImage() {
        return urlImage;
    }

    public String getTitle() {
        return title;
    }

    public String getUrlArticle() {
        return urlArticle;
    }

    @Override
    public String toString() {
        return "Article{" +
                "urlImage='" + urlImage + '\'' +
                ", title='" + title + '\'' +
                ", urlArticle='" + urlArticle + '\'' +
                '}';
    }
}
