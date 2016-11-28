package com.niallod.quickbudget.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.niallod.quickbudget.R;
import com.niallod.quickbudget.adapters.MyArrayAdapter;
import com.niallod.quickbudget.business.Item;
import com.niallod.quickbudget.database.DatabaseManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Activity to remove an item from the database
 * @author Niall O Donnell
 */
public class RemoveItem extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    //Views
    private Spinner monthInput;
    private Spinner yearInput;
    private Button searchButton;
    private ListView incomeList;
    private ListView expList;

    //Adapter variables
    private String[] months;
    private String[] years;
    private ArrayAdapter<String> monthsAdapter;
    private ArrayAdapter<String> yearsAdapter;

    private List<Item> incomeData;
    private List<Item> expData;
    private List<String> incomeDataNames = new ArrayList<>();
    private List<String> expDataNames = new ArrayList<>();

    private int month;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        monthInput = (Spinner) findViewById(R.id.item_month_input_spinner);
        yearInput = (Spinner) findViewById(R.id.item_year_input_spinner);
        searchButton = (Button) findViewById(R.id.item_go);

        searchButton.setOnClickListener(this);

        //adapters for spinners
        months = getResources().getStringArray(R.array.months);
        years = getResources().getStringArray(R.array.years);

        monthsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        monthInput.setAdapter(monthsAdapter);
        yearInput.setAdapter(yearsAdapter);

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        init();

    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.item_go) {
            // gets the month selected
            String monthStr = (String) monthInput.getSelectedItem();
            switch (monthStr) {
                case "January": { month = 0; } break;
                case "February": { month = 1; } break;
                case "March": { month = 2; } break;
                case "April": { month = 3; } break;
                case "May": { month = 4; } break;
                case "June": { month = 5; } break;
                case "July": { month = 6; } break;
                case "August": { month = 7; } break;
                case "September": { month = 8; } break;
                case "October": { month = 9; } break;
                case "November": { month = 10; } break;
                case "December": { month = 11; } break;
                default: { month = -1; } break;
            }

            year = Integer.parseInt((String) yearInput.getSelectedItem());

        }

        init();
    }

    /**
     * Dynamically change the listViews
     */
    private void init() {

        incomeList = null;
        expList = null;

        incomeDataNames = new ArrayList<>();
        expDataNames = new ArrayList<>();

        DatabaseManager databaseManager = new DatabaseManager(this);
        databaseManager.openDatabase();
        incomeData = databaseManager.getAllItemsForMonth(month, year, true);
        expData = databaseManager.getAllItemsForMonth(month, year, false);
        databaseManager.closeDatabase();

        for(Item i : incomeData) { incomeDataNames.add(i.getName()); }
        for(Item i : expData) { expDataNames.add(i.getName()); }

        incomeList = (ListView) findViewById(R.id.income_list);
        incomeList.setAdapter(new MyArrayAdapter(this, R.layout.item_kist_view_row, incomeData, incomeDataNames, false));
        expList = (ListView) findViewById(R.id.exp_list);
        expList.setAdapter(new MyArrayAdapter(this, R.layout.item_kist_view_row, expData, expDataNames, false));

        incomeList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        expList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        incomeList.setOnItemClickListener(this);
        expList.setOnItemClickListener(this);

        monthInput.setSelection(month);
        yearInput.setSelection(convertYearToIndex(years, year));
    }

    // deletes the item selected
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        boolean result = false;
        if(adapterView.getId() == R.id.income_list) {
            DatabaseManager databaseManager = new DatabaseManager(this);
            databaseManager.openDatabase();
            result = databaseManager.deleteItemByID(incomeData.get(i).getId());
            databaseManager.closeDatabase();
        } else if(adapterView.getId() == R.id.exp_list) {
            DatabaseManager databaseManager = new DatabaseManager(this);
            databaseManager.openDatabase();
            result = databaseManager.deleteItemByID(expData.get(i).getId());
            databaseManager.closeDatabase();
        }

        if(result) {
            Toast.makeText(this, "Successfully Deleted Item", Toast.LENGTH_SHORT).show();
            init();
        } else {
            Toast.makeText(this, "Successfully Deleted Item", Toast.LENGTH_SHORT).show();
        }
    }

    //appbar methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.remove_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();
        switch (itemID) {
            case R.id.action_add_item: {
                Intent i = new Intent(this, AddItem.class);
                startActivity(i);
            } break;

            case R.id.action_back_arrow: {
                finish();
            } break;

            case R.id.action_edit_item: {
                Intent i = new Intent(this, EditItem.class);
                startActivity(i);
            } break;

            case R.id.action_home_item: {
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            } break;

            default: {

            } break;
        }
        return true;
    }

    /**
     * Turns the year given into the correct year figure
     * @param years String array of all possible years
     * @param year index of the year in the string array
     * @return the numerical version of the year selected
     */
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
