package com.contacts;

import android.content.Context;

import com.contacts.Activity.HomeActivity;

public class GetModelClass {
    Context context;
    Listner listner;

    public GetModelClass(Context context,Listner listner){
        this.context = context;
        this.listner = listner;
    }

    public GetModelClass(Context context){
        this.context = context;
    }

    public void NewClass(){
        if (context instanceof HomeActivity){
            listner.getResult();
        }
    }
}
