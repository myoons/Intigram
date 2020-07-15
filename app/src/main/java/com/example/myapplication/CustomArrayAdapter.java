package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class CustomArrayAdapter extends BaseAdapter  {
    private LayoutInflater inflate;
    private ArrayList _profiles;
    private int _layout;
    String num = null;
    public static Context context;
    private List<String> list;
    private CustomArrayAdapter.ViewHolder viewHolder;
//    public ArrayList <Info> arr_top7 = ((MainActivity) MainActivity.context).arr_top7;
    private static final String TAG = "PhotoViewerActivity";

    public CustomArrayAdapter(Context context, int layout, ArrayList<Listviewitem> profiles, List<String> list) {
        this.inflate = LayoutInflater.from(context);
        this._layout = layout;
        this._profiles = profiles;
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();//array size
    }

    @Override
    public String getItem(int pos) {
        Listviewitem item = (Listviewitem) _profiles.get(pos);
        return item.getName();
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }//index넘버

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflate.inflate(_layout, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.label = (TextView) convertView.findViewById(R.id.name);

            convertView.setTag(viewHolder);
        } else { viewHolder = (CustomArrayAdapter.ViewHolder)convertView.getTag();}

        viewHolder.label.setText(list.get(pos));

        Listviewitem item = (Listviewitem) _profiles.get(pos);

        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.profile);
        RoundedBitmapDrawable result = RoundedBitmapDrawableFactory.create(Resources.getSystem(),item.getBig_picture());
        result.setCircular(true);
        thumbnail.setImageDrawable(result);

        ///////////////////////////////////////////////////////////////////////////


        ImageClickListener imageViewClickListener = new ImageClickListener(context, item.getBig_picture());
        thumbnail.setOnClickListener(imageViewClickListener);

        TextView name = (TextView)convertView.findViewById(R.id.name);
        name.setText(item.getName());

        TextView telephone = (TextView)convertView.findViewById(R.id.pnum);
        telephone.setText(item.getPnum());

        ImageView calling;
        calling = (ImageView) convertView.findViewById(R.id.calling);
        num = item.getPnum();
        CallClickListener callClickListener = new CallClickListener(context, num);
        calling.setOnClickListener(callClickListener);

        ImageView message;
        message = (ImageView) convertView.findViewById(R.id.message);
        MessageClickListener messageClickListener = new MessageClickListener(context, num);
        message.setOnClickListener(messageClickListener);

        return convertView;
    }

    class ViewHolder{
        public TextView label;
    }

//    double getDistance(int x1,int y1, int x2, int y2){
//        double distance;
//        double disX=Math.abs(x1-x2);
//        double disY=Math.abs((y1-y2));
//        distance=Math.sqrt(Math.pow(disX,2)+(Math.pow(disY,2)));
//
//        return distance;
//    }


}