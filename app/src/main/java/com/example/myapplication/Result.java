package com.example.myapplication;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Comparator;

public class Result implements Serializable, Comparable<Result> {

    String name;
    int score;


    Result(int score,String name){
        this.name = name;
        this.score = score;

    }

    public void setName(String name){
        this.name = name;
    }
    public void setScore(int score) { this.score = score; }


    public String getName(){return name;}
    public int getScore(){return score;}


    @Override
    public int compareTo(Result result) {
        return result.getScore() - this.score;
    }

}
