package com.azwallpaper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.library.FocusResizeAdapter;
import com.example.library.FocusResizeScrollListener;
import com.fmsirvent.ParallaxEverywhere.PEWImageView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.model.GsonResponseWallpaperList;
import com.model.JsonImageModel;
import com.squareup.picasso.Transformation;
import com.utils.Blur;
import com.utils.RealmBackupRestore;
import com.utils.StaticData;
import com.utils.TouchImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.fabric.sdk.android.services.concurrency.AsyncTask;
import io.realm.Realm;
import io.realm.RealmResults;


/**

 */
public class LocalWallpaperActivity extends AppCompatActivity {
    private static final String TAG = LocalWallpaperActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private GridLayoutManager mLayoutManager;

    private static final int COLUMN_NUM = 2;
    private GalleryAdapter mAdapter;
    private DefaultAnimatedAdapter defaultAdapter;
    FloatingActionButton fabDownload;
    FloatingActionButton fabShare;
    RelativeLayout rlImage;
    TouchImageView ivFullScreen;
    ImageView ivClose;
    TextView tvSetWallpaper;
    Bitmap bitmap = null;

    String id = "1";
    String strTitle = "Wallpaper";


    Realm realm;

    Animation slide_down;
    Animation slide_up;


    /**
     * Sets the Action Bar for new Android versions.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void actionBarSetup() {
        try {
            this.getSupportActionBar().setTitle(strTitle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Log.d(TAG, "----------onCreate----------");

            super.onCreate(savedInstanceState);
            setContentView(R.layout.content_dashboard);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            rlImage = findViewById(R.id.rlImage);
            fabDownload = findViewById(R.id.fabDownload);
            fabShare = findViewById(R.id.fabShare);
            ivFullScreen = findViewById(R.id.ivFullScreen);
            ivClose = findViewById(R.id.ivClose);
            tvSetWallpaper = findViewById(R.id.tvSetWallpaper);
            tvSetWallpaper.setTypeface(App.getFont_Regular());
            //Load animation
            slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.slide_down);

            slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.slide_up);


            tvSetWallpaper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bitmap != null) {
                        progressBar.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setWallpaper(3);
                            }
                        }, 500);

                    } else {
                        App.showSnackBar(tvSetWallpaper, "Please wait downloading image....!");
                    }
                }
            });

            fabDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bitmap != null) {
                        progressBar.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setWallpaper(1);
                            }
                        }, 500);

                    } else {
                        App.showSnackBar(tvSetWallpaper, "Please wait downloading image....!");
                    }
                }
            });
            fabShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bitmap != null) {
                        progressBar.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setWallpaper(2);
                            }
                        }, 500);

                    } else {
                        App.showSnackBar(tvSetWallpaper, "Please wait downloading image....!");
                    }
                }
            });

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.GONE);

                    fabDownload.setVisibility(View.GONE);
                    fabShare.setVisibility(View.GONE);

                    rlImage.startAnimation(slide_down);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rlImage.setVisibility(View.GONE);
                        }
                    },400);


                }
            });


            mRecyclerView = findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);


            if (getIntent() != null && getIntent().getExtras() != null) {
                if (getIntent().getExtras().getString("id") != null) {
                    id = getIntent().getExtras().getString("id");
                }

                if (getIntent().getExtras().getString("title") != null) {
                    strTitle = getIntent().getExtras().getString("title");
                    actionBarSetup();
                }

            }

            if (id != null) {






           /* // Clear the realm from last time
            Realm.deleteRealm(realmConfiguration);
*/
            /*RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                    .encryptionKey(App.getEncryptRawKey())
                    .build();
*/
                //realm = Realm.getInstance(realmConfiguration);
                realm = Realm.getInstance(App.getRealmConfiguration());


                if (realm != null) {

                    //insertValues();
                    getWallpaperValues(true);

                } else {
                    App.showLog("=====realm===null==");
                }
            } else {
                progressBar.setVisibility(View.GONE);
            }

            setDisplayBanner();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    AdView mAdView;
    RelativeLayout rlAds;

    private void setDisplayBanner() {


        //String deviceid = tm.getDeviceId();

        rlAds = findViewById(R.id.rlAds);
        rlAds.setVisibility(View.VISIBLE);
        mAdView = new AdView(LocalWallpaperActivity.this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(App.ADS_ID_BANNER2);

        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
        rlAds.addView(mAdView);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("35588250B43518CCDA7DE425504EE232")
                .build();
        //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        //.addTestDevice(deviceid).build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
                rlAds.setVisibility(View.GONE);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                Log.i("Ads", "onAdClosed");
            }
        });
    }


    private void insertValues() {
        try {
            // for the insert value
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    ArrayList<JsonImageModel> arrayListJsonImageModel = new ArrayList<>();
                    if (id.equalsIgnoreCase("13")) {
                        arrayListJsonImageModel = App.getJsonFromGson("popular.json", id, realm);
                        // App.gettingRecordFromJson("popular.json");
                        //arrayListJsonImageModel = App.gettingRecordFromJson("z12cate_.json");
                        //arrayListJsonImageModel = App.gettingRecordFromJson("popular.json");
                        Collections.reverse(arrayListJsonImageModel);
                    } else if (id.equalsIgnoreCase("12")) {
                        arrayListJsonImageModel = App.getJsonFromGson("z" + id + "cate_.json", id, realm);
                        Collections.reverse(arrayListJsonImageModel);
                    } else {
                        arrayListJsonImageModel = App.getJsonFromGson("z" + id + "cate_.json", id, realm);
                        //arrayListJsonImageModel = App.gettingRecordFromJson("z" + id + "cate_.json");
                    }
                    if (arrayListJsonImageModel != null) {
                        App.showLog("====arrayListJsonImageModel===" + arrayListJsonImageModel.size());
                        mLayoutManager = new GridLayoutManager(LocalWallpaperActivity.this, COLUMN_NUM);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mAdapter = new GalleryAdapter(LocalWallpaperActivity.this, arrayListJsonImageModel);
                        mRecyclerView.setAdapter(mAdapter);
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }, 1500);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void getWallpaperValues(final boolean isGrid) {
        try {
            // for the insert value
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    ArrayList<JsonImageModel> arrayListJsonImageModel = new ArrayList<>();
                    if (id.equalsIgnoreCase("13")) {
                        arrayListJsonImageModel = getDataWallpaper("popular.json", id, realm);
                        // App.gettingRecordFromJson("popular.json");
                        //arrayListJsonImageModel = App.gettingRecordFromJson("z12cate_.json");
                        //arrayListJsonImageModel = App.gettingRecordFromJson("popular.json");
                        Collections.reverse(arrayListJsonImageModel);
                    } else if (id.equalsIgnoreCase("12")) {
                        arrayListJsonImageModel = getDataWallpaper("z" + id + "cate_.json", id, realm);
                        Collections.reverse(arrayListJsonImageModel);
                    } else {
                        arrayListJsonImageModel = getDataWallpaper("z" + id + "cate_.json", id, realm);
                        //arrayListJsonImageModel = App.gettingRecordFromJson("z" + id + "cate_.json");
                    }
                    if (arrayListJsonImageModel != null) {
                        App.showLog("====arrayListJsonImageModel===" + arrayListJsonImageModel.size());

                        mRecyclerView = findViewById(R.id.recycler_view);
                        mRecyclerView.setHasFixedSize(true);

                        if (isGrid == true) {
                            mLayoutManager = new GridLayoutManager(LocalWallpaperActivity.this, COLUMN_NUM);

                            mRecyclerView.setLayoutManager(mLayoutManager);
                            mAdapter = new GalleryAdapter(LocalWallpaperActivity.this, arrayListJsonImageModel);
                            mRecyclerView.setAdapter(mAdapter);
                            progressBar.setVisibility(View.GONE);
                        } else {
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LocalWallpaperActivity.this);
                            defaultAdapter = new DefaultAnimatedAdapter(LocalWallpaperActivity.this, (int) getResources().getDimension(R.dimen.custom_item_height), arrayListJsonImageModel);

                            mRecyclerView.setLayoutManager(linearLayoutManager);
                            mRecyclerView.setAdapter(defaultAdapter);
                            mRecyclerView.addOnScrollListener(new FocusResizeScrollListener<>(defaultAdapter, linearLayoutManager));
                            progressBar.setVisibility(View.GONE);


                           /* mLayoutManager = new GridLayoutManager(LocalWallpaperActivity.this, COLUMN_NUM);
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            mAdapter = new GalleryAdapter(LocalWallpaperActivity.this, arrayListJsonImageModel);
                            mRecyclerView.setAdapter(mAdapter);
                            progressBar.setVisibility(View.GONE);*/
                        }


                    } else {
                        progressBar.setVisibility(View.GONE);
                    }

                    if (arrayListJsonImageModel == null || arrayListJsonImageModel.size() <= 1) {
                        if (App.checkDbFileIsExist() == true && realm != null) {
                            RealmBackupRestore realmBackupRestore = new RealmBackupRestore(LocalWallpaperActivity.this, realm);
                            realmBackupRestore.restore();

                        } else {
                            App.downloadPhoto2();
                        }
                    }
                }
            }, 1500);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public ArrayList<JsonImageModel> getDataWallpaper(String strFileName, String id, Realm realm) {
        List<JsonImageModel> list = new ArrayList<>();
        try {

            App.showLog("========getDataWallpaper=====");
            ArrayList<GsonResponseWallpaperList> arrGsonResponseWallpaperList = new ArrayList<>();

            RealmResults<GsonResponseWallpaperList> arrDLocationModel = realm.where(GsonResponseWallpaperList.class)
                    .beginGroup()
                    .equalTo("filename", strFileName)
                    /*.or()
                    .contains("name", "Jo")*/
                    .endGroup()

                    .findAll();

            App.sLog("===arrDLocationModel==" + arrDLocationModel);
            List<GsonResponseWallpaperList> gsonResponseWallpaperList = arrDLocationModel;
            arrGsonResponseWallpaperList = new ArrayList<GsonResponseWallpaperList>(gsonResponseWallpaperList);

            for (int k = 0; k < arrGsonResponseWallpaperList.size(); k++) {
                App.sLog(k + "===arrGsonResponseWallpaperList=name=" + arrGsonResponseWallpaperList.get(k).filename);

                if (arrGsonResponseWallpaperList.get(k).arrayListJsonImageModel != null && arrGsonResponseWallpaperList.get(k).arrayListJsonImageModel.size() > 0) {
                    App.sLog(k + "===arrGsonResponseWallpaperList=size=" + arrGsonResponseWallpaperList.get(k).arrayListJsonImageModel.size());
                    list = arrGsonResponseWallpaperList.get(k).arrayListJsonImageModel;


                    break;
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<JsonImageModel>(list);
    }

    private void setWallpaper(int checkType) // 1 - download 2-share 3-set wallpaper
    {

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        String filename = ts + "_photo.png";
        //File sd = Environment.getExternalStorageDirectory();

        String sdCardPath = Environment.getExternalStorageDirectory().toString();
        File sd = new File(sdCardPath + "/" + App.APP_FOLDERNAME);


        final File dest = new File(sd, filename);

        try {
            FileOutputStream out = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

            progressBar.setVisibility(View.GONE);

            final Uri uri = Uri.fromFile(dest);
            if (checkType == 3) {

                Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setDataAndType(uri, "image/*");
                intent.putExtra("mimeType", "image/*");

                this.startActivity(Intent.createChooser(intent, "Set as"));
            } else if (checkType == 2) {

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                this.startActivity(Intent.createChooser(share, "Share wallpaper"));

            } else if (checkType == 1) {

                Snackbar.make(tvSetWallpaper, "Wallpaper Download Success", Snackbar.LENGTH_LONG)
                        .setAction("Open", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setDataAndType(uri, "image/*");
                                startActivity(intent);
                            }
                        }).show();

            }
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_grid, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {

            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;

            case R.id.menu_list_grid:
                // search action

                if (item.isChecked() == true) {
                    App.showLog("=menu_list_grid===true=");
                    item.setChecked(false);
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_grid_on_white_24dp));

                    if (realm != null) {

                        //insertValues();
                        getWallpaperValues(false);

                    } else {
                        App.showLog("=====realm===null==");
                    }
                } else {
                    App.showLog("=menu_list_grid===false=");
                    item.setChecked(true);
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_list_white_24dp));

                    if (realm != null) {

                        //insertValues();
                        getWallpaperValues(true);

                    } else {
                        App.showLog("=====realm===null==");
                    }
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // For the gallary adapter
    public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

        private Context mContext;   // get resource of this component
        private List<JsonImageModel> mList;

        public GalleryAdapter(Context mContext, List<JsonImageModel> mList) {
            this.mContext = mContext;
            this.mList = mList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public PEWImageView mImageView;
            public TextView tvTitle;
            public CardView cvCard;


            public ViewHolder(View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.gallery_item);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                cvCard = itemView.findViewById(R.id.cvCard);

                tvTitle.setTypeface(App.getFont_Regular());
            }
        }

        @Override
        public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_wallpaper,
                    parent, false);
            GalleryAdapter.ViewHolder vh = new GalleryAdapter.ViewHolder(v);
            return vh;
        }


        @Override
        public void onBindViewHolder(final GalleryAdapter.ViewHolder holder, final int position) {
//        Contact item = mList.get(position);
            final JsonImageModel item = mList.get(position);
            holder.tvTitle.setText(position + " ♥ " + item.title);
            holder.cvCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        //App.expand2(rlImage);
                        if (bitmap != null)
                            bitmap.recycle();

                        bitmap = null;
                        rlImage.setVisibility(View.VISIBLE);
                        fabDownload.setVisibility(View.VISIBLE);
                        fabShare.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.VISIBLE);

                        rlImage.startAnimation(slide_up);

                        App.splash_url = mList.get(position).thumbnail_url;
                        App.showLog("==img==" + App.splash_url);

                        Glide.with(LocalWallpaperActivity.this).load(App.splash_url).asBitmap().into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                try {
                                    App.sharePrefrences.setPref(StaticData.key_splash_bg, App.splash_url);
                                    bitmap = resource;
                                    ivFullScreen.setImageBitmap(resource);
                                    progressBar.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Glide.with(LocalWallpaperActivity.this).load(App.splash_url).placeholder(R.drawable.ic_placeholder).into(ivFullScreen);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            Glide.with(LocalWallpaperActivity.this)
                    .load(item.thumbnail_url)
                    .thumbnail(0.5f)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.mImageView);



         /*   Picasso.with(mContext)
                    .load(item.thumbnail_url)
                    .resize(10, 10)
                    //.fit()
                    //.centerCrop()

                    .error(R.mipmap.ic_launcher)
                    .transform(blurTransformation)
                    .into(holder.mImageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Picasso.with(mContext)
                                    .load(item.thumbnail_url) // image url goes here

                                    .into(holder.mImageView);
                        }

                        @Override
                        public void onError() {
                        }
                    });*/

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        //    public void addAll(List<Contact> newList) {
        public void addAll(List<JsonImageModel> newList) {
            mList.addAll(newList);
        }

        public void add(JsonImageModel item) {
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


    //for the list animated adapter

    public class DefaultAnimatedAdapter extends FocusResizeAdapter<RecyclerView.ViewHolder> {

        private List<JsonImageModel> items;

        public DefaultAnimatedAdapter(Context context, int height, List<JsonImageModel> listData) {
            super(context, height);
            items = new ArrayList<>();
            items = listData;
        }

        @Override
        public int getFooterItemCount() {
            // Return items size
            return items.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
            // Inflate your custom item layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_anim_list, parent, false);
            return new DefaultCustomViewHolder(v);
        }

        @Override
        public void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
            // Set your data into your custom layout
            JsonImageModel customObject = items.get(position);
            fill((DefaultCustomViewHolder) holder, customObject, position);
        }

        private void fill(DefaultCustomViewHolder holder, JsonImageModel customObject, final int position) {

            holder.tvTitle.setText(position + " ♥ " + customObject.title);
            holder.rlMainAnim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //App.expand2(rlImage);
                    bitmap = null;
                    rlImage.setVisibility(View.VISIBLE);//
                    fabDownload.setVisibility(View.VISIBLE);//
                    fabShare.setVisibility(View.VISIBLE);//
                    progressBar.setVisibility(View.VISIBLE);

                    rlImage.startAnimation(slide_up);

                    App.splash_url = items.get(position).thumbnail_url;
                    App.showLog("==img==" + App.splash_url);

                    Glide.with(LocalWallpaperActivity.this).load(items.get(position).image_path).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            try {
                                App.sharePrefrences.setPref(StaticData.key_splash_bg, App.splash_url);
                                bitmap = resource;
                                ivFullScreen.setImageBitmap(resource);
                                progressBar.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    Glide.with(LocalWallpaperActivity.this).load(items.get(position).image_path).placeholder(R.color.light_gray).into(ivFullScreen);

                }
            });

            Glide.with(LocalWallpaperActivity.this)
                    .load(customObject.thumbnail_url)
                    .thumbnail(0.5f)
                    .placeholder(R.color.light_gray)
                    .into(holder.mImageView);
        }

        public class DefaultCustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView mImageView;
            public TextView tvTitle;
            public RelativeLayout rlMainAnim;


            public DefaultCustomViewHolder(View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.image_custom_item);
                tvTitle = itemView.findViewById(R.id.title_custom_item);
                rlMainAnim = itemView.findViewById(R.id.rlMainAnim);

                tvTitle.setTypeface(App.getFont_Bold());
            }
        }


        @Override
        public void onItemBigResize(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {
            // The focused item will resize to big size while is scrolling
        }

        @Override
        public void onItemBigResizeScrolled(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {
            // The focused item resize to big size when scrolled is finished
        }

        @Override
        public void onItemSmallResizeScrolled(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {
            // All items except the focused item will resize to small size when scrolled is finished
        }

        @Override
        public void onItemSmallResize(RecyclerView.ViewHolder viewHolder, int position, int dyAbs) {
            // All items except the focused item will resize to small size while is scrolling
        }

        @Override
        public void onItemInit(RecyclerView.ViewHolder viewHolder) {
            // Init first item when the view is loaded
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
    public void onBackPressed() {
        if (ivClose != null && rlImage != null && rlImage.getVisibility() == View.VISIBLE) {
            ivClose.performClick();
        } else {
            super.onBackPressed();
        }
    }
}
