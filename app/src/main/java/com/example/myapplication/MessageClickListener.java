package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class MessageClickListener implements OnClickListener {

    Context context;
    String number;

    public MessageClickListener(Context context, String number) {
        this.context = context;
        this.number = number;
    }


    public void onClick(View v) {
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra("message", number);
        context.startActivity(intent);
    }
}
