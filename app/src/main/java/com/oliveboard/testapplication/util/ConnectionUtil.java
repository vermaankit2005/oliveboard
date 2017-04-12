package com.oliveboard.testapplication.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.util.Log;

/**
 * Utility for connection to server.
 */
public class ConnectionUtil {

    private static final String TAG = "ConnectionUtil";

    /**
     * Connects to the given URL and returns the response in the form of String
     * @param requstURL
     * @return
     */
    public static String getDataFromServer(String requstURL) {

        StringBuilder response = new StringBuilder();

        try {
            // Opening the connection to the requested URL.
            URL url = new URL(requstURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // Reading the response from the server.
            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    response.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
            e.printStackTrace();
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
            e.printStackTrace();
        }

        return response.toString();
    }
}

