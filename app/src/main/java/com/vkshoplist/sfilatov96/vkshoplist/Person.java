package com.vkshoplist.sfilatov96.vkshoplist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sfilatov96 on 18.10.16.
 */
public class Person {
    String name;
    String is_online;
    String avater;
    int id;
    private final static String TAG = Person.class.getSimpleName();


    Person(String name, String is_online, String avatar, int id) {
        this.id = id;
        this.name = name;
        this.is_online = is_online;
        this.avater = avatar;
    }

}
