package com.vkshoplist.sfilatov96.vkshoplist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.vk.sdk.VKScope;

public class CreateListActivty extends AppCompatActivity {
    final String VKUSERID = "VkUserId";
    int user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = getIntent().getExtras().getInt(VKUSERID);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list_activty);
        Toast.makeText(this,user,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(VKUSERID,String.format("%d",user));
        Toast.makeText(this,user,Toast.LENGTH_LONG).show();

    }
}
