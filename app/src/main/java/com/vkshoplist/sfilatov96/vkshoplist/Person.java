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
    String first_name;
    String last_name;
    String is_online;
    String avater;
    Bitmap bitmap;

    Person(String first_name, String last_name, String is_online, String avatar) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.is_online = is_online;
        this.avater = avatar;
    }

}
