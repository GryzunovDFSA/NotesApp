package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notesapp.Models.Note;
import com.example.notesapp.Models.Vigener;

public class NoteActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private EditText editTitle, editText;
    private int countItemAdapters;
    private String key, intentTitle, intentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        editTitle = findViewById(R.id.editTitle);
        editText = findViewById(R.id.editText);

        countItemAdapters = getIntent().getIntExtra("countItemAdapter", 0);
        key = getIntent().getStringExtra("key");

        intentTitle = getIntent().getStringExtra("noteTitle");
        intentText = getIntent().getStringExtra("noteText");

        editTitle.setText(intentTitle);
        editText.setText(intentText);

    }

    @Override
    protected void onDestroy() {
        if(TextUtils.isEmpty(intentTitle) && TextUtils.isEmpty(intentText)) {
            String title = editTitle.getText().toString().trim();
            String text = editText.getText().toString().trim();
            int isEncryption;

            String noteTitle;
            String noteText;

            if (!TextUtils.isEmpty(key)) {
                Vigener vigener = new Vigener(1072, 33);
                noteTitle = vigener.encrypt(title, key);
                noteText = vigener.encrypt(text, key);
                isEncryption = 1;
            } else {
                noteTitle = title;
                noteText = text;
                isEncryption = 0;
            }

            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(text)) {

                //Создаём объект заметки
                Note note = new Note(
                        countItemAdapters + 1,
                        noteTitle.toLowerCase(),
                        noteText.toLowerCase(),
                        isEncryption
                );

                //Добавляем заметку в БД
                ContentValues cv = new ContentValues();
                cv.put(DatabaseHelper.COLUMN_ID, note.getId());
                cv.put(DatabaseHelper.COLUMN_TITLE, note.getTitle());
                cv.put(DatabaseHelper.COLUMN_TEXT, note.getText());
                cv.put(DatabaseHelper.COLUMN_ENCRYPTION, note.getIsEncryption());

                db = databaseHelper.getReadableDatabase();
                db.insert(DatabaseHelper.TABLE, null, cv);
                db.close();

                Toast.makeText(NoteActivity.this, "Заметка создана!", Toast.LENGTH_SHORT).show();
            }
            startActivity(new Intent(NoteActivity.this, MainActivity.class));
        }
        super.onDestroy();
    }
}