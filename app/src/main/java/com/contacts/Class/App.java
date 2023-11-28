package com.contacts.Class;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.contacts.Activity.HomeActivity;

public class App extends Application {

    public static Context context ;

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (context instanceof HomeActivity){
//            ((HomeActivity) contacts).fragmentshow();
        }

    }
}
