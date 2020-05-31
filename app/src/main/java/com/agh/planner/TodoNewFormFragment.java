package com.agh.planner;

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

public class TodoNewFormFragment extends Fragment {

    DatabaseHelper db;
    EditText description;
    TextView date;
    EditText temperature;
    EditText weather;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initDb();
        View view = inflater.inflate(R.layout.fragment_todo_new, container, false);
        date = view.findViewById(R.id.date_text); //dd-mm-yyyy
        description = view.findViewById(R.id.description_text);
        temperature = view.findViewById(R.id.temperature);
        weather = view.findViewById(R.id.weather_type);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = db.addPlan(description.getText().toString(), date.getText().toString(), Float.valueOf(temperature.getText().toString()), weather.getText().toString());
                if (result) {
                    Toast.makeText(getContext(), "Successfully added plan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Unsuccessfully added plan", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void initDb() {
        if (db == null) {
            db = new DatabaseHelper(getActivity());
        }
    }
}
