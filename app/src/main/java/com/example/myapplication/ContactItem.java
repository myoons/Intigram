package com.example.myapplication;

import java.io.Serializable;

public class ContactItem implements Serializable {
    private String user_phNumber, user_Name,photo_Uri,thumbnail_Uri;
    private long id;

    public ContactItem(){}

    public void setUser_phNumber(String user_phNumber) {
        this.user_phNumber = user_phNumber;
    }
    public void setUser_Name(String user_Name) {
        this.user_Name = user_Name;
    }
    public void setPhoto_Uri(String photo_Uri) {
        this.photo_Uri = photo_Uri;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setThumbnail_Uri(String thumbnail_Uri) {this.thumbnail_Uri  = thumbnail_Uri;}

    public String getUser_phNumber() {
        return user_phNumber;
    }
    public String getUser_Name() {
        return user_Name;
    }
    public String getPhoto_Uri() {
        return photo_Uri;
    }
    public String getThumbnail_Uri() {return thumbnail_Uri;}
    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.user_phNumber;
    }

    @Override
    public int hashCode() {
        return getPhNumberChanged().hashCode();
    }
    public String getPhNumberChanged(){
        return user_phNumber.replace("-","");
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ContactItem)
            return getPhNumberChanged().equals(((ContactItem) o).getPhNumberChanged());

        return false;
    }
}