package com.agh.planner;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.agh.planner.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TodoListFragment extends Fragment {

    DatabaseHelper db;
    EditText description;
    EditText date;
    EditText temperature;
    EditText weather;
    List<String> ids;
    List<String> menuItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        initDb();
        initListView(view);

        return view;
    }

    private void initDb() {
        if (db == null) {
            db = new DatabaseHelper(getActivity());
        }
    }

    private void initListView(View view) {
        Cursor result = db.getAllData();
        menuItems = new ArrayList<>(result.getCount());
        ids = new ArrayList<>(result.getCount());

        while (result.moveToNext()) {
            ids.add( result.getString(0));
            menuItems.add(
                    "Description: " +result.getString(1) + "\n" +
                    "Date: " + result.getString(2)+ "\n"  +
                    "Temperature: " + result.getString(3)+ "\n" +
                    "Weather type: " + result.getString(4));
        }

        ListView listView = view.findViewById(R.id.list_view);
        final ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                Objects.requireNonNull(getActivity()),
                android.R.layout.simple_list_item_1,
                menuItems
        );
        listView.setAdapter(listViewAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (db.deleteRow(Integer.parseInt(ids.get(position))) > 0) {
                    ids.remove(position);
                    menuItems.remove(position);
                    listViewAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
    }
}
