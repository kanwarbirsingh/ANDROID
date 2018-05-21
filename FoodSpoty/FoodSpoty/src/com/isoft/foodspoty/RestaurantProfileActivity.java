package com.isoft.foodspoty;

import helper.CustomProgressDialog;
import helper.Helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.helper.adapters.ImageLoaderisoft;
import com.isoftinc.listadapter.AdapterProfileCoupons;
import com.isoftinc.listadapter.AdapterRestCoupons;
import com.isoftinc.listadapter.FragmentHomeAdapter;
import com.session.ProfileSessionManager;
import com.webservices.DatabaseHandlerFav;
import com.webservices.ServiceHandler;
import com.webservices.Serviceurl;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RestaurantProfileActivity extends FragmentActivity
implements
OnMapClickListener,
OnMapLongClickListener,
OnMarkerDragListener{
	 
	Button btnBack;
	//ListView connectedListview;
	public static GridView couponGridview;
	RelativeLayout detailsLayout;
	RelativeLayout btnTabCoupons, btnTabDetails;
	View tabDetailsFooter, tabCouponsFooter;
	 String strCurrent_latitude;  
		String strCurrent_longitude; 
		GPSTracker gps;
		Polyline lineOption=null;
		private GoogleMap map;
	String selectedTab;
	Timer timer; 
	CameraPosition cameraPosition;
	Location location;
	TextView restAddress;
	double CurrentLat=0.0;
	double CurrentLong=0.0;
	String Current_latitude;  
	String Current_longitude; 
	MarkerOptions options;
	MarkerOptions optionsRed;
	Marker kiel1;
	Marker kielred;
	ArrayList<LatLng> markerPoints;
	LatLng p;
	ProfileSessionManager profsession;
	public static String restlat="",restlong="",restid="",restname="",strrestAddress="";
	double dblrestlat=0,dblrestlong=0;
	LatLng dest,origin;
	public static   CustomProgressDialog pDialog;
    public static  ArrayList<HashMap<String, String>> profileData;
	public static String webrestaurant_id="",webrestaurant_name="",webrestaurant_address="",webrestaurant_longitude="",webrestaurant_latitude="",webrestaurant_mobile1="",webrestaurant_contact_mail="",webrestaurant_profile_image="", webDistance="",webrestaurant_closetime="",webrestaurant_opentime="",webrestaurant_rating="";
	public static String Key_Rdcoupon_id="rcoupon_id";
	public static String Key_Rdcoupon_code="rcoupon_code";
	public static String Key_Rdcoupon_name="rcoupon_name";
	public static String Key_Rdcoupon_image="rcoupon_image";
	public static String Key_Rdpoints_required="rpoints_required";
	public static String Key_Rdpoints_in="rpoints_in";
	public static String Key_Rdcoupon_status="rcoupon_status";
	public static String Key_Rdcoupon_start_date="rcoupon_start_date";
	public static String Key_Rdcoupon_expiry_date="rcoupon_expiry_date";
	public static String Key_RdRestName="restname";
	CustomToast ct;
	ImageView imgrestPic;
	TextView txtrestName;
	TextView txtrestTiming;
	Button btnRate;
	ImageLoaderisoft imageloader;
	DatabaseHandlerFav dbfav;
	Button btnLike;
	Button btngetroute;
	Marker current=null;
	Marker kiel2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_profile);
        profsession=new ProfileSessionManager(RestaurantProfileActivity.this);
        ct=new CustomToast(RestaurantProfileActivity.this);
        dbfav=new DatabaseHandlerFav(this);
        btnBack = (Button)findViewById(R.id.btnBack);
        imageloader=new ImageLoaderisoft(RestaurantProfileActivity.this);
        detailsLayout 	  = (RelativeLayout)findViewById(R.id.detailsLayout);
        couponGridview	  = (GridView)findViewById(R.id.couponGridview);
        restAddress			=(TextView)findViewById(R.id.restAddress);
        txtrestName			=(TextView)findViewById(R.id.restName);
        txtrestTiming			=(TextView)findViewById(R.id.restTiming);
        btnLike				=(Button)findViewById(R.id.btnLike);
        imgrestPic			=(ImageView)findViewById(R.id.restPic);
        btnRate				=(Button)findViewById(R.id.btnRate);
        btnTabDetails  	  = (RelativeLayout)findViewById(R.id.btnTabDetails);
        btnTabCoupons     = (RelativeLayout)findViewById(R.id.btnTabCoupons);
        btngetroute		 = (Button)findViewById(R.id.btngetroute);
        tabDetailsFooter = (View)findViewById(R.id.tabDetailsFooter);
        tabCouponsFooter    = (View)findViewById(R.id.tabCouponsFooter);
        options = new MarkerOptions();
        optionsRed = new MarkerOptions();
       
        markerPoints = new ArrayList<LatLng>();
        selectedTab = "connections" ;
        tabDetailsFooter.setVisibility(View.VISIBLE);
        tabCouponsFooter.setVisibility(View.INVISIBLE);
        detailsLayout.setVisibility(View.VISIBLE);
        couponGridview.setVisibility(View.INVISIBLE);
        
        Intent oIntent = getIntent();
		 
		 try{
			  restname = oIntent.getExtras().getString("restname");
			  restid = oIntent.getExtras().getString("restid");
			  restlat = oIntent.getExtras().getString("restlat");
			  restlong = oIntent.getExtras().getString("restlong");
			  strrestAddress=oIntent.getExtras().getString("restAddress");
			  profsession.SetSharedPreferences(ProfileSessionManager.KEY_SelectedRestorent, restid);
			  restAddress.setText(strrestAddress);
			 if(dbfav.CheckExistItemOrNot(restid)==1)
			 {
				 btnLike.setBackgroundResource(R.drawable.ic_like50green );
			 }
			 else
			 {
				 btnLike.setBackgroundResource(R.drawable.ic_like50);
			 }
		 }catch(Exception ev){
			 
		 }
		 btngetroute.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getLocation();
			}
		});
        btnLike.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			if(dbfav.CheckExistItemOrNot(restid)==0)
			{
				dbfav.AddFilter(restid, restname, restlat, restlong);
				btnLike.setBackgroundResource(R.drawable.ic_like50green);
			}
			else
			{
			dbfav.DeleteOneService(restid);
			btnLike.setBackgroundResource(R.drawable.ic_like50);
			}
			}
		});
        new LoadHomeData().execute(selectedTab);
        
        
        btnTabDetails.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        selectedTab = "connections" ;
		        tabDetailsFooter.setVisibility(View.VISIBLE);
		        tabCouponsFooter.setVisibility(View.INVISIBLE);
		        detailsLayout.setVisibility(View.VISIBLE);
		        couponGridview.setVisibility(View.INVISIBLE);
		        //new LoadData().execute(selectedTab);
			}
		});
        
        btnTabCoupons.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        selectedTab = "coupons" ;
		        tabDetailsFooter.setVisibility(View.INVISIBLE);
		        tabCouponsFooter.setVisibility(View.VISIBLE);
		        detailsLayout.setVisibility(View.INVISIBLE);
		        couponGridview.setVisibility(View.VISIBLE);
		      //  new LoadData().execute(selectedTab);
			}
		}); 
        
        btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        
        
        pDialog = new CustomProgressDialog(RestaurantProfileActivity.this);
		pDialog.setMessage("Update your location please wait ...");
		pDialog.setCancelable(false);
		pDialog.show();
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1);
		map = fm.getMap();
		//map.setOnMapClickListener(this);
		//map.setOnMapLongClickListener(this);
		//map.setOnMarkerDragListener(this);

		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(true);

		// Enable / Disable my location button
	//	map.getUiSettings().setMyLocationButtonEnabled(true);

		// Enable / Disable Compass icon
		map.getUiSettings().setCompassEnabled(true);

		// Enable / Disable Rotate gesture
		map.getUiSettings().setRotateGesturesEnabled(true);

		// Enable / Disable zooming functionality
		map.getUiSettings().setZoomGesturesEnabled(true);

		LocationManager locationManager = (LocationManager) this
				.getSystemService(LOCATION_SERVICE);
		/*location = locationManager    
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);*/
	
	/*	if (locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER)
				&& locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
		*/	 
			 Bundle extras = getIntent().getExtras();
			 
		        if(extras != null){
		            String data1 = extras.getString("data1");
		          //  String data2 = extras.getString("data2");
		            System.out.println("Ddata1 : " + data1);
		            map.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
						
						@Override
						public boolean onMyLocationButtonClick() {
							// TODO Auto-generated method stub
							getLocation();
							
							return false;
						}
					});
		          
		        }
			
			if (profsession.isNetworkAvailable()) {
				LocationManager locationManager1 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				Location location1 = locationManager1    
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				  location=location1;
				  
				  cameraPosition = new CameraPosition.Builder().target(new LatLng(28.613939, 77.209021)).zoom(5).build();
				
					map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
					getLocation();
		
			} else {
				customalert("Check you Internet Connection");
			}
		/*}
		else {
			customalertgps("GPS is Disable in your\n device Would you like\n to Enable");

		}*/

	
        
        
        
        
    }

    
    /*
     * Start server data load for tab selection.
     * 
     */
    class LoadData extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new CustomProgressDialog(RestaurantProfileActivity.this);
			pDialog.setMessage("Loading. Please wait ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			//pDialog.show();
		}

		protected String doInBackground(String... args) {

			try {
					profileData = new ArrayList<HashMap<String, String>>();
					
				if(args[0].equalsIgnoreCase("connections")){
					
					for (int i = 0; i < 10; i++) {
	
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("USER_ID", ""+i);
						map.put("USER_PIC", "");
						map.put("USER_NAME", "Gary White #"+i);
						map.put("EARNED_POINT", "Total Points: 1000"+i);
	
						profileData.add(map);
					}
				}else{
					for (int i = 0; i < 10; i++) {
						
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("OFFER_ID", ""+i);
						map.put("OFFER_PIC", "");
						map.put("OFFER_NAME", ""+i+"30% Off");
	
						profileData.add(map);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {

			runOnUiThread(new Runnable() {
				public void run() {
					
					if(selectedTab != null && selectedTab.equalsIgnoreCase("connections")){
						//AdapterProfileConnection listAdapter = new AdapterProfileConnection(RestaurantProfileActivity.this, profileData ,R.layout.row_profile_connection);
						//connectedListview.setAdapter(listAdapter);
					}else{
						AdapterProfileCoupons listAdapter = new AdapterProfileCoupons(RestaurantProfileActivity.this, profileData ,R.layout.row_profile_coupons);
						couponGridview.setAdapter(listAdapter);
					}
					if(pDialog.isShowing())
					{
						pDialog.dismiss();
						
					}
				}
			});
		}
		
	}
    class LoadHomeData extends AsyncTask<String, String, String> {
    	CustomProgressDialog pDialog;
    	String webTotalCoupan="0",webTotalConnection="0";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new CustomProgressDialog(RestaurantProfileActivity.this);
			
			pDialog.show();

		}

		protected String doInBackground(String... args) {
			String responseStatus = null;
			profileData = new ArrayList<HashMap<String, String>>();
			
			try {
				ServiceHandler sh = new ServiceHandler();
				String struserpicurl=null;
				/*	try {
    				struserpicurl = URLEncoder.encode(struserpic.toString(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
    		
					String loginUrl = Url.GetUrl(Serviceurl.restaurant_detail+"restaurant_id="+restid);
					
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
						String rescoupan=response_obj.getString("ResCoupon");
						
						JSONArray jarrResCoupon=new JSONArray(rescoupan);
						//webTotalConnection=String.valueOf(jarrdata.length());
						
						for(int i=0;i<jarrdata.length();i++)
						{
							
						JSONObject jobjdata=jarrdata.getJSONObject(i);
						webrestaurant_id=jobjdata.getString("restaurant_id");
						webrestaurant_name=jobjdata.getString("restaurant_name");
						webrestaurant_address=jobjdata.getString("restaurant_address");
						webrestaurant_longitude=jobjdata.getString("restaurant_longitude");
						webrestaurant_latitude=jobjdata.getString("restaurant_latitude");
						restlong=jobjdata.getString("restaurant_longitude");
						restlat=jobjdata.getString("restaurant_latitude");
						webrestaurant_opentime=jobjdata.getString("restaurant_opentime");
						webrestaurant_closetime=jobjdata.getString("restaurant_closetime");
						webrestaurant_rating=jobjdata.getString("restaurant_rating");
						
						webrestaurant_mobile1=jobjdata.getString("restaurant_mobile1");
						webrestaurant_contact_mail=jobjdata.getString("restaurant_contact_mail");
						webrestaurant_profile_image=jobjdata.getString("restaurant_cover_image");
						webrestaurant_id=jobjdata.getString("restaurant_id");
						
						
						
						
						}
						for(int i=0;i<jarrResCoupon.length();i++)
						{
							
						JSONObject jobjResCoupan=jarrResCoupon.getJSONObject(i);
					
						HashMap<String , String>conmap=new HashMap<String, String>();
						conmap.put(Key_Rdcoupon_id, jobjResCoupan.getString("coupon_id"));
						conmap.put(Key_Rdcoupon_code, jobjResCoupan.getString("coupon_code"));
						conmap.put(Key_Rdcoupon_name, jobjResCoupan.getString("coupon_name"));
						conmap.put(Key_Rdcoupon_image, jobjResCoupan.getString("coupon_image"));
						conmap.put(Key_Rdpoints_required, jobjResCoupan.getString("points_required"));
						conmap.put(Key_Rdpoints_in, jobjResCoupan.getString("points_in"));
						conmap.put(Key_Rdcoupon_status, jobjResCoupan.getString("coupon_status"));
						conmap.put(Key_Rdcoupon_start_date, jobjResCoupan.getString("coupon_start_date"));
						conmap.put(Key_Rdcoupon_expiry_date, jobjResCoupan.getString("coupon_expiry_date"));
						conmap.put(Key_RdRestName, restname);
						profileData.add(conmap);
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
                		
                		
                			AdapterRestCoupons listAdapter = new AdapterRestCoupons(RestaurantProfileActivity.this, profileData ,R.layout.row_profile_coupons);
    						couponGridview.setAdapter(listAdapter);
    					txtrestName.setText(webrestaurant_name);
						btnRate.setText(webrestaurant_rating);
						txtrestTiming.setText(webrestaurant_opentime+" To "+webrestaurant_closetime);
						imageloader.DisplayImage(webrestaurant_profile_image, imgrestPic, R.drawable.temp_user);
						getLocation();
                	
                	}
                	else {
			   			ct.showToastError("Limited Internet Connectivity");
			   			
                	}

         }
	}
	    
    /*
     * 
     */
    protected void customalertgps(String Msg)
    {
    	
    	final Dialog dialog = new Dialog(RestaurantProfileActivity.this);
    	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	
    	dialog.setContentView(R.layout.custom_alert_gps);
    	TextView text = (TextView) dialog.findViewById(R.id.txtMsg);
    	text.setText(Msg);

    	Button dialogbtnok = (Button) dialog.findViewById(R.id.btnok);
    	Button dialogbtncancel = (Button) dialog.findViewById(R.id.btncancel);
    	dialogbtnok.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			
    			Intent gpsOptionsIntent = new Intent(
    					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    			gpsOptionsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    			RestaurantProfileActivity.this.startActivity(gpsOptionsIntent);
    			dialog.dismiss();
    		}
    	});
    	dialogbtncancel.setOnClickListener(new OnClickListener() {
    		
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			
    			dialog.dismiss();
    			
    			
    			
    		}
    	});
    	dialog.show();  
    }		
	protected void customalert(String Msg)
	{
		
		final Dialog dialog = new Dialog(RestaurantProfileActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		dialog.setContentView(R.layout.custom_alertmsg1);
		TextView text = (TextView) dialog.findViewById(R.id.AlertMsgTextView);
		text.setText(Msg);

		Button dialogButton = (Button) dialog.findViewById(R.id.AlertMsgOk);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				dialog.dismiss();
			}
		});
		   

		dialog.show();
	}
	

public void getLocation()
{
     
	if(pDialog.isShowing())
	{
		pDialog.dismiss();
	}
    // create class object
    gps = new GPSTracker(RestaurantProfileActivity.this);

    // check if GPS enabled     
    if(gps.canGetLocation()){
     
       
	    
    	 double latitude = gps.getLatitude();
         double longitude = gps.getLongitude();
	     if(restlat!=null&& !restlat.equals("") && !restlat.equals("null")       && restlong!=null&& !restlong.equals("") && !restlong.equals("null"))
			{
				  
	    	
	          dblrestlat = Double.parseDouble(restlat);
	          dblrestlong = Double.parseDouble(restlong);
	 	     CurrentLat=latitude;
	 	     CurrentLong=longitude;
		    	  
		    	  
	       
	             
				
					
				    dest=new LatLng(dblrestlat,dblrestlong);
				    optionsRed.position(dest);
				    optionsRed.title("Restaurent Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        			
				    /***********add marker*********/	
        			markerPoints.add(dest);
				    
			        
			         
			      

			        
		          
		                 origin = new LatLng(CurrentLat, CurrentLong);
		                 options.position(origin);
		                 options.title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		                 markerPoints.add(origin);
		                 CameraUpdate    cameraUpdate = CameraUpdateFactory.newLatLngZoom((origin), 17);
		                 map.animateCamera(cameraUpdate);

		               /* if(origin==null){
		            		
		        			options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		        			map.addMarker(options);
		        			
		        		}*/

		        				if(current!=null)
		        				{
		        					
		        				}
	             
		        				current=map.addMarker(options);
		        				if(kiel1!=null)
		        				{
		        					current.remove();	
		        				}
	        					kiel1=map.addMarker(optionsRed);	
		        				
				    			
		        			     cameraUpdate = CameraUpdateFactory.newLatLngZoom((origin), 15);
		        			     map.moveCamera(CameraUpdateFactory.newLatLng(origin)); 
		        				   map.animateCamera(cameraUpdate);
	             
	             
		        				   if(origin!=null && dest!=null){
		        						// Getting URL to the Google Directions API
		        						String url = getDirectionsUrl(dest,origin);				
		        						
		        						DownloadTask downloadTask = new DownloadTask();
		        						
		        						// Start downloading json data from Google Directions API
		        						downloadTask.execute(url);
		        				}
		        					   
	             
	             
	         
			}     
	     
	     
	     
	     
	     
	     
	     
	     
	     
	     
	     
	     
        //-----------
/*if(latitude!=0.0 && dblrestlat!=0.0 &&dblrestlat!=0)
{
if(pDialog.isShowing())
{
	pDialog.dismiss();
}

	Current_latitude=String.valueOf(latitude);
	Current_longitude=String.valueOf(longitude);
	
	strCurrent_latitude=String.valueOf(latitude);
	strCurrent_longitude=String.valueOf(longitude);
	
	
		Log.d("old", "lat :  " + latitude);
		Log.d("old", "long :  " + longitude);
		System.out.println("shivram good location  "+latitude+" longi "+longitude);
		// Enable / Disable zooming controls
		map.getUiSettings().setZoomControlsEnabled(true);

		// Enable / Disable my location button
	//	map.getUiSettings().setMyLocationButtonEnabled(true);

		// Enable / Disable Compass icon
		map.getUiSettings().setCompassEnabled(true);

		// Enable / Disable Rotate gesture
		map.getUiSettings().setRotateGesturesEnabled(true);

		// Enable / Disable zooming functionality
		map.getUiSettings().setZoomGesturesEnabled(true);

		cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(latitude, longitude)).zoom(25).build();

		map.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));

		markerPoints = new ArrayList<LatLng>();
		//----------SHIVRAM----
		final Geocoder geocoder = new Geocoder(RestaurantProfileActivity.this);
		Address startAddress = null;

		String LocationName = "";
		
		try {
			startAddress = geocoder.getFromLocation(latitude,
					longitude, 1).get(0);

			
			LocationName = StringAddress.asString(startAddress);

		} catch (Exception e) {

		
		}

		
		if(kiel1!=null)
		{
			kiel1.remove();
		}

	
		kiel1 = map
				.addMarker(options
						.position(new LatLng(latitude, longitude))
						.title("Current Location")
						.snippet(LocationName)
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.markerred)));
		restAddress.setText(LocationName);
		cameraPosition = new CameraPosition.Builder()
		.target(new LatLng(latitude, longitude)).zoom(10).build();

		map.animateCamera(CameraUpdateFactory
		.newCameraPosition(cameraPosition));

	
}
else
{
//getLocation();
	customalert("Location Not Available");

}
        */
        
        //-------------
        
        
        
        // \n is for new line
     //   Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show(); 
    }else{
        // can't get location
        // GPS or Network is not enabled
        // Ask user to enable GPS/network in settings
        gps.showSettingsAlert();
    }
 

}


// Fetches data from url passed
class DownloadTask extends AsyncTask<String, Void, String>{			
			
	// Downloading data in non-ui thread
	@Override
	protected String doInBackground(String... url) {
			
		// For storing data from web service
		String data = "";
				
		try{
			// Fetching the data from web service
			data = downloadUrl(url[0]);
		}catch(Exception e){
			Log.d("Background Task",e.toString());
		}
		return data;		
	}
	
	// Executes in UI thread, after the execution of
	// doInBackground()
	@Override
	protected void onPostExecute(String result) {			
		super.onPostExecute(result);			
		
		ParserTask parserTask = new ParserTask();
		
		// Invokes the thread for parsing the JSON data
		parserTask.execute(result);
			
	}		
}

/** A class to parse the Google Places in JSON format */
class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
	
	// Parsing the data in non-ui thread    	
	@Override
	protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
		
		JSONObject jObject;	
		List<List<HashMap<String, String>>> routes = null;			           
        
        try{
        	jObject = new JSONObject(jsonData[0]);
        	DirectionsJSONParser parser = new DirectionsJSONParser();
        	
        	// Starts parsing data
        	routes = parser.parse(jObject);    
        }catch(Exception e){
        	e.printStackTrace();
        }
        return routes;
	}
	
	// Executes in UI thread, after the parsing process
	@Override
	protected void onPostExecute(List<List<HashMap<String, String>>> result) {
		ArrayList<LatLng> points = null;
		PolylineOptions lineOptions = null;
		MarkerOptions markerOptions = new MarkerOptions();
		String distance = "";
		String duration = "";
		
		
		
		if(result.size()<1){
			Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
			return;
		}
			
		
		// Traversing through all the routes
		for(int i=0;i<result.size();i++){
			points = new ArrayList<LatLng>();
			lineOptions = new PolylineOptions();
			
			// Fetching i-th route
			List<HashMap<String, String>> path = result.get(i);
			
			// Fetching all the points in i-th route
			for(int j=0;j<path.size();j++){
				HashMap<String,String> point = path.get(j);	
				
				if(j==0){	// Get distance from the list
					distance = (String)point.get("distance");						
					continue;
				}else if(j==1){ // Get duration from the list
					duration = (String)point.get("duration");
					continue;
				}
				
				double lat = Double.parseDouble(point.get("lat"));
				double lng = Double.parseDouble(point.get("lng"));
				LatLng position = new LatLng(lat, lng);	
				
				points.add(position);
				//CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom((position), 12);
				//map.animateCamera(cameraUpdate);
			}
			
			//Adding all the points in the route to LineOptions
			lineOptions.addAll(points);
			lineOptions.width(4);
			lineOptions.color(Color.BLUE);	
			
		}
		if(distance!=null)
		{
		//tvDistanceDuration.setText("Distance:"+distance+","+"Duration:"+duration);
		
		// Drawing polyline in the Google Map for the i-th route
		if(lineOption!=null)
			lineOption.remove();
		lineOption=map.addPolyline(lineOptions);
		
	   }	
 }		
}   


@Override
public void onMarkerDrag(Marker arg0) {

}

@Override
public void onMarkerDragEnd(Marker marker) {
	
	Double x = marker.getPosition().latitude;
	Double y = marker.getPosition().longitude;

	Address startAddress = null;
	String LocName;
	p = new LatLng(x, y);
	if (profsession.isNetworkAvailable()) {
		final Geocoder geocoder = new Geocoder(RestaurantProfileActivity.this);

		try {
			startAddress = geocoder.getFromLocation(x, y, 1).get(0);

			LocName = StringAddress.asString(startAddress);

		} catch (Exception e) {
		}
	} else {
	}


	
	if(kiel1!=null)
	{
		kiel1.remove();
	}
	kiel1 = map.addMarker(options
			.position(p)
			.title("Your Current Location")
			.snippet("")
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.markerred)));

	kiel1.setDraggable(true);

}
@Override
public void onMarkerDragStart(Marker marker) {
	
}

@Override
public void onMapLongClick(LatLng point) {
	// TODO Auto-generated method stub

}

@Override
public void onMapClick(LatLng point) {
	// TODO Auto-generated method stub

}
String getDirectionsUrl(LatLng origin,LatLng dest){
	
	// Origin of route
	String str_origin = "origin="+origin.latitude+","+origin.longitude;
	
	// Destination of route
	String str_dest = "destination="+dest.latitude+","+dest.longitude;		
	
				
	// Sensor enabled
	String sensor = "sensor=false";			
				
	// Building the parameters to the web service
	String parameters = str_origin+"&"+str_dest+"&"+sensor;
				
	// Output format
	String output = "json";
	
	// Building the url to the web service
	String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
	
	
	return url;
}

/** A method to download json data from url */
 String downloadUrl(String strUrl) throws IOException{
    String data = "";
    InputStream iStream = null;
    HttpURLConnection urlConnection = null;
    try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url 
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url 
            urlConnection.connect();

            // Reading data from url 
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                    sb.append(line);
            }
            
            data = sb.toString();

            br.close();

    }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
    }finally{
            iStream.close();
            urlConnection.disconnect();
    }
    return data;
 }


}
