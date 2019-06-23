package amirmh.footballnews.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BCR extends BroadcastReceiver{

    BCRListener bcrListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            bcrListener.OnInternetConnectionChange(ConnectionManager.isConnected(context));
        }
        // For our example, we'll also update all of the widgets when the timezone
        // changes, or the user or network sets the time.
        /**
         if (action.equals(Intent.ACTION_TIMEZONE_CHANGED)
         || action.equals(Intent.ACTION_TIME_CHANGED)) {
         AppWidgetManager gm = AppWidgetManager.getInstance(context);
         ArrayList<Integer> appWidgetIds = new ArrayList<Integer>();
         ArrayList<String> texts = new ArrayList<String>();
         new WidgetConfigure().runUpdate();

         } **/
    }

    public void setBcrListener(BCRListener bcrListener) {
        this.bcrListener = bcrListener;
    }
}