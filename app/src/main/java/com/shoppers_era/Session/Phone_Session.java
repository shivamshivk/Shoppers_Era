package com.shoppers_era.Session;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class Phone_Session {


    // LogCat tag
    private static String TAG = Session_Manager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "RasPhone";
    private static final String KEY_IS_CUSTOMERID = "phone_no";



    public Phone_Session(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    public void setPhonNo(String id) {

        editor.putString(KEY_IS_CUSTOMERID, id);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }




    public String getPhoneNO(){
        return pref.getString(KEY_IS_CUSTOMERID, "0");
    }


}