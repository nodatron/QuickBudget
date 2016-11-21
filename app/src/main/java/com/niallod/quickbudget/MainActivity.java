package com.niallod.quickbudget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.niallod.quickbudget.adapters.MyExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expandableListView = (ExpandableListView) findViewById(R.id.main_screen_exp_list_view_income);

        List<String> parent_titles = new ArrayList<String>();
        List<String> child_list = new ArrayList<String>();
        List<String> child_list2 = new ArrayList<String>();
        List<Double> child_list_values = new ArrayList<Double>();
        List<Double> child_list_values2 = new ArrayList<Double>();

        HashMap<String, List<String>> child_parent_link = new HashMap<String, List<String>>();
        HashMap<List<String>, List<Double>> child_values = new HashMap<List<String>, List<Double>>();

        String heading_items[] = getResources().getStringArray(R.array.parent_titles);
        String child_list1_array[] = getResources().getStringArray(R.array.income_dummy_names);
        String child_list2_array[] = getResources().getStringArray(R.array.exp_dummy_names);
        String child_list1_values[] = getResources().getStringArray(R.array.income_dummy_values);
        String child_list2_values[] = getResources().getStringArray(R.array.exp_dummy_values);

        for(String s : heading_items) {
            parent_titles.add(s);
        }
        for(String s : child_list1_array) {
            child_list.add(s);
        }
        for(String s : child_list2_array) {
            child_list2.add(s);
        }
        for(String s : child_list1_values) {
            child_list_values.add(Double.parseDouble(s));
        }
        for(String s : child_list2_values) {
            child_list_values2.add(Double.parseDouble(s));
        }

        child_parent_link.put(parent_titles.get(0), child_list);
        child_parent_link.put(parent_titles.get(1), child_list2);
        child_parent_link.put(parent_titles.get(2), child_list);

        child_values.put(child_list, child_list_values);
        child_values.put(child_list2, child_list_values2);

        MyExpandableListAdapter myExpandableListAdapter =
                new MyExpandableListAdapter(this, parent_titles, child_parent_link, child_values);

        expandableListView.setAdapter(myExpandableListAdapter);


    }
}
