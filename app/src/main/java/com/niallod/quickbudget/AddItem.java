package com.niallod.quickbudget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class AddItem extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

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
    private CheckBox useUserInputtedLocation;
    private EditText userInputtedLocation;
    private RadioButton useLocationInput;

    private ImageButton clearButton;
    private ImageButton submitButton;

    private String typeOfItem = "";
    private int month = 0;
    private int year = 0;
    private boolean isIncome = true;
    private boolean isExpense = false;
    private boolean isRepeat = false;
    private boolean autoLocation = false;

    private ArrayAdapter<String> incomeTypeAdapter;
    private ArrayAdapter<String> expTypeAdapter;
    private ArrayAdapter<String> monthsAdapter;
    private ArrayAdapter<String> yearsAdapter;

    private String[] incomeTypes;
    private String[] expTypes;
    private String[] months;
    private String[] years;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        nameInput = (EditText) findViewById(R.id.add_item_name_input);
        valueInput = (EditText) findViewById(R.id.add_item_value_input);
        itemType = (Spinner) findViewById(R.id.add_item_type_input_spinner);
        monthInput = (Spinner) findViewById(R.id.add_item_month_input_spinner);
        yearInput = (Spinner) findViewById(R.id.add_item_year_input_spinner);
        incomeInput = (RadioButton) findViewById(R.id.add_item_income);
        expInput = (RadioButton) findViewById(R.id.add_item_exp);
        repeatInput = (RadioButton) findViewById(R.id.add_item_repeat);
        useUserInputtedLocation = (CheckBox) findViewById(R.id.add_item_manual_location);
        userInputtedLocation = (EditText) findViewById(R.id.add_item_manual_location_input);
        useLocationInput = (RadioButton) findViewById(R.id.add_item_auto_location);
        clearButton = (ImageButton) findViewById(R.id.add_item_cancel);
        submitButton = (ImageButton) findViewById(R.id.add_item_submit);

        incomeInput.setChecked(isIncome);
        expInput.setChecked(isExpense);
        repeatInput.setChecked(isRepeat);

        incomeInput.setOnClickListener(this);
        expInput.setOnClickListener(this);
        repeatInput.setOnClickListener(this);
        useLocationInput.setOnClickListener(this);

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

        submitButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);

        if(isIncome) {
            itemType.setAdapter(incomeTypeAdapter);
        } else {
            itemType.setAdapter(expTypeAdapter);
        }
        monthInput.setAdapter(monthsAdapter);
        yearInput.setAdapter(yearsAdapter);

        useUserInputtedLocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.add_item_income: {
                isIncome = !isIncome;
                incomeInput.setChecked(isIncome);
                if(isIncome) {
                    expInput.setChecked(false);
                    isExpense = false;
                    itemType.setAdapter(incomeTypeAdapter);
                } else {
                    expInput.setChecked(true);
                    isExpense = true;
                    itemType.setAdapter(expTypeAdapter);
                }
            } break;

            case R.id.add_item_exp: {
                isExpense = !isExpense;
                expInput.setChecked(isExpense);
                if(isExpense) {
                    incomeInput.setChecked(false);
                    isIncome = false;
                    itemType.setAdapter(incomeTypeAdapter);
                } else {
                    incomeInput.setChecked(true);
                    isIncome = true;
                    itemType.setAdapter(expTypeAdapter);
                }
            } break;

            case R.id.add_item_repeat: {
                isRepeat = !isRepeat;
                repeatInput.setChecked(isRepeat);
            } break;

            case R.id.add_item_auto_location: {
                autoLocation = !autoLocation;

            } break;

            case R.id.add_item_cancel: {
                clearAllInputs();
            } break;

            case R.id.add_item_submit: {
                if(updateDatabaseWithNewItem()) {
                    Toast.makeText(this, "New Item Successfully added", Toast.LENGTH_SHORT).show();
                    clearAllInputs();
                } else {
                    Toast.makeText(this, "Item not added", Toast.LENGTH_SHORT).show();
                }
            } break;

            case R.id.add_item_manual_location: {
                if(useUserInputtedLocation.isChecked()) {
                    useLocationInput.setVisibility(View.GONE);
                    userInputtedLocation.setVisibility(View.VISIBLE);
                } else {
                    useLocationInput.setVisibility(View.VISIBLE);
                    userInputtedLocation.setVisibility(View.GONE);
                }
            } break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (view.getId()) {
            case R.id.add_item_type_input_spinner: {
                if(isIncome)
                    typeOfItem = incomeTypes[i];
                else
                    typeOfItem = expTypes[i];
            } break;

            case R.id.add_item_month_input_spinner: {
                String monthStr = months[i];
                Log.d("month spinner", "Data given back " + monthStr);
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
            } break;

            case R.id.add_item_year_input_spinner: {
                year = Integer.parseInt(years[i]);
            } break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private boolean updateDatabaseWithNewItem() {
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
            String location = "";
            if(useUserInputtedLocation.isChecked()) {
                location = userInputtedLocation.getText().toString();
            } else {
                //TODO Location Tracking
                location = "knocklyon";
            }

            Item theItem = itemMaker.makeItem(name, value, typeOfItem, month, year,
                    isIncome, isExpense, isRepeat, location);

            DatabaseManager databaseManager = new DatabaseManager(this);
            databaseManager.openDatabase();
            boolean result = databaseManager.addNewItem(theItem);
            databaseManager.closeDatabase();

            return result;
        }

        return false;
    }

    private void clearAllInputs() {
        nameInput.setText("");
        valueInput.setText("");
        userInputtedLocation.setText("");
        incomeInput.setChecked(true);
        expInput.setChecked(false);
        repeatInput.setChecked(false);
        useLocationInput.setChecked(false);
        isIncome = true;
        isExpense = false;
        isRepeat = false;
        autoLocation = false;
    }

    private boolean inputValid() {

        if(nameInput.getText().toString().equals("") ||
                valueInput.getText().toString().equals("")  ||
                (isIncome && isExpense) || year == 0 ||
                month == 0 || typeOfItem.equals(""))
            return false;

        return true;
    }

}
