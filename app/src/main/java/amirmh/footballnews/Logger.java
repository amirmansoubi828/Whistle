package amirmh.footballnews;

import android.util.Log;

public class Logger {
    public static void i(String message) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        Log.i(stackTrace[3].getClassName(), stackTrace[3].getMethodName() + " : " + message);
    }

    public static void i() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        Log.i(stackTrace[3].getClassName(), stackTrace[3].getMethodName() + " : ");
    }

    public static void i(Object message) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        Log.i(stackTrace[3].getClassName(), stackTrace[3].getMethodName() + " : " + String.valueOf(message));
    }


}
