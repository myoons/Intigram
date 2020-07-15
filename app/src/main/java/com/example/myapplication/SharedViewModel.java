package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<String> liveText = new MutableLiveData<>();

    public LiveData<String> getinfoarray(){
        return liveText;
    }

    public void setText(String text){
        liveText.setValue(text);
    }

}
