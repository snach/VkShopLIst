package com.vkshoplist.sfilatov96.vkshoplist;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;

/**
 * Created by sfilatov96 on 12.10.16.
 */
public class VkApplication extends android.app.Application {
    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                //Intent intent = new Intent(VkApplication.this, SignInActivity.class);
                //intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK|intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(intent);
            }
        }
    };
    String TAG = "VkApplication";
    @Override
    public void onCreate() {

        super.onCreate();
        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
        Log.d(TAG, "Завелось");

    }
}
