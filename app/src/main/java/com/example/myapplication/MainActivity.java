package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.android.material.tabs.TabLayout;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import static com.example.myapplication.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {

    public ArrayList <Info> arr_top7 = new ArrayList<Info>();
    public static Context context;
    public String conveyedstring = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        checkSelfPermission();

        ViewPager vp = findViewById(R.id.viewpager);
        VPAdapter adapter = new VPAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);

        // 연동

        TabLayout tablayout = findViewById(R.id.layout_tab);
        tablayout.setupWithViewPager(vp);

        tablayout.getTabAt(0).setIcon(R.drawable.main_on);
        tablayout.getTabAt(1).setIcon(R.drawable.address_on);
        tablayout.getTabAt(2).setIcon(R.drawable.gallery_on);
        tablayout.getTabAt(3).setIcon(R.drawable.intigram_on);

        countCall();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode ==1){
            int length = permissions.length;
            for (int i=0; i<length ; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivity","Permission accepted : " + permissions[i]);
                }
            }
        }
    }


    public void checkSelfPermission() {
        String temp = "";

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.CAMERA + " ";
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_CONTACTS + " ";
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_CONTACTS + " ";
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_CALL_LOG + " ";
        }

        if (TextUtils.isEmpty(temp) == false) {
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),1);
        } else {
            Toast.makeText(this, "All permission Accepeted",Toast.LENGTH_SHORT).show();
        }
    }

    private void countCall() {

        Cursor managedCursor = getApplicationContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);//얘로 대상 인식. 나머지는 점수 산정에만 쓴다.
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE); //수신,발신은 2점, OUTGOING은 1점
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION); //30분 넘으면 5점
        int name_index = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
//        managedCursor.moveToFirst();

        while (managedCursor.moveToNext()) {

            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDuration = managedCursor.getString(duration);
            String name = managedCursor.getString(name_index);
            Info input = new Info();

            int score = 0;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    score = 2;
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    score = 2;
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    score = 1;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + dircode);
            }
            if(Integer.parseInt(callDuration)>=1800) score = 3;
//            Log.d("Read",phNumber+Integer.toString(score));

            if(name == null) {
                input.score = score;
                input.name = phNumber;
            }
            else {
                input.score = score;
                input.name = name;
            }
            save_contact(input);
        }
        managedCursor.close();

        Collections.sort(arr_top7);
//        if(arr_top8.size()>7){
//            for(int i = 0; i < 7 ; i++)
//            {arr_top7.add(arr_top8.get(i));}
//        }
//        else{
//            for(int i = 0; i < arr_top8.size(); i++)
//            {arr_top7.add(arr_top8.get(i));}
//        }
        int i = 1;
        int size = arr_top7.size();
        while(arr_top7.size()>5){
            arr_top7.remove(size-i);
            i++;
        }
    }

    //listview 객체들을 data base에 넣어준다. 점수는 0점으로 시작함
    private void save_contact(Info input){

        int recent = isExist(input.name);
        int add_score = input.score;

        if(recent > 0)
        {
            Info add_new = new Info();
            add_new.setName(input.name);
            add_new.setScore(add_score + recent);
            arr_top7.add(add_new);
        }
        else{//없음
            arr_top7.add(input);
        }
    }
    private int isExist(String name){//이미 DB에 있는 번호면 점수 반환
        Iterator<Info> it = arr_top7.iterator();

        while(it.hasNext()){
            Info i = it.next();
            if (i.name.equals(name)){
                arr_top7.remove(i);
                return i.score;
            }
        }
        return 0;
    }

    public void changeText(String text)
    {
        conveyedstring = text;
//        Toast.makeText(this,conveyedstring,Toast.LENGTH_SHORT).show();
    }


}