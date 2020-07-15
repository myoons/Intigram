package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class MessageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent receivedIntent = getIntent();
        String sms = "sms:" + receivedIntent.getStringExtra("message");
        startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse(sms)));
    }
}
