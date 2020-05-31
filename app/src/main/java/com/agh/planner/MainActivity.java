package com.agh.planner;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private JSONObject weatherForecast;

    public JSONObject getWeatherForecast() {
        return weatherForecast;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


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


        //setting up todo-list fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new TodoListFragment()).commit();

        new weatherTask().execute();
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


    class weatherTask extends AsyncTask<String, Void, String> {
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
                JSONObject jsonObj = new JSONObject(result);
                weatherForecast = jsonObj;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
