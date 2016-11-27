package com.niallod.quickbudget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.niallod.quickbudget.business.Item;
import com.niallod.quickbudget.business.ItemMaker;
import com.niallod.quickbudget.database.DatabaseManager;

import java.util.Locale;

public class ItemModifyActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText nameInput;
    private EditText valueInput;
    private Spinner itemType;
    private Spinner monthInput;
    private Spinner yearInput;
    //    private EditText monthInput;
//    private EditText yearInput;
    private RadioButton incomeInput;
    private RadioButton expInput;
    private RadioButton repeatInput;
    private EditText userInputtedLocation;

    private ImageButton clearButton;
    private ImageButton submitButton;

    private boolean isIncome;
    private boolean isExpense;
    private boolean isRepeat;

    private int month;
    private int year;
    private String typeOfItem;

    private ArrayAdapter<String> incomeTypeAdapter;
    private ArrayAdapter<String> expTypeAdapter;
    private ArrayAdapter<String> monthsAdapter;
    private ArrayAdapter<String> yearsAdapter;

    private String[] incomeTypes;
    private String[] expTypes;
    private String[] months;
    private String[] years;

    private Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_modify);

        bundle = getIntent().getExtras();

        isIncome = bundle.getBoolean("item_income");
        isExpense = bundle.getBoolean("item_exp");
        isRepeat = bundle.getBoolean("item_repeat");

        nameInput = (EditText) findViewById(R.id.modify_item_name_input);
        valueInput = (EditText) findViewById(R.id.modify_item_value_input);
        itemType = (Spinner) findViewById(R.id.modify_item_type_input_spinner);
        monthInput = (Spinner) findViewById(R.id.modify_item_month_input_spinner);
        yearInput = (Spinner) findViewById(R.id.modify_item_year_input_spinner);
        incomeInput = (RadioButton) findViewById(R.id.modify_item_income);
        expInput = (RadioButton) findViewById(R.id.modify_item_exp);
        repeatInput = (RadioButton) findViewById(R.id.modify_item_repeat);
        userInputtedLocation = (EditText) findViewById(R.id.modify_item_manual_location_input);
        clearButton = (ImageButton) findViewById(R.id.modify_item_cancel);
        submitButton = (ImageButton) findViewById(R.id.modify_item_submit);

        incomeInput.setOnClickListener(this);
        expInput.setOnClickListener(this);
        repeatInput.setOnClickListener(this);
        submitButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);

        incomeTypes = getResources().getStringArray(R.array.income_types);
        incomeTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, incomeTypes);
        incomeTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expTypes = getResources().getStringArray(R.array.expenditure_types);
        expTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, expTypes);
        expTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        months = getResources().getStringArray(R.array.months);
        monthsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        years = getResources().getStringArray(R.array.years);
        yearsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        init();
        monthInput.setAdapter(monthsAdapter);
        yearInput.setAdapter(yearsAdapter);

        nameInput.setText(bundle.getString("item_name"));
        itemType.setSelection(bundle.getInt("item_type") - 1);
        valueInput.setText(String.format(Locale.UK, "%6.2f", bundle.getFloat("item_value")));
        monthInput.setSelection(bundle.getInt("item_month"));
        yearInput.setSelection(convertYearToIndex(years, bundle.getInt("item_year")));
        incomeInput.setChecked(isIncome);
        expInput.setChecked(isExpense);
        repeatInput.setChecked(isRepeat);
        userInputtedLocation.setText(bundle.getString("item_location"));

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.modify_item_income: {
                isIncome = !isIncome;
                incomeInput.setChecked(isIncome);
                if (isIncome) {
                    expInput.setChecked(false);
                    isExpense = false;
                } else {
                    expInput.setChecked(true);
                    isExpense = true;
                }
                init();
            } break;

            case R.id.modify_item_exp: {
                isExpense = !isExpense;
                expInput.setChecked(isExpense);
                if (isExpense) {
                    incomeInput.setChecked(false);
                    isIncome = false;
                } else {
                    incomeInput.setChecked(true);
                    isIncome = true;
                }
                init();
            } break;

            case R.id.modify_item_repeat: {
                isRepeat = !isRepeat;
                repeatInput.setChecked(isRepeat);
            } break;

            case R.id.modify_item_cancel: {
                finish();
            } break;

            case R.id.modify_item_submit: {
                if(modifyDatabaseWIthEdit()) {
                    Toast.makeText(this, "New Item Successfully added", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Item not added", Toast.LENGTH_SHORT).show();
                }
            } break;
        }


    }

    private boolean inputValid() {

        if(nameInput.getText().toString().equals("") ||
                valueInput.getText().toString().equals("")  ||
                (isIncome && isExpense) || year == 0 ||
                month == 0 || typeOfItem.equals(""))
            return false;

        return true;
    }

    private boolean modifyDatabaseWIthEdit() {
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
        typeOfItem = (String) itemType.getSelectedItem();

        if(inputValid()) {
            ItemMaker itemMaker = new ItemMaker();

            String name = nameInput.getText().toString();
            double value = Double.parseDouble(valueInput.getText().toString());
            String location = userInputtedLocation.getText().toString();
            String[] types;
            if(isIncome) {
                types = incomeTypes;
            } else {
                types = expTypes;
            }
            Item theItem = itemMaker.makeItem(bundle.getInt("item_id"), name, value, typeOfItem, month, year,
                    isIncome, isExpense, isRepeat, location,types);

            DatabaseManager databaseManager = new DatabaseManager(this);
            databaseManager.openDatabase();
            boolean result = databaseManager.updateExistingItem(theItem);
            databaseManager.closeDatabase();

            return result;
        }
        return false;
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

    private void init() {

        if(isIncome) {
            itemType.setAdapter(incomeTypeAdapter);
        } else {
            itemType.setAdapter(expTypeAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.modify_item_menu, menu);
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
}
