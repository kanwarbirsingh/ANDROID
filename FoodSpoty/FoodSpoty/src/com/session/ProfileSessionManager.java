package com.session;

import java.util.HashMap;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
 
@SuppressLint("CommitPrefEdits") 
public class ProfileSessionManager {
    // Shared Preferences
    SharedPreferences pref;
     
    // Editor for Shared preferences
    Editor editor;
     
    // Context
    Context _context;
     
    // Shared pref mode
    int PRIVATE_MODE = 0;
     
    // Sharedpref file name
    private static final String PREF_NAME = "foodSpotyeUserProfile";
     
    // All Shared Preferences Keys
    private static final String IS_PROFILECREATED = "isCreateIn";
     
    // User name (make variable public to access from outside)
    public static final String KEY_ID        = "user_id";
    public static final String KEY_USERNAME  = "user_name";
    public static final String KEY_MOBILE    = "mobile_no";
    public static final String KEY_EMAIL     = "email_id";
    public static final String KEY_ADDRESS     = "user_address";
    public static final String KEY_Password     = "userpassword";
    public static final String KEY_UserPic     = "userpic";
    public static final String KEY_LoginWith     = "fblogin";
    public static final String KEY_userlatitude    = "ulat";
    public static final String KEY_userlongitude    = "ulong";
    public static final String KEY_userTotalPoint    = "utotalpoint";
    public static final String KEY_LoginStatus    = "loginstatus";
    public static final String KEY_SelectedRestorent    = "selectedrest";
    
    
    // Constructor
    public ProfileSessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
     
    /**
     * Create login session
     * */
    public void createProfileSession(String id, String username, String mobile, String email, String address){
        // Storing login value as TRUE
        editor.putBoolean(IS_PROFILECREATED, true);
         
        editor.putString(KEY_ID, id);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ADDRESS, address);
         
        // commit changes
        editor.commit();
    }   
     
    public void createIdSession(String id){
        // Storing login value as TRUE
        editor.putBoolean(IS_PROFILECREATED, true);
         
        editor.putString(KEY_ID, id);
         
        // commit changes
        editor.commit();
    } 
    
    public void createUserSession(String username){
        // Storing login value as TRUE
        editor.putBoolean(IS_PROFILECREATED, true);
        editor.putString(KEY_USERNAME, username);
         
        // commit changes
        editor.commit();
    } 
    
    public void createMoileSession(String mobile){
        // Storing login value as TRUE
        editor.putBoolean(IS_PROFILECREATED, true);
        editor.putString(KEY_MOBILE, mobile);
         
        // commit changes
        editor.commit();
    } 
    
    public void createEmailSession(String email){
        // Storing login value as TRUE
        editor.putBoolean(IS_PROFILECREATED, true);
        editor.putString(KEY_EMAIL, email);
         
        // commit changes
        editor.commit();
    } 
    
    public void createAddressSession(String address){
        // Storing login value as TRUE
        editor.putBoolean(IS_PROFILECREATED, true);
        editor.putString(KEY_ADDRESS, address);
         
        // commit changes
        editor.commit();
    } 
    
    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public boolean checkProfileCreated(){
        // Check login status
        if(this.isProfilecreated()){
        	return true;
        }
		return false;
    }
    
    public boolean isProfileCreated(){
        // Check login status
        if(this.isProfilecreated()){
            return true;
        }
		return false;
         
    }
     
    /**
     * Get stored session data
     * */
    public HashMap<String, String> getProfileDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, null));
         
        // return user
        return user;
    }
     
    
    /**
     *  
     */
    
    public String getUserId(){
    	
    	return pref.getString(KEY_ID, null);
    }
    

    public String getUserName(){
    	
    	return pref.getString(KEY_USERNAME, null);
    }
    

    public String getMobileNo(){
    	
    	return pref.getString(KEY_MOBILE, null);
    }
    

    public String getEmailAddress(){
    	
    	return pref.getString(KEY_EMAIL, null);
    }
    

    public String getAddress(){
    	
    	return pref.getString(KEY_ADDRESS, null);
    }

    
    /**
     * Clear session details
     * */
    public void distoreProfile(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }
     
    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isProfilecreated(){
        return pref.getBoolean(IS_PROFILECREATED, false);
    }
    
    public String GetSharedPreferences(String KeyValue) {
		SharedPreferences SP = PreferenceManager
				.getDefaultSharedPreferences(_context);
		return SP.getString(KeyValue, "");

	}

	public void SetSharedPreferences(String KeyValue, String keyString) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(_context);
		SharedPreferences.Editor spe = prefs.edit();
		spe.putString(KeyValue, keyString);
		spe.commit();
	}
	 public boolean CheckInternet(Context con) {
	 		boolean flag = false;

	 		if (isNetworkAvailable()) {
	 			flag = true;

	 		} else {

	 			/*AlertDialogMsg obAlertDialogMsg = new AlertDialogMsg(_context,
	 					"Check Internet");*/
	 			//obAlertDialogMsg.show();

	 			flag = false;
	 		}
	 		return flag;

	 	}
	     public boolean isNetworkAvailable() {
	 		ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);

	 		NetworkInfo wifiNetwork = cm
	 				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	 		if (wifiNetwork != null && wifiNetwork.isConnected()) {
	 			return true;
	 		}

	 		NetworkInfo mobileNetwork = cm
	 				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	 		if (mobileNetwork != null && mobileNetwork.isConnected()) {
	 			return true;
	 		}

	 		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	 		if (activeNetwork != null && activeNetwork.isConnected()) {
	 			return true;
	 		}

	 		return false;
	 	}
}
