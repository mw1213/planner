package com.agh.planner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.agh.planner.weather.RemoteFetch;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, WeatherTaskFragment.TaskCallbacks {
    private Receiver receiver;
    private BroadcastReceiver broadcastReceiver;

    private DrawerLayout drawer;
    private JSONObject weatherForecast;


    private WeatherTaskFragment weatherTaskFragment;
    private static final String WEATHER_TASK_FRAGMENT = "weather_task_fragment";

    public JSONObject getWeatherForecast() {
        return weatherForecast;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        receiver = new Receiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        registerReceiver(receiver, intentFilter);

        // Navigation bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // "hamburger icon toggle fro navbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_bar_open,
                R.string.navigation_bar_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        if (savedInstanceState == null) {
            //setting up todo-list fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new TodoListFragment()).commit();
        }

        weatherTaskFragment = (WeatherTaskFragment) getSupportFragmentManager().findFragmentByTag(WEATHER_TASK_FRAGMENT);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (weatherTaskFragment!=null) {
            weatherForecast = weatherTaskFragment.getWeatherForecast();
            if (weatherForecast==null)
                weatherTaskFragment.exexuteWeatherTask();
        }
        else {
            weatherTaskFragment = new WeatherTaskFragment();
            getSupportFragmentManager().beginTransaction().add(weatherTaskFragment, WEATHER_TASK_FRAGMENT).commit();
        }


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(weatherForecast == null){
                    weatherTaskFragment = new WeatherTaskFragment();
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("WIFI_ON"));


    }

    @Override
    public void onPostExecute() {
        if (weatherForecast==null) {
            weatherForecast = weatherTaskFragment.getWeatherForecast();
            System.out.println("WEATXHER XHEXKE TASK CYZ TAM FORECAST USTAWIONE");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    /**
     * Change Fragment View when
     * item menu in navigation bar
     * is clicked
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_list:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TodoListFragment()).commit();
                break;
            case R.id.nav_new_task:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TodoNewFormFragment()).commit();
                break;
            case R.id.nav_check_weather:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new WeatherChecker()).commit();

                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        if (v.getId() == R.id.datePicker_new)
            ((DatePickerFragment) newFragment).setTextView((TextView) findViewById(R.id.date_text));
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }



}
