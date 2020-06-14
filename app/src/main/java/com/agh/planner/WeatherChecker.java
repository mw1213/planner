package com.agh.planner;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agh.planner.db.DatabaseHelper;
import com.agh.planner.weather.RemoteFetch;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeatherChecker extends Fragment {
    Button check_for_today;
    Button check_for_date;
    TextView date_dest_label;
    TextView date_label;
    TextView temp_max_desc_label;
    TextView temp_max_label;
    TextView temp_min_desc_label;
    TextView temp_min_label;
    TextView temperature_desc_label;
    TextView temperature_label;
    TextView humidity_desc_label;
    TextView humidity_label;
    TextView weather_state_desc_label;
    TextView weather_state_label;
    TextView wind_direction_desc_label;
    TextView wind_direction_label;
    TextView wind_speed_desc_label;
    TextView wind_speed_label;
    ImageView weather_image;
    String[] names = {"Today","Tomorrow","2 days ahead","3 days ahead","4 days ahead"};
    JSONObject weatherForecast;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        weatherForecast = activity.getWeatherForecast();

        View view = inflater.inflate(R.layout.fragment_check_weather, container, false);


        date_dest_label = view.findViewById(R.id.date_desc_label);
        date_label = view.findViewById(R.id.date_label);
        temp_max_desc_label = view.findViewById(R.id.temp_max_desc_label);
        temp_max_label = view.findViewById(R.id.temp_max_label);
        temp_min_desc_label = view.findViewById(R.id.temp_min_desc_label);
        temp_min_label = view.findViewById(R.id.temp_min_label);
        temperature_desc_label = view.findViewById(R.id.temperature_desc_label);
        temperature_label = view.findViewById(R.id.temperature_label);
        humidity_desc_label = view.findViewById(R.id.humidity_desc_label);
        humidity_label = view.findViewById(R.id.humidity_label);
        weather_state_desc_label = view.findViewById(R.id.weather_state_desc_label);
        weather_state_label = view.findViewById(R.id.weather_state_label);
        wind_direction_desc_label = view.findViewById(R.id.wind_direction_desc_label);
        wind_direction_label = view.findViewById(R.id.wind_direction_label);
        wind_speed_desc_label = view.findViewById(R.id.wind_speed_desc_label);
        wind_speed_label = view.findViewById(R.id.wind_speed_label);
        weather_image = view.findViewById(R.id.weather_image_view);

        List<String> values = new ArrayList<>();
        values.add("SELECT DATE:");
        values.addAll(Arrays.asList(names));
        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> LTRadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        LTRadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(LTRadapter);



        view.findViewById(R.id.check_today_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                clicked_date(v, 0);

            }
        });

        view.findViewById(R.id.check_for_date_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch (spinner.getSelectedItem().toString()) {
                    case "Today":
                        clicked_date(v, 0);
                        break;
                    case "Tomorrow":
                        clicked_date(v, 1);
                        break;
                    case "2 days ahead":
                        clicked_date(v, 2);
                        break;
                    case "3 days ahead":
                        clicked_date(v, 3);
                        break;
                    case "4 days ahead":
                        clicked_date(v, 4);
                        break;
                }
            }
        });

        if (savedInstanceState != null){
            date_label.setText(savedInstanceState.getString("applicable_date"));
            temperature_label.setText(savedInstanceState.getString("the_temp"));
            temp_min_label.setText(savedInstanceState.getString("min_temp"));
            temp_max_label.setText(savedInstanceState.getString("max_temp"));
            wind_speed_label.setText(savedInstanceState.getString("wind_speed"));
            humidity_label.setText(savedInstanceState.getString("humidity"));
            wind_direction_label.setText(savedInstanceState.getString("wind_direction_compass"));
            weather_state_label.setText(savedInstanceState.getString("weather_state_name"));

            setCorrectWeatherImage(savedInstanceState.getString("weather_state_name"));
            setVisibilityOfInformation();
        }
        return view;
    }


    private void clicked_today(View view){
        System.out.println("clicked today");
    }

    private void clicked_date(View view, int number){
        try {
            JSONObject forecast =  (JSONObject) weatherForecast.getJSONArray("consolidated_weather").get(number);
            String weather_state_name = forecast.get("weather_state_name").toString();
            String wind_direction_compass = forecast.get("wind_direction_compass").toString();
            String applicable_date = forecast.get("applicable_date").toString();
            double min_temp = forecast.getDouble("min_temp");
            double max_temp = forecast.getDouble("max_temp");
            double the_temp = forecast.getDouble("the_temp");
            double wind_speed = forecast.getDouble("wind_speed");
            int humidity = forecast.getInt("humidity");
            setVisibilityOfInformation();


            date_label.setText(applicable_date);
            temperature_label.setText(String.valueOf(the_temp));
            temp_min_label.setText(String.valueOf(min_temp));
            temp_max_label.setText(String.valueOf(max_temp));
            wind_speed_label.setText(String.valueOf(wind_speed));
            humidity_label.setText(String.valueOf(humidity));
            wind_direction_label.setText(wind_direction_compass);
            weather_state_label.setText(weather_state_name);
            System.out.println(temp_min_label.getText().toString());

            setCorrectWeatherImage(weather_state_name);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setVisibilityOfInformation(){
        date_dest_label.setVisibility(View.VISIBLE);
        date_label.setVisibility(View.VISIBLE);
        temp_max_desc_label.setVisibility(View.VISIBLE);
        temp_max_label.setVisibility(View.VISIBLE);
        temp_min_desc_label.setVisibility(View.VISIBLE);
        temp_min_label.setVisibility(View.VISIBLE);
        temperature_desc_label.setVisibility(View.VISIBLE);
        temperature_label.setVisibility(View.VISIBLE);
        humidity_desc_label.setVisibility(View.VISIBLE);
        humidity_label.setVisibility(View.VISIBLE);
        weather_state_desc_label.setVisibility(View.VISIBLE);
        weather_state_label.setVisibility(View.VISIBLE);
        wind_direction_desc_label.setVisibility(View.VISIBLE);
        wind_direction_label.setVisibility(View.VISIBLE);
        wind_speed_desc_label.setVisibility(View.VISIBLE);
        wind_speed_label.setVisibility(View.VISIBLE);
    }

    public void setCorrectWeatherImage (String weather_state_name){
        if (weather_state_name==null)
            weather_state_name = "Clear";

        switch (weather_state_name){
            case "Showers":
                weather_image.setImageResource(R.drawable.showers);
                break;
            case "Snow":
                weather_image.setImageResource(R.drawable.snow);
                break;
            case "Sleet":
                weather_image.setImageResource(R.drawable.sleet);
                break;
            case "Hail":
                weather_image.setImageResource(R.drawable.hail);
                break;
            case "Thunderstorm":
                weather_image.setImageResource(R.drawable.thunderstorm);
                break;
            case "Heavy Rain":
                weather_image.setImageResource(R.drawable.heavy_rain);
                break;
            case "Light Rain":
                weather_image.setImageResource(R.drawable.light_rain);
                break;
            case "Heavy Cloud":
                weather_image.setImageResource(R.drawable.heavy_cloud);
                break;
            case "Light Cloud":
                weather_image.setImageResource(R.drawable.light_cloud);
                break;
            case "Clear":
                weather_image.setImageResource(R.drawable.clear);
                break;
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("applicable_date", date_label.getText().toString());
        outState.putString("the_temp", temperature_label.getText().toString());
        outState.putString("min_temp", temp_min_label.getText().toString());
        outState.putString("max_temp", temp_max_label.getText().toString());
        outState.putString("wind_speed", wind_speed_label.getText().toString());
        outState.putString("humidity", humidity_label.getText().toString());
        outState.putString("wind_direction_compass", wind_direction_label.getText().toString());
        outState.putString("weather_state_name", weather_state_label.getText().toString());
    }

}
