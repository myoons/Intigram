package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;

import java.io.ByteArrayOutputStream;

public class CallClickListener implements OnClickListener {

    Context context;
    String number;

    public CallClickListener(Context context, String number) {
        this.context = context;
        this.number = number;
    }


    public void onClick(View v) {

        Intent intent = new Intent(context, CallActivity.class);
        intent.putExtra("dial", number);
        context.startActivity(intent);
    }
}
