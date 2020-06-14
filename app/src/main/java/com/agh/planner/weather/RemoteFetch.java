package com.agh.planner.weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.agh.planner.R;

public class RemoteFetch {

    public static String getData(String apiUrl){
        try {
            URL url = new URL(String.format(apiUrl));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();
            return json.toString();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
