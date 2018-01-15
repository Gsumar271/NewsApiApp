package com.newsapiapp;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
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


public class ArticleUpdateService extends IntentService {

    public ArticleList articleList = ArticleList.getInstance();
    public static String TAG = "ARTICLE_UPDATE_SERVICE";


    private NotificationCompat.Builder articleNotificationBuilder;
	public static final int NOTIFICATION_ID = 1;
    NotificationManager notificationManager;

    public ArticleUpdateService() {
		super("ArticleUpdateService");
	}
    public ArticleUpdateService(String name) {
		super(name);
	}

    private AlarmManager alarmManager;
	private PendingIntent alarmIntent;


	@Override
	public void onCreate() {
        super.onCreate();

		alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);


        Intent intentToFire = new Intent(ArticleAlarmReceiver.ACTION_REFRESH_ARTICLE_ALARM);
        alarmIntent =
                PendingIntent.getBroadcast(this, 0, intentToFire, 0);

        // The id of the channel.
        String id = "channel_01";

        articleNotificationBuilder  = new NotificationCompat.Builder(this, id);

        articleNotificationBuilder
                .setAutoCancel(true)
                .setTicker("New Article")
                .setSmallIcon(R.drawable.notification_icon);


	}


	@Override
	protected void onHandleIntent(Intent intent) {


		Context context = getApplicationContext();

		SharedPreferences prefs =
				PreferenceManager.getDefaultSharedPreferences(context);


		int updateFreq =
				prefs.getInt(SettingsActivity.PREF_UPDATE_FREQ, 10);

		boolean autoUpdateChecked =
				prefs.getBoolean(SettingsActivity.PREF_AUTO_UPDATE, false);


		//Set the Alarm to send out Broadcasts
		if (autoUpdateChecked) {
			int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
			long timeToRefresh = SystemClock.elapsedRealtime() +
					updateFreq * 60 * 100;
			alarmManager.setInexactRepeating(alarmType, timeToRefresh,
					updateFreq * 60 * 100, alarmIntent);
		} else
			alarmManager.cancel(alarmIntent);

		refreshArticles();


	}


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void addNewArticle(Article _article) {

        articleList.addArticle(_article);
        // Trigger a notification.
        broadcastNotification(_article);

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
	  

	private void broadcastNotification(Article _article) {

		Intent startActivityIntent = new Intent(this, ArticleMainActivity.class);
	    PendingIntent launchIntent =
	    		PendingIntent.getActivity(this, 0, startActivityIntent, 0);

		articleNotificationBuilder
		    .setContentIntent(launchIntent)
		    .setWhen(System.currentTimeMillis())
            .setContentTitle("Story:")
    	    .setContentText( _article.getTitle());
		

        Intent localIntent =
                new Intent(ArticleMainActivity.BROADCAST_ARTICLE_REFRESHED);

        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);



		NotificationManager notificationManager
	      = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);


		notificationManager.notify(NOTIFICATION_ID,
	      articleNotificationBuilder.getNotification());
 
	  }
}

