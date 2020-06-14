package com.agh.planner;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.agh.planner.weather.RemoteFetch;

import org.json.JSONObject;

public class WeatherTaskFragment extends Fragment {

    private JSONObject weatherForecast;

    interface TaskCallbacks {
        void onPostExecute();
    }

    private TaskCallbacks mCallbacks;
    private WeatherTask weatherTask;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (MainActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);

        // Create and execute the background task.
        weatherTask = new WeatherTask();
        weatherTask.execute();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public JSONObject getWeatherForecast(){
        return weatherForecast;
    }


    class WeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            return RemoteFetch.getData();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                weatherForecast = new JSONObject(result);
                mCallbacks.onPostExecute();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}