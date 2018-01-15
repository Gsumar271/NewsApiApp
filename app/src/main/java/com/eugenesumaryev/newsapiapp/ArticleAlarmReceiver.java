package com.newsapiapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ArticleAlarmReceiver extends BroadcastReceiver {
	
	public static final String ACTION_REFRESH_ARTICLE_ALARM =
		      "com.newsapiapp.ACTION_REFRESH_ARTICLE_ALARM";
	
	@Override
	public void onReceive(Context context, Intent intent) {
      Intent startIntent = new Intent(context, ArticleUpdateService.class);
      context.startService(startIntent);
    }

}
