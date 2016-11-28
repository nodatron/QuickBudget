package com.niallod.quickbudget.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.niallod.quickbudget.R;
import com.niallod.quickbudget.adapters.MyExpandableListAdapter;
import com.niallod.quickbudget.business.Item;
import com.niallod.quickbudget.database.DatabaseManager;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ExpandableListView incomeExpandableListView;
    private ExpandableListView expExpandableListView;
    private TextView balanceLabel;
    private TextView balanceValue;
    private Spinner monthInput;
    private Spinner yearInput;
    private Button searchButton;
    private String[] months;
    private String[] years;
    private ArrayAdapter<String> monthsAdapter;
    private ArrayAdapter<String> yearsAdapter;
    private int month;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        // Adding one to month cause it give the previous month if i don't
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);

        monthInput = (Spinner) findViewById(R.id.main_month_input_spinner);
        yearInput = (Spinner) findViewById(R.id.main_year_input_spinner);
        searchButton = (Button) findViewById(R.id.main_search_button);

        searchButton.setOnClickListener(this);
        months = getResources().getStringArray(R.array.months);
        years = getResources().getStringArray(R.array.years);

        monthsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        monthInput.setAdapter(monthsAdapter);
        yearInput.setAdapter(yearsAdapter);

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
        double balance = calculateBalanceValue(incomeItems, expItems);
        if(balance <= 0) {
            balanceLabel.setTextColor(getResources().getColor(R.color.red));
            balanceValue.setTextColor(getResources().getColor(R.color.red));
        } else {
            balanceLabel.setTextColor(getResources().getColor(R.color.black));
            balanceValue.setTextColor(getResources().getColor(R.color.black));
        }
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

        yearInput.setSelection(convertYearToIndex(years, year));
        monthInput.setSelection(month - 1);

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

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.main_search_button) {
            String monthStr = (String) monthInput.getSelectedItem();
            switch (monthStr) {
                case "January": { month = 1; } break;
                case "February": { month = 2; } break;
                case "March": { month = 3; } break;
                case "April": { month = 4; } break;
                case "May": { month = 5; } break;
                case "June": { month = 6; } break;
                case "July": { month = 7; } break;
                case "August": { month = 8; } break;
                case "September": { month = 9; } break;
                case "October": { month = 10; } break;
                case "November": { month = 11; } break;
                case "December": { month = 12; } break;
                default: { month = 0; } break;
            }

            year = Integer.parseInt((String) yearInput.getSelectedItem());

        }

        init();
    }

    private int convertYearToIndex(String[] years, int year) {
        Integer theYear = year;
        for(int i = 0; i < years.length; i++) {
            if(years[i].equals(theYear.toString())) {
                return i - 1;
            }
        }
        return -1;
    }

}
