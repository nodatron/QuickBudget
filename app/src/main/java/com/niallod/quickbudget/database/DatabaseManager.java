package com.niallod.quickbudget.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.niallod.quickbudget.constants.DatabaseConstants;

/**
 * Created by nodat on 08/11/2016.
 */

public class DatabaseManager {

    //                      Start of Inner Class                                //
    private static class DatabaseCreator extends SQLiteOpenHelper {

        public DatabaseCreator(Context context) {
            super(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(DatabaseConstants.DATABASE_CREATE_STATEMENT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
    //                      End of inner class                                  //

    private final Context context;
    private DatabaseCreator databaseCreator;
    private SQLiteDatabase database;

    public DatabaseManager(final Context context) {
        this.context = context;
        databaseCreator = new DatabaseCreator(this.context);
    }



    public boolean loginUserByUsernamePassword(final String enteredUsername,
                                               final String enteredPassword) {

        Cursor cursor = getUserByUsername(enteredUsername);
        //TODO
//        final String password = cursor.getString(1);
//
//        return enteredPassword.equals(password);
        return true;
    }

    //                      Start of insert queries                             //
    public boolean registerUser(final String username,
                                final String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseConstants.Keys.USERNAME, username);
        contentValues.put(DatabaseConstants.Keys.PASSWORD, password);
        long i = database.insert(DatabaseConstants.Tables.USER, null, contentValues);
        Toast.makeText(context, "Datbase Insertion returns = " + i, Toast.LENGTH_SHORT).show();
        if(i == -1) {
            return false;
        }

        return true;
    }
    //                      End of insert Queries                               //

    //                      Select Queries for Database                         //
    private Cursor getUserByUsername(final String username) {
        return  database.query(true,
                    DatabaseConstants.Tables.USER,
                    new String[] {
                        DatabaseConstants.Keys.USERNAME,
                        DatabaseConstants.Keys.PASSWORD
                    },
                    DatabaseConstants.Keys.USERNAME + " = '" + username + "'",
                    null, null, null, null, null
                );
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
