package com.example.notesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notesapp.Adapters.NotesAdapter;
import com.example.notesapp.Models.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor userCursor;
    private RecyclerView notesRecycler;
    private NotesAdapter notesAdapter;
    private FloatingActionButton btnAddNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Создание БД
        databaseHelper = new DatabaseHelper(getApplicationContext());
        //db = databaseHelper.getReadableDatabase();

        //Удаление всех данных
        //db.execSQL("DELETE FROM " + DatabaseHelper.TABLE);

        //Вывод заметок
        setNoteRecycler();

        btnAddNote = findViewById(R.id.btnAddNote);
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddNote();
            }
        });
    }

    private void setNoteRecycler() {
        // открываем подключение
        db = databaseHelper.getReadableDatabase();

        //получаем данные из бд в виде курсора
        userCursor =  db.rawQuery("SELECT * FROM "+ DatabaseHelper.TABLE, null);

        //Список заметок
        ArrayList<Note> notes = new ArrayList<>();

        //Добавление заметок из бд в список
        while (userCursor.moveToNext()){
            notes.add(
                    new Note(
                            userCursor.getInt(0),
                            userCursor.getString(1),
                            userCursor.getString(2),
                            userCursor.getInt(3))
            );
        }

        //Закрытие соединения с бд и курсора
        db.close();
        userCursor.close();

        //Указываем настройки для отображения списка заметок
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        notesRecycler = findViewById(R.id.notesRecycler);
        notesRecycler.setLayoutManager(layoutManager);

        notesAdapter = new NotesAdapter(MainActivity.this, notes, databaseHelper);
        notesRecycler.setAdapter(notesAdapter);
    }

    private void showDialogAddNote(){
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View editEncryptionDialog = inflater.inflate(R.layout.dialog_encryption, null);

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Придумайте слово для шифрования")
                .setView(editEncryptionDialog);

        EditText editEncryption = editEncryptionDialog.findViewById(R.id.editEncryption);

        dialog.setNegativeButton("Не шифровать", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                int itemCount = notesAdapter.getItemCount();
                Intent createNote = new Intent(MainActivity.this, NoteActivity.class);
                createNote.putExtra("countItemAdapter", itemCount);
                
                finish();
                startActivity(createNote);
            }
        });

        dialog.setPositiveButton("Зашифровать", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(editEncryption.getText().toString().trim().toLowerCase().length() > 0){
                    int itemCount = notesAdapter.getItemCount();
                    String key = editEncryption.getText().toString().trim().toLowerCase();

                    Intent createNote = new Intent(MainActivity.this, NoteActivity.class);
                    createNote.putExtra("countItemAdapter", itemCount);
                    createNote.putExtra("key", key);

                    finish();
                    startActivity(createNote);
                } else {
                    Toast.makeText(MainActivity.this, "Придумайте слово для шифрования!", Toast.LENGTH_LONG).show();
                    showDialogAddNote();
                }

            }
        });

        dialog.show();
    }
}