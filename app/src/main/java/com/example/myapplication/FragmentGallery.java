package com.example.myapplication;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentGallery#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentGallery extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public String infostring = new String();
    public Integer count = 0;

    // TODO: Rename and change types of parameters
    public String mParam1;
    private String mParam2;

    public FragmentGallery() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentGallery newInstance(String param1, String param2) {
        FragmentGallery fragment = new FragmentGallery();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private Button button_convey;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_2,container,false);
        button_convey = view.findViewById(R.id.convey_button);
        button_convey.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((MainActivity)getActivity()).changeText(infostring);
                Toast.makeText(getContext(),"Data Conveyed : " + final_bitmapList.size(),Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    Button button;
    Button button_camera;
    ArrayList imageList = new ArrayList<Uri>();
    int size;
    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button = (Button)view.findViewById(R.id.button_gallery);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent,"Select Picture"), 1);
            }
        });

        button_camera = (Button)view.findViewById(R.id.button_camera);
        button_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File photoFile = null;
                try{
                    photoFile = createImageFile();
                } catch (IOException ex) {}

                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getContext(),
                            "com.example.myapplication.fileprovider",
                            photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(intent, 2);
                }
            }
        });
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getRealPathFromURI(final Context context, final Uri uri) {

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                } else {
                    String SDcardpath = getRemovableSDCardPath(context).split("/Android")[0];
                    return SDcardpath +"/"+ split[1];
                }
            }

            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }

            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getRemovableSDCardPath(Context context) {
        File[] storages = ContextCompat.getExternalFilesDirs(context, null);
        if (storages.length > 1 && storages[0] != null && storages[1] != null)
            return storages[1].toString();
        else
            return "";
    }


    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }


    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }


    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    ArrayList<Bitmap> final_bitmapList = new ArrayList<Bitmap>();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        imageList.clear();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {

                try {
                    if (data.getClipData() == null) {
                        imageList.add(data.getData());
                    } else {
                        ClipData clipData = data.getClipData();
                        size = clipData.getItemCount();
                        if (clipData.getItemCount() > 100) {
                            return;
                        } else if (clipData.getItemCount() == 1) {
                            imageList.add(clipData.getItemAt(0).getUri());
                        } else if (clipData.getItemCount() > 1 && clipData.getItemCount() <= 100) {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                imageList.add(clipData.getItemAt(i).getUri());
                            }
                        }
                    }

                    Bitmap bitmap;
                    Bitmap[] bitmapList = new Bitmap[imageList.size()];
                    Bitmap rotatedBitmap = null;

                    int orientation;

                    for (int i = 0; i < imageList.size(); i++) {

                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), ((Uri) imageList.get(i)));
                            ExifInterface ei = new ExifInterface(getRealPathFromURI(getContext(), (Uri) imageList.get(i)));
                            orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                            switch (orientation) {

                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    rotatedBitmap = rotateImage(bitmap, 90);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    rotatedBitmap = rotateImage(bitmap, 180);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    rotatedBitmap = rotateImage(bitmap, 270);
                                    break;

                                case ExifInterface.ORIENTATION_NORMAL:
                                default:
                                    rotatedBitmap = bitmap;
                            }

                            final_bitmapList.add(rotatedBitmap);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == 2) {
            if (resultCode == RESULT_OK ) {
                Bitmap bitmap;
                Bitmap[] bitmapList = new Bitmap[1];
                Bitmap rotatedBitmap;
                int orientation;

                try {

                    File file = new File(mCurrentPhotoPath);

                    Uri Camera_Uri = Uri.fromFile(file);
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.fromFile(file));
                    ExifInterface ei = new ExifInterface(getRealPathFromURI(getContext(), Camera_Uri));
                    orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                    switch (orientation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotateImage(bitmap, 90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(bitmap, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(bitmap, 270);
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            rotatedBitmap = bitmap;
                    }

//                    bitmapList[0] = rotatedBitmap;

                    final_bitmapList.add(rotatedBitmap);

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
        for (int t =0; t < final_bitmapList.size() ; t++) {
            if (t>= count) {

                try {
                    Bitmap bmp = final_bitmapList.get(t);
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
                                    infostring += ((double) Math.round(dis / dis1 * 1000) / 1000) + ",";
                                }
                                if (cnt == 6) {
                                    infostring += ((double) Math.round(dis / dis1 * 1000) / 1000);
                                    if (k < faces.size() - 1) {
                                        infostring += "+";
                                    }
                                }
                            }
                            ox = cx;
                            oy = cy;
                            cnt++;
                        }
                    }
                    infostring += "/";
                    safeDetector.release();
                    count += 1;
//                    System.out.println("string :" + infostring);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        GridView gridViewImages = (GridView) getView().findViewById(R.id.gridViewImages);
        ImageGridAdapter imageGridAdapter = new ImageGridAdapter(getContext(), final_bitmapList);
        gridViewImages.setAdapter(imageGridAdapter);

    }

    double getDistance(int x1,int y1, int x2, int y2){
        double distance;
        double disX=Math.abs(x1-x2);
        double disY=Math.abs((y1-y2));
        distance=Math.sqrt(Math.pow(disX,2)+(Math.pow(disY,2)));

        return distance;
    }

}
