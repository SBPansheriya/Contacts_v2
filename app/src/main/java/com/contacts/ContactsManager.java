package com.contacts;

import android.content.Context;
import android.content.SharedPreferences;

import com.contacts.Model.Users;
import com.google.gson.Gson;

import java.util.List;

public class ContactsManager {

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;

    private final String KEY_IMAGE = "image";
    private final String KEY_FIRSTNAME = "firstname";
    private final String KEY_LASTNAME = "lastname";
    private final String KEY_PPHONE = "pphone";
    private final String KEY_OPHONE = "ophone";
    private final String KEY_DATA = "listkey";

    public ContactsManager(Context context){
        this.context = context;
        gson = new Gson();
        sharedPreferences = context.getSharedPreferences("Contact",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void AddContact (Context context, List<Users> list){
        String json = gson.toJson(list);
        editor.putString(KEY_DATA,json);
        editor.apply();
//        editor.putString(KEY_IMAGE, users.getImage());
//        editor.putString(KEY_FIRSTNAME, users.getFirst());
//        editor.putString(KEY_LASTNAME, users.getLast());
//        editor.putString(KEY_PPHONE, users.getPersonPhone());
//        editor.putString(KEY_OPHONE, users.getOfficePhone());
        editor.commit();
    }

    public  String getContactsDetails(String key){
        String value = sharedPreferences.getString(key,null);
        return value;
    }

    public void GetContact(String image,String firstname,String lastname){
        editor.putString(KEY_IMAGE,image);
        editor.putString(KEY_FIRSTNAME,firstname);
        editor.putString(KEY_LASTNAME,lastname);
    }
}
