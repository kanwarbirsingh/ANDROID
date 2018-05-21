package com.isoft.foodspoty;

import helper.CustomProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.isoftinc.listadapter.AdapterNotification;
import com.isoftinc.listadapter.AdapterRedeemCoupans;
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

public class NotificationActivity extends Activity{
	 
	ListView notificationListview;
    private ProgressDialog pDialog;
	private ArrayList<HashMap<String, String>> notiData;
	public static String Key_notification_id="notification_id";
	public static String Key_notification_message="notification_message";
	public static String Key_notification_status="notification_status";
	public static String Key_pic="pic";
	public static String Key_notification_send_date="notification_send_date";
	ProfileSessionManager profileSession;
	CustomToast ct;
	Button btn_slidemenu, btn_menuback, btn_menuSearch, btn_menuFilter;
	TextView headTitle;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        profileSession=new ProfileSessionManager(NotificationActivity.this);       
        notificationListview = (ListView)findViewById(R.id.notificationListview);
        ct=new CustomToast(NotificationActivity.this);
        btn_slidemenu  = (Button)findViewById(R.id.btn_slidemenu);
        btn_menuback   = (Button)findViewById(R.id.btn_menuback);
        btn_menuSearch = (Button)findViewById(R.id.btn_menuSearch);
        btn_menuFilter = (Button)findViewById(R.id.btn_menuFilter);
        headTitle	   = (TextView)findViewById(R.id.headTitle);

        btn_slidemenu.setVisibility(View.GONE);
        btn_menuback.setVisibility(View.VISIBLE);
        btn_menuSearch.setVisibility(View.INVISIBLE);
        btn_menuFilter.setVisibility(View.GONE);
        headTitle.setText("Notifications");
        
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
    class LoadData extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(NotificationActivity.this);
			pDialog.setMessage("Loading. Please wait ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			try {
					notiData = new ArrayList<HashMap<String, String>>();
					
					for (int i = 0; i < 10; i++) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("SOCIAL_ID", ""+i);
						map.put("SOCIAL_PIC", "");
						map.put("SOCIAL_TITLE", "");
	
						notiData.add(map);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			return null;
		}

		protected void onPostExecute(String file_url) {

			runOnUiThread(new Runnable() {
				public void run() {
					
					AdapterNotification listAdapter = new AdapterNotification(NotificationActivity.this, notiData ,R.layout.row_notification);
					notificationListview.setAdapter(listAdapter);
					pDialog.dismiss();
				}
			});
		}
		
	}
    
    /*
     * 
     */
    class LoadProfiledata extends AsyncTask<String, String, String> {
    	CustomProgressDialog pDialog;
    	String webTotalCoupan="0",webTotalConnection="0";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new CustomProgressDialog(NotificationActivity.this);
			
			pDialog.show();

		}

		protected String doInBackground(String... args) {
			String responseStatus = null;
			notiData = new ArrayList<HashMap<String, String>>();
			
			
			
			try {
				ServiceHandler sh = new ServiceHandler();
				String struserpicurl=null;
				/*	try {
    				struserpicurl = URLEncoder.encode(struserpic.toString(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
    		
					String loginUrl = Url.GetUrl(Serviceurl.get_notification + "user_id=" + profileSession.GetSharedPreferences(ProfileSessionManager.KEY_ID));
					
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
						JSONArray jarrdata=response_obj.getJSONArray("data");
						for(int i=0;i<jarrdata.length();i++)
						{
							JSONObject jobdata=jarrdata.getJSONObject(i);
							JSONArray jarrcashBack=jobdata.getJSONArray("cashBack");
							
							for(int i1=0;i1<jarrcashBack.length();i1++)
							{
							JSONObject jobcashback=jarrcashBack.getJSONObject(i1);
							HashMap<String , String>cashback=new HashMap<String, String>();
							cashback.put(Key_notification_id, jobcashback.getString("notification_id"));
							cashback.put(Key_notification_message, jobcashback.getString("notification_message"));
							cashback.put(Key_notification_status, jobcashback.getString("notification_status"));
							cashback.put(Key_pic, jobcashback.getString("pic"));
							cashback.put(Key_notification_send_date, jobcashback.getString("notification_send_date"));
							
							notiData.add(cashback);
							}
							
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
                		
                		AdapterNotification listAdapter = new AdapterNotification(NotificationActivity.this, notiData ,R.layout.row_notification);
    					notificationListview.setAdapter(listAdapter);
    				
                	
                	}

                }
            });
            
         }
	}
	
}
