package com.yukselaggoz.yuztanima;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Veritabani extends SQLiteOpenHelper {

    private static final String DB_NAME = "my_database";
    private static final int DB_VERSION=1;

    private static final String TABLE_SUCLULAR = "suclular";
    private static final String COL_SUCLUID = "_sucluid";
    private static final String COL_SUCLUNAME = "sucluname";
    private static final String COL_SUCLUFACE = "sucluyuz";

    private static final String TABLE_PERSONELLER = "personeller";
    private static final String COL_PERSONELID = "_personelid";
    private static final String COL_PERSONELNAME = "personelname";
    private static final String COL_PERSONELNICK = "personelnick";
    private static final String COL_PERSONELPASSWD = "personelpasswd";

    //public ArrayList<byte[]> suclular=new ArrayList<byte[]>();
    //public ArrayList<String> personeller=new ArrayList<String>();

    public Veritabani(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tablo_suclular = "CREATE TABLE " + TABLE_SUCLULAR + " (" +
                COL_SUCLUID + " INTEGER PRIMARY KEY, " +
                COL_SUCLUNAME + " TEXT, " +
                COL_SUCLUFACE + "blob," + ");";
        db.execSQL(tablo_suclular);

        String tablo_personeller = "CREATE TABLE " + TABLE_PERSONELLER + " (" +
                COL_PERSONELID + " INTEGER PRIMARY KEY, " +
                COL_PERSONELNAME + " TEXT, " +
                COL_PERSONELNICK + " TEXT, " +
                COL_PERSONELPASSWD + "TEXT);";
        db.execSQL(tablo_personeller);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_SUCLULAR);
        db.execSQL("drop table if exists "+TABLE_PERSONELLER);
        onCreate(db);
    }

    public long AddPersonel(VeritabaniHelper vh) {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(COL_PERSONELID, vh.getPersonelId());
        contentValues.put(COL_PERSONELNAME, vh.getPersonelName());
        contentValues.put(COL_PERSONELNICK, vh.getPersonelNick());
        contentValues.put(COL_PERSONELPASSWD, vh.getPersonelPasswd());

        long sonuc = database.insert(TABLE_PERSONELLER, null, contentValues);
        database.close();
        return sonuc;
    }

    public boolean isNickFromSuclular(String nickname, String passwd){
        String passwdTest = null;
        boolean test = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PERSONELLER + " WHERE " + COL_PERSONELNICK + " like '" + nickname + "'", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    passwdTest = cursor.getString(cursor.getColumnIndex(COL_PERSONELPASSWD));
                    if (passwdTest == passwd) {
                        test = true;
                        break;
                    }
                } while (cursor.moveToFirst());
            }
        }
        return test;
    }

    public long AddSuclu(VeritabaniHelper vh) {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(COL_SUCLUID, vh.getSucluId());
        contentValues.put(COL_SUCLUNAME, vh.getSucluName());
        contentValues.put(COL_SUCLUFACE, vh.getSucluYuz());

        long sonuc = database.insert(TABLE_SUCLULAR, null, contentValues);
        database.close();
        return sonuc;
    }

    public byte[] getFaceFromSuclular(int id){
        byte[] face = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SUCLULAR + " WHERE " + COL_SUCLUID + " like '" + id + "'", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    face = cursor.getBlob(cursor.getColumnIndex(COL_SUCLUFACE));
                } while (cursor.moveToFirst());
            }
        }
        return face;
    }
    public String getNameFromSuclular(int id){
        String name = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SUCLULAR + " WHERE " + COL_SUCLUID + " like '" + id + "'", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    name = cursor.getString(cursor.getColumnIndex(COL_SUCLUNAME));
                } while (cursor.moveToFirst());
            }
        }
        return name;
    }
}
