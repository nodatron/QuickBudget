package com.niallod.quickbudget.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.niallod.quickbudget.R;
import com.niallod.quickbudget.business.Item;
import com.niallod.quickbudget.business.ItemMaker;
import com.niallod.quickbudget.database.DatabaseManager;

import java.util.Calendar;
import java.util.TimeZone;

/** This is am activity class that holds all the functionality for adding a new item to the database
 * @author Niall O Donnell
 */
public class AddItem extends AppCompatActivity implements View.OnClickListener {

    // all the widget variables
    private EditText nameInput;
    private EditText valueInput;
    private TextView typeLabel;
    private Spinner itemType;
    private Spinner monthInput;
    private Spinner yearInput;
    private RadioButton incomeInput;
    private RadioButton expInput;
    private EditText userInputtedLocation;

    private ImageButton clearButton;
    private ImageButton submitButton;

    // variables used in the insert
    private String typeOfItem = "";
    private int month = 0;
    private int year = 0;
    private boolean isIncome = true;
    private boolean isExpense = false;

    // adapters and string needed
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

        // getting all the views from the layout
        nameInput = (EditText) findViewById(R.id.add_item_name_input);
        valueInput = (EditText) findViewById(R.id.add_item_value_input);
        itemType = (Spinner) findViewById(R.id.add_item_type_input_spinner);
        monthInput = (Spinner) findViewById(R.id.add_item_month_input_spinner);
        yearInput = (Spinner) findViewById(R.id.add_item_year_input_spinner);
        incomeInput = (RadioButton) findViewById(R.id.add_item_income);
        expInput = (RadioButton) findViewById(R.id.add_item_exp);
        userInputtedLocation = (EditText) findViewById(R.id.add_item_manual_location_input);
        clearButton = (ImageButton) findViewById(R.id.add_item_cancel);
        submitButton = (ImageButton) findViewById(R.id.add_item_submit);
        typeLabel = (TextView) findViewById(R.id.add_item_type_label);

        typeLabel.setText(getResources().getString(R.string.type_label));

        //auto setting radio buttons
        incomeInput.setChecked(isIncome);
        expInput.setChecked(isExpense);

        // setting listeners
        incomeInput.setOnClickListener(this);
        expInput.setOnClickListener(this);
        submitButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);

        // populating the adapters
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
        // method used to change a spinner dynamically
        init();
        //set month and year adapters
        monthInput.setAdapter(monthsAdapter);
        yearInput.setAdapter(yearsAdapter);

        // set the month and year spinner to the current month
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        monthInput.setSelection(month);
        yearInput.setSelection(convertYearToIndex(years, year));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            // Income clicked so make sure expense is the opposite and change the spinner for types
            case R.id.add_item_income: {
                isIncome = !isIncome;
                incomeInput.setChecked(isIncome);
                if(isIncome) {
                    expInput.setChecked(false);
                    isExpense = false;
                } else {
                    expInput.setChecked(true);
                    isExpense = true;
                }
                init();
            } break;
            // Same as above but expense is clicked
            case R.id.add_item_exp: {
                isExpense = !isExpense;
                expInput.setChecked(isExpense);
                if(isExpense) {
                    incomeInput.setChecked(false);
                    isIncome = false;
                } else {
                    incomeInput.setChecked(true);
                    isIncome = true;
                }
                init();
            } break;

            // cancel button clicked
            case R.id.add_item_cancel: {
                clearAllInputs();
            } break;

            //submit button clicked
            case R.id.add_item_submit: {
                if(insertNewItemIntoDatabase()) {
                    Toast.makeText(this, "New Item Successfully added", Toast.LENGTH_SHORT).show();
                    clearAllInputs();
                } else {
                    Toast.makeText(this, "Item not added", Toast.LENGTH_SHORT).show();
                }
            } break;
        }

    }

    /**
     * Attempts to add the new item to the database
     * @return if the insert was successfull
     */
    private boolean insertNewItemIntoDatabase() {
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
        typeOfItem = (String) itemType.getSelectedItem();

        // checks if all required fields are selected
        if(inputValid()) {
            ItemMaker itemMaker = new ItemMaker();

            // gets all data and queries the database
            String name = nameInput.getText().toString();
            double value = Double.parseDouble(valueInput.getText().toString());
            String location = userInputtedLocation.getText().toString();
            String[] types;
            if(isIncome) {
                types = incomeTypes;
            } else {
                types = expTypes;
            }
            Item theItem = itemMaker.makeItem(name, value, typeOfItem, month, year,
                    isIncome, isExpense, location, types);

            DatabaseManager databaseManager = new DatabaseManager(this);
            databaseManager.openDatabase();
            boolean result = databaseManager.addNewItem(theItem);
            databaseManager.closeDatabase();

            return result;
        }

        return false;
    }

    /**
     * Clears all inputs and sets the views back to default
     */
    private void clearAllInputs() {
        nameInput.setText("");
        valueInput.setText("");
        userInputtedLocation.setText("");
        incomeInput.setChecked(true);
        expInput.setChecked(false);
        isIncome = true;
        isExpense = false;
    }

    /**
     *  Checks is all required fields have been filled
     * @return if all required fields are filled
     */
    private boolean inputValid() {

        if(nameInput.getText().toString().equals("") ||
                valueInput.getText().toString().equals("")  ||
                (isIncome && isExpense) || year == 0 ||
                month == -1 || typeOfItem.equals(""))
            return false;

        return true;
    }

    /**
     * Switches the type adapter based on whether income or expense is clicked
     */
    private void init() {
        if(isIncome) {
            itemType.setAdapter(incomeTypeAdapter);
        } else {
            itemType.setAdapter(expTypeAdapter);
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

    // Next two methods are for the appbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();
        switch (itemID) {
            case R.id.action_remove_item: {
                Intent i = new Intent(this, RemoveItem.class);
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



}
