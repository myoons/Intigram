package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class PopupActivity extends Activity {

    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    ImageView img5;

    TextView txtText1;
    TextView txtText2;
    TextView txtText3;
    TextView txtText4;
    TextView txtText5;

    public ArrayList<ContactItem> getContactList() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_URI,
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
        };

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Cursor c = getContentResolver().query(uri, projection,null, null, sortOrder);
        LinkedHashSet<ContactItem> hashlist = new LinkedHashSet<>();

        if (c.moveToFirst()) {
            do {
                ContactItem contactItem = new ContactItem();
                contactItem.setUser_phNumber(c.getString(0));
                contactItem.setUser_Name(c.getString(1));
                contactItem.setPhoto_Uri(c.getString(2));
                contactItem.setId(c.getLong(3));
                contactItem.setThumbnail_Uri(c.getString(4));

                hashlist.add(contactItem);

            } while (c.moveToNext());
        }

        c.close();
        ArrayList contactItems = new ArrayList<>(hashlist);
        return contactItems;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_activity);

        //UI 객체생성

        img1 = (ImageView)findViewById(R.id.img1);
        img2 = (ImageView)findViewById(R.id.img2);
        img3 = (ImageView)findViewById(R.id.img3);
        img4 = (ImageView)findViewById(R.id.img4);
        img5 = (ImageView)findViewById(R.id.img5);

        txtText1 = (TextView)findViewById(R.id.txtText1);
        txtText2 = (TextView)findViewById(R.id.txtText2);
        txtText3 = (TextView)findViewById(R.id.txtText3);
        txtText4 = (TextView)findViewById(R.id.txtText4);
        txtText5 = (TextView)findViewById(R.id.txtText5);

        //데이터 가져오기
        Intent intent = getIntent();
        ArrayList<Result> data = (ArrayList<Result>) intent.getSerializableExtra("data");


        ArrayList contactItems = getContactList();

        for (int i=0;i<contactItems.size();i++) {

            ContactItem tempItem = (ContactItem) contactItems.get(i);
            Bitmap big_bitmap = null;

            if (tempItem.getPhoto_Uri() == null) {
                big_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person2);
            } else {
                try {
                    big_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(tempItem.getPhoto_Uri()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (int j = 0; j < data.size(); j++) {
                if ((data.get(j).getName()).equals(tempItem.getUser_Name())) {

                    try {
                        if (j==0)
                            img1.setImageBitmap(big_bitmap);
                        else if (j==1)
                            img2.setImageBitmap(big_bitmap);
                        else if (j==2)
                            img3.setImageBitmap(big_bitmap);
                        else if (j==3)
                            img4.setImageBitmap(big_bitmap);
                        else if (j==4)
                            img5.setImageBitmap(big_bitmap);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        img1.setBackground(new ShapeDrawable(new OvalShape()));//rounding
        img1.setClipToOutline(true);

        img2.setBackground(new ShapeDrawable(new OvalShape()));//rounding
        img2.setClipToOutline(true);

        img3.setBackground(new ShapeDrawable(new OvalShape()));//rounding
        img3.setClipToOutline(true);

        img4.setBackground(new ShapeDrawable(new OvalShape()));//rounding
        img4.setClipToOutline(true);

        img5.setBackground(new ShapeDrawable(new OvalShape()));//rounding
        img5.setClipToOutline(true);

        txtText1.setText("Your Best Friend!\n" + "Name : " + data.get(0).getName() +"\n"+ "Score : " + data.get(0).getScore());
        txtText2.setText("Name : " + data.get(1).getName() +"\n" +"Score : " + data.get(1).getScore());
        txtText3.setText("Name : " + data.get(2).getName() + "\n"+"Score : " + data.get(2).getScore());
        txtText4.setText("Name : " + data.get(3).getName() + "\n"+"Score : " + data.get(3).getScore());
        txtText5.setText("Name : " + data.get(4).getName() + "\n"+"Score : " + data.get(4).getScore());

    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}