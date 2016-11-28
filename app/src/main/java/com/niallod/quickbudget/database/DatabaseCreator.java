package com.niallod.quickbudget.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.niallod.quickbudget.constants.DatabaseConstants;

// Reference: The following code is from Susan McKeever Sheet
class DatabaseCreator extends SQLiteOpenHelper {

    DatabaseCreator(Context context) {
        super(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DatabaseConstants.DATABASE_CREATE_STATEMENT_BUDGET);
        sqLiteDatabase.execSQL(DatabaseConstants.DATABASE_CREATE_STATEMENT_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
