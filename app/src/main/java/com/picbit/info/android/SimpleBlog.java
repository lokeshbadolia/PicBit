package com.picbit.info.android;

import android.app.Application;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by lokesh badolia on 6/9/2017.
 */

public class SimpleBlog extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if(!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        //Picasso.Builder builder =new Picasso.Builder(this);
        //builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
       // Picasso built = builder.build();
       // built.setIndicatorsEnabled(false);
       // built.setLoggingEnabled(true);
        //Picasso.setSingletonInstance(built);

    }


}
