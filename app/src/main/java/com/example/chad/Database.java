package com.example.chad;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by user on 10/01/2018.
 */

public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "DailyPos";

    private static final String TABLE_QUESTIONNAIRE = "tblquestionnaire";

    private static final String TITLE_ID = "id";
    private static final String TITLE_NAME = "title_name";
    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

/*    String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + USER_NAME + " TEXT NOT NULL,"
            + USER_PASS + " TEXT NOT NULL,"
            + ROLE + " TEXT NOT NULL,"
            + PHONE_NUMBER + " TEXT NOT NULL,"
            + STATUS + " INTEGER NOT NULL" + ")";*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            //db.execSQL(CREATE_USERS_TABLE);
            //db.execSQL(CREATE_SALES_DELETED_RECORD_TABLE);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        // Create tables again
        onCreate(db);
    }
}
