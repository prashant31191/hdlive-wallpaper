package com.utils;

import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class Contact {

    private String name="default";
    //private
    public ImageView image=null;

    public static int NoItems = 0;
    private static final String TAG = "Contact" ;

    private static Contact instance = getInstance();

    public static Contact getInstance() {
        if (instance == null) {
            instance = new Contact();
        }

        return instance;
    }

    public String getName() {
        return name;
    }

    public ImageView getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

      // can not generate imageview object here !
    public void setImage() {
        Log.d(TAG, "setImage now " );
//        this.image.setImageResource(R.drawable.dog);
//        this.image = newimage;
    }


    // this is actually initialization for name
    public static List<Contact> generateSampleList(int samples){
        List<Contact> list = new ArrayList<>();
        //Log.d(TAG, "--------generateSampleList-----samples is " + samples + " ----NoItems---- is " + NoItems);

        for(int i= 0 ; i < samples; i++){  // dynamically initialize photoes
            //Contact contact = new Contact();
            Contact contact = getInstance();
            contact.setName("Name - " + i);

            // before add each image item into Adapter, iamge items cannot get imageview layout info thus only null pointers ?
//            contact.image.setImageResource(R.drawable.dog);
//            contact.setImage();//  only get null pointer

            list.add(contact);
        }
        //NoItems = NoItems + samples;
        return list;
    }


}
