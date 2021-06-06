package pr.code.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.http.Url;

/**
 * This class implements ping to server feature
 */
public class PingAsync extends AsyncTask<Void,Void,Boolean> {

    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean isReachable = false;

        try {

            //URL url = new URL("http://10.0.2.2");
            URL url = new URL("http://192.168.0.93");
            //URL url = new URL("http://www.google.com/");
            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setRequestProperty("User-Agent", "Android Application");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(500);
            urlc.connect();
            isReachable = (urlc.getResponseCode() == 200);
        } catch (IOException e) {
            Log.e("dec", e.getMessage());
        }

        return isReachable;
    }
}
