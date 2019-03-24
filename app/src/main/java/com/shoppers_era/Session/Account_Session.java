package com.shoppers_era.Session;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class Account_Session {

    private static final String KEY_IS_SELLERID = "account_no";
    // LogCat tag
    private static String TAG = Session_Manager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "account";


    public Account_Session(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    public void setAccountNo(String accountNo) {

        editor.putString(KEY_IS_SELLERID, accountNo);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }



    public String getAccountNo(){
        return pref.getString(KEY_IS_SELLERID, "null");
    }


}