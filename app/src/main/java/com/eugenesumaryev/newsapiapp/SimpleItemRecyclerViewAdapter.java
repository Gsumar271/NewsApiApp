package com.newsapiapp;

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
 * Created on 1/2/18.
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
    public void onBindViewHolder(final SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {

        holder.mContentView.setText(mValues.get(position).getTitle());

        if (mValues.get(position).getBitmap() != null) {
            holder.mImgView.setImageBitmap(
                    Bitmap.createScaledBitmap(
                            mValues.get(position).getBitmap(), 50, 50, false));

        }


         holder.itemView.setTag(mValues.get(position));
         holder.itemView.setOnClickListener(mOnClickListener);

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
