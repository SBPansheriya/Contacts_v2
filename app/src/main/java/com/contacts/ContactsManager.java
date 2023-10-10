package com.contacts;

import android.content.Context;
import android.content.SharedPreferences;

import com.contacts.Model.Users;

public class ContactsManager {

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private final String KEY_IMAGE = "image";
    private final String KEY_FIRSTNAME = "firstname";
    private final String KEY_LASTNAME = "lastname";
    private final String KEY_PPHONE = "pphone";
    private final String KEY_OPHONE = "ophone";

    public ContactsManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("Contact",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void AddContact (Users users){
        editor.putString(KEY_IMAGE, users.getImage());
        editor.putString(KEY_FIRSTNAME, users.getFirst());
        editor.putString(KEY_LASTNAME, users.getLast());
        editor.putString(KEY_PPHONE, users.getPersonPhone());
        editor.putString(KEY_OPHONE, users.getOfficePhone());
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
