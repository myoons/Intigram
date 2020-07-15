package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.google.android.gms.vision.face.FaceDetector;

import android.graphics.Rect;
import android.os.Bundle;
import android.widget.ImageView;
import android.app.AlertDialog;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

public class ImageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_image);

        Intent receivedIntent = getIntent();
        ImageView myImageView = (ImageView)findViewById(R.id.imageView);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable=true;
        View view = findViewById(R.id.imageView);

        byte[] byteArray = receivedIntent.getByteArrayExtra("image");
        Bitmap myBitmap = BitmapFactory.decodeByteArray(byteArray,0, byteArray.length,options);

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(40);
        paint.setStrokeWidth(3);

        Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(myBitmap, 0, 0, null);

        FaceDetector faceDetector= new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();

        if(!faceDetector.isOperational()){
            new AlertDialog.Builder(view.getContext()).setMessage("Could not set up the face detector!").show();
            return;
        }

        Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
        SparseArray<Face> faces = faceDetector.detect(frame);

        double viewWidth = tempCanvas.getWidth();
        double viewHeight = tempCanvas.getHeight();
        double imageWidth = myBitmap.getWidth();
        double imageHeight = myBitmap.getHeight();
        double scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);

        Rect destBounds = new Rect(0, 0, (int)(imageWidth * scale), (int)(imageHeight * scale));
        tempCanvas.drawBitmap(myBitmap, null, destBounds, null);

        for (int i = 0; i < faces.size(); ++i) {
            Face face = faces.valueAt(i);
            int cnt=0;
            int ox=0,oy=0;
            double dis1=0,dis2=0,dis3=0,dis4=0,dis5=0,dis6=0,dis7=0;
            for (Landmark landmark : face.getLandmarks()) {
                int cx = (int) (landmark.getPosition().x * scale);
                int cy = (int) (landmark.getPosition().y * scale);
                if(cnt>0) {
                    double dis=getDistance(ox,oy,cx,cy);
                    if(cnt==1)
                        dis1=dis;
                    else if(cnt==2)
                        dis2=dis;
                    else if(cnt==3)
                        dis3=dis;
                    else if(cnt==4)
                        dis4=dis;
                    else if(cnt==5)
                        dis5=dis;
                    else if(cnt==6)
                        dis6=dis;
                    else if(cnt==7)
                        dis7=dis;
                    if(cnt==1||cnt==4||cnt==6)
                        tempCanvas.drawText((double)Math.round(dis/dis1*1000)/1000+"",(cx+ox)/2,(cy+oy)/2,paint);
                }
                ox=cx;
                oy=cy;
                cnt++;
                tempCanvas.drawCircle(cx, cy, 10, paint);
            }
        }



        myImageView.setImageDrawable(new BitmapDrawable(getResources(),tempBitmap));

    }

    double getDistance(int x1,int y1, int x2, int y2){
        double distance;
        double disX=Math.abs(x1-x2);
        double disY=Math.abs((y1-y2));
        distance=Math.sqrt(Math.pow(disX,2)+(Math.pow(disY,2)));

        return distance;
    }

}
