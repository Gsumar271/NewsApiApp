package com.newsapiapp;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created on 12/7/17.
 */

public class ArticleWebFragment extends Fragment {

    public static final String ARG_ITEM_URL = "item_url";
    public static final String ARG_ITEM_TITLE = "item_title";

    private String articleUrl;
    private String articleTitle;

    private WebView mWebView;

    public ArticleWebFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_URL)) {
            // Load the content specified by the fragment
            // arguments.
            articleUrl = getArguments().getString(ARG_ITEM_URL);
            articleTitle = getArguments().getString(ARG_ITEM_TITLE);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(articleTitle);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.article_web_layout, container, false);

        if (articleUrl != null) {

            mWebView = (WebView) view.findViewById(R.id.activity_webview);
            mWebView.loadUrl(articleUrl);
            // Force links and redirects to open in the WebView instead of in a browser
            mWebView.setWebViewClient(new WebViewClient());
        }


        return view;

    }
}
