package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import static android.graphics.BitmapFactory.decodeStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAddress#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAddress extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArrayList <Info> arr_top7 = ((MainActivity) MainActivity.context).arr_top7;
    public static Context context;
    public FragmentAddress() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAddress newInstance(String param1, String param2) {
        FragmentAddress fragment = new FragmentAddress();
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
            context = getContext();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_1,container,false);
        return view;
    }

    ArrayList<Listviewitem> par_arr = new ArrayList<Listviewitem>();

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

    private  List<String> list_search = new ArrayList<String>();
    private  ListView listView;          // 검색을 보여줄 리스트변수
    private  EditText editSearch;        // 검색어를 입력할 Input 창
    private CustomArrayAdapter adapt;
    private  ArrayList<String> arraylist_search = new ArrayList<String>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list_search.clear();
        par_arr.clear();
        arraylist_search.clear();

        listView = (ListView) view.findViewById(R.id.listView1);//레이아웃에 정의된 리스트뷰 아이디

        ArrayList contactItems = getContactList();

        for (int i=0;i<contactItems.size();i++) {

            ContactItem tempItem = (ContactItem)contactItems.get(i);

            list_search.add(tempItem.getUser_Name()); // 검색에 사용될 리스트에 더한다

            Bitmap big_bitmap = null;
            Bitmap small_bitmap = null;

            if (tempItem.getPhoto_Uri() == null) { big_bitmap  = BitmapFactory.decodeResource(getResources(), R.drawable.person2); }
            else {
                try { big_bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(tempItem.getPhoto_Uri())); }
                catch (IOException e) { e.printStackTrace(); }
            }

            if (tempItem.getThumbnail_Uri() == null) { small_bitmap  = BitmapFactory.decodeResource(getResources(), R.drawable.person2); }
            else {
                try { small_bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(tempItem.getThumbnail_Uri())); }
                catch (IOException e) { e.printStackTrace(); }
            }

            Listviewitem lvi = new Listviewitem(big_bitmap,tempItem.getUser_Name(),tempItem.getUser_phNumber(),tempItem.getId());
            par_arr.add(lvi);

        }

        Collections.sort(par_arr, new Comparator<Listviewitem>() {
            @Override
            public int compare(Listviewitem listviewitem, Listviewitem t1) {
                return listviewitem.getName().compareTo(t1.getName());
            }
        });

        //이제 이름:번호 로 저장되어있는 정보를 각각 텍스트에 넣는다.
        arraylist_search.addAll(list_search);
        editSearch = (EditText) getActivity().findViewById(R.id.editSearch);
        adapt = new CustomArrayAdapter(getActivity(), R.layout.listview_item, par_arr,list_search);
        listView.setAdapter(adapt);

        /**
         검색 기능 구현
         **/
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editSearch.getText().toString();
                search(text);
            }
        });

    }

    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list_search.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list_search.addAll(arraylist_search);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist_search.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist_search.get(i).toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    list_search.add(arraylist_search.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapt.notifyDataSetChanged();
    }

}