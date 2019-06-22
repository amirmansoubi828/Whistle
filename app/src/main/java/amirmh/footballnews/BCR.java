package amirmh.footballnews;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class BCR extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        Logger.i("Whistle action: " + action);
        if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            Toast.makeText(context, "Changed Connection", Toast.LENGTH_SHORT).show();
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
}