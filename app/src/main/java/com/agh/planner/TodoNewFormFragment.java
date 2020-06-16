package com.agh.planner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.agh.planner.db.DatabaseHelper;
import com.agh.planner.weather.RemoteFetch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TodoNewFormFragment extends Fragment {
    private JSONArray weatherForecast;

    DatabaseHelper db;
    EditText description;
    TextView date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initDb();
        View view = inflater.inflate(R.layout.fragment_todo_new, container, false);
        date = view.findViewById(R.id.date_text); //dd-mm-yyyy
        description = view.findViewById(R.id.description_text);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String apiUrl = "https://www.metaweather.com/api/location/523920/"+ date.getText().toString();
                WeatherTaskForSingleDay weatherTask;
                weatherTask = new WeatherTaskForSingleDay(apiUrl);
                weatherTask.execute();

            }
        });
        return view;
    }

    private void initDb() {
        if (db == null) {
            db = new DatabaseHelper(getActivity());
        }
    }


    class WeatherTaskForSingleDay extends AsyncTask<String, Void, String> {
        private String apiUrl;
        public WeatherTaskForSingleDay(String apiUrl) {
            this.apiUrl = apiUrl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            return RemoteFetch.getData(this.apiUrl);
        }

        @Override
        protected void onPostExecute(String data) {
            try {
                weatherForecast = new JSONArray(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String weatherType = "";
            float temp = (float) 0.0;
            try {
                if (weatherForecast == null ) {
                    Toast.makeText(getContext(), "couldn't get data", Toast.LENGTH_SHORT).show();

                }
                JSONObject info = (JSONObject) weatherForecast.get(0);
                weatherType = info.get("weather_state_name").toString();
                temp = (float) info.getDouble("the_temp");
            } catch (Exception e) {
                weatherType = "Unknown";
                temp = (float) -999.99;
            }
            boolean result = db.addPlan(description.getText().toString(), date.getText().toString(), temp, weatherType);
            if (result) {
                Toast.makeText(getContext(), "Successfully added plan", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Unsuccessfully added plan", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
