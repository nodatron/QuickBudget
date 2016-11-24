package com.niallod.quickbudget.business;

import android.database.Cursor;

import com.niallod.quickbudget.constants.DatabaseConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nodat on 24/11/2016.
 */

public class ItemMaker {

    public ItemMaker() {
    }

    public List<Item> convertCursorToList(Cursor cursor) {
        List<Item> items = new ArrayList<>();

        if (cursor.moveToFirst()) {

            do {
                String name = cursor.getString(cursor.getColumnIndex(DatabaseConstants.Keys.BUDGET_ITEM_NAME));
                int type = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.Keys.BUDGET_ITEM_TYPE));
                float value = cursor.getFloat(cursor.getColumnIndex(DatabaseConstants.Keys.BUDGET_ITEM_VALUE));
                boolean isExpense = false;
                if(cursor.getString(cursor.getColumnIndex(DatabaseConstants.Keys.BUDGET_ITEM_EXP)).equals("true")) {
                    isExpense = true;
                }
                boolean isIncome = !isExpense;
                if(cursor.getString(cursor.getColumnIndex(DatabaseConstants.Keys.BUDGET_ITEM_INCOME)).equals("true")) {
                    isIncome = true;
                }
                boolean isRepeatable = false;
                if(cursor.getString(cursor.getColumnIndex(DatabaseConstants.Keys.BUDGET_ITEM_REPEAT)).equals("true")) {
                    isRepeatable = true;
                }
                int month = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.Keys.BUDGET_ITEM_MONTH));
                int year = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.Keys.BUDGET_ITEM_YEAR));
                String location = cursor.getString(cursor.getColumnIndex(DatabaseConstants.Keys.BUDGET_ITEM_LOCATION));

                items.add(new Item(
                        name, type, value, isRepeatable, isIncome, isExpense, month, year, location
                ));
            } while(cursor.moveToNext());

        }

        return items;
    }
}