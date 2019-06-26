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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;

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
        if (mainPresenter != null) {
            mainPresenter.detachService();
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
        if (isConnected && checkLastChangeDate()) {
            mainPresenter = new MainPresenter();
            mainPresenter.attachService(this);
            mainPresenter.getSkySports();
            mainPresenter.getFourFourTwo();
            mainPresenter.getBleacherReport();
            Logger.i("data requested");
        }
    }

    @Subscribe
    public void updateListView(final ArrayList<SkySportsNews> newsArrayList) {
        Logger.i("data received : " + String.valueOf(newsArrayList.size()));
        saveLastNewsObject(newsArrayList);
        makeNotification(newsArrayList);
    }

    @Subscribe
    public void showErr(Throwable throwable) {
        throwable.printStackTrace();
    }

    private void makeNotification(ArrayList<SkySportsNews> newsArrayList) {
        Logger.i(newsArrayList.get(0).getTitle());
        ArrayList<String> notifyWords = readNotifyWords();
        for (SkySportsNews skySportsNews :
                newsArrayList) {
            String all_message = skySportsNews.getTitle() + " " + skySportsNews.getShortdesc();
            all_message = all_message.toLowerCase();
            if (checkWords(all_message, notifyWords)) {
                try {
                    if (!isRepeatedNews(skySportsNews.getTitle(), skySportsNews.getLink())) {
                        notificationBuilder(skySportsNews.getTitle(), skySportsNews.getShortdesc(), skySportsNews.getLink(), skySportsNews.getImgsrc());
                    }
                } catch (IOException e) {
                    notificationBuilder(skySportsNews.getTitle(), skySportsNews.getShortdesc(), skySportsNews.getLink(), skySportsNews.getImgsrc());
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void notificationBuilder(final String title, final String content, final String link, String imageUrl) {
        final int NOTIFICATION_ID = determineSource(link).charAt(0);
        final String NOTIFICATION_CHANNEL_ID = "Whistle";

        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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
        final PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        final Context context = getApplicationContext();
        Logger.i("loading pic");
        Picasso.get().load(imageUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.whistle_icon)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.whistle_icon))
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true);

                notificationManager.notify(NOTIFICATION_ID, builder.build());
                try {
                    saveLastNews(title, link);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                saveLastChangeDate();
                Logger.i("notified");
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Logger.i("error loading");
                e.printStackTrace();
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.whistle_icon)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.whistle_icon))
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true);

                notificationManager.notify(NOTIFICATION_ID, builder.build());
                try {
                    saveLastNews(title, link);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                saveLastChangeDate();
                Logger.i("notified");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Logger.i("loading");
            }
        });

    }

    private void saveLastNews(String title, String link) throws IOException {
        String source = determineSource(link);
        FileOutputStream fileOutputStream = openFileOutput("notification_" + source, MODE_PRIVATE);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        outputStreamWriter.write(title);
        outputStreamWriter.close();
    }

    private boolean isRepeatedNews(String title, String link) throws IOException {
        try {
            FileInputStream fileInputStream = openFileInput("notification_" + determineSource(link));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            if (line.equals(title)) {
                Logger.i("is Repeated");
                return true;
            } else {
                Logger.i("isn't Repeated ");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.i("isn't Repeated ");
            return false;
        }
    }

    private ArrayList<String> readNotifyWords() {
        try {
            FileInputStream fileInputStream = openFileInput("notification_name");
            ;
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Object readObject = objectInputStream.readObject();
            ArrayList<String> names = (ArrayList<String>) readObject;
            objectInputStream.close();
            return names;
        } catch (Exception e) {
            ArrayList<String> names = new ArrayList<>();
            names.add("ManUtd");
            names.add("Man Utd");
            names.add("ManchesterUnited");
            names.add("Manchester United");
            names.add("pogba");
            names.add("lukaku");
            return names;
        }
    }

    private boolean checkWords(String message, ArrayList<String> words) {
        if (words.size() == 0) {
            return true;
        }
        for (String word : words
        ) {
            if (message.contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private String determineSource(String link) {
        String[] sites = new String[]{"bleacherreport", "skysports", "fourfourtwo"};
        for (String site : sites
        ) {
            if (link.toLowerCase().contains(site)) {
                Logger.i(site);
                return site;
            }
        }
        Logger.i("unknown");
        return "unknown";
    }

    private void saveLastChangeDate() {
        try {
            Date date = new Date();
            FileOutputStream fileOutputStream = openFileOutput("notification_lastdate", MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(date);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkLastChangeDate() {
        try {
            FileInputStream fileInputStream = openFileInput("notification_lastdate");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Date lastDate = (Date) objectInputStream.readObject();
            objectInputStream.close();
            return new Date().getTime() - lastDate.getTime() > 1000 * 60 * 5;
        } catch (Exception e) {
            return true;
        }
    }

    private void saveLastNewsObject(ArrayList<SkySportsNews> skySportsNewsArrayList) {
        try {
            FileOutputStream fileOutputStream = openFileOutput("last_news_" + determineSource(skySportsNewsArrayList.get(0).getLink()), MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(skySportsNewsArrayList);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
