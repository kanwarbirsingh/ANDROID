package com.isoft.foodspoty;

import helper.CustomProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.helper.adapters.ImageLoaderisoft;
import com.isoftinc.listadapter.AdapterProfileConnection;
import com.isoftinc.listadapter.AdapterProfileCoupons;
import com.session.ProfileSessionManager;
import com.webservices.ServiceHandler;
import com.webservices.Serviceurl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProfileActivity extends Activity{
	 
	Button btnBack;
	public static ListView connectedListview;
	public static GridView couponGridview;
	
	RelativeLayout btnTabCoupons, btnTabConnection;
	View tabConnectionFooter, tabCouponsFooter;
	public static TextView txtuserName,txtuserTotalpoint,txtConnection,txtCoupons;
	String selectedTab;
	public static ImageView userPic;
	ProfileSessionManager profileSession;
    private ProgressDialog pDialog;
    
    public static ArrayList<HashMap<String, String>> profileData;
	public static  ArrayList<HashMap<String, String>> connectionsList;
	public static ArrayList<HashMap<String, String>> coupansList;
	
	public static String Key_Cuser_id="user_id";
	public static String Key_Cuser_name="user_name";
	public static String Key_Cuser_email="user_email";
	public static String Key_Cmobile="mobile";
	public static String Key_Cpassword="password";
	public static String Key_Cuser_pic="user_pic";
	public static String Key_Cuser_longitude="user_longitude";
	public static String Key_Cuser_latitude="user_latitude";
	public static String Key_Ctotal_point="total_point";
	public static String Key_Cconnection_status="connection_status";
	
	
	public static String Key_Rdcoupon_id="rdcoupon_id";
	public static String Key_Rdrestaurant_id="rdrestaurant_id";
	public static String Key_Rdcoupon_code="rcoupon_code";
	public static String Key_Rdcoupon_name="rdcoupon_name";
	public static String Key_Rdcoupon_image="rdcoupon_image";
	public static String Key_Rdpoints_required="rdpoints_required";
	public static String Key_Rdpoints_in="rdpoints_in";
	public static String Key_Rdpoints_taken="rdpoints_taken";
	public static String Key_Rddelivery_status="rddelivery_status";
	ImageLoaderisoft imageloader;
	CustomToast ct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageloader=new ImageLoaderisoft(ProfileActivity.this);
        ct=new CustomToast(ProfileActivity.this);
        btnBack = (Button)findViewById(R.id.btnBack);
        profileSession=new ProfileSessionManager(ProfileActivity.this);
        connectedListview = (ListView)findViewById(R.id.connectedListview);
        couponGridview	  = (GridView)findViewById(R.id.couponGridview);
        
        btnTabConnection  = (RelativeLayout)findViewById(R.id.btnTabConnection);
        btnTabCoupons     = (RelativeLayout)findViewById(R.id.btnTabCoupons);

        tabConnectionFooter = (View)findViewById(R.id.tabConnectionFooter);
        tabCouponsFooter    = (View)findViewById(R.id.tabCouponsFooter);
        
        txtConnection		= (TextView)findViewById(R.id.txtConnection);
        txtCoupons			= (TextView)findViewById(R.id.txtCoupons);
        txtuserName			= (TextView)findViewById(R.id.userName);
        txtuserTotalpoint	= (TextView)findViewById(R.id.userTotalpoint);
        userPic				= (ImageView)findViewById(R.id.userPic);
        
        selectedTab = "connections" ;
        tabConnectionFooter.setVisibility(View.VISIBLE);
        tabCouponsFooter.setVisibility(View.INVISIBLE);
        connectedListview.setVisibility(View.VISIBLE);
        couponGridview.setVisibility(View.INVISIBLE);
        new LoadProfiledata().execute(selectedTab);
        
        
        btnTabConnection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        selectedTab = "connections" ;
		        tabConnectionFooter.setVisibility(View.VISIBLE);
		        tabCouponsFooter.setVisibility(View.INVISIBLE);
		        connectedListview.setVisibility(View.VISIBLE);
		        couponGridview.setVisibility(View.INVISIBLE);
		        //new LoadProfiledata().execute(selectedTab);
			}
		});
        
        btnTabCoupons.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        selectedTab = "coupons" ;
		        tabConnectionFooter.setVisibility(View.INVISIBLE);
		        tabCouponsFooter.setVisibility(View.VISIBLE);
		        connectedListview.setVisibility(View.INVISIBLE);
		        couponGridview.setVisibility(View.VISIBLE);
		       // new LoadData().execute(selectedTab);
			}
		}); 
        
        btnBack.setOnClickListener(new OnClickListener() {
			
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
    	String webUserId,webUserName,webUserEmail,webMobile,webuserPic,webUserLatitude,webUserLongitude,webTotalPoint,webPassword="";
    	String webTotalCoupan="0",webTotalConnection="0";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new CustomProgressDialog(ProfileActivity.this);
			
			pDialog.show();

		}

		protected String doInBackground(String... args) {
			String responseStatus = null;
			connectionsList = new ArrayList<HashMap<String, String>>();
			coupansList = new ArrayList<HashMap<String, String>>();
			
			
			try {
				ServiceHandler sh = new ServiceHandler();
				String struserpicurl=null;
				/*	try {
    				struserpicurl = URLEncoder.encode(struserpic.toString(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
    		
					String loginUrl = Url.GetUrl(Serviceurl.get_profile + "user_id=" + profileSession.GetSharedPreferences(ProfileSessionManager.KEY_ID));
					
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
						JSONArray jarrUserInfo=response_obj.getJSONArray("userInfo");
						JSONArray jarrconnection=response_obj.getJSONArray("connection");
						JSONArray jarrRedeemCoupon=response_obj.getJSONArray("RedeemCoupon");
						webTotalConnection=String.valueOf(jarrconnection.length());
						webTotalCoupan=String.valueOf(jarrRedeemCoupon.length());
						for(int i=0;i<jarrUserInfo.length();i++)
						{
						JSONObject jobjdata=jarrUserInfo.getJSONObject(i);
						webUserId=jobjdata.getString("user_id");
						webUserName=jobjdata.getString("user_name");
						webUserEmail=jobjdata.getString("user_email");
						webMobile=jobjdata.getString("mobile");
						webuserPic=jobjdata.getString("user_pic");
						webUserLongitude=jobjdata.getString("user_longitude");
						webUserLatitude=jobjdata.getString("user_latitude");
						webTotalPoint=jobjdata.getString("total_point");
						webPassword=jobjdata.getString("password");
						profileSession.SetSharedPreferences(ProfileSessionManager.KEY_ID, webUserId);
						profileSession.SetSharedPreferences(ProfileSessionManager.KEY_USERNAME, webUserName);
						profileSession.SetSharedPreferences(ProfileSessionManager.KEY_EMAIL, webUserEmail);
						profileSession.SetSharedPreferences(ProfileSessionManager.KEY_MOBILE, webMobile);
						profileSession.SetSharedPreferences(ProfileSessionManager.KEY_UserPic, webuserPic);
						profileSession.SetSharedPreferences(ProfileSessionManager.KEY_userlatitude, webUserLatitude);
						profileSession.SetSharedPreferences(ProfileSessionManager.KEY_userlongitude, webUserLongitude);
						profileSession.SetSharedPreferences(ProfileSessionManager.KEY_userTotalPoint, webTotalPoint);
						profileSession.SetSharedPreferences(ProfileSessionManager.KEY_Password, webPassword);
						}
						for(int i=0;i<jarrconnection.length();i++)
						{
						JSONObject jobjcon=jarrconnection.getJSONObject(i);
						HashMap<String , String>conmap=new HashMap<String, String>();
						conmap.put(Key_Cuser_id, jobjcon.getString("user_id"));
						conmap.put(Key_Cuser_name, jobjcon.getString("user_name"));
						conmap.put(Key_Cuser_email, jobjcon.getString("user_email"));
						conmap.put(Key_Cmobile, jobjcon.getString("mobile"));
						conmap.put(Key_Cpassword, jobjcon.getString("password"));
						conmap.put(Key_Cuser_pic, jobjcon.getString("user_pic"));
						conmap.put(Key_Cuser_longitude, jobjcon.getString("user_longitude"));
						conmap.put(Key_Cuser_latitude, jobjcon.getString("user_latitude"));
						conmap.put(Key_Ctotal_point, jobjcon.getString("total_point"));
						conmap.put(Key_Cuser_latitude, jobjcon.getString("user_latitude"));
						conmap.put(Key_Cconnection_status, jobjcon.getString("connection_status"));
						connectionsList.add(conmap);
						}
						
						for(int i=0;i<jarrRedeemCoupon.length();i++)
						{
						JSONObject jobjcoupan=jarrRedeemCoupon.getJSONObject(i);
						HashMap<String , String>coupmap=new HashMap<String, String>();
						if(jobjcoupan.has("coupon_id"))
						{
							coupmap.put(Key_Rdcoupon_id, jobjcoupan.getString("coupon_id"));
							coupmap.put(Key_Rdrestaurant_id, jobjcoupan.getString("restaurant_id"));
							coupmap.put(Key_Rdcoupon_code, jobjcoupan.getString("coupon_code"));
							coupmap.put(Key_Rdcoupon_name, jobjcoupan.getString("coupon_name"));
							coupmap.put(Key_Rdcoupon_image, jobjcoupan.getString("coupon_image"));
							coupmap.put(Key_Rdpoints_required, jobjcoupan.getString("points_required"));
							coupmap.put(Key_Rdpoints_in, jobjcoupan.getString("points_in"));
							coupmap.put(Key_Rdpoints_taken, jobjcoupan.getString("points_taken"));
							coupmap.put(Key_Rddelivery_status, jobjcoupan.getString("delivery_status"));
						
						}
						else
						{
							coupmap.put(Key_Rdcoupon_id, "");
							coupmap.put(Key_Rdrestaurant_id, "");
							coupmap.put(Key_Rdcoupon_code, "Delivered");
							coupmap.put(Key_Rdcoupon_name, "Delivered");
							coupmap.put(Key_Rdcoupon_image, "");
							coupmap.put(Key_Rdpoints_required, "");
							coupmap.put(Key_Rdpoints_in, "");
							coupmap.put(Key_Rdpoints_taken, jobjcoupan.getString("points_taken"));
							coupmap.put(Key_Rddelivery_status, jobjcoupan.getString("delivery_status"));
						
						}
						
								coupansList.add(coupmap);
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
                			AdapterProfileConnection listAdapter = new AdapterProfileConnection(ProfileActivity.this, connectionsList ,R.layout.row_profile_connection);
    						connectedListview.setAdapter(listAdapter);
    						AdapterProfileCoupons listAdaptergrid = new AdapterProfileCoupons(ProfileActivity.this, coupansList ,R.layout.row_profile_coupons);
    						couponGridview.setAdapter(listAdaptergrid);
    						txtuserName.setText(webUserName);
    						txtuserTotalpoint.setText("TOTAL POINTS: "+webTotalPoint);
    						imageloader.DisplayImage(webuserPic, userPic, R.drawable.ic_user_pic);
    						txtConnection.setText(webTotalConnection);
    						txtCoupons.setText(webTotalCoupan);
                	
                	}

                }
            });
            
         }
	}
	
  
    /*
     * 
     */
    
}
