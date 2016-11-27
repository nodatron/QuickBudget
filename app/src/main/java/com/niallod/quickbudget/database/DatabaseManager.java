package com.niallod.quickbudget.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.niallod.quickbudget.business.Item;
import com.niallod.quickbudget.business.ItemMaker;
import com.niallod.quickbudget.constants.DatabaseConstants;

import java.util.List;

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
    public boolean addNewItem(Item item) {

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
            contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_EXP, "true");
            contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_INCOME, "false");
        } else {
            contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_INCOME, "true");
            contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_EXP, "false");
        }

        if(item.isRepeatable()) {
            contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_REPEAT, "true");
        } else {
            contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_REPEAT, "false");
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

    private boolean doesItemExist(int id) {

        Cursor cursor = database.query(true, DatabaseConstants.Tables.BUDGET_ITEMS,
                new String[] {DatabaseConstants.Keys.BUDGET_ID},
                DatabaseConstants.Keys.BUDGET_ID + " = " + id,
                null, null, null, null, null);
        if(!(cursor.moveToFirst()) || cursor.getCount() == 0) {
            return false;
        }

        return true;
    }

    public List<Item> getAllItemsForMonth(int month, int year, boolean income) {
        String type = DatabaseConstants.Keys.BUDGET_ITEM_EXP;
        if (income) {
            type = DatabaseConstants.Keys.BUDGET_ITEM_INCOME;
        }
        Cursor cursor = database.query(DatabaseConstants.Tables.BUDGET_ITEMS,
                new String[] {
                        DatabaseConstants.Keys.BUDGET_ID,
                        DatabaseConstants.Keys.BUDGET_ITEM_NAME,
                        DatabaseConstants.Keys.BUDGET_ITEM_TYPE,
                        DatabaseConstants.Keys.BUDGET_ITEM_VALUE,
                        DatabaseConstants.Keys.BUDGET_ITEM_LOCATION,
                        DatabaseConstants.Keys.BUDGET_ITEM_MONTH,
                        DatabaseConstants.Keys.BUDGET_ITEM_YEAR,
                        DatabaseConstants.Keys.BUDGET_ITEM_INCOME,
                        DatabaseConstants.Keys.BUDGET_ITEM_EXP,
                        DatabaseConstants.Keys.BUDGET_ITEM_REPEAT
                },
                DatabaseConstants.Keys.BUDGET_ITEM_MONTH + " = " + month + " and "
                + DatabaseConstants.Keys.BUDGET_ITEM_YEAR + " = " + year + " and "
                + type + " = " + "'true'",
                null,null,null,null,null);

        ItemMaker itemMaker = new ItemMaker();
        return itemMaker.convertCursorToList(cursor);
    }
    //                      End of select queries                               //


    //                      Database Update Methods                             //
    //TODO Make this better by only updating the required fields
    public boolean updateExistingItem(Item item) {

        if(!(doesItemExist(item.getId()))) {
            return false;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_NAME, item.getName());
        contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_TYPE, item.getType());
        contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_VALUE, item.getValue());
        contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_LOCATION, item.getLocation());
        contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_MONTH, item.getMonth());
        contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_YEAR, item.getYear());
        if (item.isExpenditure()) {
            contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_EXP, "true");
            contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_INCOME, "false");
        } else {
            contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_INCOME, "true");
            contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_EXP, "false");
        }

        if(item.isRepeatable()) {
            contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_REPEAT, "true");
        } else {
            contentValues.put(DatabaseConstants.Keys.BUDGET_ITEM_REPEAT, "false");
        }

        if(database.update(DatabaseConstants.Tables.BUDGET_ITEMS,
                           contentValues,
                           DatabaseConstants.Keys.BUDGET_ID + " = " + item.getId(),
                           null) == 1) {
            return true;
        }
        return false;
    }
    //                      End of Update Methods                               //

    //                      Database Delete Methods                             //
    public boolean deleteItemByID(int id) {
        if(doesItemExist(id)) {
            if(database.delete(DatabaseConstants.Tables.BUDGET_ITEMS,
                    DatabaseConstants.Keys.BUDGET_ID + " = " + id,
                    null) == 1) {
                return true;
            }
        }

        return false;
    }
    //                      End of Delete Methods                               //

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
