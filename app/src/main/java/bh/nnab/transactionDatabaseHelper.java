package bh.nnab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Shayan on 17-Sep-16.
 */
public class transactionDatabaseHelper extends SQLiteOpenHelper{

    public static final String TABLE_NAME = "transaction_entries";
    public static final String COLUMN_NAME_ID = "ID";
    public static final String COLUMN_NAME_TIME = "TIME";
    public static final String COLUMN_NAME_DATE = "DATE";
    public static final String COLUMN_NAME_AMOUNT = "AMOUNT";
    public static final String COLUMN_NAME_TID = "TRANSACTION_ID";
    public static final String COLUMN_NAME_RID = "RECEIVER_ID";


    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    COLUMN_NAME_TIME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_AMOUNT + REAL_TYPE + COMMA_SEP +
                    COLUMN_NAME_TID + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_RID + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NNAB.db";

    public transactionDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(double amount, String transactionID){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(cal.getTime());
        int month = cal.get(Calendar.MONTH);

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME_AMOUNT, amount);
        cv.put(COLUMN_NAME_TIME, time);
        cv.put(COLUMN_NAME_DATE, month);
        cv.put(COLUMN_NAME_TID, transactionID);
        if( sqLiteDatabase.insert(TABLE_NAME, null, cv) == -1){
            return false;
        }
        return true;
    }

    public boolean insertData(String time, double amount, String transactionID, String date, String receiverID){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME_AMOUNT, amount);
        cv.put(COLUMN_NAME_TIME, time);
        cv.put(COLUMN_NAME_DATE, date);
        cv.put(COLUMN_NAME_TID, transactionID);
        cv.put(COLUMN_NAME_RID, receiverID);
        if( sqLiteDatabase.insert(TABLE_NAME, null, cv) == -1){
            return false;
        }
        return true;
    }

    public boolean updateData(int aPosition, double amount, String transactionID){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(cal.getTime());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME_AMOUNT, amount);
        cv.put(COLUMN_NAME_TIME, time);
        cv.put(COLUMN_NAME_TID, transactionID);

        String whereClause = "ID = " + Integer.toString(aPosition);

        if(sqLiteDatabase.update(TABLE_NAME, cv, whereClause, null) == 0){return false;}
        return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public void deleteAllData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public  Integer deleteData(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME, "ID = " + id, null);
    }

    public void deleteDataUsingTransactionID(String transactionID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, COLUMN_NAME_TID + " = " + transactionID, null);
    }
}
