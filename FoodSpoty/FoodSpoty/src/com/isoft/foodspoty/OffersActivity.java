package com.isoft.foodspoty;

import helper.CustomProgressDialog;
import helper.Helper;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.isoftinc.listadapter.AdapterOffers;
import com.isoftinc.listadapter.AdapterProfileConnection;
import com.isoftinc.listadapter.AdapterProfileCoupons;
import com.session.ProfileSessionManager;
import com.webservices.ServiceHandler;
import com.webservices.Serviceurl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class OffersActivity extends Activity{
	 
	public static ListView offersListview;
	public static ProgressDialog pDialog;
	public static ArrayList<HashMap<String, String>> Dailydeallist;
	public static ArrayList<HashMap<String, String>> Discountlist;
	public static ArrayList<HashMap<String, String>> Cashbacklist;
	String RestorentName="";
	public static String Key_offer_id="offer_id";
	public static String Key_offer_code="offer_code";
	public static String Key_offer_discount="offer_discount";
	public static String Key_discount_in="discount_in";
	public static String Key_offer_image="offer_image";
	public static String Key_offer_title="offer_title";
	public static String Key_offer_Address="address";
	public static String key_restname="restorentname";
	
	//tab control
	Button btnDailyDeals, btnDiscounts, btnCashback;
	View lineDailyDeals, lineDiscounts, lineCashback;
	public static String selectedTab = null;
	
	Button btn_slidemenu, btn_menuback, btn_menuSearch, btn_menuFilter;
	TextView headTitle;
	ProfileSessionManager profileSession;
	CustomToast ct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
         ct=new CustomToast(OffersActivity.this);       
        offersListview = (ListView)findViewById(R.id.offersListview);
        profileSession=new ProfileSessionManager(OffersActivity.this);
        btn_slidemenu  = (Button)findViewById(R.id.btn_slidemenu);
        btn_menuback   = (Button)findViewById(R.id.btn_menuback);
        btn_menuSearch = (Button)findViewById(R.id.btn_menuSearch);
        btn_menuFilter = (Button)findViewById(R.id.btn_menuFilter);
        headTitle	   = (TextView)findViewById(R.id.headTitle);
        Helper.SelectedOffertab="Daily";
        btn_slidemenu.setVisibility(View.GONE);
        btn_menuback.setVisibility(View.VISIBLE);
        btn_menuSearch.setVisibility(View.INVISIBLE);
        btn_menuFilter.setVisibility(View.GONE);
        headTitle.setText("Offers");
        
        //tab contril declare here
        btnDailyDeals = (Button)findViewById(R.id.btnDailyDeals);
        btnDiscounts  = (Button)findViewById(R.id.btnDiscounts);
        btnCashback   = (Button)findViewById(R.id.btnCashback);

        lineDailyDeals = (View)findViewById(R.id.lineDailyDeals);
        lineDiscounts  = (View)findViewById(R.id.lineDiscounts);
        lineCashback   = (View)findViewById(R.id.lineCashback);
        
        selectedTab = "btnDailyDeals" ;
        btnDailyDeals.setTextColor(Color.parseColor("#f5952a"));
        btnDiscounts.setTextColor(Color.parseColor("#ffffff"));
        btnCashback.setTextColor(Color.parseColor("#ffffff"));

        lineDailyDeals.setVisibility(View.VISIBLE);
        lineDiscounts.setVisibility(View.INVISIBLE);
        lineCashback.setVisibility(View.INVISIBLE);
        
        if(profileSession.isNetworkAvailable())
    	{
    		new LoadProfiledata().execute();	
    	}
        
        btn_menuback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});

        btnDailyDeals.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        selectedTab = "btnDailyDeals" ;
		        btnDailyDeals.setTextColor(Color.parseColor("#f5952a"));
		        btnDiscounts.setTextColor(Color.parseColor("#ffffff"));
		        btnCashback.setTextColor(Color.parseColor("#ffffff"));

		        lineDailyDeals.setVisibility(View.VISIBLE);
		        lineDiscounts.setVisibility(View.INVISIBLE);
		        lineCashback.setVisibility(View.INVISIBLE);
		        
		        Helper.SelectedOffertab="Daily";
		        if(Dailydeallist!=null&&Dailydeallist.size()>0)
		        {
		        	AdapterOffers listAdapter = new AdapterOffers(OffersActivity.this, Dailydeallist ,R.layout.row_offers);
					offersListview.setAdapter(listAdapter);
		        }
		        else
		        {
		        	if(profileSession.isNetworkAvailable())
		        	{
		        		new LoadProfiledata().execute();	
		        	}
		        	
		        }
			}
		});
        btnDiscounts.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectedTab = "btnDiscounts" ;
		        btnDailyDeals.setTextColor(Color.parseColor("#ffffff"));
		        btnDiscounts.setTextColor(Color.parseColor("#f5952a"));
		        btnCashback.setTextColor(Color.parseColor("#ffffff"));
	
		        lineDailyDeals.setVisibility(View.INVISIBLE);
		        lineDiscounts.setVisibility(View.VISIBLE);
		        lineCashback.setVisibility(View.INVISIBLE);
		        Helper.SelectedOffertab="Discount";
		        if(Discountlist!=null&&Discountlist.size()>0)
		        {
		        	AdapterOffers listAdapter = new AdapterOffers(OffersActivity.this, Discountlist ,R.layout.row_offers);
					offersListview.setAdapter(listAdapter);
		        }
		        else
		        {
		        	if(profileSession.isNetworkAvailable())
		        	{
		        		new LoadProfiledata().execute();	
		        	}
		        }
				
			}
		});
        btnCashback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectedTab = "btnCashback" ;
		        btnDailyDeals.setTextColor(Color.parseColor("#ffffff"));
		        btnDiscounts.setTextColor(Color.parseColor("#ffffff"));
		        btnCashback.setTextColor(Color.parseColor("#f5952a"));
	
		        lineDailyDeals.setVisibility(View.INVISIBLE);
		        lineDiscounts.setVisibility(View.INVISIBLE);
		        lineCashback.setVisibility(View.VISIBLE);
		        Helper.SelectedOffertab="Cashback";
		        if(Cashbacklist!=null&&Cashbacklist.size()>0)
		        {
		        	AdapterOffers listAdapter = new AdapterOffers(OffersActivity.this, Cashbacklist ,R.layout.row_offers);
					offersListview.setAdapter(listAdapter);
		        }
		        else
		        {
		        	if(profileSession.isNetworkAvailable())
		        	{
		        		new LoadProfiledata().execute();	
		        	}
		        }
				
			}
		});
        
        
    }

    
    /*
     * Start server data load for tab selection.
     * 
     */
   /*
     * 
     */
    class LoadProfiledata extends AsyncTask<String, String, String> {
    	CustomProgressDialog pDialog;
    	String webTotalCoupan="0",webTotalConnection="0";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new CustomProgressDialog(OffersActivity.this);
			
			pDialog.show();

		}

		protected String doInBackground(String... args) {
			String responseStatus = null;
			Cashbacklist = new ArrayList<HashMap<String, String>>();
			Dailydeallist = new ArrayList<HashMap<String, String>>();
			Discountlist = new ArrayList<HashMap<String, String>>();
		
			
			
			try {
				ServiceHandler sh = new ServiceHandler();
				String struserpicurl=null;
				/*	try {
    				struserpicurl = URLEncoder.encode(struserpic.toString(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
    		
					String loginUrl = Url.GetUrl(Serviceurl.get_offer + "user_id=" + profileSession.GetSharedPreferences(ProfileSessionManager.KEY_ID));
					
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
							JSONArray jarrdiscount=jobdata.getJSONArray("discount");
							JSONArray jarrdailyDeals=jobdata.getJSONArray("dailyDeals");
							
							for(int i1=0;i1<jarrcashBack.length();i1++)
							{
							JSONObject jobcashback=jarrcashBack.getJSONObject(i1);
							HashMap<String , String>cashback=new HashMap<String, String>();
							cashback.put(Key_offer_id, jobcashback.getString("offer_id"));
							cashback.put(Key_offer_code, jobcashback.getString("offer_code"));
							cashback.put(Key_offer_discount, jobcashback.getString("offer_discount"));
							cashback.put(Key_discount_in, jobcashback.getString("discount_in"));
							cashback.put(Key_offer_image, jobcashback.getString("offer_image"));
							cashback.put(Key_offer_title, jobcashback.getString("offer_title"));
							cashback.put(Key_offer_Address, "Address N/A");
							JSONArray restaurantDetails=jobcashback.getJSONArray("restaurantDetails");
							for(int ires=0;ires<restaurantDetails.length();ires++)
							{
								JSONObject jobrest=restaurantDetails.getJSONObject(ires);
								RestorentName=jobrest.getString("restaurant_name");
							}
							cashback.put(key_restname, RestorentName);
							
							Cashbacklist.add(cashback);
							
							}
							for(int id=0;id<jarrdiscount.length();id++)
							{
							JSONObject jobdiscount=jarrdiscount.getJSONObject(id);
							HashMap<String , String>cashback=new HashMap<String, String>();
							cashback.put(Key_offer_id, jobdiscount.getString("offer_id"));
							cashback.put(Key_offer_code, jobdiscount.getString("offer_code"));
							cashback.put(Key_offer_discount, jobdiscount.getString("offer_discount"));
							cashback.put(Key_discount_in, jobdiscount.getString("discount_in"));
							cashback.put(Key_offer_image, jobdiscount.getString("offer_image"));
							cashback.put(Key_offer_title, jobdiscount.getString("offer_title"));
							cashback.put(Key_offer_Address, "Address N/A");
							JSONArray restaurantDetails=jobdiscount.getJSONArray("restaurantDetails");
							for(int ires=0;ires<restaurantDetails.length();ires++)
							{
								JSONObject jobrest=restaurantDetails.getJSONObject(ires);
								RestorentName=jobrest.getString("restaurant_name");
							}
							cashback.put(key_restname, RestorentName);
							
							
							Discountlist.add(cashback);
							}
							
							for(int idd=0;idd<jarrdailyDeals.length();idd++)
							{
							JSONObject jobdailyDeals=jarrdailyDeals.getJSONObject(idd);
							HashMap<String , String>cashback=new HashMap<String, String>();
							cashback.put(Key_offer_id, jobdailyDeals.getString("offer_id"));
							cashback.put(Key_offer_code, jobdailyDeals.getString("offer_code"));
							cashback.put(Key_offer_discount, jobdailyDeals.getString("offer_discount"));
							cashback.put(Key_discount_in, jobdailyDeals.getString("discount_in"));
							cashback.put(Key_offer_image, jobdailyDeals.getString("offer_image"));
							cashback.put(Key_offer_title, jobdailyDeals.getString("offer_title"));
							cashback.put(Key_offer_Address, "Address N/A");
							if(jobdailyDeals.has("restaurantDetails"))
							{
								JSONArray restaurantDetails=jobdailyDeals.getJSONArray("restaurantDetails");
								for(int ires=0;ires<restaurantDetails.length();ires++)
								{
									JSONObject jobrest=restaurantDetails.getJSONObject(ires);
									RestorentName=jobrest.getString("restaurant_name");
								}
								cashback.put(key_restname, RestorentName);
							}
							
						
							Dailydeallist.add(cashback);
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
                		
                		if( Helper.SelectedOffertab.equals("Daily"))
                		{
                			AdapterOffers listAdapter = new AdapterOffers(OffersActivity.this, Dailydeallist ,R.layout.row_offers);
        					offersListview.setAdapter(listAdapter);
                    	
                		}
                		else if( Helper.SelectedOffertab.equals("Discount"))
                		{
                			AdapterOffers listAdapter = new AdapterOffers(OffersActivity.this, Discountlist ,R.layout.row_offers);
        					offersListview.setAdapter(listAdapter);
                    	
                		}
                		else if( Helper.SelectedOffertab.equals("Cashback"))
                		{
                			AdapterOffers listAdapter = new AdapterOffers(OffersActivity.this, Cashbacklist ,R.layout.row_offers);
        					offersListview.setAdapter(listAdapter);
                    	
                		}
                	
                	}

                }
            });
            
         }
	}
	
 
}
