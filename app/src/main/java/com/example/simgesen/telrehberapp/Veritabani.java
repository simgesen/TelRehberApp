package com.example.simgesen.telrehberapp;

/**
 * Created by SimgeSen on 23.12.2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

public class Veritabani {
    //VeriTabanı Tablo ve isim adlarını yaz
    private static final String DATABASE_ISIM = "Kisiler";
    private static final String DATABASE_TABLO = "Rehber";
    private static final int DATABASE_VERSION = 1;

    //Veritabanını kullanacak sınıfları tutan Context nesnesi
    private final Context contextim;
    //Oluşturduğumuz veritabanı yardımcı sınıfının nesnesi
    private VeritabaniHelper veritabanihelper;
    //Veritabanımızın nesnesi
    private SQLiteDatabase veritabanim;

    // Oluşturulacak insanlar tablosunun sütunları
    public static final String KEY_ROW_ID = "_id";
    public static final String KEY_ISIM = "isim";
    public static final String KEY_TELEFON = "telefon";

    public Veritabani(Context c) {
        this.contextim = c;
    }

    public Veritabani baglantiyiAc() throws SQLException {

        veritabanihelper = new VeritabaniHelper(contextim);
        veritabanim = veritabanihelper.getWritableDatabase();

        return this;
    }

    public void baglantiyiKapat() {

        veritabanihelper.close();

    }

    private static class VeritabaniHelper extends SQLiteOpenHelper {

        public VeritabaniHelper(Context contextim) {
            super(contextim, DATABASE_ISIM, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DATABASE_TABLO + " (" + KEY_ROW_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT , " + KEY_ISIM
                    + " TEXT NOT NULL, " + KEY_TELEFON + " TEXT NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLO);
            onCreate(db);
        }

    }

    public long isimYasBilgisiniKaydet(String ad, String telefon) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ISIM, ad);
        cv.put(KEY_TELEFON, telefon);
        return veritabanim.insert(DATABASE_TABLO, null, cv);
    }

    public String tumKayitlar() {
        String[] sutunlar = new String[]{KEY_ROW_ID, KEY_ISIM, KEY_TELEFON};
        Cursor c = veritabanim.query(DATABASE_TABLO, sutunlar, null, null,
                null, null, null);

        String tumKayitlar = "";

//sütunların id'leri değişkenlere atandı
//id sütunu 0, isim sütunu 1 ve yas sütünu 2 indexlerine sahip
        int idSiraNo = c.getColumnIndex(KEY_ROW_ID);
        int isimSiraNo = c.getColumnIndex(KEY_ISIM);
        int yasSiraNo = c.getColumnIndex(KEY_TELEFON);

//tüm kayıtların okunması bu for döngüsüyle sağlandı
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

            tumKayitlar = tumKayitlar + c.getString(idSiraNo) + "    "
                    + c.getString(isimSiraNo) + "  " + c.getString(yasSiraNo)
                    + " \n";
        }

        return tumKayitlar;
    }

    public String getName(long aranacakID) {
        String[] sutunlar = new String[]{KEY_ROW_ID, KEY_ISIM, KEY_TELEFON};
        Cursor c = veritabanim.query(DATABASE_TABLO, sutunlar, KEY_ROW_ID + "="
                + aranacakID, null, null, null, null);

        if (c != null) {
            c.moveToFirst();
            String isim = c.getString(1);
            return isim;
        }

        return null;
    }

    public String getYas(long aranacakID) {
        String[] sutunlar = new String[]{KEY_ROW_ID, KEY_ISIM, KEY_TELEFON};
        Cursor c = veritabanim.query(DATABASE_TABLO, sutunlar, KEY_ROW_ID + "="
                + aranacakID, null, null, null, null);

        if (c != null) {
            c.moveToFirst();
            String yas = c.getString(2);
            return yas;
        }
        return null;
    }
}