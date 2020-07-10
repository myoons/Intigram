package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import static com.example.myapplication.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        ViewPager vp = findViewById(R.id.viewpager);
        VPAdapter adapter = new VPAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);

        // 연동
        TabLayout tablayout = findViewById(R.id.layout_tab);
        tablayout.setupWithViewPager(vp);

        // ArrayList<Integer> images = new ArrayList<>();
        // images.add(R.drawable.cal);
        // images.add(R.drawable.sea);
        // images.add(R.drawable.set);

        // for(int i=0; i<3; i++) tab.getTabAt(i);

    }

}