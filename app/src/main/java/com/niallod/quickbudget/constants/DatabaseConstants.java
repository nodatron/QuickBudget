package com.niallod.quickbudget.constants;

/**
 * Created by nodat on 06/11/2016.
 */

public class DatabaseConstants {

    public static final String DATABASE_NAME = "QuickBudgetV2";
    public static final String DATABASE_CREATE_STATEMENT_BUDGET =
            "Create table budget(month integer, year integer, primary key(month, year));";
    public static final String DATABASE_CREATE_STATEMENT_ITEMS =
            "Create table items(itemid integer primary key autoincrement, name text, type integer," +
            "location text, repeating text, income text, expenditure text, value real, month integer, year integer, foreign key (" +
            "month, year) references budget(month, year));";
    public static final int DATABASE_VERSION = 1;

    public static class Error {
        public static final String DATABASE_NOT_FOUND = "Database cannot be reached by the application";
        public static final String NO_DATA_FOUND = "No results found for the query";
    }

    public static class Tables {
        public static final String BUDGET = "budget";
        public static final String BUDGET_ITEMS = "items";

    }

    public static class Keys {
        public static final String BUDGET_ID = "itemid";
        public static final String BUDGET_ITEM_NAME = "name";
        public static final String BUDGET_ITEM_TYPE = "type";
        public static final String BUDGET_ITEM_INCOME = "income";
        public static final String BUDGET_ITEM_EXP = "expenditure";
        public static final String BUDGET_ITEM_MONTH = "month";
        public static final String BUDGET_ITEM_YEAR = "year";
        public static final String BUDGET_ITEM_LOCATION = "location";
        public static final String BUDGET_ITEM_REPEAT = "repeating";
        public static final String BUDGET_ITEM_VALUE = "value";
    }
}
