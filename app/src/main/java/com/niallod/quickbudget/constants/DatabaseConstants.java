package com.niallod.quickbudget.constants;

/**
 * static class for database constants
 */
public class DatabaseConstants {

    public static final String DATABASE_NAME = "QBDatabase";
    public static final String DATABASE_CREATE_STATEMENT_BUDGET =
            "Create table budget(month integer, year integer, primary key(month, year));";
    public static final String DATABASE_CREATE_STATEMENT_ITEMS =
            "Create table items(itemid integer primary key autoincrement, name text, type integer," +
            "location text, income text, expenditure text, value real, month integer, year integer, foreign key (" +
            "month, year) references budget(month, year));";
    public static final int DATABASE_VERSION = 1;

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
        public static final String BUDGET_ITEM_VALUE = "value";
    }
}
