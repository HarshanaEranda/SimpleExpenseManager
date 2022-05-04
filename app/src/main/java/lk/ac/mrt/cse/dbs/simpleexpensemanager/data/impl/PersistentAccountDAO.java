package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO extends Persistence implements AccountDAO {
    public static final int DATABASE_VERSION = 1;
//    public static final String DATABASE_NAME = "Details.db";
    //Table1 ane column name of it
    public static final String TABLE1_NAME = "AccountDetails";
    //    public static final String ID = "id";
    public static final String ACCOUNT_NUMBER = "account_number";
    public static final String BANK = "bank";
    public static final String ACCOUNT_HOLDER = "account_holder";
    public static final String INITIAL_BALANCE = "initial_balance";

//    private static final
//    String SQL_CREATE_ENTRIES1 = "CREATE TABLE " + TABLE1_NAME + " (account_number TEXT PRIMARY KEY,bank TEXT,account_holder  TEXT,initial_balance TEXT)";

    private static final String SQL_DELETE_ENTRIES1 = "DROP TABLE IF EXISTS " +  "AccountDetails";
    public PersistentAccountDAO(Context context) {
        super(context);

    }

//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        try{
//        String SQL_CREATE_ENTRIES1 = "CREATE TABLE " + TABLE1_NAME + " (account_number TEXT PRIMARY KEY,bank TEXT,account_holder  TEXT,initial_balance TEXT)";
//        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES1);}
//        catch (Exception e){}
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        try{
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS AccountDetails");
//        onCreate(sqLiteDatabase);}
//        catch (Exception e){}
//    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> AccNum_List=new ArrayList<>();

        SQLiteDatabase db3 = this.getReadableDatabase();
        Cursor res = db3.rawQuery(" select account_number from AccountDetails",null);
//        while(res.isAfterLast()){
//
//            String acc_Number=res.getString(res.getColumnIndex(ACCOUNT_NUMBER));
//            AccNum_List.add(acc_Number);
//            res.moveToNext();
//
//        }
//        db3.close();
//        return AccNum_List;

        if (res.moveToFirst()) {
            do {
                String acc_Number=res.getString(0);
                AccNum_List.add(acc_Number);
            } while (res.moveToNext());
        }

        res.close();

        return AccNum_List;
    }

    @Override
    public List<Account> getAccountsList() {
        ArrayList<Account> List=new ArrayList<>();

        SQLiteDatabase db3 = this.getReadableDatabase();
        Cursor res = db3.rawQuery("select * from AccountDetails",null);
//        while(res.isAfterLast()){
//            Account accountList = new Account(res.getString(res.getColumnIndex(ACCOUNT_NUMBER)),
//                    res.getString(res.getColumnIndex(BANK)),
//                    res.getString(res.getColumnIndex(ACCOUNT_HOLDER)),res.getDouble(res.getColumnIndex(INITIAL_BALANCE)));
//            List.add(accountList);
//            res.moveToNext();
//        }
//        db3.close();
//        return List;
//        Cursor cursor = openR().rawQuery("SELECT * FROM " + TABLE1, null);
        if (res.moveToFirst()) {
            do {
                List.add(new Account(res.getString(0),
                        res.getString(1),
                        res.getString(2),
                        res.getDouble(3)));
            } while (res.moveToNext());
        }
        res.close();
        return List;


    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor res =  db1.rawQuery( "select * from AccountDetails where account_number = "+ accountNo, null );
        if (res.moveToFirst()){
            Account account=new Account(res.getString(0),res.getString(1),res.getString(2),res.getDouble(3));
            return account;
        }
        db1.close();
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);

    }

    @Override
    public void addAccount(Account account) {

        SQLiteDatabase db2 = this.getWritableDatabase();


        ContentValues values2 = new ContentValues();

        values2.put("account_number", account.getAccountNo());
        values2.put("bank", String.valueOf(account.getBankName()));

        values2.put("account_holder",String.valueOf(account.getAccountHolderName()));
        values2.put("initial_balance", String.valueOf(account.getBalance()));
        // Insert the new row, returning the primary key value of the new row
         db2.insert("AccountDetails", null, values2);
         db2.close();

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = this.getWritableDatabase();
         if(db.delete("AccountDetails", "account_number = ? ",new String[] {accountNo})==0){
             String msg = "Account " + accountNo + " is invalid.";
             throw new InvalidAccountException(msg);
         };
         db.close();


    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db1 = this.getReadableDatabase();
        Cursor res =  db1.rawQuery( "select * from AccountDetails where account_number = "+ accountNo, null );

        if (res.moveToFirst()) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
           switch (expenseType) {
                case EXPENSE:
                    contentValues.put("initial_balance",res.getDouble(3) - amount);
                    break;
                case INCOME:
                    contentValues.put("initial_balance", res.getDouble(3) + amount);
                    break;
            }

            contentValues.put("account_number", accountNo);
            contentValues.put("bank", res.getString(1));

            contentValues.put("account_holder",res.getString(2));

            db.update("AccountDetails", contentValues, "account_number = ? ", new String[] { accountNo} );
            db.close();
        }
        else {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }







    }
}
