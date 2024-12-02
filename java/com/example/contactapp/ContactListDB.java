package com.example.contactapp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ContactListDB extends SQLiteOpenHelper {
    public ContactListDB(Context context) {
        super(context, "ContactListDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("DB @ OnCreate");
        String sql ="CREATE TABLE ContactList("
                +"uid INTEGER,"
                +"name TEXT,"
                +"email TEXT,"
                +"phoneHome TEXT,"
                +"phoneOffice TEXT,"
                +"image TEXT"
                +")";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("Write code to modify database schema here");
        // db.execSQL("ALTER table my_table  ......");
        // db.execSQL("CREATE TABLE  ......");
    }
    public void insertContactList(String uid, String name, String email, String phoneHome, String phoneOffice, String image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("uid", uid);
        cv.put("name", name);
        cv.put("email", email);
        cv.put("phoneHome", phoneHome);
        cv.put("phoneOffice", phoneOffice);
        cv.put("image",image);
        db.insert("ContactList", null, cv);
//        db.close();
    }
    public void updateContactList(String uid, String name, String email, String phoneHome, String phoneOffice, String image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("uid", uid);
        cv.put("name", name);
        cv.put("email", email);
        cv.put("phoneHome", phoneHome);
        cv.put("phoneOffice", phoneOffice);
        cv.put("image", image);
        db.update("ContactList", cv, "uid=?", new String[] {uid});
//        db.close();
    }
    public void deleteContactList(String uid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("ContactList", "uid=?", new String[] {uid});
        db.close();
    }
    public Cursor selectContactList(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = null;
        try{
            res = db.rawQuery(query, null);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return res;
    }

}