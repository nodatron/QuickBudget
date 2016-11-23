package com.niallod.quickbudget.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.niallod.quickbudget.business.Item;
import com.niallod.quickbudget.constants.DatabaseConstants;

/**
 * Created by nodat on 08/11/2016.
 */

public class DatabaseManager {

    private final Context context;
    private DatabaseCreator databaseCreator;
    private SQLiteDatabase database;

    public DatabaseManager(final Context context) {
        this.context = context;
        databaseCreator = new DatabaseCreator(this.context);
    }


    //                      Start of insert queries                             //
    public boolean addNewIncomeItem(Item item) {

        if(!doesBudgetAlreadyExist(item.getMonth(), item.getYear())) {
            ContentValues newBudgetValues = new ContentValues();

            newBudgetValues.put(DatabaseConstants.Keys.BUDGET_ITEM_MONTH, item.getMonth());
            newBudgetValues.put(DatabaseConstants.Keys.BUDGET_ITEM_YEAR, item.getYear());

            if(database.insert(DatabaseConstants.Tables.BUDGET, null, newBudgetValues) == -1L) {
                return false;
            }
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_NAME, item.getName());
        contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_TYPE, item.getType());
        contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_VALUE, item.getValue());
        contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_LOCATION, item.getLocation());
        contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_MONTH, item.getMonth());
        contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_YEAR, item.getYear());
        if (item.isExpenditure()) {
            contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_EXP, true);
            contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_INCOME, false);
        } else {
            contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_INCOME, true);
            contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_EXP, false);
        }

        if(item.isRepeatable()) {
            contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_REPEAT, true);
        } else {
            contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_REPEAT, false);
        }

        if(database.insert(DatabaseConstants.Tables.BUDGET_ITEMS, null, contentValues) == -1L) {
            return false;
        }
        return true;
    }
    //                      End of insert Queries                               //

    //                      Select Queries for Database                         //
    private boolean doesBudgetAlreadyExist(int month, int year) {

        Cursor cursor = database.query(true, DatabaseConstants.Tables.BUDGET, new String[] {
                DatabaseConstants.Keys.BUDGET_ITEM_MONTH, DatabaseConstants.Keys.BUDGET_ITEM_YEAR
                },
                DatabaseConstants.Keys.BUDGET_ITEM_MONTH + " = " + month + " and " +
                DatabaseConstants.Keys.BUDGET_ITEM_YEAR + " = " + year,
                null,null,null,null,null);

        if(!(cursor.moveToFirst()) || cursor.getCount() == 0) {
            cursor.close();
            return false;
        }

        return true;
    }
    //                      End of select queries                               //


    //                      Database Control Methods                            //
    public DatabaseManager openDatabase()
            throws SQLException {
        database = databaseCreator.getWritableDatabase();
        return this;
    }

    public void closeDatabase() {
        database.close();
    }
    //                      End of Control Methods                              //


}
