package com.vkshoplist.sfilatov96.vkshoplist;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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

import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;
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
    private RecyclerView recyclerView;
    RVAdapter adapter;
    Tracker mTracker;
    FirebaseAnalytics firebaseAnalytics;
    private ArrayList<Person> persons;
    public final String ONLINE = "online";
    public final String OFFLINE = "offline";
    public final String VKUSERID = "VkUserId";
    private final static String TAG = MainActivity.class.getSimpleName();



    //private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, "onCreate");
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);


        if (VKSdk.isLoggedIn()) {

            findFriends();
            getProfileInNavHeader();
        } else {
            VKSdk.login(MainActivity.this, VKScope.MESSAGES, VKScope.FRIENDS, VKScope.WALL);

        }
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Person person = adapter.getIdByPosition(position);
                        Intent intent = new Intent(MainActivity.this, CreateListActivty.class);
                        String id = String.valueOf(person.id);
                        intent.putExtra("id", id);
                        intent.putExtra("name", person.name);
                        intent.putExtra("avatar", person.avater);
                        startActivity(intent);
                        FlurryAgent.logEvent("friend_clicked");
                        mTracker.send(new HitBuilders.EventBuilder().setCategory("friends_content").setAction("friend_clicked").build());
                    }
                })
        );


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                recyclerView.smoothScrollToPosition(0);
                FlurryAgent.logEvent("fab_clicked");
                mTracker.send(new HitBuilders.EventBuilder().setCategory("friends_content").setAction("fab_clicked").build());
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"fab_clicked");
                firebaseAnalytics.logEvent("friends_content", bundle);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        VkApplication application = (VkApplication) getApplication();
        mTracker = application.getDefaultTracker();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        FlurryAgent.onStartSession(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.vkshoplist.sfilatov96.vkshoplist/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (null != searchManager) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(getApplicationContext(),query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
        //searchView.setQuery("lal",true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_friends) {
            mTracker.send(new HitBuilders.EventBuilder().setCategory("main_menu").setAction("nav_friends").build());
            FlurryAgent.logEvent("nav_friends");
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"nav_friends");
            firebaseAnalytics.logEvent("main_menu", bundle);

        } else if (id == R.id.nav_list_in) {
            mTracker.send(new HitBuilders.EventBuilder().setCategory("main_menu").setAction("nav_list_in").build());
            FlurryAgent.logEvent("nav_list_in");
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"nav_list_in");
            firebaseAnalytics.logEvent("main_menu", bundle);

            List<TableShopListClass> allContacts = TableShopListClass.listAll(TableShopListClass.class);
            TableShopListClass.deleteAll(TableShopListClass.class);


        } else if (id == R.id.nav_list_out) {
            mTracker.send(new HitBuilders.EventBuilder().setCategory("main_menu").setAction("nav_list_out").build());
            FlurryAgent.logEvent("nav_list_out");
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"nav_list_out");
            firebaseAnalytics.logEvent("main_menu", bundle);

        } else if (id == R.id.nav_draft) {
            mTracker.send(new HitBuilders.EventBuilder().setCategory("main_menu").setAction("nav_draft").build());
            FlurryAgent.logEvent("nav_draft");
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"nav_draft");
            firebaseAnalytics.logEvent("main_menu", bundle);

        } else if (id == R.id.nav_about) {
            mTracker.send(new HitBuilders.EventBuilder().setCategory("main_menu").setAction("nav_about").build());
            FlurryAgent.logEvent("nav_about");
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"nav_about");
            firebaseAnalytics.logEvent("main_menu", bundle);

        } else if (id == R.id.nav_go_out) {
            mTracker.send(new HitBuilders.EventBuilder().setCategory("main_menu").setAction("nav_go_out").build());
            FlurryAgent.logEvent("nav_go_out");
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"nav_go_out");
            firebaseAnalytics.logEvent("main_menu", bundle);

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

                mTracker.send(new HitBuilders.EventBuilder().setCategory("vkAPI_authorization").setAction("success").build());
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"success");
                FlurryAgent.logEvent("vkAPI_authorization_success");
                firebaseAnalytics.logEvent("vkAPI_authorization", bundle);
                Log.d(TAG, "vkAPI_authorization_success");

            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(), R.string.auth_error, Toast.LENGTH_SHORT).show();

                mTracker.send(new HitBuilders.EventBuilder().setCategory("vkAPI_authorization").setAction("error").build());
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"error");
                FlurryAgent.logEvent("vkAPI_authorization_error");
                firebaseAnalytics.logEvent("vkAPI_authorization", bundle);
                Log.d(TAG, "vkAPI_authorization_error");
                finish();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void findFriends() {
        final VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "photo_200,order"));

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                parseUser(response);

            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, error.toString());
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //mTracker.setScreenName("Image~" + "VkShopList");
        //mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        Log.d(TAG, "onResume");

    }

    private void parseUser(VKResponse response) {
        VKList<VKApiUser> users = ((VKList<VKApiUser>) response.parsedModel);
        persons = new ArrayList<>();
        for (VKApiUser u : users) {
            if (u.online) {
                persons.add(new Person(u.toString(), ONLINE, u.photo_200, u.id));
            } else {
                persons.add(new Person(u.toString(), OFFLINE, u.photo_200, u.id));
            }

        }
        adapter = new RVAdapter(this, persons);
        recyclerView.setAdapter(adapter);

    }

    private void getProfileInNavHeader() {
        Log.d("profile", "voshel");

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
                if (screen_name != null)
                    getProfileById(screen_name);


                //fillNavHeaderViews(userProfile);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.d(TAG, error.toString());
            }
        });
    }


    private void getProfileById(String screen_name) {
        final VKRequest request = new VKRequest("users.get", VKParameters.from(VKApiConst.USER_IDS, String.format("%s", screen_name),
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
                Log.d(TAG, error.toString());
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
            Log.d(TAG, e.toString());
        }
    }

    public void fillNavHeaderViews(JSONObject userProfile) {
        TextView profile_name = (TextView) findViewById(R.id.profile_name);
        TextView profile_email = (TextView) findViewById(R.id.profile_email);
        ImageView profile_photo = (ImageView) findViewById(R.id.profile_photo);

        try {
            profile_name.setText(userProfile.getString("first_name") + " " + userProfile.getString("last_name"));
            profile_email.setText(userProfile.getString("screen_name"));
            Picasso.with(this).load(userProfile.getString("photo_200")).transform(new CircularTransformation(200)).placeholder(R.drawable.user_placeholder).into(profile_photo);
        } catch (JSONException e) {
            Log.d(TAG, e.toString());
        }
        //}

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.vkshoplist.sfilatov96.vkshoplist/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        FlurryAgent.onEndSession(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }
}
