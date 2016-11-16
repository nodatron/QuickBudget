package com.niallod.quickbudget.constants;

/**
 * Created by nodat on 06/11/2016.
 */

public class DatabaseConstants {

    public static final String DATABASE_NAME = "QuickBudget";
    public static final String DATABASE_CREATE_STATEMENT = "Create table user(username text primary key, password text);";
    public static final int DATABASE_VERSION = 1;

    public static class Error {
        public static final String DATABASE_NOT_FOUND = "Database cannot be reached by the application";
        public static final String NO_DATA_FOUND = "No results found for the query";
    }

    public static class Tables {
        public static final String USER = "user";
        public static final String BUDGET = "budget";
        public static final String INCOME = "income";
        public static final String EXPENDITURE = "expenditure";

    }

    public static class Keys {
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
    }
}
