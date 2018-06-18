package com.azwallpaper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.utils.HttpHandler;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class CategoryExpanActivity extends AppCompatActivity {

    public static final int FIRST_LEVEL_COUNT = 6;
    public static final int SECOND_LEVEL_COUNT = 4;
    public static final int THIRD_LEVEL_COUNT = 20;
    private ExpandableListView expandableListView;
    private Button btnReload;

    private static final String TAG = CategoryExpanActivity.class.getSimpleName();

    ArrayList<GroupModel> arrayListGroup = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.category_expan_activity);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            expandableListView = findViewById(R.id.mainList);
            btnReload = findViewById(R.id.btnReload);
            new GetCategory().execute();

            btnReload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new GetCategory().execute();
                }
            });


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

                arrayListGroup = new ArrayList<>();

                JSONObject objMainJson = new JSONObject(strJsonResponse);
                Iterator keyMainJson = objMainJson.keys();

                while (keyMainJson.hasNext()) {
                    // loop to get the dynamic key
                    String currentDynamicKey = (String) keyMainJson.next();
                    App.showLog("==0000==Key==" + currentDynamicKey);

                    // get the value of the dynamic key
                    JSONObject currentDynamicValue = objMainJson.getJSONObject(currentDynamicKey);
                    App.showLog("==0000=====JSONObject==" + currentDynamicValue.toString());

                    GroupModel groupModel = new GroupModel("", currentDynamicKey, "1");


                    String id = currentDynamicValue.getString("id");
                    groupModel.id = id;
                    String name = currentDynamicValue.getString("name");
                    groupModel.name = name;

                    App.showLog(id + "===ID==111==Name=" + name);


                    ArrayList<ChildModel> arrayListChildModel = new ArrayList<>();

                    Iterator keyMainJson1 = currentDynamicValue.keys();
                    while (keyMainJson1.hasNext()) {
                        // loop to get the dynamic key
                        String currentDynamicKey1 = (String) keyMainJson1.next();
                        App.showLog("==1111==Key==" + currentDynamicKey1);

                        if (currentDynamicKey1.contains("childs")) {

                            // get the value of the dynamic key
                            JSONObject currentDynamicValue1 = currentDynamicValue.getJSONObject(currentDynamicKey1);
                            App.showLog("=1111==JSONObject==" + currentDynamicValue1.toString());
                            // do something here with the value...

                            Iterator keyMainJson2 = currentDynamicValue1.keys();
                            while (keyMainJson2.hasNext()) {
                                // loop to get the dynamic key
                                String currentDynamicKey2 = (String) keyMainJson2.next();
                                App.showLog("==2222==Key==" + currentDynamicKey2);

                                ChildModel childModel = new ChildModel("", currentDynamicKey2, "2");


                                ArrayList<SubChildModel> arrayListSubChildModel = new ArrayList<>();
                                //if(currentDynamicKey2.contains("childs"))
                                {
                                    // get the value of the dynamic key
                                    JSONObject currentDynamicValue2 = currentDynamicValue1.getJSONObject(currentDynamicKey2);
                                    App.showLog("=222==JSONObject==" + currentDynamicValue2.toString());
                                    // do something here with the value...

                                    String subId = currentDynamicValue2.getString("id");
                                    childModel.id = subId;
                                    String subName = currentDynamicValue2.getString("name");
                                    childModel.name = subName;

                                    App.showLog(subId + "===ID==2222==Name=" + subName);


                                    Iterator keyMainJson3 = currentDynamicValue2.keys();
                                    while (keyMainJson3.hasNext()) {
                                        // loop to get the dynamic key
                                        String currentDynamicKey3 = (String) keyMainJson3.next();
                                        App.showLog("==333==Key==" + currentDynamicKey3);

                                        if (currentDynamicKey3.contains("childs")) {
                                            // get the value of the dynamic key
                                            JSONObject currentDynamicValue3 = currentDynamicValue2.getJSONObject(currentDynamicKey3);
                                            App.showLog("=333==JSONObject==" + currentDynamicValue3.toString());
                                            // do something here with the value...


                                            Iterator keyMainJson4 = currentDynamicValue3.keys();
                                            while (keyMainJson4.hasNext()) {
                                                // loop to get the dynamic key
                                                String currentDynamicKey4 = (String) keyMainJson4.next();
                                                App.showLog("==444==Key==" + currentDynamicKey4);
                                                SubChildModel subChildModel = new SubChildModel("", currentDynamicKey4, "3");


                                                //if(currentDynamicKey3.contains("childs"))
                                                {
                                                    // get the value of the dynamic key
                                                    JSONObject currentDynamicValue4 = currentDynamicValue3.getJSONObject(currentDynamicKey4);
                                                    App.showLog("=444==JSONObject==" + currentDynamicValue4.toString());

                                                    String subsubId = currentDynamicValue4.getString("id");
                                                    subChildModel.id = subsubId;
                                                    String subsubName = currentDynamicValue4.getString("name");
                                                    subChildModel.name = subsubName;

                                                    App.showLog(subsubId + "===ID==444==Name=" + subsubName);
                                                    // do something here with the value...
                                                }

                                                arrayListSubChildModel.add(subChildModel);
                                            }
                                        }
                                    }

                                }
                                childModel.arrayListSubChildModel = arrayListSubChildModel;
                                arrayListChildModel.add(childModel);
                            }
                        }
                    }

                    groupModel.arrayListChildModel = arrayListChildModel;
                    arrayListGroup.add(groupModel);

                    // do something here with the value...
                }

                // set adapter
                expandableListView.setAdapter(new ParentLevel(this));
                printAllData();


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
                convertView = inflater.inflate(R.layout.row_second, null);
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
                convertView = inflater.inflate(R.layout.row_third, null);
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


    private void printAllData() {
        for (GroupModel groupModel : arrayListGroup) {


            App.showLog(groupModel.id + "===GroupModel==" + groupModel.name);
            for (ChildModel childModel : groupModel.arrayListChildModel) {
                App.showLog(childModel.id + "===ChildModel==" + childModel.name);
                for (SubChildModel subChildModelModel : childModel.arrayListSubChildModel) {
                    App.showLog(subChildModelModel.id + "===SubChildModel==" + subChildModelModel.name);
                }
            }
        }
    }


    class GroupModel {

        String id = "";
        String name = "";
        String level = "";
        ArrayList<ChildModel> arrayListChildModel = new ArrayList<>();

        public GroupModel(String id, String name, String level) {
            this.id = id;
            this.name = name;
            this.level = level;
        }
    }


    class ChildModel {

        String id = "";
        String name = "";
        String level = "";
        ArrayList<SubChildModel> arrayListSubChildModel = new ArrayList<>();

        public ChildModel(String id, String name, String level) {
            this.id = id;
            this.name = name;
            this.level = level;
        }
    }

    class SubChildModel {

        String id = "";
        String name = "";
        String level = "";

        public SubChildModel(String id, String name, String level) {
            this.id = id;
            this.name = name;
            this.level = level;
        }
    }


}



