package com.niallod.quickbudget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.niallod.quickbudget.adapters.MyExpandableListAdapter;
import com.niallod.quickbudget.business.Item;
import com.niallod.quickbudget.database.DatabaseManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private ExpandableListView incomeExpandableListView;
    private ExpandableListView expExpandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        incomeExpandableListView = (ExpandableListView) findViewById(R.id.main_screen_exp_list_view_income);
        expExpandableListView = (ExpandableListView) findViewById(R.id.main_screen_exp_list_view_exp);


        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

//        DatabaseManager dbm = new DatabaseManager(this);
//        dbm.openDatabase();
//        dbm.addNewItem(new Item("work", 1, 300.00f, false, true, false, month, year, "Kncoklyon"));
//        dbm.addNewItem(new Item("birthday", 1, 240.00f, false, true, false, month, year, "Kncoklyon"));
//        dbm.addNewItem(new Item("bonus", 1, 450.00f, false, true, false, month, year, "Kncoklyon"));
//
//        dbm.addNewItem(new Item("bonus", 1, 450.00f, false, false, true, month, year, "Kncoklyon"));
//        dbm.addNewItem(new Item("bonus", 1, 450.00f, false, false, true, month, year, "Kncoklyon"));
//        dbm.addNewItem(new Item("bonus", 1, 450.00f, false, false, true, month, year, "Kncoklyon"));
//        dbm.closeDatabase();

        String parentTitle = getResources().getString(R.string.parent_title_income);
        List<Item> items;

        String expParentTitle = getResources().getString(R.string.parent_title_exp);
        List<Item> expItems;

        DatabaseManager databaseManager = new DatabaseManager(this);
        databaseManager.openDatabase();
        items = databaseManager.getAllItemsForMonth(month, year, true);
        expItems = databaseManager.getAllItemsForMonth(month, year, false);
        databaseManager.closeDatabase();

        HashMap<String, List<Item>> incomeChildItems = new HashMap<>();
        incomeChildItems.put(parentTitle, items);
        final MyExpandableListAdapter incomeExpandableListAdapter =
                new MyExpandableListAdapter(this, parentTitle, incomeChildItems);

        HashMap<String, List<Item>> expChildItems = new HashMap<>();
        expChildItems.put(expParentTitle, expItems);
        final MyExpandableListAdapter expandableListAdapter =
                new MyExpandableListAdapter(this, expParentTitle, expChildItems);

        incomeExpandableListView.setAdapter(incomeExpandableListAdapter);
        expExpandableListView.setAdapter(expandableListAdapter);

        incomeExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {

                if (expandableListView.isGroupExpanded(i)){
                    expandableListView.collapseGroup(i);
                } else {
                    expandableListView.expandGroup(i);
                }
                return true;
            }
        });

        expExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {

                if (expandableListView.isGroupExpanded(i)){
                    expandableListView.collapseGroup(i);
                } else {
                    expandableListView.expandGroup(i);
                }
                return true;
            }
        });


    }
}
