package com.eugenesumaryev.newsapiapp;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

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


public class ArticleUpdateService extends IntentService {

	private ArrayList<Article> articles = new ArrayList<Article>();

	public static String TAG = "ARTICLE_UPDATE_SERVICE";

	//  private Notification.Builder articleNotificationBuilder;
	public static final int NOTIFICATION_ID = 1;


	public ArticleUpdateService() {
		super("ArticleUpdateService");
	}

	public ArticleUpdateService(String name) {
		super(name);
	}


	public static String ARTICLES_REFRESHED =
			"com.article.ASTEROIDS_REFRESHED";

	private AlarmManager alarmManager;
	private PendingIntent alarmIntent;


	@Override
	public void onCreate() {


		super.onCreate();
		  alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

		  String ALARM_ACTION ="com.article.ACTION_REFRESH_ARTICLE_ALARM";

		  Intent intentToFire = new Intent(ALARM_ACTION);
		  alarmIntent =
			 PendingIntent.getBroadcast(this, 0, intentToFire, 0);

	}


	@Override
	protected void onHandleIntent(Intent intent) {


		//newest added from book
		Context context = getApplicationContext();

		SharedPreferences prefs =
				PreferenceManager.getDefaultSharedPreferences(context);

		/*
		int updateFreq =
				prefs.getInt(PreferencesActivity.PREF_UPDATE_FREQ_INDEX, 60);

		boolean autoUpdateChecked =
				prefs.getBoolean(PreferencesActivity.PREF_AUTO_UPDATE, false);
		*/

		int updateFreq = 50;

		boolean autoUpdateChecked = true;


		if (autoUpdateChecked) {
			int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
			long timeToRefresh = SystemClock.elapsedRealtime() +
					updateFreq * 60 * 100;
			alarmManager.setInexactRepeating(alarmType, timeToRefresh,
					updateFreq * 60 * 100, alarmIntent);
		} else
			alarmManager.cancel(alarmIntent);

		refreshArticles();

		sendBroadcast(new Intent(ARTICLES_REFRESHED));


	}


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void addNewArticle(Article _article) {

		articles.add(_article);

	}


	public void refreshArticles() {

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

					addNewArticle(article);
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

	  
	  
	 /*
	private void broadcastNotification(Asteroid _asteroid) {
		Intent startActivityIntent = new Intent(this, MainActivity.class);
	    PendingIntent launchIntent =
	    		PendingIntent.getActivity(this, 0, startActivityIntent, 0);

		asteroidNotificationBuilder
		    .setContentIntent(launchIntent)
		    .setWhen(System.currentTimeMillis())
            .setContentTitle("M:" + _asteroid.getMagnitude())
    	    .setContentText("D: " + _asteroid.getDiameter());
		
		if (_asteroid.getMagnitude() > 6) {
		   Uri ringURI =
		     RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

	      asteroidNotificationBuilder.setSound(ringURI);
		}
		
		double vibrateLength = 100* Math.exp(0.53*_asteroid.getMagnitude());
	    long[] vibrate = new long[] {100, 100, (long)vibrateLength };
	    asteroidNotificationBuilder.setVibrate(vibrate);
	    
	    int color;
	    if (_asteroid.getMagnitude() < 5.4)
	      color = Color.GREEN;
	    else if (_asteroid.getMagnitude() < 6)
	      color = Color.YELLOW;
	    else
	      color = Color.RED;

	    asteroidNotificationBuilder.setLights(
	      color, 
	      (int)vibrateLength, 
	      (int)vibrateLength);
		
		NotificationManager notificationManager
	      = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

	    notificationManager.notify(NOTIFICATION_ID,
	      asteroidNotificationBuilder.getNotification());
	    
	    
	  
	  }
	  */


/*
Your API key is: 096dca08f6c34d1bb2e91517aefcd69d
 */
