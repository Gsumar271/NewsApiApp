package com.newsapiapp;

import java.util.ArrayList;

/**
 * Created on 1/9/18.
 */

//Utilize a singleton pattern
//This class is used to store Article 
//Array throughout the application

public class ArticleList {

    public ArrayList<Article> allArticles;
    private static ArticleList articleList;

    private  ArticleList (){

        allArticles = new ArrayList<Article>();
    }

    public static ArticleList getInstance(){
        if(articleList == null){
            articleList = new ArticleList();
        }
        return articleList;
    }

    public ArrayList<Article> getAllArticles() {
        return allArticles;
    }


    public void addArticle(Article _article){

        allArticles.add(_article);
    }


}
