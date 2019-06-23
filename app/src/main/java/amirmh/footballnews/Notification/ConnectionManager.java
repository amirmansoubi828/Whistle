package amirmh.footballnews.Notification;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import amirmh.footballnews.Logger;

public class ConnectionManager {

    static boolean isConnected(Context context) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        //Logger.i(String.valueOf(connected));
        return connected;
    }
}
