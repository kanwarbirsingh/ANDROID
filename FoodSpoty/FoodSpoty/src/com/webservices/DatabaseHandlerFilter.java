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

public class DatabaseHandlerFilter extends SQLiteOpenHelper {
    // Database Version
    public static final int DATABASE_VERSION = 2;
 
    // Database Name
    public static final String DATABASE_NAME = "FSFilter";
    public static final String TableNameFilterTable = "Filtertable";
    

    // Labels table name
    public static final String Key_ID = "id";
     public static final String Key_FilterTitle = "title";
     public static final String Key_FilterType = "filtertype";
    public static  String strFilterId,strFilterTitle,strFilterType;
  
    public DatabaseHandlerFilter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 //INTEGER PRIMARY KEY   AUTOINCREMENT
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	// Category table create query
    	String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TableNameFilterTable + "("
    			+ Key_ID + " INTEGER PRIMARY KEY   AUTOINCREMENT,"+ Key_FilterTitle + " TEXT,"+ Key_FilterType+ " TEXT)";
    	
    	System.out.println("Table Created "+CREATE_CATEGORIES_TABLE);

    	/*String CREATE_CATEGORIES_TABLE2 = "CREATE TABLE " + TableNameFilterTable + "("
        		+ Key_FilterId + " INTEGER PRIMARY KEY," + Key_FilterTitle + " TEXT+" + Key_TotalProductPrice + " TEXT+" + Key_ProductQty + " TEXT)";*/
    	db.execSQL(CREATE_CATEGORIES_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TableNameFilterTable);
 
        // Create tables again
        onCreate(db);
    }
    
    /**
     * Inserting new lable into lables table
     * */
    public String AddFilter(String FilterTitle,String FilterType){
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	try
    	{
    	JSONObject JOb = new JSONObject();
    	ContentValues values = new ContentValues();
    	values.put(Key_FilterTitle, FilterTitle);
    	values.put(Key_FilterType, FilterType);
    	 	
    	// Inserting Row
        db.insert(TableNameFilterTable, null, values);
        System.out.println("Filter Insert FilterTitle "+FilterTitle+" FilterType "+FilterType);
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
        String selectQuery ="SELECT  * FROM " + TableNameFilterTable ;
//     /select * from Login where UserName=? and Pass=? 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	//labels.add(cursor.getString(1));
            	HashMap<String, String> map = new HashMap<String, String>();
            	
            	strFilterTitle=cursor.getString(cursor.getColumnIndexOrThrow(Key_FilterTitle));
            	
            	map.put(Key_FilterTitle, strFilterTitle);
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
     String selectQuery ="SELECT  * FROM " + TableNameFilterTable ;
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
         	strFilterTitle=cursor.getString(cursor.getColumnIndexOrThrow(Key_FilterTitle));
         	strFilterType=cursor.getString(cursor.getColumnIndexOrThrow(Key_FilterType));
         	
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

 
 public void RemoveAllFilter(){
 	SQLiteDatabase db = this.getWritableDatabase();
 	
 	db.execSQL("delete from "+ TableNameFilterTable);
 	
 
     db.close(); // Closing database connection
 } 
 
 public String RemoveOneFilter1(String FilterTitle){

	    String Query = "Delete from " + TableNameFilterTable + " where " + Key_FilterTitle + " = '" + FilterTitle+"'";
	    SQLiteDatabase db = this.getReadableDatabase();
	   
	 	
	 try
	 {
		 Cursor cursor = db.rawQuery(Query, null);

 	
   //  db.insert(TableNameCartList, null, values);
     db.close(); // Closing database connection
	 }
	 catch(Exception ex)
	 {
		 return "0";
	 }
     return "1";
 } 
 public String RemoveOneFilter(String FilterTitle){

	 SQLiteDatabase db = this.getWritableDatabase();
 	
	 try
	 {
 	db.delete(TableNameFilterTable ,Key_FilterTitle+"='" + FilterTitle+"'", null);

 	
   //  db.insert(TableNameFilterTable, null, values);
 	System.out.println("Filter Remove FilterTitle "+"'+FilterTitle+'");
     db.close(); // Closing database connection
	 }
	 catch(Exception ex)
	 {
		 return "0";
	 }
     return "1";
 } 
 

 public String getTotalCartItem() {
	 String totalCount;   
	 String countQuery = "SELECT  * FROM " + TableNameFilterTable;
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(countQuery, null);
	    int cnt = cursor.getCount();
	    
	    cursor.close();
	    return String.valueOf(cnt);
	}
 

 public  int CheckExistItemOrNot(String Filterttile) {
	
	    String Query = "Select * from " + TableNameFilterTable + " where " + Key_FilterTitle + " = '" + Filterttile+"'";
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
