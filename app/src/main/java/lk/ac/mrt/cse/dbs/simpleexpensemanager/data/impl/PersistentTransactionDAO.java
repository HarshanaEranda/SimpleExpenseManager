package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO extends Persistence implements TransactionDAO {

    public static final int DATABASE_VERSION = 1;
//    public static final String DATABASE_NAME = "Details.db";
    public static final String TABLE2_NAME = "TransactionDetails";
    public static final String ACCOUNT_NUMBER = "account_number";
    public static final String DATE = "date";
    public static final String TYPE = "type";
    public static final String AMOUNT = "amount";

    private static final String SQL_DELETE_ENTRIES2 = "DROP TABLE IF EXISTS " + TABLE2_NAME;


    public PersistentTransactionDAO(Context context) {
        super(context);
    }




    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        SQLiteDatabase db1 = this.getWritableDatabase();
        try {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = dateFormat.format(date);


        String expenseType_ = expenseType.toString();

        ContentValues values = new ContentValues();
        values.put(DATE,date1);
        values.put(ACCOUNT_NUMBER,accountNo );
        values.put(TYPE, expenseType_);

        values.put(AMOUNT, amount);


        db1.insert(TABLE2_NAME, null, values);
        db1.close();}
        catch (Exception ex) {
            //error
        }




    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> courseArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE2_NAME, null);

        if (cursor.moveToFirst()) {
            do {

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    System.out.println(cursor.getString(1) );
                    System.out.println(cursor.getString(2) );
                    System.out.println(cursor.getString(3) );
                   System.out.println(cursor.getString(4) );
                    Date date = dateFormat.parse(cursor.getString(1));


                String Enum = cursor.getString(3);
                ExpenseType expenseType = Enum.equals("INCOME") ? ExpenseType.INCOME : ExpenseType.EXPENSE;
                courseArrayList.add(new Transaction(date,cursor.getString(2),expenseType,cursor.getDouble(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());

        }
        cursor.close();
        return courseArrayList;




    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        List<Transaction> list2 = getAllTransactionLogs();
        int size = list2.size();
        if (size <= limit) {
            return list2;
        }
        return list2.subList(size - limit, size);

    }

}
