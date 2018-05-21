package com.isoft.foodspoty;

import helper.CustomProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.isoftinc.listadapter.AdapterPeople;
import com.isoftinc.listadapter.AdapterProfileConnection;
import com.isoftinc.listadapter.AdapterProfileCoupons;
import com.session.ProfileSessionManager;
import com.webservices.ServiceHandler;
import com.webservices.Serviceurl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class PeopleActivity extends Activity{
	 
	ListView peopleListview;
    private ProgressDialog pDialog;
	private ArrayList<HashMap<String, String>> peopleData;
	
	Button btn_slidemenu, btn_menuback, btn_menuSearch, btn_menuFilter;
	TextView headTitle;
	ProfileSessionManager profileSession;
	public static String Key_Puser_id="puser_id";
	public static String Key_Puser_name="puser_name";
	public static String Key_Puser_email="puser_email";
	public static String Key_Pmobile="pmobile";
	public static String Key_Puser_pic="puser_pic";
	public static String Key_Puser_longitude="puser_longitude";
	public static String Key_Puser_latitude="puser_latitude";
	public static String Key_Ptotal_point="ptotal_point";
	public static String Key_Pconnection_status="connection_status";
	CustomToast	ct;
  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
         ct=new CustomToast(PeopleActivity.this);       
        peopleListview = (ListView)findViewById(R.id.peopleListview);
        profileSession=new ProfileSessionManager(PeopleActivity.this);
        btn_slidemenu  = (Button)findViewById(R.id.btn_slidemenu);
        btn_menuback   = (Button)findViewById(R.id.btn_menuback);
        btn_menuSearch = (Button)findViewById(R.id.btn_menuSearch);
        btn_menuFilter = (Button)findViewById(R.id.btn_menuFilter);
        headTitle	   = (TextView)findViewById(R.id.headTitle);

        btn_slidemenu.setVisibility(View.GONE);
        btn_menuback.setVisibility(View.VISIBLE);
        btn_menuSearch.setVisibility(View.INVISIBLE);
        btn_menuFilter.setVisibility(View.GONE);
        headTitle.setText("People");
        
        new LoadProfiledata().execute();
        
        btn_menuback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
        
        
    }

    
    /*
     * Start server data load for tab selection.
     * 
     */
    
    class LoadProfiledata extends AsyncTask<String, String, String> {
    	CustomProgressDialog pDialog;
    	String webUserId,webUserName,webUserEmail,webMobile,webuserPic,webUserLatitude,webUserLongitude,webTotalPoint,webPassword;
    	String webTotalCoupan="0",webTotalConnection="0";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new CustomProgressDialog(PeopleActivity.this);
			
			pDialog.show();

		}

		protected String doInBackground(String... args) {
			String responseStatus = null;
			peopleData = new ArrayList<HashMap<String, String>>();
		
			try {
				ServiceHandler sh = new ServiceHandler();
				String struserpicurl=null;
				/*	try {
    				struserpicurl = URLEncoder.encode(struserpic.toString(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
    		
					String loginUrl = Url.GetUrl(Serviceurl.get_people + "user_id=" + profileSession.GetSharedPreferences(ProfileSessionManager.KEY_ID));
					
					Log.d("Link", loginUrl);
				
					String jsonStr = sh.makeServiceCall(loginUrl,ServiceHandler.GET);
					Log.d("JSON Response", jsonStr);
				
				if (jsonStr != null) {
					JSONObject jsonObj = new JSONObject(jsonStr);
					
					JSONObject response_obj = jsonObj.getJSONObject("response");
					Log.d("responce", response_obj.toString());

					
					String status = response_obj.getString("status");
					String message = response_obj.getString("message");
					if(status.equals("1"))
					{
						JSONArray jarrpeople=response_obj.getJSONArray("people");
						
						for(int i=0;i<jarrpeople.length();i++)
						{
						JSONObject jobjcon=jarrpeople.getJSONObject(i);
						HashMap<String , String>conmap=new HashMap<String, String>();
						conmap.put(Key_Puser_id, jobjcon.getString("user_id"));
						conmap.put(Key_Puser_name, jobjcon.getString("user_name"));
						conmap.put(Key_Puser_email, jobjcon.getString("user_email"));
						conmap.put(Key_Pmobile, jobjcon.getString("mobile"));
						conmap.put(Key_Puser_pic, jobjcon.getString("user_pic"));
						conmap.put(Key_Puser_longitude, jobjcon.getString("user_longitude"));
						conmap.put(Key_Puser_latitude, jobjcon.getString("user_latitude"));
						conmap.put(Key_Ptotal_point, jobjcon.getString("total_point"));
						conmap.put(Key_Pconnection_status, jobjcon.getString("connection_status"));
						peopleData.add(conmap);
						}
						
						
						
					}
					responseStatus = status + "`" + message;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return responseStatus;
		}

		protected void onPostExecute(final String responseStatus) {
        	runOnUiThread(new Runnable() 
            {
                public void run()
                {
                	
                	pDialog.dismiss();
                	String getResponseStatus  = null ;
                	String getResponseMessage = null ;
                	
                	if(responseStatus != null && !responseStatus.equalsIgnoreCase("")){
                		
                		String[] responseArr = responseStatus.split("`");
                		getResponseStatus  = responseArr[0];
                		try{   
                			getResponseMessage = responseArr[1];
                		}catch(Exception ev){
                			getResponseMessage = "" ;
                		}
                	}
                	
                	if(getResponseStatus != null && getResponseStatus.equalsIgnoreCase("0")){
                		ct.showToastError(getResponseMessage);
                		//Toast.makeText(getApplicationContext(), getResponseMessage.toString(), Toast.LENGTH_LONG).show();
                	}else if(getResponseStatus != null && getResponseStatus.equalsIgnoreCase("1")){
                		//Toast.makeText(getApplicationContext(), "Successfully created account.", Toast.LENGTH_LONG).show();
                		AdapterPeople listAdapter = new AdapterPeople(PeopleActivity.this, peopleData ,R.layout.row_people);
    					peopleListview.setAdapter(listAdapter);
    					
                	
                	}

                }
            });
            
         }
	}
	
  
    
    
    
    /*
     * 
     */
    
}
