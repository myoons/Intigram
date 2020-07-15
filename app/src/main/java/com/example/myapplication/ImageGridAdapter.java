package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.util.ArrayList;

import static java.security.AccessController.getContext;


public class ImageGridAdapter extends BaseAdapter {

    Context context = null;
    private static final String TAG = "PhotoViewerActivity";

    //-----------------------------------------------------------
    // imageIDs는 이미지 파일들의 리소스 ID들을 담는 배열입니다.
    // 이 배열의 원소들은 자식 뷰들인 ImageView 뷰들이 무엇을 보여주는지를
    // 결정하는데 활용될 것입니다.

    ArrayList<Bitmap> imageIDs = null;

    public ImageGridAdapter(Context context, ArrayList<Bitmap> imageIDs) {
        this.context = context;
        this.imageIDs = imageIDs;
    }

    public int getCount() {
        if (null != imageIDs)
            return imageIDs.size();
        else
            return 0;
    }

    public Object getItem(int position) {
        if (null != imageIDs)
            return imageIDs.get(position);
        else
            return 0;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = null;
        ImageView raw_imageView = null;

        if (null != convertView) {
            imageView = (ImageView) convertView;
            raw_imageView = (ImageView) convertView;
        } else {
            //---------------------------------------------------------------
            // GridView 뷰를 구성할 ImageView 뷰의 비트맵을 정의합니다.
            // 그리고 그것의 크기를 320*240으로 줄입니다.
            // 크기를 줄이는 이유는 메모리 부족 문제를 막을 수 있기 때문입니다.

            Bitmap bmp = imageIDs.get(position);
            bmp = Bitmap.createScaledBitmap(bmp, 320, 320, false);

            //---------------------------------------------------------------
            // GridView 뷰를 구성할 ImageView 뷰들을 정의합니다.
            // 뷰에 지정할 이미지는 앞에서 정의한 비트맵 객체입니다.

            imageView = new ImageView(context);
            imageView.setAdjustViewBounds(true);

            //

            raw_imageView = new ImageView(context);
            raw_imageView.setAdjustViewBounds(true);
            raw_imageView.setImageBitmap(bmp);

            //
            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setStrokeWidth(3);

            Bitmap tempBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);
            Canvas tempCanvas = new Canvas(tempBitmap);
            tempCanvas.drawBitmap(bmp, 0, 0, null);

            FaceDetector detector = new FaceDetector.Builder(context)
                    .setTrackingEnabled(false)
                    .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                    .build();

            Detector<Face> safeDetector = new SafeFaceDetector(detector);

            if (!safeDetector.isOperational()) {
                Log.w(TAG, "Face detector dependencies are not yet available.");
                IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
                boolean hasLowStorage = context.registerReceiver(null, lowstorageFilter) != null;
                if (hasLowStorage) {
                    Toast.makeText(context, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                    Log.w(TAG, context.getString(R.string.low_storage_error));
                }
            }

            Frame frame = new Frame.Builder().setBitmap(bmp).build();
            SparseArray<Face> faces = safeDetector.detect(frame);

            double viewWidth = tempCanvas.getWidth();
            double viewHeight = tempCanvas.getHeight();
            double imageWidth = bmp.getWidth();
            double imageHeight = bmp.getHeight();
            double scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);

            Rect destBounds = new Rect(0, 0, (int)(imageWidth * scale), (int)(imageHeight * scale));
            tempCanvas.drawBitmap(bmp, null, destBounds, null);

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

            imageView.setImageDrawable(new BitmapDrawable(context.getResources(),tempBitmap));

            safeDetector.release();

            //---------------------------------------------------------------
            // 지금은 사용하지 않는 코드입니다.

            //raw_imageView.setMaxWidth(320);
            //raw_imageView.setMaxHeight(240);
            //raw_imageView.setImageResource(imageIDs[position]);

            //---------------------------------------------------------------
            // 사진 항목들의 클릭을 처리하는 ImageClickListener 객체를 정의합니다.
            // 그리고 그것을 ImageView의 클릭 리스너로 설정합니다.

            ImageClickListener imageViewClickListener = new ImageClickListener(context, bmp);
            raw_imageView.setOnClickListener(imageViewClickListener);
        }
        return raw_imageView;
    }

    double getDistance(int x1,int y1, int x2, int y2){
        double distance;
        double disX=Math.abs(x1-x2);
        double disY=Math.abs((y1-y2));
        distance=Math.sqrt(Math.pow(disX,2)+(Math.pow(disY,2)));

        return distance;
    }

}