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

import com.niallod.quickbudget.R;
import com.niallod.quickbudget.adapters.MyArrayAdapter;
import com.niallod.quickbudget.business.Item;
import com.niallod.quickbudget.database.DatabaseManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/** Activity class for choosing which item you wish to edit
 * @author Niall O Donnell
 */
public class EditItem extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    // View variables
    private Spinner monthInput;
    private Spinner yearInput;
    private Button searchButton;
    private ListView incomeList;
    private ListView expList;

    // Variable for adapters
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

        // getting views by id
        monthInput = (Spinner) findViewById(R.id.item_month_input_spinner);
        yearInput = (Spinner) findViewById(R.id.item_year_input_spinner);
        searchButton = (Button) findViewById(R.id.item_go);

        searchButton.setOnClickListener(this);

        // inflating rows using adapters
        months = getResources().getStringArray(R.array.months);
        years = getResources().getStringArray(R.array.years);

        monthsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //setting adapters
        monthInput.setAdapter(monthsAdapter);
        yearInput.setAdapter(yearsAdapter);

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        // Used to update listviews dynamically
        init();

    }


    @Override
    public void onClick(View view) {
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

        init();
    }

    private void init() {

        //used to populate listviews
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
        incomeList.setAdapter(new MyArrayAdapter(this, R.layout.item_kist_view_row, incomeData, incomeDataNames, true));
        expList = (ListView) findViewById(R.id.exp_list);
        expList.setAdapter(new MyArrayAdapter(this, R.layout.item_kist_view_row, expData, expDataNames, true));

        incomeList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        expList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        incomeList.setOnItemClickListener(this);
        expList.setOnItemClickListener(this);

        yearInput.setSelection(convertYearToIndex(years, year));
        monthInput.setSelection(month);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // sends all the data for an item to a new activity
        if(adapterView.getId() == R.id.income_list) {
            Intent intent = new Intent(this, ItemModifyActivity.class);
            intent.putExtra("item_id", incomeData.get(i).getId());
            intent.putExtra("item_name", incomeData.get(i).getName());
            intent.putExtra("item_value", incomeData.get(i).getValue());
            intent.putExtra("item_type", incomeData.get(i).getType());
            intent.putExtra("item_month", incomeData.get(i).getMonth() - 1);
            intent.putExtra("item_year", incomeData.get(i).getYear());
            intent.putExtra("item_income", incomeData.get(i).isIncome());
            intent.putExtra("item_exp", incomeData.get(i).isExpenditure());
            intent.putExtra("item_location", incomeData.get(i).getLocation());
            startActivity(intent);
        } else if(adapterView.getId() == R.id.exp_list) {
            Intent intent = new Intent(this, ItemModifyActivity.class);
            intent.putExtra("item_id", expData.get(i).getId());
            intent.putExtra("item_name", expData.get(i).getName());
            intent.putExtra("item_value", expData.get(i).getValue());
            intent.putExtra("item_type", expData.get(i).getType());
            intent.putExtra("item_month", expData.get(i).getMonth() - 1);
            intent.putExtra("item_year", expData.get(i).getYear());
            intent.putExtra("item_income", expData.get(i).isIncome());
            intent.putExtra("item_exp", expData.get(i).isExpenditure());
            intent.putExtra("item_location", expData.get(i).getLocation());
            startActivity(intent);
        }
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

    // Appbar methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
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

            case R.id.action_remove_item: {
                Intent i = new Intent(this, RemoveItem.class);
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

    // Refresh listviews
    @Override
    public void onResume() {
        super.onResume();
        init();
    }
}
