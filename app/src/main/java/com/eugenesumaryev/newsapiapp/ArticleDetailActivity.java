package com.newsapiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

/**
 * An activity representing a single screen. This
 * activity is only used on narrow width devices. 
 */
public class ArticleDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
    
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


   
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ArticleWebFragment.ARG_ITEM_URL,
                    getIntent().getStringExtra(ArticleWebFragment.ARG_ITEM_URL));

            arguments.putString(ArticleWebFragment.ARG_ITEM_TITLE,
                    getIntent().getStringExtra(ArticleWebFragment.ARG_ITEM_TITLE));

            ArticleWebFragment fragment = new ArticleWebFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
  
            NavUtils.navigateUpTo(this, new Intent(this, ArticleMainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
