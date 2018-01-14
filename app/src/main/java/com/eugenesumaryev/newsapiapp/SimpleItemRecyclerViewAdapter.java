package com.eugenesumaryev.newsapiapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by eugenesumaryev on 1/2/18.
 */

public class SimpleItemRecyclerViewAdapter
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
                    arguments.putString(ArticleWebFragment.ARG_ITEM_URL, item.getUrlArticle());
                    arguments.putString(ArticleWebFragment.ARG_ITEM_TITLE, item.getTitle());
                    //ArticleDetailFragment fragment = new ArticleDetailFragment();
                    ArticleWebFragment fragment = new ArticleWebFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ArticleDetailActivity.class);
                    intent.putExtra(ArticleWebFragment.ARG_ITEM_URL, item.getUrlArticle());
                    intent.putExtra(ArticleWebFragment.ARG_ITEM_TITLE, item.getTitle());

                    context.startActivity(intent);
                }


           /*
            Context context = view.getContext();
            Intent intent = new Intent(context, ArticleDetailActivity.class);
            intent.putExtra(ArticleDetailFragment.ARG_ITEM_ID, item.getTitle());

            context.startActivity(intent);
            */
        }

    };

    SimpleItemRecyclerViewAdapter(ArticleMainActivity parent,
                                  List<Article> items,
                                  boolean twoPane) {
        mValues = items;
       // mValues = ArticleList.getInstance().getAllArticles();
        mParentActivity = parent;
        mTwoPane = twoPane;
      //  Log.v("SIRVA constructor:", "called");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // super(parent, viewType);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.headline_view, parent, false);
      //  Log.v("OnCreateViewHolder:", "called");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {

        holder.mContentView.setText(mValues.get(position).getTitle());

        if (mValues.get(position).getBitmap() != null) {
            holder.mImgView.setImageBitmap(
                    Bitmap.createScaledBitmap(
                            mValues.get(position).getBitmap(), 50, 50, false));

        }


         holder.itemView.setTag(mValues.get(position));
         holder.itemView.setOnClickListener(mOnClickListener);
      //  Log.v("OnBindViewHolder", "called");

    }

    @Override
    public int getItemCount() {
        /*
        Log.v("getItemCount:", "called");
        Log.v("itemcount:",  String.valueOf(mValues.size()));
        */

        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView mImgView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mImgView = (ImageView) view.findViewById(R.id.headline_image);
            mContentView = (TextView) view.findViewById(R.id.article_headline);
           // Log.v("ViewHolder constructor:", "called");
        }
    }
}