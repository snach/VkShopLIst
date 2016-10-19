package com.vkshoplist.sfilatov96.vkshoplist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiUsers;
import com.vk.sdk.api.model.VKApiApplicationContent;
import com.vk.sdk.api.model.VKApiOwner;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    private List<Person> persons;
    public final String ONLINE="online";
    public final String OFFLINE="offline";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("oncreate","ocreate");
        if ( VKSdk.isLoggedIn()) {

            findFriends();
            getProfileInNavHeader();
        } else {
            VKSdk.login(MainActivity.this, VKScope.MESSAGES, VKScope.FRIENDS, VKScope.WALL);

        }
        recyclerView = (RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);


        Log.d("tag","oncreate");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                recyclerView.smoothScrollToPosition(0);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                findFriends();
                getProfileInNavHeader();
                Toast.makeText(getApplicationContext(), R.string.auth_success, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(), R.string.auth_error, Toast.LENGTH_SHORT).show();
                finish();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
    private void findFriends() {
        final VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "photo_200"));

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                parseUser(response);

            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(), "oshibka", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {

            }
        });

    }
    private void parseUser(VKResponse response){
        VKList<VKApiUser> users  = ((VKList<VKApiUser>) response.parsedModel);
        persons = new ArrayList<>();
        for(VKApiUser u: users){
            if(u.online){
                persons.add(new Person(u.first_name, u.last_name, ONLINE, u.photo_200));
            } else {
                persons.add(new Person(u.first_name, u.last_name, OFFLINE, u.photo_200));
            }

        }
        RVAdapter adapter = new RVAdapter(this, persons);
        recyclerView.setAdapter(adapter);

    }
    private void getProfileInNavHeader() {
        Log.d("profile","voshel");

        final VKRequest requestProfile = new VKRequest("account.getProfileInfo", VKParameters.from(VKApiConst.FIELDS, "first_name,last_name,email"));
        requestProfile.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {

                super.onComplete(response);
                JSONObject jsonObject = response.json;
                String screen_name = null;

                try {
                    screen_name = jsonObject.getJSONObject("response").getString("screen_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(screen_name != null)
                    getProfileById(screen_name);


                //fillNavHeaderViews(userProfile);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.d("profile", error.toString());
            }
        });
    }



    private void getProfileById(String screen_name){
        final VKRequest request = new VKRequest("users.get", VKParameters.from(VKApiConst.USER_IDS, String.format("%s",screen_name),
                VKApiConst.FIELDS, "photo_200,screen_name"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                goAway(response);

            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(), "oshibka", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {

            }
        });

    }



    private void goAway(VKResponse response) {
        try {
            JSONObject r = response.json.getJSONArray("response").getJSONObject(0);
            // Здесь обрабатываем полученный response.
           fillNavHeaderViews(r);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void fillNavHeaderViews(JSONObject userProfile) {
        TextView profile_name = (TextView) findViewById(R.id.profile_name);
        TextView profile_email = (TextView) findViewById(R.id.profile_email);
        ImageView profile_photo = (ImageView) findViewById(R.id.profile_photo);

        try {
            profile_name.setText(userProfile.getString("first_name") + " " + userProfile.getString("last_name"));
            profile_email.setText(userProfile.getString("screen_name"));
            Picasso.with(this).load(userProfile.getString("photo_200")).transform(new CircularTransformation(100)).placeholder(R.drawable.user_placeholder).into(profile_photo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //}

    }
}
