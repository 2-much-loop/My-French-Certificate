package com.maximeg.my_french_certificate.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.maximeg.my_french_certificate.models.QRCode;

import java.util.ArrayList;
import java.util.List;

public class QRDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "QR.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + QRContract.TABLE_NAME + " (" +
                    QRContract._ID + " INTEGER PRIMARY KEY," +
                    QRContract.COLUMN_NAME_FIRST_NAME + " TEXT," +
                    QRContract.COLUMN_NAME_LAST_NAME + " TEXT," +
                    QRContract.COLUMN_NAME_FILE_NAME + " TEXT," +
                    QRContract.COLUMN_NAME_DATE + " TEXT," +
                    QRContract.COLUMN_NAME_TIME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + QRContract.TABLE_NAME;

    public QRDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public List<QRCode> getQRCodes(){
        String sortOrder = QRContract._ID + " DESC";

        Cursor cursor = getReadableDatabase().query(QRContract.TABLE_NAME, null, null, null, null, null, sortOrder, null);

        List<QRCode> items = new ArrayList<>();
        while(cursor.moveToNext()) {
            items.add(new QRCode(
                cursor.getLong(cursor.getColumnIndex(QRContract._ID)),
                cursor.getString(cursor.getColumnIndex(QRContract.COLUMN_NAME_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndex(QRContract.COLUMN_NAME_LAST_NAME)),
                cursor.getString(cursor.getColumnIndex(QRContract.COLUMN_NAME_FILE_NAME)),
                cursor.getString(cursor.getColumnIndex(QRContract.COLUMN_NAME_DATE)),
                cursor.getString(cursor.getColumnIndex(QRContract.COLUMN_NAME_TIME))
            ));
        }
        cursor.close();

        getReadableDatabase().close();

        return items;
    }

    public void deleteQRCode(long id){
        getWritableDatabase().execSQL("DELETE FROM " + QRContract.TABLE_NAME + " WHERE " + QRContract._ID + "=='" + id + "'");
        getWritableDatabase().close();
    }
}
