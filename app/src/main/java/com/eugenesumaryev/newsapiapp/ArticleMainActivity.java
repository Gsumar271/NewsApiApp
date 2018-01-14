package com.eugenesumaryev.newsapiapp;

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

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ArticleDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ArticleMainActivity extends AppCompatActivity
        /*implements ArticleService.OnDataChangedListener*/
{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private ArrayList<Article> allArticles;
    private SimpleItemRecyclerViewAdapter sa;
   // private ArticleService service;
    private View rView;
   // private Handler handler = new Handler();

    // Defines a custom Intent action
    public static final String BROADCAST_ARTICLE_REFRESHED =
            "com.eugenesumaryev.newsapiapp.ACTION_ARTICLE_REFRESHED";


    // public static String TAG = "Refresh";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                sa.notifyDataSetChanged();

                Log.v("Onclicked:", String.valueOf(sa.getItemCount()));

                // Article someArticle = new Article("someImg", "someTitle", "someArticle");


               // allArticles.add(someArticle);
               // sa.notifyDataSetChanged();


            }
        });
       */

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

        /*

        handler.post(
                new Runnable() {
                    public void run() {
                        populateArticles();
                    }
                });
        */

     //   populateArticles();

        setupRecyclerView((RecyclerView) recyclerView);

        startService(new Intent(this,
                ArticleUpdateService.class));


        /*
        //create a service class, passing the resources
        service = new ArticleService(this);

        //  setupRecyclerView((RecyclerView) recyclerView);

        /*
        Thread t = new Thread(new Runnable() {
            public void run() {

                //run the service class to populate article array
                service.populateArticles();

                //  populateArticles();
                //  sa.notifyDataSetChanged();
            }
        });
        t.start();
        */


        // final Handler handler = new Handler();


        // Log.v("AllArticles", String.valueOf(allArticles.size()));
        //  setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        // recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, allArticles, mTwoPane));


        sa = new SimpleItemRecyclerViewAdapter(this, allArticles, mTwoPane);
        recyclerView.setAdapter(sa);
        //  sa.notifyDataSetChanged();

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

         //   allArticles.clear();


         //   allArticles = ArticleList.getInstance().getAllArticles();

         //   setupRecyclerView((RecyclerView) rView);


           sa.notifyDataSetChanged();

         //  Log.v("OnReceive:", String.valueOf(sa.getItemCount()));

           // Log.v("OnReceiveArticles", String.valueOf(allArticles.size()));
          // sa.notifyItemChanged(0);

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

                /*
                // Display the fragment as the main content.
                getFragmentManager().beginTransaction()
                        .replace(android.R.id.content, new PreferencesFragment())
                        .commit();
                */

                return true;

            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       // if (requestCode == SHOW_PREFERENCES)


        startService(new Intent(this, ArticleUpdateService.class));
    }




    /*
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


  */


    /*

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ArticleMainActivity mParentActivity;
        private final List<Article> mValues;
        private final boolean mTwoPane;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Article item = (Article) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ArticleDetailFragment.ARG_ITEM_ID, item.getTitle());
                    ArticleDetailFragment fragment = new ArticleDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ArticleDetailActivity.class);
                    intent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, item.getTitle());

                    context.startActivity(intent);
                }

            }

        };

        SimpleItemRecyclerViewAdapter(ArticleMainActivity parent,
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
public class ArticleMainActivity extends AppCompatActivity {


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
        extends RecyclerView.Adapter<ArticleMainActivity.SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final ArticleMainActivity mParentActivity;
    private final List<DummyContent.DummyItem> mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(ArticleDetailFragment.ARG_ITEM_ID, item.id);
                ArticleDetailFragment fragment = new ArticleDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ArticleDetailActivity.class);
                intent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, item.id);

                context.startActivity(intent);
            }
        }
    };

    SimpleItemRecyclerViewAdapter(ArticleMainActivity parent,
                                  List<DummyContent.DummyItem> items,
                                  boolean twoPane) {
        mValues = items;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @Override
    public ArticleMainActivity.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);
        return new ArticleMainActivity.SimpleItemRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ArticleMainActivity.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
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
