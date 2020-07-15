package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimerTask;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentIntimacy#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentIntimacy extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Bitmap[] bitmaparray = new Bitmap[5];
    String[] numberarray = new String[5];

    Info me = new Info();

    ArrayList<Result> resultarray = new ArrayList<Result>();
    ArrayList<Info> fin_score = ((MainActivity) MainActivity.context).arr_top7;
    public FragmentIntimacy() {
        // Required empty public constructor
    }

    public byte[] bitmapToByteArray( Bitmap $bitmap ) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
        $bitmap.compress( Bitmap.CompressFormat.JPEG, 100, stream) ;
        byte[] byteArray = stream.toByteArray() ;
        return byteArray ;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentIntimacy newInstance(String param1, String param2) {
        FragmentIntimacy fragment = new FragmentIntimacy();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    Button button_accepted;
    Button button_popup;
    String from_image;

    Animation animation;
    Animation animation_short;

    public void setWidth(String name, int score){
        //두께 및 색상 변경
        if(score<=0) return;
        int width = score + 1; // 기본두께 4dp
        //lineID = "line1"+Integer.toString(i+2); input으로 들어올 것
        int view_l = getResources().getIdentifier(name, "id",getContext().getPackageName());
        line = getView().findViewById(view_l);

        //색상
        if(width > 12){
            line.setBackgroundColor(getResources().getColor(R.color.starbucks_gold));
            width += 6;
        }
        else if(width >= 7){
            line.setBackgroundColor(getResources().getColor(R.color.starbucks_green));
            width += 2;
        }
        else{
            line.setBackgroundColor(getResources().getColor(R.color.real_black));
        }

        //두께 - 30dp 이하
        android.view.ViewGroup.LayoutParams layoutParams = line.getLayoutParams();
        layoutParams.width = width * 3;
        line.setLayoutParams(layoutParams);
        line.startAnimation(animation);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_3,container,false);
        button_accepted = view.findViewById(R.id.button_accepted);
        button_accepted.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v)
            {
                from_image = ((MainActivity) MainActivity.context).conveyedstring;
                Toast.makeText(getContext(),"Data Accepted",Toast.LENGTH_SHORT).show();

                animation = AnimationUtils.loadAnimation(getContext(),R.anim.alpha);
                animation_short = AnimationUtils.loadAnimation(getContext(),R.anim.alpha_short);

                final int [] picture_score;
                //picture score 계산값
                picture_score = pic_score(from_image);

                for(int i = 0; i<fin_score.size(); i++){
                    final Info info = fin_score.get(i);

                    //이름설정
                    String nameID = "textview"+Integer.toString(i+2);
                    int textID = getResources().getIdentifier(nameID, "id", getContext().getPackageName());
                    text = getView().findViewById(textID);
                    text.setText(info.getName());

                    //이미지 설정
                    Bitmap bigPictureBitmap  = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.person3);//임시로 익명 이미지 저장

                    Drawable d = new BitmapDrawable(getResources(), bitmaparray[i]);

                    String imageID = "imageView"+Integer.toString(i+2);
                    int imID = getResources().getIdentifier(imageID, "id", getContext().getPackageName());

                    image = getView().findViewById(imID);
                    image.setImageDrawable(d);
                    image.setBackground(new ShapeDrawable(new OvalShape()));//rounding
                    image.setClipToOutline(true);

                    image.startAnimation(animation_short);

                    String num = numberarray[i];
                    CallClickListener callClickListener = new CallClickListener(getContext(), num);
                    image.setOnClickListener(callClickListener);

                    //두께 및 색상 변경 - setWidth()로 묶음

                    String lineID = "line1"+Integer.toString(i+2);

                    Result temp_result = new Result(info.getScore()+3*picture_score[i],fin_score.get(i).getName());
                    resultarray.add(temp_result);
//
                    System.out.println(info.getScore()+3*picture_score[i]);
                    setWidth(lineID, info.getScore() + 3*picture_score[i]);

                }

                //나 외의 사람들 관계선 셋팅
                int[] index_bias = new int[] {0, -2, 2, 6, 8, 9};
                for(int x = 2; x<6 ; x++){
                    for(int y = x+1; y<=6; y++){
                        int index = index_bias[x] + y;
                        int pic_sco = 3 * picture_score[index];
                        String line_ID = "line" + Integer.toString(x*10+y);
                        setWidth(line_ID, pic_sco);
                    }
                }
            }
        });

        button_popup = view.findViewById(R.id.popup);
        button_popup.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v)
            {

                Collections.sort(resultarray);

//

                Intent intent = new Intent(getContext(), PopupActivity.class);
                // 여기 수정하면 된다
                intent.putExtra("data", resultarray);
                startActivity(intent);
            }
        });

        return view;
    }

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
        Cursor c = getActivity().getContentResolver().query(uri, projection,null, null, sortOrder);
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

    double getDistance(int x1,int y1, int x2, int y2){
        double distance;
        double disX=Math.abs(x1-x2);
        double disY=Math.abs((y1-y2));
        distance=Math.sqrt(Math.pow(disX,2)+(Math.pow(disY,2)));

        return distance;
    }
    TextView text;
    View line;
    ImageView image;
    @SuppressLint("NewApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList contactItems = getContactList();

        /////////////////////////////// Info me 에다가 mouth랑 nose.
        Drawable drawable = getResources().getDrawable(R.drawable.me);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

        try {
            Bitmap bmp = bitmap;
            bmp = Bitmap.createScaledBitmap(bmp, 320, 320, false);

            Bitmap tempBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);
            Canvas tempCanvas = new Canvas(tempBitmap);
            tempCanvas.drawBitmap(bmp, 0, 0, null);

            // set bitmap 320*320
            FaceDetector detector = new FaceDetector.Builder(getContext())
                    .setTrackingEnabled(false)
                    .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                    .build();

            SafeFaceDetector safeDetector = new SafeFaceDetector(detector);

            if (!safeDetector.isOperational()) {
                IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
                boolean hasLowStorage = getContext().registerReceiver(null, lowstorageFilter) != null;
                if (hasLowStorage) {
                    Toast.makeText(getContext(), R.string.low_storage_error, Toast.LENGTH_LONG).show();
                }
            }
            Frame frame = new Frame.Builder().setBitmap(bmp).build();
            SparseArray<Face> faces = safeDetector.detect(frame);

            double viewWidth = tempCanvas.getWidth();
            double viewHeight = tempCanvas.getHeight();
            double imageWidth = bmp.getWidth();
            double imageHeight = bmp.getHeight();
            double scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);
            for (int k = 0; k < faces.size(); ++k) {
                Face face = faces.valueAt(k);
                int cnt = 0;
                int ox = 0, oy = 0;
                double dis1 = 0;
                for (Landmark landmark : face.getLandmarks()) {
                    int cx = (int) (landmark.getPosition().x * scale);
                    int cy = (int) (landmark.getPosition().y * scale);

                    if (cnt > 0) {
                        double dis = getDistance(ox, oy, cx, cy);
                        if (cnt == 1)
                            dis1 = dis;
                        if (cnt == 4) {
                            me.setNose((double) Math.round(dis / dis1 * 1000) / 1000);
                        }
                        if (cnt == 6) {
                            me.setMouth((double) Math.round(dis / dis1 * 1000) / 1000);
                        }
                    }
                    ox = cx;
                    oy = cy;
                    cnt++;
                }
            }
            safeDetector.release();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ///////////////////////////////

        for (int i=0;i<contactItems.size();i++) {

            ContactItem tempItem = (ContactItem) contactItems.get(i);
            Bitmap big_bitmap = null;

            if (tempItem.getPhoto_Uri() == null) {
                big_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person2);
            } else {
                try {
                    big_bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(tempItem.getPhoto_Uri()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (int j = 0; j < fin_score.size(); j++) {
                if ((fin_score.get(j).getName()).equals(tempItem.getUser_Name())) {

                    bitmaparray[j] = big_bitmap;

//                    Result temp_result = new Result(fin_score.get(j).getScore(),fin_score.get(j).getName(),tempItem.getUser_phNumber());
//                    resultarray.add(temp_result);

                    try {
                        Bitmap bmp = big_bitmap;
                        bmp = Bitmap.createScaledBitmap(bmp, 320, 320, false);

                        Bitmap tempBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);
                        Canvas tempCanvas = new Canvas(tempBitmap);
                        tempCanvas.drawBitmap(bmp, 0, 0, null);

                        // set bitmap 320*320
                        FaceDetector detector = new FaceDetector.Builder(getContext())
                                .setTrackingEnabled(false)
                                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                                .build();

                        SafeFaceDetector safeDetector = new SafeFaceDetector(detector);

                        if (!safeDetector.isOperational()) {
                            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
                            boolean hasLowStorage = getContext().registerReceiver(null, lowstorageFilter) != null;
                            if (hasLowStorage) {
                                Toast.makeText(getContext(), R.string.low_storage_error, Toast.LENGTH_LONG).show();
                            }
                        }
                        Frame frame = new Frame.Builder().setBitmap(bmp).build();
                        SparseArray<Face> faces = safeDetector.detect(frame);

                        double viewWidth = tempCanvas.getWidth();
                        double viewHeight = tempCanvas.getHeight();
                        double imageWidth = bmp.getWidth();
                        double imageHeight = bmp.getHeight();
                        double scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);
                        for (int k = 0; k < faces.size(); ++k) {
                            Face face = faces.valueAt(k);
                            int cnt = 0;
                            int ox = 0, oy = 0;
                            double dis1 = 0;
                            for (Landmark landmark : face.getLandmarks()) {
                                int cx = (int) (landmark.getPosition().x * scale);
                                int cy = (int) (landmark.getPosition().y * scale);

                                if (cnt > 0) {
                                    double dis = getDistance(ox, oy, cx, cy);
                                    if (cnt == 1)
                                        dis1 = dis;
                                    if (cnt == 4) {
                                        fin_score.get(j).setNose((double) Math.round(dis / dis1 * 1000) / 1000);
                                    }
                                    if (cnt == 6) {
                                        fin_score.get(j).setMouth((double) Math.round(dis / dis1 * 1000) / 1000);
                                    }
                                }
                                ox = cx;
                                oy = cy;
                                cnt++;
                            }
                        }
                        safeDetector.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    numberarray[j] = tempItem.getUser_phNumber();
                    break;
                }
            }
        }

//        System.out.println("size:" + Integer.toString(fin_score.size()));

        image = getView().findViewById(R.id.imageView1);
        image.setBackground(new ShapeDrawable(new OvalShape()));//rounding
        image.setClipToOutline(true);

    }

    public int[] pic_score(String from_image){
        StringTokenizer st1 = new StringTokenizer(from_image, "/");
        ArrayList<String> InOnePic = new ArrayList<String>();

        while(st1.hasMoreTokens()){
            InOnePic.add(st1.nextToken());
        }

        int[] index_bias = new int[] {0, -2, 2, 6, 8, 9};
        int[] res = new int[28];//반환값

        for(int x = 0; x<InOnePic.size() ; x++){
            String[] PersonInfo = InOnePic.get(x).split("\\+");
            int [] who = new int[16];//사람들을 index로 저장함 ex) 5등이랑 2등이랑 같이있음

            for(int k=0; k<PersonInfo.length ; k++ ){//이 사람이 누구인지 분석
                if (PersonInfo[k]!=null) { who[k] = getindex(PersonInfo[k]); } //등수반영
            }
            //X번째 사람, Y번째 사람인거 알았으면 여기서 저장
            //나는 7. line 23에 들어가는 사람은 1등과 2등이다.
            //int index = index_bias[x] + y; 1번부터 시작함

            Arrays.sort(who);//ex) 0 0 2 5
            for(int i = 0; i<who.length ; i++){ // i=2부터 시작함
                if(who[i]==0||who[i]==5) continue;
                for(int j = i+1; j<who.length ; j++)
                {
                    if(who[j]==0) continue;
                    else if(who[j]==7){//me
                        res[who[i]-2]++;
                        continue;
                    }
                    else {
                        int index = index_bias[who[i]+1]+who[j]+1; // line25에 대응하는 선에 +1. i,j에서 who[i]로 수정함
                        res[index]++;
                    }
                }
            }
        }
        return res;
    }


    public int getindex(String one_pick){
        String[] one_per = one_pick.split(",");

        Double nose = Double.parseDouble(one_per[0]);
        Double mouth = Double.parseDouble(one_per[1]);


        Double nose_o = Math.pow(nose - me.getNose(),2);
        Double mouth_o = Math.pow(nose - me.getMouth(),2);
        Double differ = Math.sqrt(nose_o + mouth_o);
        int index = 7;

        //1~5위랑 비교
        for(int i =1 ; i<fin_score.size()+1 ; i++){
            Info person = fin_score.get(i-1);//찾자
            nose_o = Math.pow(nose - person.getNose(),2);
            mouth_o = Math.pow(mouth - person.getMouth(),2);

            Double tmp = Math.sqrt(nose_o + mouth_o);


            if(tmp<differ){
                differ = tmp;
                index = i;
            }
        }
        //threshold 설정
        System.out.println(differ);
        //Log.d("--error bound--", Double.toString(differ));
        if(differ > 0.07) {return 0;}
        else {return index;}
    }

}
