package com.example.notesapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.DatabaseHelper;
import com.example.notesapp.Models.Note;
import com.example.notesapp.Models.Vigener;
import com.example.notesapp.NoteActivity;
import com.example.notesapp.R;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>{

    Context context;
    ArrayList<Note> notes;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;


    public NotesAdapter(Context context, ArrayList<Note> notes, DatabaseHelper databaseHelper) {
        this.context = context;
        this.notes = notes;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Note note = notes.get(position);

        if(note.getTitle() != null){
            holder.noteTitle.setText(note.getTitle());
        } else {
            holder.noteTitle.setText("Название");
        }

        if(note.getText() != null){
            holder.noteText.setText(note.getText());
        } else {
            holder.noteText.setText("Текст");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("AAA", String.valueOf(note.getIsEncryption()));
                if(note.getIsEncryption() == 0){
                    Intent openNote = new Intent(context, NoteActivity.class);
                    openNote.putExtra("noteTitle", note.getTitle());
                    openNote.putExtra("noteText", note.getText());
                    context.startActivity(openNote);
                } else {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View editDecryptionDialog = inflater.inflate(R.layout.dialog_decryption, null);

                    AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                            .setTitle("Введите ключ для дешифровки")
                            .setView(editDecryptionDialog);

                    EditText editDecryption = editDecryptionDialog.findViewById(R.id.editDecryption);

                    dialog.setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    });

                    dialog.setPositiveButton("Открыть", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(editDecryption.getText().toString().trim().length() > 0){
                                String key = editDecryption.getText().toString().trim().toLowerCase();
                                Vigener vigener = new Vigener(1072, 33);
                                String titleDec = vigener.decrypt(note.getTitle(), key);
                                String textDec = vigener.decrypt(note.getText(), key);
                                Intent openNote = new Intent(context, NoteActivity.class);
                                openNote.putExtra("noteTitle", titleDec);
                                openNote.putExtra("noteText", textDec);
                                context.startActivity(openNote);
                            } else {
                                Toast.makeText(context, "Введите ключ для дешифровки!", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                    dialog.show();
                }
            }
        });

        holder.btnDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Открываем соединение с БД
                db = databaseHelper.getReadableDatabase();

                //Удаляем указанное уведомление
                db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(notes.get(position).getId())});

                //Закрываем соединение с БД
                db.close();

                //Удаляем напоминание
                notes.remove(position);

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        TextView noteTitle, noteText;
        ImageButton btnDeleteNote;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteText = itemView.findViewById(R.id.noteText);
            btnDeleteNote = itemView.findViewById(R.id.btnDeleteNote);
        }
    }
}
