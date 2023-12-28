package com.example.notesapp.Models;

public class Note {
    int id, isEncryption;
    String title, text;

    public Note(int id, String title, String text, int isEncryption) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.isEncryption = isEncryption;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIsEncryption() {
        return isEncryption;
    }

    public void setIsEncryption(int isEncryption) {
        this.isEncryption = isEncryption;
    }
}

