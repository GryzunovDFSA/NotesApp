package com.example.notesapp.Models;

public class Vigener {

    private int bias, letters;

    public Vigener(int bias, int letters) {
        this.bias = bias;
        this.letters = letters;
    }

    public String encrypt(String text, String key) {
        String encrypt = "";
        int keyLen = key.length();
        for (int i = 0, len = text.length(); i < len; i++) {
            if(text.charAt(i) != ' '){
                encrypt += (char) (((text.charAt(i) + key.charAt(i % keyLen) - 2 * this.bias) % this.letters) + this.bias);
            } else {
                encrypt += ' ';
            }
        }
        return encrypt;
    }

    public String decrypt(String cipher, String key) {
        String decrypt = "";
        int keyLen = key.length();
        for (int i = 0, len = cipher.length(); i < len; i++) {
            if(cipher.charAt(i) != ' '){
                decrypt += (char) (((cipher.charAt(i) - key.charAt(i % keyLen) + this.letters) % this.letters) + this.bias);
            } else {
                decrypt += ' ';
            }
        }
        return decrypt;
    }

}
