package amirmh.footballnews.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.squareup.otto.Subscribe;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import amirmh.footballnews.DataType.SkySportsNews;
import amirmh.footballnews.Logger;
import amirmh.footballnews.Presenter.MainPresenter;
import amirmh.footballnews.R;
import amirmh.footballnews.View.MainActivity;


public class NotificationService extends Service {
    BroadcastReceiver bcr;
    MainPresenter mainPresenter;

    public NotificationService() {
        super();
        Logger.i();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Logger.i();
        bcr = new BCR();
        ((BCR) bcr).setBcrListener(new BCRListener() {
            @Override
            public void OnInternetConnectionChange(boolean isConnected) {
                OnInternetConnectionChanged(isConnected);
            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(bcr, intentFilter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i("destroyed");
        if (bcr != null) {
            unregisterReceiver(bcr);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void OnInternetConnectionChanged(boolean isConnected) {
        if (isConnected) {
            mainPresenter = new MainPresenter();
            mainPresenter.attachService(this);
            mainPresenter.getSkySports();
            Logger.i("data requested");
        }
    }

    @Subscribe
    public void updateListView(final ArrayList<SkySportsNews> newsArrayList) {
        Logger.i("data received : " + String.valueOf(newsArrayList.size()));
        makeNotification(newsArrayList);
    }

    @Subscribe
    public void showErr(Throwable throwable) {
        throwable.printStackTrace();
    }

    private void makeNotification(ArrayList<SkySportsNews> newsArrayList) {
        Logger.i(newsArrayList.get(0).getTitle());
        for (SkySportsNews skySportsNews :
                newsArrayList) {
            String all_message = skySportsNews.getTitle() + " " + skySportsNews.getShortdesc();
            all_message = all_message.toLowerCase();
            if (all_message.contains("manutd") || all_message.contains("manchester united") || all_message.contains("man utd")) {
                try {
                    if (!isRepeatedNews(skySportsNews.getTitle())) {
                        saveLastNews(skySportsNews.getTitle());
                        notificationBuilder(skySportsNews.getTitle(), skySportsNews.getShortdesc());
                        Logger.i("notified");
                    }
                } catch (IOException e) {
                    notificationBuilder(skySportsNews.getTitle(), skySportsNews.getShortdesc());
                    Logger.i("notified");
                    try {
                        saveLastNews(skySportsNews.getTitle());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void notificationBuilder(String title, String content) {

        int NOTIFICATION_ID = 1;
        String NOTIFICATION_CHANNEL_ID = "whistle";

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.whistle_sound);
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setSound(sound, attributes);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.whistle_icon)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void saveLastNews(String title) throws IOException {
        FileOutputStream fileOutputStream = openFileOutput("notification", MODE_PRIVATE);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        outputStreamWriter.write(title);
        outputStreamWriter.close();
    }

    private boolean isRepeatedNews(String title) throws IOException {
        FileInputStream fileInputStream = openFileInput("notification");
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = bufferedReader.readLine();
        if (line.equals(title)) {
            return true;
        } else {
            return false;
        }
    }
}
