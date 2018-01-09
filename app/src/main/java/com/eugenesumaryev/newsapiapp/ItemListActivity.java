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


import com.eugenesumaryev.newsapiapp.dummy.DummyContent;

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
                // Article someArticle = new Article("someImg", "someTitle", "someArticle");

                /*
                allArticles.add(someArticle);
                sa.notifyDataSetChanged();
                */

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

                //  populateArticles();
                //  sa.notifyDataSetChanged();
            }
        });
        t.start();

        /*
        final Handler handler = new Handler();

        handler.post(
                new Runnable() {
                    public void run(){
                        populateArticles();
                    }
                });

        */

        // Log.v("AllArtrticles", String.valueOf(allArticles.size()));
        //  setupRecyclerView((RecyclerView) recyclerView);


    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        // recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, allArticles, mTwoPane));


        sa = new SimpleItemRecyclerViewAdapter(this, allArticles, mTwoPane);
        recyclerView.setAdapter(sa);
        //  sa.notifyDataSetChanged();

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


    public void populateArticles() {

        int objCount;
        Bitmap _bitmap = null;

        URL url;
        try {
            String articlesFeed = getString(R.string.articles_feed);
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


                JSONArray articleArray = (JSONArray) obj.get("articles");

                //for each article in array
                for (objCount = 0; objCount < articleArray.length(); objCount++) {

                    JSONObject articleObject = (JSONObject) articleArray.get(objCount);

                    String _urlImage = articleObject.getString("urlToImage");
                    String _title = articleObject.getString("title");
                    String _urlLink = articleObject.getString("url");

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


    }

}


// A Version of Article Update Service that actually works: Had to set up a View adapter
// inside the runOnUIThread in the callback method onDataChanged
/*
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

        res = getResources();

        //create a service class, passing the resources
        service = new ArticleService(res, this);


        Thread t = new Thread(new Runnable() {
            public void run(){

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
  */


    /*

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final List<Article> mValues;
        private final boolean mTwoPane;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Article item = (Article) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.getTitle());
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.getTitle());

                    context.startActivity(intent);
                }

            }

        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<Article> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.headline_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
           // holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).getTitle());
          //  holder.mImgView.setImageBitmap(mValues.get(position).getBitmap());

            holder.mImgView.setImageBitmap(
                    Bitmap.createScaledBitmap(
                            mValues.get(position).getBitmap(), 50, 50, false));
            // Log.v("article", mValues.get(position).getTitle());
           // holder.itemView.setTag(mValues.get(position));
          //  holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView mImgView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mImgView = (ImageView) view.findViewById(R.id.headline_image);
                mContentView = (TextView) view.findViewById(R.id.article_headline);
            }
        }
    }
    */


/*
public class ItemListActivity extends AppCompatActivity {


    //  Whether or not the activity is in two-pane mode, i.e. running on a tablet
    //  device.

private boolean mTwoPane;

    private ArrayList<Article> allArticles;

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

        populateArticles();

        setupRecyclerView((RecyclerView) recyclerView);


    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));
    }


    public void populateArticles(){


    }


public static class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<ItemListActivity.SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final ItemListActivity mParentActivity;
    private final List<DummyContent.DummyItem> mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
                ItemDetailFragment fragment = new ItemDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);

                context.startActivity(intent);
            }
        }
    };

    SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                  List<DummyContent.DummyItem> items,
                                  boolean twoPane) {
        mValues = items;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @Override
    public ItemListActivity.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);
        return new ItemListActivity.SimpleItemRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemListActivity.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mIdView = (TextView) view.findViewById(R.id.id_text);
            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }
}
}

 */
