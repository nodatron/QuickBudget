package com.niallod.quickbudget;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private ExpandableListView incomeExpandableListView;
    private ExpandableListView expExpandableListView;
    private TextView balanceLabel;
    private TextView balanceValue;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overview_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();
        Intent i = null;
        switch (itemID) {
            case R.id.action_add_item: {
                i = new Intent(this, AddItem.class);
            } break;

            case R.id.action_edit_item: {
                i = new Intent(this, EditItem.class);
            } break;

            case R.id.action_remove_item: {
                i = new Intent(this, RemoveItem.class);
            } break;

            case R.id.action_settings: {
                i = new Intent(this, SettingsActivity.class);
            } break;

            default: {

            } break;
        }
        startActivity(i);
        return true;
    }

    private double calculateBalanceValue(List<Item> incomeItems, List<Item> expItems) {
        double balance = 0.0;
        for(Item i : incomeItems) {
            balance += i.getValue();
        }
        for(Item i : expItems) {
            balance -= i.getValue();
        }
        return balance;
    }

    private void init() {
        incomeExpandableListView = (ExpandableListView) findViewById(R.id.main_screen_exp_list_view_income);
        expExpandableListView = (ExpandableListView) findViewById(R.id.main_screen_exp_list_view_exp);
        balanceLabel = (TextView) findViewById(R.id.balance_label_main_menu);
        balanceValue = (TextView) findViewById(R.id.balance_total_main_menu);



        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        // Adding one to month cause it give the previous month if i don't
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        List<Item> incomeItems;
        List<Item> expItems;

        DatabaseManager databaseManager = new DatabaseManager(this);
        databaseManager.openDatabase();
        incomeItems = databaseManager.getAllItemsForMonth(month, year, true);
        expItems = databaseManager.getAllItemsForMonth(month, year, false);
        databaseManager.closeDatabase();

        String parentTitle = getResources().getString(R.string.parent_title_income);
        String expParentTitle = getResources().getString(R.string.parent_title_exp);
        balanceLabel.setTypeface(null, Typeface.BOLD);
        balanceLabel.setText(getResources().getString(R.string.parent_title_balance));
        balanceValue.setText(String.format(Locale.UK, "%6.2f", calculateBalanceValue(incomeItems, expItems)));


        HashMap<String, List<Item>> incomeChildItems = new HashMap<>();
        incomeChildItems.put(parentTitle, incomeItems);
        final MyExpandableListAdapter incomeExpandableListAdapter =
                new MyExpandableListAdapter(this, parentTitle, incomeChildItems);

        HashMap<String, List<Item>> expChildItems = new HashMap<>();
        expChildItems.put(expParentTitle, expItems);
        final MyExpandableListAdapter expandableListAdapter =
                new MyExpandableListAdapter(this, expParentTitle, expChildItems);

        incomeExpandableListView.setAdapter(incomeExpandableListAdapter);
        expExpandableListView.setAdapter(expandableListAdapter);

        incomeExpandableListView.expandGroup(0);
        expExpandableListView.expandGroup(0);

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
