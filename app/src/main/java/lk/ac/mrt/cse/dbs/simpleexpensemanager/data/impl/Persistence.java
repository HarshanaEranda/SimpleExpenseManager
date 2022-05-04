package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.io.Serializable;

public abstract class Persistence extends SQLiteOpenHelper implements Serializable {
    public static final String DB_NAME = "190709P_Details.db";
    public static final String TABLE1 = "AccountDetails";
    public static final String TABLE2 = "TransactionDetails";
    private static int version = 1;



    public Persistence(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String statement1 = "CREATE TABLE " + TABLE1 + "(account_number TEXT PRIMARY KEY,bank TEXT,account_holder  TEXT,initial_balance TEXT)";
            String statement2 = "CREATE TABLE " + TABLE2 + "( id INTEGER PRIMARY KEY Autoincrement, date TEXT,account_number  TEXT, type TEXT,amount  REAL,FOREIGN KEY(account_number) REFERENCES " + TABLE1 + "(account_number))";
            db.execSQL(statement1);
            db.execSQL(statement2);

        } catch (Exception er) {
            //error
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            version = newVersion;
            db.execSQL("DROP TABLE IF EXISTS " + TABLE1);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE2);
            onCreate(db);
        } catch (Exception er) {
            //error
        }
    }
//    // get readable/writable database
//    public SQLiteDatabase openR() throws SQLException {
//        return this.getReadableDatabase();
//    }
//    public SQLiteDatabase openW() throws SQLException {
//        return this.getWritableDatabase();
//    }

}
