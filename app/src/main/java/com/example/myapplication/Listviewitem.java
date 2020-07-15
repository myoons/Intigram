package com.example.myapplication;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class Listviewitem implements Comparable{
    private Bitmap big_picture;
    private String name;
    private String pnum;
    private Long contact_id;

    @Override
    public int compareTo(Object o) {
        Listviewitem i = (Listviewitem) o;
        return this.name.compareTo(i.name);
    }

    public Listviewitem(Bitmap big_picture, String name, String pnum, Long contact_id){
        this.big_picture = big_picture;
        this.name = name;
        this.pnum = pnum;
        this.contact_id = contact_id;
    }

    public void setBig_picture(Bitmap big_picture){
        this.big_picture = big_picture;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setPnum(String pnum){ this.pnum = pnum; }
    public void setContact_id(Long contact_id) {this.contact_id = contact_id;};

    public Bitmap getBig_picture(){
        return this.big_picture;
    }
    public String getName(){
        return this.name;
    }
    public String getPnum(){
        return this.pnum;
    }
    public Long getContact_id() {return this.contact_id;}

}
