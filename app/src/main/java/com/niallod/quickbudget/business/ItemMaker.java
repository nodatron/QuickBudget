package com.niallod.quickbudget.business;

import android.database.Cursor;

import com.niallod.quickbudget.constants.DatabaseConstants;

import java.util.ArrayList;
import java.util.List;

/** Used to make Items or an Items
 * @author Niall O Donnell
 */
public class ItemMaker {

    public ItemMaker() {
    }

    // makes an item
    public Item makeItem(String name, double value, String type,
                         int month, int year, boolean isIncome,
                         boolean isExpense, String location,
                         String[] types) {
        Item item = new Item();
        item.setName(name);
        item.setValue((float) value);
        item.setType(findTypeIntValue(type, types));
        item.setMonth(month);
        item.setYear(year);
        item.setLocation(location);
        item.setIncome(isIncome);
        item.setExpenditure(isExpense);

        return item;
    }

    // makes an item
    public Item makeItem(int id, String name, double value, String type,
                         int month, int year, boolean isIncome,
                         boolean isExpense, String location,
                         String[] types) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setValue((float) value);
        item.setType(findTypeIntValue(type, types));
        item.setMonth(month);
        item.setYear(year);
        item.setLocation(location);
        item.setIncome(isIncome);
        item.setExpenditure(isExpense);

        return item;
    }

    //turns a cursor into a list of items
    public List<Item> convertCursorToList(Cursor cursor) {
        List<Item> items = new ArrayList<>();

        if (cursor.moveToFirst()) {

            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.Keys.BUDGET_ID));
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
                int month = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.Keys.BUDGET_ITEM_MONTH));
                int year = cursor.getInt(cursor.getColumnIndex(DatabaseConstants.Keys.BUDGET_ITEM_YEAR));
                String location = cursor.getString(cursor.getColumnIndex(DatabaseConstants.Keys.BUDGET_ITEM_LOCATION));

                items.add(new Item(
                        id, name, type, value, isIncome, isExpense, month, year, location
                ));
            } while(cursor.moveToNext());

        }

        return items;
    }

    // gets the int value of a type
    private int findTypeIntValue(String type, String[] types) {
        for(int i = 0; i < types.length; i++) {
            if(type.equals(types[i])) {
                return i;
            }
        }
        return -1;
    }
}
