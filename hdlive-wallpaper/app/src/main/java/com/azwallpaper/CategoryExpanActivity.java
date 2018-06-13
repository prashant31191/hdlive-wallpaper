package com.azwallpaper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fmsirvent.ParallaxEverywhere.PEWImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.model.JsonDashboardModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.utils.Blur;
import com.utils.HttpHandler;
import com.utils.RealmBackupRestore;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class CategoryExpanActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private static final int COLUMN_NUM = 1;
    private GalleryAdapter mAdapter;
    ArrayList<JsonDashboardModel> arrayListJsonDashboardModel = new ArrayList<>();

    Realm realm;

    FloatingActionButton fab;
    FloatingActionButton fabCreateBackup;
    FloatingActionButton fabRestoreBackup;

    private static final String TAG = CategoryExpanActivity.class.getSimpleName();

    //for the video ads
    private RewardedVideoAd mRewardedVideoAd;
    boolean isClickEnable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_dashboard);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            mRecyclerView = findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);

            fab = findViewById(R.id.fab);
            fabCreateBackup = findViewById(R.id.fabCreateBackup);
            fabCreateBackup.setVisibility(View.GONE);

            fabRestoreBackup = findViewById(R.id.fabRestoreBackup);

            fabRestoreBackup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (App.checkDbFileIsExist() == true) {
                        RealmBackupRestore realmBackupRestore = new RealmBackupRestore(CategoryExpanActivity.this, realm);
                        realmBackupRestore.restore();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 3000);


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CategoryExpanActivity.this);

                        builder.setTitle("New wallpaper available here approx size = 2.3 MB");
                        builder.setMessage("Are you sure restore backup?");

                        builder.setPositiveButton("DOWNLOAD", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing but close the dialog

                                App.downloadPhoto2();

                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Do nothing
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                }
            });

            fabCreateBackup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Snackbar.make(view, "Are you sure create backup ?", Snackbar.LENGTH_LONG)
                            .setAction("Yes", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    RealmBackupRestore realmBackupRestore = new RealmBackupRestore(CategoryExpanActivity.this, realm);
                                    realmBackupRestore.backup();

                                }
                            }).show();
                }
            });


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CategoryExpanActivity.this, GalleryActivity.class);
                    startActivity(intent);

/*
                    RealmBackupRestore realmBackupRestore = new RealmBackupRestore(DashboardActivity.this, realm);
                    realmBackupRestore.backup();
*/


                    //realmBackupRestore.restore();


                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                }
            });

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);


            //RealmConfiguration realmConfiguration =

/*
            RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                    .encryptionKey(App.getEncryptRawKey())
                    .build();
*/


            // Clear the realm from last time
            // Realm.deleteRealm(realmConfiguration);
            //realm = Realm.getInstance(realmConfiguration);
            realm = Realm.getInstance(App.getRealmConfiguration());

            //Clear All Data
            //RealmBackupRestore.setClearDatabase(realm);

           /*
           RealmBackupRestore realmBackupRestore = new RealmBackupRestore(DashboardActivity.this, realm);
            //realmBackupRestore.backup();
            realmBackupRestore.restore();
            realm = Realm.getInstance(realmConfiguration);
            */


// for the insert # !@#!@#
//  # !@#!@#

            if (realm != null && arrayListJsonDashboardModel != null) {
                //  # !@#!@#
                // insertDashboard();
                getDataDashboard();
            } else {
                App.showLog("===no insert database dashboard==");
            }

            setupAds();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isInserted = false;

    private void insertDashboard() {
        try {
            App.showLog("========insertDashboard=====");
            arrayListJsonDashboardModel = App.gettingRecordFromJsonDashboard(R.raw.dashboard);

            if (arrayListJsonDashboardModel != null) {

                isInserted = true;
                realm.beginTransaction();
                Collection<JsonDashboardModel> realmDJsonDashboardModel = realm.copyToRealm(arrayListJsonDashboardModel);
                realm.commitTransaction();

                getDataDashboard();

            } else {
                App.showLog("===arrayListJsonDashboardModel ==null==no insert database dashboard==");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDataDashboard() {
        try {
            App.showLog("========getDataDashboard=====");

            RealmResults<JsonDashboardModel> arrDLocationModel = realm.where(JsonDashboardModel.class).findAll();
            App.sLog("===arrDLocationModel==" + arrDLocationModel);
            List<JsonDashboardModel> arraDLocationModel = arrDLocationModel;
            arrayListJsonDashboardModel = new ArrayList<JsonDashboardModel>(arraDLocationModel);

            if (arrayListJsonDashboardModel != null) {
                App.showLog("====arrayListJsonDashboardModel===" + arrayListJsonDashboardModel.size());


                mLayoutManager = new GridLayoutManager(CategoryExpanActivity.this, COLUMN_NUM);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mAdapter = new GalleryAdapter(CategoryExpanActivity.this, arrayListJsonDashboardModel);
                mRecyclerView.setAdapter(mAdapter);
            }

            if (arrayListJsonDashboardModel.size() > 0) {

            } else {
                if (isInserted == false) {
                    insertDashboard();
                }
            }

            /*
            for (int k = 0; k < arrayListJsonDashboardModel.size(); k++) {
                App.sLog(k + "===arrayListJsonDashboardModel==" + arrayListJsonDashboardModel.get(k).name);
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
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
            Intent intent = new Intent(CategoryExpanActivity.this, GalleryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_sync) {
            if (fabRestoreBackup != null)
                fabRestoreBackup.performClick();
        } else if (id == R.id.nav_more_app) {
            //
            String url = "https://www.amazon.com/s/ref=bl_sr_mobile-apps/131-3886681-0807921?_encoding=UTF8&field-brandtextbin=wewer&node=2350149011";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        } else if (id == R.id.nav_share) {
            shareApp();
        } else if (id == R.id.nav_send) {
            shareApp();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareApp() {
        try {
            /*Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Awesome applications");
            String sAux = "\nLet me recommend you this application please download and review.\n\n";
            sAux = sAux + "https://www.amazon.com/s/ref=bl_sr_mobile-apps/131-3886681-0807921?_encoding=UTF8&field-brandtextbin=wewer&node=2350149011 \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "Share application"));*/


            new GetCategory().execute();
        } catch (Exception e) {
            //e.toString();
        }

    }


    private void parsJson(String strJsonResponse) {
        try {
            try {

                // Parse the data into jsonobject to get original data in form of json.

                // searchResult refers to the current element in the array "search_result"
                //JSONObject objMainJson = searchResult.getJSONObject("question_mark");


                JSONObject objMainJson = new JSONObject(strJsonResponse);
                Iterator keyMainJson = objMainJson.keys();

                while (keyMainJson.hasNext()) {
                    // loop to get the dynamic key
                    String currentDynamicKey = (String) keyMainJson.next();
                    App.showLog("==0000====" + currentDynamicKey);

                    // get the value of the dynamic key
                    JSONObject currentDynamicValue = objMainJson.getJSONObject(currentDynamicKey);
                    App.showLog("==0000=====currentDynamicValue==" + currentDynamicValue.toString());


                    Iterator keyMainJson1 = currentDynamicValue.keys();
                    while (keyMainJson1.hasNext()) {
                        // loop to get the dynamic key
                        String currentDynamicKey1 = (String) keyMainJson1.next();
                        App.showLog("==1111====" + currentDynamicKey1);

                        if (currentDynamicKey1.contains("childs")) {
                            // get the value of the dynamic key
                            JSONObject currentDynamicValue1 = currentDynamicValue.getJSONObject(currentDynamicKey1);
                            App.showLog("=1111==currentDynamicValue1==" + currentDynamicValue1.toString());
                            // do something here with the value...

                            Iterator keyMainJson2 = currentDynamicValue1.keys();
                            while (keyMainJson2.hasNext()) {
                                // loop to get the dynamic key
                                String currentDynamicKey2 = (String) keyMainJson2.next();
                                App.showLog("==2222====" + currentDynamicKey2);
                                //if(currentDynamicKey2.contains("childs"))
                                {
                                    // get the value of the dynamic key
                                    JSONObject currentDynamicValue2 = currentDynamicValue1.getJSONObject(currentDynamicKey2);
                                    App.showLog("=222==currentDynamicValue2==" + currentDynamicValue2.toString());
                                    // do something here with the value...

                                    Iterator keyMainJson3 = currentDynamicValue2.keys();
                                    while (keyMainJson3.hasNext()) {
                                        // loop to get the dynamic key
                                        String currentDynamicKey3 = (String) keyMainJson3.next();
                                        App.showLog("==333====" + currentDynamicKey3);

                                        if (currentDynamicKey3.contains("childs")) {
                                            // get the value of the dynamic key
                                            JSONObject currentDynamicValue3 = currentDynamicValue2.getJSONObject(currentDynamicKey3);
                                            App.showLog("=333==currentDynamicValue3==" + currentDynamicValue3.toString());
                                            // do something here with the value...

                                            Iterator keyMainJson4 = currentDynamicValue3.keys();
                                            while (keyMainJson4.hasNext()) {
                                                // loop to get the dynamic key
                                                String currentDynamicKey4 = (String) keyMainJson4.next();
                                                App.showLog("==444====" + currentDynamicKey4);

                                                //if(currentDynamicKey3.contains("childs"))
                                                {
                                                    // get the value of the dynamic key
                                                    JSONObject currentDynamicValue4 = currentDynamicValue3.getJSONObject(currentDynamicKey4);
                                                    App.showLog("=444==currentDynamicValue4==" + currentDynamicValue4.toString());
                                                    // do something here with the value...
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }

                    // do something here with the value...
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void parsJsonFile() {
        try {

            //Get Data From Text Resource File Contains Json Data.

            InputStream inputStream = getResources().openRawResource(R.raw.category_shopping);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int ctr;
            try {
                ctr = inputStream.read();
                while (ctr != -1) {
                    byteArrayOutputStream.write(ctr);
                    ctr = inputStream.read();
                }
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("Text Data", byteArrayOutputStream.toString());
            try {

                // Parse the data into jsonobject to get original data in form of json.

                // searchResult refers to the current element in the array "search_result"
                //JSONObject objMainJson = searchResult.getJSONObject("question_mark");


                JSONObject objMainJson = new JSONObject(byteArrayOutputStream.toString());
                Iterator keyMainJson = objMainJson.keys();

                while (keyMainJson.hasNext()) {
                    // loop to get the dynamic key
                    String currentDynamicKey = (String) keyMainJson.next();
                    App.showLog("==0000====" + currentDynamicKey);

                    // get the value of the dynamic key
                    JSONObject currentDynamicValue = objMainJson.getJSONObject(currentDynamicKey);
                    App.showLog("==0000=====currentDynamicValue==" + currentDynamicValue.toString());


                    Iterator keyMainJson1 = currentDynamicValue.keys();
                    while (keyMainJson1.hasNext()) {
                        // loop to get the dynamic key
                        String currentDynamicKey1 = (String) keyMainJson1.next();
                        App.showLog("==1111====" + currentDynamicKey1);

                        if (currentDynamicKey1.contains("childs")) {
                            // get the value of the dynamic key
                            JSONObject currentDynamicValue1 = currentDynamicValue.getJSONObject(currentDynamicKey1);
                            App.showLog("=1111==currentDynamicValue1==" + currentDynamicValue1.toString());
                            // do something here with the value...

                            Iterator keyMainJson2 = currentDynamicValue1.keys();
                            while (keyMainJson2.hasNext()) {
                                // loop to get the dynamic key
                                String currentDynamicKey2 = (String) keyMainJson2.next();
                                App.showLog("==2222====" + currentDynamicKey2);
                                //if(currentDynamicKey2.contains("childs"))
                                {
                                    // get the value of the dynamic key
                                    JSONObject currentDynamicValue2 = currentDynamicValue1.getJSONObject(currentDynamicKey2);
                                    App.showLog("=222==currentDynamicValue2==" + currentDynamicValue2.toString());
                                    // do something here with the value...

                                    Iterator keyMainJson3 = currentDynamicValue2.keys();
                                    while (keyMainJson3.hasNext()) {
                                        // loop to get the dynamic key
                                        String currentDynamicKey3 = (String) keyMainJson3.next();
                                        App.showLog("==333====" + currentDynamicKey3);

                                        if (currentDynamicKey3.contains("childs")) {
                                            // get the value of the dynamic key
                                            JSONObject currentDynamicValue3 = currentDynamicValue2.getJSONObject(currentDynamicKey3);
                                            App.showLog("=333==currentDynamicValue3==" + currentDynamicValue3.toString());
                                            // do something here with the value...

                                            Iterator keyMainJson4 = currentDynamicValue3.keys();
                                            while (keyMainJson4.hasNext()) {
                                                // loop to get the dynamic key
                                                String currentDynamicKey4 = (String) keyMainJson4.next();
                                                App.showLog("==444====" + currentDynamicKey4);

                                                //if(currentDynamicKey3.contains("childs"))
                                                {
                                                    // get the value of the dynamic key
                                                    JSONObject currentDynamicValue4 = currentDynamicValue3.getJSONObject(currentDynamicKey4);
                                                    App.showLog("=444==currentDynamicValue4==" + currentDynamicValue4.toString());
                                                    // do something here with the value...
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }

                    // do something here with the value...
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }

    }


    public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

        private Context mContext;   // get resource of this component
        private List<JsonDashboardModel> mList;

        public GalleryAdapter(Context mContext, List<JsonDashboardModel> mList) {
            this.mContext = mContext;
            this.mList = mList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public PEWImageView mImageView;
            public TextView tvTitle;


            public ViewHolder(View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.gallery_item);
                tvTitle = itemView.findViewById(R.id.tvTitle);

                tvTitle.setTypeface(App.getFont_Bold());

            }
        }

        @Override
        public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dashboard,
                    parent, false);
            GalleryAdapter.ViewHolder vh = new GalleryAdapter.ViewHolder(v);
            return vh;
        }


        @Override
        public void onBindViewHolder(final GalleryAdapter.ViewHolder holder, final int position) {
//        Contact item = mList.get(position);
            final JsonDashboardModel item = mList.get(position);
            holder.tvTitle.setText(item.name);
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mList.get(position).id != null && mList.get(position).id.equalsIgnoreCase("14")) {
                        Intent intent = new Intent(CategoryExpanActivity.this, GalleryActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(CategoryExpanActivity.this, LocalWallpaperActivity.class);
                        intent.putExtra("id", mList.get(position).id);
                        intent.putExtra("title", mList.get(position).name);
                        startActivity(intent);
                    }


                }
            });

            Picasso.with(mContext)
                    .load(item.image)
                    .resize(10, 10)
                    //.fit()
                    //.centerCrop()
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.mipmap.ic_launcher)
                    .transform(blurTransformation)
                    .into(holder.mImageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Picasso.with(mContext)
                                    .load(item.image) // image url goes here

                                    .into(holder.mImageView);
                        }

                        @Override
                        public void onError() {
                        }
                    });

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        //    public void addAll(List<Contact> newList) {
        public void addAll(List<JsonDashboardModel> newList) {
            mList.addAll(newList);
        }

        public void add(JsonDashboardModel item) {
            mList.add(item);
        }

        public void clear() {
            mList.clear();
        }


        Transformation blurTransformation = new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                Bitmap blurred = Blur.fastblur(mContext, source, 10);
                source.recycle();
                return blurred;
            }

            @Override
            public String key() {
                return "blur()";
            }
        };
    }


    private void setupAds() {
        try {
            // Initialize the Mobile Ads SDK.
            MobileAds.initialize(this, App.APP_ID);

            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
            mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                @Override
                public void onRewardedVideoAdLoaded() {
                    Log.i(TAG, "====onRewardedVideoAdLoaded====");
                    if (isClickEnable == false) {
                        isClickEnable = true;
                        showRewardedVideo();
                    }

                    loadRewardedVideoAd();


                }

                @Override
                public void onRewardedVideoAdOpened() {
                    Log.i(TAG, "====onRewardedVideoAdOpened====");
                }

                @Override
                public void onRewardedVideoStarted() {
                    Log.i(TAG, "====onRewardedVideoStarted====");
                }

                @Override
                public void onRewardedVideoAdClosed() {
                    Log.i(TAG, "====onRewardedVideoAdClosed====");
                }

                @Override
                public void onRewarded(RewardItem rewardItem) {
                    Log.i(TAG, "====onRewarded====");
                }

                @Override
                public void onRewardedVideoAdLeftApplication() {
                    Log.i(TAG, "====onRewardedVideoAdLeftApplication====");
                }

                @Override
                public void onRewardedVideoAdFailedToLoad(int i) {
                    Log.i(TAG, "====onRewardedVideoAdFailedToLoad====");

                }
            });

            loadRewardedVideoAd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadRewardedVideoAd() {
        if (mRewardedVideoAd != null && !mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.loadAd(App.ADS_ID_RVID, new AdRequest.Builder().build());
        }
    }

    private void showRewardedVideo() {
        Log.i(TAG, "====showRewardedVideo====");

        if (mRewardedVideoAd != null && mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }

    @Override
    protected void onResume() {

        if (App.checkDbFileIsExist() == true) {
            if (realm != null && fabRestoreBackup != null) {
                fabRestoreBackup.performClick();
            }
        }

        super.onResume();
    }


    /**
     * Async task class to get json by making HTTP call
     */
    ProgressDialog pDialog;

    private class GetCategory extends AsyncTask<Void, Void, Void> {


        String url = "http://avaa.co.in/api/category.php";
        String jsonResponse = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(CategoryExpanActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                jsonResponse = jsonStr;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            parsJson(jsonResponse);

        }

    }
}
