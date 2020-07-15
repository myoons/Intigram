package com.example.myapplication;

import android.graphics.Bitmap;

import com.google.android.material.internal.ParcelableSparseArray;

import java.io.Serializable;

public class Info implements Comparable<Info>, Serializable {
    String name;
    int score;
    double nose;
    double mouth;


    public void Info(String name, int score, double nose, double mouth){
        this.name = name;
        this.score = score;
        this.nose = nose;
        this.mouth = mouth;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setScore(int score) { this.score = score; }
    public void setNose(double nose) { this.nose = nose; }
    public void setMouth(double mouth) { this.mouth = mouth; }

    public String getName(){return name;}
    public int getScore(){return score;}
    public double getNose(){return nose;}
    public double getMouth(){return mouth;}

    @Override
    public int compareTo(Info info) {
        return info.getScore() - this.score;
    }
}
