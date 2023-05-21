package com.example.arbird.AddressDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;

    private static final String DATABASE_NAME = "adresses.db";
    private static final String TABLE_NAME = "Adresses";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ADRES = "adres";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_COUNTRY = "country";
    private static final String COLUMN_POSTIND = "postind";
    private static final String COLUMN_NAME = "name";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_ADRES = 1;
    private static final int NUM_COLUMN_CITY = 2;
    private static final int NUM_COLUMN_COUNTRY = 3;
    private static final int NUM_COLUMN_POSTIND = 4;
    private static final int NUM_COLUMN_NAME = 5;

    private final SQLiteDatabase database;

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        database = getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ADRES + " TEXT, " +
                COLUMN_CITY + " TEXT, " +
                COLUMN_COUNTRY + " TEXT, " +
                COLUMN_POSTIND + " TEXT, " +
                COLUMN_NAME + " TEXT); ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public List<AdresData> getAll(){
        Cursor cursor = database.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        ArrayList result = new ArrayList();
        if(cursor.isAfterLast()) return result;
        do{
            result.add(
                    new AdresData(
                            cursor.getString(NUM_COLUMN_ADRES),
                            cursor.getString(NUM_COLUMN_CITY),
                            cursor.getString(NUM_COLUMN_COUNTRY),
                            cursor.getString(NUM_COLUMN_POSTIND),
                            cursor.getString(NUM_COLUMN_NAME)
                    )
            );
        }while(cursor.moveToNext());
        cursor.close();
        return result;
    }
    public void add(AdresData data){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ADRES, data.getAddres());
        cv.put(COLUMN_CITY, data.getCity());
        cv.put(COLUMN_COUNTRY, data.getCountry());
        cv.put(COLUMN_POSTIND, data.getPostalCode());
        cv.put(COLUMN_NAME, data.getKnownName());
        database.insert(TABLE_NAME, null, cv);
    }

    @Override
    public synchronized void close() {
        super.close();
        database.close();
    }
}
