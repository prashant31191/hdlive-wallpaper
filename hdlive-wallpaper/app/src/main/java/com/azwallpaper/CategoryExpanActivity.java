package com.azwallpaper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.utils.HttpHandler;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Iterator;

public class CategoryExpanActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int FIRST_LEVEL_COUNT = 6;
    public static final int SECOND_LEVEL_COUNT = 4;
    public static final int THIRD_LEVEL_COUNT = 20;
    private ExpandableListView expandableListView;

    private static final String TAG = CategoryExpanActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.category_expan_activity);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            expandableListView = findViewById(R.id.mainList);


            new GetCategory().execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
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

        } else if (id == R.id.nav_more_app) {
            //
            String url = "https://www.amazon.com/s/ref=bl_sr_mobile-apps/131-3886681-0807921?_encoding=UTF8&field-brandtextbin=wewer&node=2350149011";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

                // set adapter
                expandableListView.setAdapter(new ParentLevel(this));


            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class ParentLevel extends BaseExpandableListAdapter {

        private Context context;

        public ParentLevel(Context context) {
            this.context = context;
        }

        @Override
        public Object getChild(int arg0, int arg1) {
            return arg1;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            SecondLevelExpandableListView secondLevelELV = new SecondLevelExpandableListView(CategoryExpanActivity.this);
            secondLevelELV.setAdapter(new SecondLevelAdapter(context));
            secondLevelELV.setGroupIndicator(null);
            return secondLevelELV;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return SECOND_LEVEL_COUNT;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupPosition;
        }

        @Override
        public int getGroupCount() {
            return FIRST_LEVEL_COUNT;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_first, null);
                TextView text = convertView.findViewById(R.id.eventsListEventRowText);
                text.setText("FIRST LEVEL");
            }
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    public class SecondLevelExpandableListView extends ExpandableListView {

        public SecondLevelExpandableListView(Context context) {
            super(context);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            //999999 is a size in pixels. ExpandableListView requires a maximum height in order to do measurement calculations.
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(999999, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public class SecondLevelAdapter extends BaseExpandableListAdapter {

        private Context context;

        public SecondLevelAdapter(Context context) {
            this.context = context;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupPosition;
        }

        @Override
        public int getGroupCount() {
            return 1;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_third, null);
                TextView text = convertView.findViewById(R.id.eventsListEventRowText);
                text.setText("SECOND LEVEL");
            }
            return convertView;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_second, null);
                TextView text = convertView.findViewById(R.id.eventsListEventRowText);
                text.setText("THIRD LEVEL");
            }
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return THIRD_LEVEL_COUNT;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}



