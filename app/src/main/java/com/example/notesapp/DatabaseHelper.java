package com.example.notesapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    Context context;
    private static final String DATABASE_NAME = "NotesApp.db"; // название бд
    public static int SCHEMA = 1; // версия базы данных
    public static final String TABLE = "notes"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_ENCRYPTION = "encryption";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            //Создание таблицы
            db.execSQL("CREATE TABLE IF NOT EXISTS notes ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY,"
                    + COLUMN_TITLE + " TEXT, "
                    + COLUMN_TEXT + " TEXT,"
                    + COLUMN_ENCRYPTION + " INTEGER" + ");"
            );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
}

