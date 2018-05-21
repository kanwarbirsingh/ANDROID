package com.webservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandlerFav extends SQLiteOpenHelper {
    // Database Version
    public static final int DATABASE_VERSION = 2;
 
    // Database Name
    public static final String DATABASE_NAME = "FSFavourite";
    public static final String FavTable = "Favtable";
    

    // Labels table name
      public static final String Key_RestaurentName = "restname";
     public static final String Key_RestorentId = "restId";
     public static final String Key_RestorentLat = "restlat";
     public static final String Key_RestorentLong = "restlong";
     
    public static  String strFilterId,strFilterTitle,strFilterType;
  
    public DatabaseHandlerFav(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }   
 //INTEGER PRIMARY KEY   AUTOINCREMENT
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	// Category table create query
    	String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + FavTable + "("
    			+ Key_RestorentId + " INTEGER PRIMARY KEY ,"+  Key_RestaurentName + " TEXT,"+Key_RestorentLat + " TEXT,"+ Key_RestorentLong+ " TEXT)";
    	
    	System.out.println("Table Created "+CREATE_CATEGORIES_TABLE);

    	/*String CREATE_CATEGORIES_TABLE2 = "CREATE TABLE " + FavTable + "("
        		+ Key_FilterId + " INTEGER PRIMARY KEY," + Key_RestaurentName + " TEXT+" + Key_TotalProductPrice + " TEXT+" + Key_ProductQty + " TEXT)";*/
    	db.execSQL(CREATE_CATEGORIES_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + FavTable);
 
        // Create tables again
        onCreate(db);
    }
    
    /**
     * Inserting new lable into lables table
     * */
    public String AddFilter(String restorentid,String restorentname,String restorentlat,String  restlong){
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	try
    	{
    	JSONObject JOb = new JSONObject();
    	ContentValues values = new ContentValues();
    	values.put(Key_RestorentId, restorentid);
    	values.put(Key_RestaurentName, restorentname);
    	values.put(Key_RestorentLat, restorentlat);
    	values.put(Key_RestorentLong, restlong);
    	 	
    	// Inserting Row
        db.insert(FavTable, null, values);
        //System.out.println("Filter Insert FilterTitle "+FilterTitle+" FilterType "+FilterType);
        db.close(); // Closing database connection
    	}
    	catch(Exception e)
    	{
    		return "0";
    	}
        
        return "1";
    }  
    
    /**
     * Getting all labels
     * returns list of labels
     * */
 public ArrayList<HashMap<String, String>> getFilterArraylist(){
    	
    	
    	JSONArray ItemJsonArr = new JSONArray();
    	ArrayList result =new ArrayList<HashMap<String, String>>();
        // Select All Query
        String selectQuery ="SELECT  * FROM " + FavTable ;
//     /select * from Login where UserName=? and Pass=? 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	//labels.add(cursor.getString(1));
            	HashMap<String, String> map = new HashMap<String, String>();
            	
            	strFilterTitle=cursor.getString(cursor.getColumnIndexOrThrow(Key_RestaurentName));
            	
            	map.put(Key_RestaurentName, strFilterTitle);
				result.add(map);
		  		
			
            	
            } while (cursor.moveToNext());
        }
        
        // closing connection
        cursor.close();
        db.close();
    	
    	// returning lables
        System.out.println("getArraylist "+result.toString());
    	return result;
    }
 
 public JSONObject getFilterArray(){
 	
	 ArrayList<String> result 			=new  ArrayList<String>();
	 JSONArray ArrCategory 		=new  JSONArray();
	 JSONArray ArrCity 		=new  JSONArray();
	 JSONObject jobjmain        =new JSONObject();
     // Select All Query
     String selectQuery ="SELECT  * FROM " + FavTable ;
//  /select * from Login where UserName=? and Pass=? 
     SQLiteDatabase db = this.getReadableDatabase();
     Cursor cursor = db.rawQuery(selectQuery, null);
     JSONArray ItemJsonArr = new JSONArray();
     // looping through all rows and adding to list
     if (cursor.moveToFirst()) {
         do {
         	//labels.add(cursor.getString(1));
        	 try {
        	 JSONObject JOb = new JSONObject();
         	strFilterTitle=cursor.getString(cursor.getColumnIndexOrThrow(Key_RestaurentName));
         	strFilterType=cursor.getString(cursor.getColumnIndexOrThrow(Key_RestorentId));
         	
         	if(strFilterType.equals("Category"))
         	{
         		JOb.put("filtertype",strFilterType);
        		JOb.put("filtername",strFilterTitle);
    	  		ArrCategory.put(JOb);
         	}
         	else if(strFilterType.equals("City"))
         	{
         		JOb.put("filtertype",strFilterType);
        		JOb.put("filtername",strFilterTitle);
    	  		ArrCity.put(JOb);
         	}
         
         		 result.add(strFilterTitle);
         		 System.out.println("Filter  getFilter "+strFilterTitle+" FilterType ");
         		
        	 }catch(Exception ex)
        		{
        			ex.printStackTrace();
        		}
         } while (cursor.moveToNext());
     }
     try
     {
     jobjmain.put("categoryfilter", ArrCategory);
     jobjmain.put("cityfilter", ArrCity);
    
     }
     catch(Exception ex)
     {
    	 ex.printStackTrace();
     }
     // closing connection
     cursor.close();
     db.close();
 	
 	// returning lables
     System.out.println("getArraylist "+result.toString());
 	return jobjmain;
 }

 
 public void RemoveAllFav(){
 	SQLiteDatabase db = this.getWritableDatabase();
 	
 	db.execSQL("delete from "+ FavTable);
 	
 
     db.close(); // Closing database connection
 } 
 
 
 public void DeleteOneService(String RestorentId){
	 	SQLiteDatabase db = this.getWritableDatabase();
	 	db.execSQL("delete from "+ FavTable+" where " + Key_RestorentId + " = " + RestorentId);
	 	//db.delete(TableNameCartList ,Key_ItemId + "=" + ItemId+" where " + Key_RestorentId + " = " + profSession.GetSharedPreferences(ProfileSessionManager.KEY_SelectedRestorentid, null);

	 	
	   //  db.insert(TableNameCartList, null, values);
	     db.close(); // Closing database connection
	 } 
	 

 public String getTotalCartItem() {
	 String totalCount;   
	 String countQuery = "SELECT  * FROM " + FavTable;
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(countQuery, null);
	    int cnt = cursor.getCount();
	    
	    cursor.close();
	    return String.valueOf(cnt);
	}
 

 public  int CheckExistItemOrNot(String restid) {
	
	    String Query = "Select * from " + FavTable + " where " + Key_RestorentId + " = " + restid;
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(Query, null);
	        if(cursor.getCount() <= 0){
	            cursor.close();
	            return 0;
	        }
	    cursor.close();
	    return 1;
	}
}
