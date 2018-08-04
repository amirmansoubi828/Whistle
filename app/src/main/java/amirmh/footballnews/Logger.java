package amirmh.footballnews;

import android.util.Log;

public class Logger {
    public static void i(String message) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        Log.i(stackTrace[3].getClassName(), stackTrace[3].getMethodName() + " : " + message);

    }

}