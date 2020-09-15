package com.example.a1to50db;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class dbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mycontacts.db";
    private static final int DATABASE_VERSION = 2;
    private Activity mActivity;
    int match_number_int = 1;

    public dbHelper(Activity activity)
    {

        super(activity, DATABASE_NAME,null, DATABASE_VERSION);
        mActivity = activity;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE contacts ( _id INTEGER PRIMARY KEY AUTOINCREMENT," + "name TEXT,tel DATETIME);");


        //SQLiteDatabase db = helper.getWritableDatabase();
        //TextView TextView = (TextView) mActivity.findViewById(R.id.time_out);
        //db.execSQL("INSERT INTO contacts VALUES (null, " + "'"+ TextView.getText().toString() + "'" + ", '010-1234-100" +  "');");
        //TextView TextView = (TextView) mActivity.findViewById(R.id.time_out);
        //db.execSQL("INSERT INTO contacts VALUES (null, " + "'"+ "5"+ "'" + ", '010-1234-100" +  "');");
        /*
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("_id", "null");
        values.put("name",mActivity.findViewById(R.id.time_out);
        values.put("tel","10");
        long newRowId;
        newRowId = db.insert("contacts",null,values);
        */
    }



    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }


}
