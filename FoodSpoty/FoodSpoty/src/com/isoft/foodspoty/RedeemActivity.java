package com.isoft.foodspoty;

import helper.CustomProgressDialog;
import helper.Helper;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.isoftinc.listadapter.AdapterOffers;
import com.isoftinc.listadapter.AdapterProfileCoupons;
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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class RedeemActivity extends Activity{
	 
	public static GridView redeemGridview;
	public static ProgressDialog pDialog;
	public static ArrayList<HashMap<String, String>> redeemData;
	public static String Key_coupon_id="coupon_id";
	public static String Key_coupon_code="coupon_code";
	public static String Key_coupon_name="coupon_name";
	public static String Key_points_required="points_required";
	public static String Key_points_in="points_in";
	public static String Key_coupon_image="coupon_image";
	public static String Key_currentpoints="wallet_amount";
	
	Button btn_slidemenu, btn_menuback, btn_menuSearch, btn_menuFilter;
	TextView headTitle;
	ProfileSessionManager profileSession;
	CustomToast ct;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);
        ct=new CustomToast(RedeemActivity.this);       
        profileSession=new ProfileSessionManager(RedeemActivity.this);
         
        redeemGridview = (GridView)findViewById(R.id.redeemGridview);

        btn_slidemenu  = (Button)findViewById(R.id.btn_slidemenu);
        btn_menuback   = (Button)findViewById(R.id.btn_menuback);
        btn_menuSearch = (Button)findViewById(R.id.btn_menuSearch);
        btn_menuFilter = (Button)findViewById(R.id.btn_menuFilter);
        headTitle	   = (TextView)findViewById(R.id.headTitle);

        btn_slidemenu.setVisibility(View.GONE);
        btn_menuback.setVisibility(View.VISIBLE);
        btn_menuSearch.setVisibility(View.INVISIBLE);
        btn_menuFilter.setVisibility(View.GONE);
        headTitle.setText("Redeem");
        
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
			pDialog = new ProgressDialog(RedeemActivity.this);
			pDialog.setMessage("Loading. Please wait ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			try {
					redeemData = new ArrayList<HashMap<String, String>>();
					
					for (int i = 0; i < 10; i++) {
	
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("OFFER_ID", ""+i);
						map.put("OFFER_PIC", "");
						map.put("OFFER_NAME", "Minimum Points Req. "+i+"10 Available");
	
						redeemData.add(map);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			return null;
		}

		protected void onPostExecute(String file_url) {

			runOnUiThread(new Runnable() {
				public void run() {
					
					AdapterProfileCoupons listAdapter = new AdapterProfileCoupons(RedeemActivity.this, redeemData ,R.layout.row_redeem);
					redeemGridview.setAdapter(listAdapter);
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
			pDialog = new CustomProgressDialog(RedeemActivity.this);
			
			pDialog.show();

		}

		protected String doInBackground(String... args) {
			String responseStatus = null;
			redeemData = new ArrayList<HashMap<String, String>>();
			
			
			
			try {
				ServiceHandler sh = new ServiceHandler();
				String struserpicurl=null;
				/*	try {
    				struserpicurl = URLEncoder.encode(struserpic.toString(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
    		
					String loginUrl = Url.GetUrl(Serviceurl.get_redeem + "user_id=" + profileSession.GetSharedPreferences(ProfileSessionManager.KEY_ID));
					
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
						String walletAmount = response_obj.getString("wallet_amount");
						JSONArray jarrdata=response_obj.getJSONArray("data");
						for(int i=0;i<jarrdata.length();i++)
						{
							
							
							JSONObject jobcashback=jarrdata.getJSONObject(i);
							HashMap<String , String>cashback=new HashMap<String, String>();
							cashback.put(Key_coupon_id, jobcashback.getString("coupon_id"));
							cashback.put(Key_coupon_code, jobcashback.getString("coupon_code"));
							cashback.put(Key_coupon_name, jobcashback.getString("coupon_name"));
							cashback.put(Key_points_required, jobcashback.getString("points_required"));
							cashback.put(Key_points_in, jobcashback.getString("points_in"));
							cashback.put(Key_coupon_image, jobcashback.getString("coupon_image"));
							cashback.put(Key_currentpoints, walletAmount);
							//cashback.put(Key_offer_Address, "Address N/A");
							
							redeemData.add(cashback);
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
                		
                		AdapterRedeemCoupans listAdapter = new AdapterRedeemCoupans(RedeemActivity.this, redeemData ,R.layout.row_redeem);
    					redeemGridview.setAdapter(listAdapter);
    					
                	
                	}

                }
            });
            
         }
	}
	
}
