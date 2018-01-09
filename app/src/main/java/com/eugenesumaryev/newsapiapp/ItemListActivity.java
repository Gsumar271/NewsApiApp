package com.eugenesumaryev.newsapiapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


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
import java.util.Iterator;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity implements ArticleService.OnDataChangedListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private ArrayList<Article> allArticles;
    private SimpleItemRecyclerViewAdapter sa;
    private ArticleService service;
    private View rView;


    public static String TAG = "Refresh";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;

        rView = recyclerView;


        allArticles = new ArrayList<Article>();

        //create a service class, passing the resources
        service = new ArticleService(this);

        //  setupRecyclerView((RecyclerView) recyclerView);

        Thread t = new Thread(new Runnable() {
            public void run() {

                //run the service class to populate article array
                service.populateArticles();

            }
        });
        t.start();

    

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        sa = new SimpleItemRecyclerViewAdapter(this, allArticles, mTwoPane);
        recyclerView.setAdapter(sa);

    }


    //notify main thread that data has been changed abd view needs to be updated
    @Override
    public void onDataChanged(ArrayList<Article> articleList) {

        allArticles = articleList;

        runOnUiThread(
                new Runnable() {

                    @Override
                    public void run() {
                      setupRecyclerView((RecyclerView) rView);
                    }
                });
    }

}
    
