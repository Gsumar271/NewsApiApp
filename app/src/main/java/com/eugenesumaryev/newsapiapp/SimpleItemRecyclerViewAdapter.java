package com.eugenesumaryev.newsapiapp;

import android.graphics.Bitmap;
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

    private final ItemListActivity mParentActivity;
    private final List<Article> mValues;
    private final boolean mTwoPane;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
              
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
    public void onBindViewHolder(final SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {

        holder.mContentView.setText(mValues.get(position).getTitle());


        holder.mImgView.setImageBitmap(
                Bitmap.createScaledBitmap(
                        mValues.get(position).getBitmap(), 50, 50, false));

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
