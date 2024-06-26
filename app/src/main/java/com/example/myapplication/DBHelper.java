package com.example.myapplication;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;






public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "Login.db";
    public static final int VERSION = 1;
    public DBHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDb) {
        MyDb.execSQL("create Table users(username TEXT primary key,password TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDb, int oldVersion, int newVersion) {
        MyDb.execSQL("drop Table if exists users");
        onCreate(MyDb);
    }

    public Boolean insertData(String username, String password){
        SQLiteDatabase MyDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("username", username);
        contentValues.put("password", password);

        long result = MyDb.insert("users",null,contentValues);

        if(result == -1){
            return false;
        }
        else{
            return true;
        }

    }

    public Boolean checkusername(String username){
        SQLiteDatabase MyDb = this.getWritableDatabase();

        Cursor cursor = MyDb.rawQuery("select * from users where username = ?",new String[] {username});

        if(cursor.getCount()>0){
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MyDb = this.getWritableDatabase();
        Cursor cursor = MyDb.rawQuery("select * from users where username = ? and password = ?", new String[] {username,password});

        if(cursor.getCount()>0){
            return true;
        }
        else{
            return false;
        }
    }
}
