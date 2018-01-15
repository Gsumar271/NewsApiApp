package com.newsapiapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import java.util.ArrayList;

public class ArticleMainActivity extends AppCompatActivity
 
{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private ArrayList<Article> allArticles;
    private SimpleItemRecyclerViewAdapter sa;
    private View rView;

    // Defines a custom Intent action
    public static final String BROADCAST_ARTICLE_REFRESHED =
            "com.eugenesumaryev.newsapiapp.ACTION_ARTICLE_REFRESHED";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

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
        allArticles = ArticleList.getInstance().getAllArticles();

        // The filter's action is BROADCAST_ARTICLE_REFRESHED
        IntentFilter statusIntentFilter = new IntentFilter(
                BROADCAST_ARTICLE_REFRESHED);

        // Instantiates a new ArticleStateReceiver
        ArticleStateReceiver mArticleStateReceiver =
                new ArticleStateReceiver();
        // Registers the ArticleStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mArticleStateReceiver,
                statusIntentFilter);

        setupRecyclerView((RecyclerView) recyclerView);

        startService(new Intent(this,
                ArticleUpdateService.class));
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        sa = new SimpleItemRecyclerViewAdapter(this, allArticles, mTwoPane);
        recyclerView.setAdapter(sa);
 
    }

    public void populateArticles() {

        allArticles = ArticleList.getInstance().getAllArticles();
        setupRecyclerView((RecyclerView) rView);

    }





    //Private BroadcastReceiver class to receive notifications when new Article is downloaded
    private class ArticleStateReceiver extends BroadcastReceiver {

        //prevents instantiation
        private ArticleStateReceiver(){
        }

        @Override
        public void onReceive(Context context, Intent intent) {

           sa.notifyDataSetChanged();

        }

    }

    private static final int SHOW_PREFERENCES = 1;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case (R.id.menu_refresh): {
                startService(new Intent(this, ArticleUpdateService.class));
                return true;
            }
            case (R.id.menu_preferences): {

                Class c =
                        SettingsActivity.class;
                Intent i = new Intent(this, c);
                startActivityForResult(i, SHOW_PREFERENCES);

                return true;

            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        startService(new Intent(this, ArticleUpdateService.class));
    }

}
