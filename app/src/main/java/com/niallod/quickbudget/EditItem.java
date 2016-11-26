package com.niallod.quickbudget;

import android.app.Activity;
import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.niallod.quickbudget.adapters.EditItemsListAdapter;
import com.niallod.quickbudget.business.Item;
import com.niallod.quickbudget.database.DatabaseManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class EditItem extends AppCompatActivity implements View.OnClickListener{

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
        monthInput = (Spinner) findViewById(R.id.edit_item_month_input_spinner);
        yearInput = (Spinner) findViewById(R.id.edit_item_year_input_spinner);
        searchButton = (Button) findViewById(R.id.edit_item_go);

        months = getResources().getStringArray(R.array.months);
        years = getResources().getStringArray(R.array.years);

        monthsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        monthInput.setAdapter(monthsAdapter);
        yearInput.setAdapter(yearsAdapter);

        searchButton.setOnClickListener(this);


        DatabaseManager databaseManager = new DatabaseManager(this);
        databaseManager.openDatabase();
        List<Item> incomeData = databaseManager.getAllItemsForMonth(month, year, true);
        List<Item> expData = databaseManager.getAllItemsForMonth(month, year, false);
        databaseManager.closeDatabase();

        for(Item i : incomeData) { incomeDataNames.add(i.getName()); }
        for(Item i : expData) { expDataNames.add(i.getName()); }

        incomeList = (ListView) findViewById(R.id.income_edit_list);
        incomeList.setAdapter(new EditItemsListAdapter(this, R.layout.edit_item_row, incomeData, incomeDataNames));
        expList = (ListView) findViewById(R.id.exp_edit_list);
        expList.setAdapter(new EditItemsListAdapter(this, R.layout.edit_item_row, expData, expDataNames));
    }
}
