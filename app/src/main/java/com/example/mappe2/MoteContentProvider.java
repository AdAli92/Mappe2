package com.example.mappe2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class MoteContentProvider extends ContentProvider {
    private static final String Mote_ID = "Mote_ID";
    private static final String Tittel = "Tittel";
    private static final String Type = "Type";
    private static final String Dato = "Dato";
    private static final String Tid = "Tid";
    private static final String Sted = "Sted";
    private static final String DB_NAME = "MotePersonDb";
    private static final int DB_VERSJON = 3;
    private static final String TABLE = "Mote";
    private static final String PROVIDER = "com.example.mappe2";
    private static final int Mote = 1;
    private static final int MMote = 2;
    MoteContentProvider.DatabaseHelper DBhelper;
    SQLiteDatabase db;

    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER + "/Mote");
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER, "Mote", MMote);
        uriMatcher.addURI(PROVIDER, "Mote/#", Mote);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSJON);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "CREATE TABLE " + TABLE
                    + "(" + Mote_ID + " INTEGER PRIMARY KEY," + Tittel + " TEXT," + Type + " TEXT," + Sted + " TEXT,"
                    + Dato + " DATETIME," + Tid + " TEXT" + ")";
            ;
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists " + TABLE);
            onCreate(db);
        }

    }

    public MoteContentProvider() {
    }

    @Override
    public boolean onCreate() {
        DBhelper = new MoteContentProvider.DatabaseHelper(getContext());
        db = DBhelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cur = null;
        if (uriMatcher.match(uri) == Mote) {
            cur = db.query(TABLE, projection, Mote_ID + "=" + uri.getPathSegments().get(1), selectionArgs, null, null, sortOrder);
            return cur;
        } else {
            cur = db.query(TABLE, null, null, null, null, null, null);
            return cur;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) == Mote) {
            db.delete(TABLE, Mote_ID + " = " + uri.getPathSegments().get(1), selectionArgs);
            getContext().getContentResolver().notifyChange(uri, null);
            return 1;
        }
        if (uriMatcher.match(uri) == MMote) {
            db.delete(TABLE, null, null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 2;
        }
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case MMote:
                return "vnd.android.cursor.dir/vnd.example.Mote";
            case Mote:
                return "vnd.android.cursor.item/vnd.example.Mote";
            default:
                throw new
                        IllegalArgumentException("Ugyldig URI" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = DBhelper.getWritableDatabase();
        db.insert(TABLE, null, values);
        Cursor c = db.query(TABLE, null, null, null, null, null, null);
        c.moveToLast();
        long minid = c.getLong(0);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, minid);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        if (uriMatcher.match(uri) == Mote) {
            db.update(TABLE, values, Mote_ID + " = " + uri.getPathSegments().get(1), null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 1;
        }
        if (uriMatcher.match(uri) == MMote) {
            db.update(TABLE, null, null, null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 2;
        }
        return 0;
    }
}