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
import com.niallod.quickbudget.SettingsActivity;
import com.niallod.quickbudget.adapters.EditItemsListAdapter;
import com.niallod.quickbudget.business.Item;
import com.niallod.quickbudget.database.DatabaseManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
// TODO Make changing the budget month and year
// TODO change to new activity to edit
// TODO Add appbar
public class EditItem extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private Spinner monthInput;
    private Spinner yearInput;
    private Button searchButton;

    private String[] months;
    private String[] years;
    private ArrayAdapter<String> monthsAdapter;
    private ArrayAdapter<String> yearsAdapter;

    private List<Item> incomeData;
    private List<Item> expData;
    private List<String> incomeDataNames = new ArrayList<>();
    private List<String> expDataNames = new ArrayList<>();

    private ListView incomeList;
    private ListView expList;

    private int month;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        monthInput = (Spinner) findViewById(R.id.edit_item_month_input_spinner);
        yearInput = (Spinner) findViewById(R.id.edit_item_year_input_spinner);
        searchButton = (Button) findViewById(R.id.edit_item_go);

        searchButton.setOnClickListener(this);
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
        if(view.getId() == R.id.edit_item_go) {
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

        incomeList = (ListView) findViewById(R.id.income_edit_list);
        incomeList.setAdapter(new EditItemsListAdapter(this, R.layout.edit_item_row, incomeData, incomeDataNames, true));
        expList = (ListView) findViewById(R.id.exp_edit_list);
        expList.setAdapter(new EditItemsListAdapter(this, R.layout.edit_item_row, expData, expDataNames, true));

        incomeList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        expList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        incomeList.setOnItemClickListener(this);
        expList.setOnItemClickListener(this);

        yearInput.setSelection(convertYearToIndex(years, year));
        monthInput.setSelection(month - 1);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.income_edit_list) {
            Intent intent = new Intent(this, ItemModifyActivity.class);
            intent.putExtra("item_id", incomeData.get(i).getId());
            intent.putExtra("item_name", incomeData.get(i).getName());
            intent.putExtra("item_value", incomeData.get(i).getValue());
            intent.putExtra("item_type", incomeData.get(i).getType());
            intent.putExtra("item_month", incomeData.get(i).getMonth() - 1);
            intent.putExtra("item_year", incomeData.get(i).getYear());
            intent.putExtra("item_income", incomeData.get(i).isIncome());
            intent.putExtra("item_exp", incomeData.get(i).isExpenditure());
            intent.putExtra("item_repeat", incomeData.get(i).isRepeatable());
            intent.putExtra("item_location", incomeData.get(i).getLocation());
            startActivity(intent);
        } else if(adapterView.getId() == R.id.exp_edit_list) {
            Intent intent = new Intent(this, ItemModifyActivity.class);
            intent.putExtra("item_id", expData.get(i).getId());
            intent.putExtra("item_name", expData.get(i).getName());
            intent.putExtra("item_value", expData.get(i).getValue());
            intent.putExtra("item_type", expData.get(i).getType());
            intent.putExtra("item_month", expData.get(i).getMonth() - 1);
            intent.putExtra("item_year", expData.get(i).getYear());
            intent.putExtra("item_income", expData.get(i).isIncome());
            intent.putExtra("item_exp", expData.get(i).isExpenditure());
            intent.putExtra("item_repeat", expData.get(i).isRepeatable());
            intent.putExtra("item_location", expData.get(i).getLocation());
            startActivity(intent);
        }
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

            case R.id.action_settings: {
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
            } break;

            default: {

            } break;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }
}
