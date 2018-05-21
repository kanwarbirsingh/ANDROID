package com.isoftinc.fragment;

import helper.CustomProgressDialog;
import helper.Helper;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract.Helpers;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.internal.di;
import com.isoft.foodspoty.CustomToast;
import com.isoft.foodspoty.GPSTracker;
import com.isoft.foodspoty.MainActivity;
import com.isoft.foodspoty.ProfileActivity;
import com.isoft.foodspoty.R;
import com.isoft.foodspoty.RestaurantProfileActivity;
import com.isoft.foodspoty.StringAddress;
import com.isoft.foodspoty.Url;
import com.isoftinc.listadapter.AdapterProfileConnection;
import com.isoftinc.listadapter.AdapterProfileCoupons;
import com.isoftinc.listadapter.AdapterSearchCategory;
import com.isoftinc.listadapter.AdapterSearchCity;
import com.isoftinc.listadapter.FragmentHomeAdapter;
import com.rangebar.RangeBar;
import com.session.ProfileSessionManager;
import com.webservices.DatabaseHandlerFav;
import com.webservices.DatabaseHandlerFilter;
import com.webservices.ServiceHandler;
import com.webservices.Serviceurl;

@SuppressLint({ "InflateParams", "ClickableViewAccessibility" }) @SuppressWarnings("unused")
public class FragmentMain extends Fragment {
	
	private ListView restListview;
    private ProgressDialog pDialog;
    private ArrayList<HashMap<String, String>> restData;
    private Button btnFilter;
    ProfileSessionManager profsession;
    double CurrentLat=0.0;
   	double CurrentLong=0.0;
   	JSONArray ArrCategory 		=new  JSONArray();
	JSONArray ArrCity 			=new  JSONArray();
    //Popup control declartion
    private ArrayList<HashMap<String, String>> cityList, categoryList;
    public static String Key_hrestaurant_id="hrestaurant_id";
    public static String Key_hrestaurant_name="hrestaurant_name";
    public static String Key_hrestaurant_address="hrestaurant_address";
    public static String Key_hrestaurant_longitude="hrestaurant_longitude";
    public static String Key_hrestaurant_latitude="hrestaurant_latitude";
    public static String Key_hrestaurant_cover_image="hrestaurant_cover_image";
    public static String Key_hrestaurant_type="hrestaurant_type";
   public static String Key_hrestorentdistance="hrestaurant_distance";
    public static String Key_hrestaurant_city="hrestaurant_city";
    DatabaseHandlerFilter dbf;
    DatabaseHandlerFav dbFav;
    public static	ArrayList<String> SearchArr;
	ArrayList<String> AllreadyAddedFilter;
	ArrayList<HashMap<String, String>> searchListtemp;
    GPSTracker gps;
    String strCurrent_latitude;  
    public static JSONObject FilterListArray ;
	String strCurrent_longitude; 
	CustomToast ct;
	RelativeLayout rlerror,rlblank;
	ArrayList<HashMap<String, String>>  FillterHasMapArray1;
	ArrayList<String>AllreadyFiltered,AllreadyFiltered2;
	 Dialog dialogfilter;
	CheckBox fchkbar,fchkrestorent,fchklounge,fchkBakery,fchkCafe,fchkPubs,fchkcityHyderabad,fchkcityBangalore;
  
	int fromrange=0,torange=50000;
    Button btn_menuSearch;
    RelativeLayout rlsearch;
    ImageView search_search_icon,search_back_icon;
    AutoCompleteTextView search_etxt;
	public FragmentMain() {
    	
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        profsession=new ProfileSessionManager(getActivity());
        ct=new CustomToast(getActivity());
        dbf=new DatabaseHandlerFilter(getActivity());
        dbFav=new DatabaseHandlerFav(getActivity());
        dbf.RemoveAllFilter();
        FilterListArray=new JSONObject();
        restListview = (ListView)view.findViewById(R.id.restaurantListview);
        btnFilter    = (Button)view.findViewById(R.id.btnFilter);
        rlerror			=(RelativeLayout)view.findViewById(R.id.rlerror);
        rlerror.setVisibility(View.GONE);
        rlblank			=(RelativeLayout)view.findViewById(R.id.rlblank);
        rlblank.setVisibility(View.VISIBLE);
        customalertfilter();
        getLocation();
        restListview.setOnItemClickListener(new OnItemClickListener()
        {
           @Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id)
        	{
        	   HashMap<String , String>homelistmap=restData.get(position);
				String restlat=homelistmap.get(Key_hrestaurant_latitude);
				String restlong=homelistmap.get(Key_hrestaurant_longitude);
				String restid=homelistmap.get(Key_hrestaurant_id);
				String restname=homelistmap.get(Key_hrestaurant_name);
				String restAddress=homelistmap.get(Key_hrestaurant_address);
				
        	   Intent intent = new Intent(getActivity(), RestaurantProfileActivity.class);
        	   intent.putExtra("restlat", restlat);
        	   intent.putExtra("restlong", restlong);
        	   intent.putExtra("restid", restid);
        	   intent.putExtra("restname", restname);
        	   intent.putExtra("restAddress", restAddress);
        	   startActivity(intent);
        	}
        });
        
        btnFilter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				dialogfilter.show();
				
			}
		});
        
        rlsearch=(RelativeLayout)getActivity().findViewById(R.id.rlsearch);
        rlsearch.setVisibility(View.GONE);
        search_search_icon=(ImageView)getActivity().findViewById(R.id.search_back_icon);
        search_back_icon=(ImageView)getActivity().findViewById(R.id.search_back_icon);
        btn_menuSearch=(Button)getActivity().findViewById(R.id.btn_menuSearch);
        search_etxt=(AutoCompleteTextView)getActivity().findViewById(R.id.search_etxt);
      
        btn_menuSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				  rlsearch.setVisibility(View.VISIBLE);
			      
			}
		});
        search_search_icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rlsearch.setVisibility(View.GONE);
			}
		});
        search_back_icon.setOnClickListener(new OnClickListener() {
			
     			@Override
     			public void onClick(View v) {
     				// TODO Auto-generated method stub
     				rlsearch.setVisibility(View.GONE);
     			}
     		});
 search_etxt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence cs, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				String tempstr="";
				System.out.println("temstr "+tempstr);
				if(s!=null)
				{
					tempstr=s.toString();
					System.out.println("temstr2 "+tempstr);
				}
				if(tempstr==null || tempstr.length()==0)
				{
					FragmentHomeAdapter listAdapter = new FragmentHomeAdapter(getActivity(), restData ,R.layout.row_restaurant);
					restListview.setAdapter(listAdapter);
					rlblank.setVisibility(View.GONE);
					rlerror.setVisibility(View.GONE);
            	}
				else
				{
					System.out.println("temstr4 "+tempstr);
					searchListtemp = new ArrayList<HashMap<String, String>>();
					AllreadyAddedFilter=new ArrayList<String>();
					try
					{
				         for (int i = 0; i < restData.size(); i++) {
				        	 HashMap<String, String> menuValue = new HashMap<String, String>();
				                menuValue = restData.get(i);
				                HashMap<String, String> map = new HashMap<String, String>();
				                System.out.println("temstr5 "+tempstr);
				                String temcaffename=menuValue.get(Key_hrestaurant_id).toLowerCase();
				                String tempCaffeAdd=menuValue.get(Key_hrestaurant_name).toLowerCase();
				                String temstrs=tempstr;
				                
				                //CategoryName,Productname,ProductVarityeName,SellerDistance,ask_status,ask_id
								
				                if(menuValue.get(Key_hrestaurant_type).toLowerCase().contains(tempstr.toLowerCase())){
//				                /	System.out.println("temstr8 "+tempstr+ " caffename "+menuValue.get(key_category_name));
				                	
				                	map.put(Key_hrestaurant_id, menuValue.get(Key_hrestaurant_id));
								 	map.put(Key_hrestaurant_name, menuValue.get(Key_hrestaurant_name));
								 	map.put(Key_hrestaurant_address, menuValue.get(Key_hrestaurant_address));
								 	map.put(Key_hrestaurant_longitude, menuValue.get(Key_hrestaurant_longitude));
								 	map.put(Key_hrestaurant_latitude, menuValue.get(Key_hrestaurant_latitude));
								 	map.put(Key_hrestaurant_cover_image, menuValue.get(Key_hrestaurant_cover_image));
								 	map.put(Key_hrestaurant_type, menuValue.get(Key_hrestaurant_type));
								 	map.put(Key_hrestorentdistance, menuValue.get(Key_hrestorentdistance));
								 	map.put(Key_hrestaurant_city, menuValue.get(Key_hrestaurant_city));
									
	        						
	        						
	        						
		        				  // adding HashList to ArrayListz
	        						if(AllreadyAddedFilter.contains(menuValue.get(Key_hrestaurant_id)))
	        						{
	        							
	        						}
	        						else
	        						{
	        							
	        							searchListtemp.add(map);
	        							AllreadyAddedFilter.add(menuValue.get(Key_hrestaurant_id));
	        						}
				                   }
				                
				                if(menuValue.get(Key_hrestaurant_address).toLowerCase().contains(tempstr.toLowerCase())){
//				                	/System.out.println("temstr9 "+tempstr+ " caffename "+menuValue.get(key_product_name));
				                	map.put(Key_hrestaurant_id, menuValue.get(Key_hrestaurant_id));
								 	map.put(Key_hrestaurant_name, menuValue.get(Key_hrestaurant_name));
								 	map.put(Key_hrestaurant_address, menuValue.get(Key_hrestaurant_address));
								 	map.put(Key_hrestaurant_longitude, menuValue.get(Key_hrestaurant_longitude));
								 	map.put(Key_hrestaurant_latitude, menuValue.get(Key_hrestaurant_latitude));
								 	map.put(Key_hrestaurant_cover_image, menuValue.get(Key_hrestaurant_cover_image));
								 	map.put(Key_hrestaurant_type, menuValue.get(Key_hrestaurant_type));
								 	map.put(Key_hrestorentdistance, menuValue.get(Key_hrestorentdistance));
								 	map.put(Key_hrestaurant_city, menuValue.get(Key_hrestaurant_city));
									
	        						
	        						
	        						
	        						
		        				  // adding HashList to ArrayListz
	        						if(AllreadyAddedFilter.contains(menuValue.get(Key_hrestaurant_id)))
	        						{
	        							
	        						}
	        						else
	        						{
	        							
	        							searchListtemp.add(map);
	        							AllreadyAddedFilter.add(menuValue.get(Key_hrestaurant_id));
	        						}
				                   }
				                if(menuValue.get(Key_hrestaurant_name).toLowerCase().contains(tempstr.toLowerCase())){
				                	map.put(Key_hrestaurant_id, menuValue.get(Key_hrestaurant_id));
								 	map.put(Key_hrestaurant_name, menuValue.get(Key_hrestaurant_name));
								 	map.put(Key_hrestaurant_address, menuValue.get(Key_hrestaurant_address));
								 	map.put(Key_hrestaurant_longitude, menuValue.get(Key_hrestaurant_longitude));
								 	map.put(Key_hrestaurant_latitude, menuValue.get(Key_hrestaurant_latitude));
								 	map.put(Key_hrestaurant_cover_image, menuValue.get(Key_hrestaurant_cover_image));
								 	map.put(Key_hrestaurant_type, menuValue.get(Key_hrestaurant_type));
								 	map.put(Key_hrestorentdistance, menuValue.get(Key_hrestorentdistance));
								 	map.put(Key_hrestaurant_city, menuValue.get(Key_hrestaurant_city));
									
	        						
	        						
	        						
	        						
		        				  // adding HashList to ArrayListz
	        						if(AllreadyAddedFilter.contains(menuValue.get(Key_hrestaurant_id)))
	        						{
	        							
	        						}
	        						else
	        						{
	        							
	        							searchListtemp.add(map);
	        							AllreadyAddedFilter.add(menuValue.get(Key_hrestaurant_id));
	        						}

				                   }  
				          	 
				            
				                
				         
				         
				         }
				         System.out.println("temstr6 "+tempstr);
				         
				         FragmentHomeAdapter listAdapter = new FragmentHomeAdapter(getActivity(), searchListtemp ,R.layout.row_restaurant);
							restListview.setAdapter(listAdapter);
							}
					catch(Exception ex)
					{
						
					}
				}
			
			}
		});

        
        
        return view;
    }
    
    
    class LoadHomeData extends AsyncTask<String, String, String> {
    	CustomProgressDialog pDialog;
    	String webUserId,webUserName,webUserEmail,webMobile,webuserPic,webUserLatitude,webUserLongitude,webTotalPoint,webPassword;
    	String webTotalCoupan="0",webTotalConnection="0";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new CustomProgressDialog(getActivity());
			
			pDialog.show();

		}

		protected String doInBackground(String... args) {
			String responseStatus = null;
			restData = new ArrayList<HashMap<String, String>>();
			
			try {
				ServiceHandler sh = new ServiceHandler();
				String struserpicurl=null;
				/*	try {
    				struserpicurl = URLEncoder.encode(struserpic.toString(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
    		
					String loginUrl = Url.GetUrl(Serviceurl.restaurant_list );
					
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
						webTotalConnection=String.valueOf(jarrdata.length());
						
						for(int i=0;i<jarrdata.length();i++)
						{
							String webDistance="";
						JSONObject jobjdata=jarrdata.getJSONObject(i);
						if(!jobjdata.getString("restaurant_longitude").equals("null")&&!jobjdata.getString("restaurant_longitude").equals(" ")&&!jobjdata.getString("restaurant_longitude").equals("0")&&!jobjdata.getString("restaurant_longitude").equals("0.0")&&!jobjdata.getString("restaurant_latitude").equals("null")&&!jobjdata.getString("restaurant_latitude").equals(" ")&&!jobjdata.getString("restaurant_latitude").equals("0")&&!jobjdata.getString("restaurant_latitude").equals("0.0"))
						{
							webDistance=Helper.distBetweenstr(CurrentLat, CurrentLong, Double.parseDouble(jobjdata.getString("restaurant_latitude")), Double.parseDouble(jobjdata.getString("restaurant_longitude")));
						}
						HashMap<String , String>conmap=new HashMap<String, String>();
						conmap.put(Key_hrestaurant_id, jobjdata.getString("restaurant_id"));
						conmap.put(Key_hrestaurant_name, jobjdata.getString("restaurant_name"));
						conmap.put(Key_hrestaurant_address, jobjdata.getString("restaurant_address"));
						conmap.put(Key_hrestaurant_longitude, jobjdata.getString("restaurant_longitude"));
						conmap.put(Key_hrestaurant_latitude, jobjdata.getString("restaurant_latitude"));
						conmap.put(Key_hrestaurant_cover_image, jobjdata.getString("restaurant_cover_image"));
						conmap.put(Key_hrestaurant_type, jobjdata.getString("restaurant_type"));
						conmap.put(Key_hrestorentdistance, webDistance);
						conmap.put(Key_hrestaurant_city, jobjdata.getString("restaurant_city"));
						
						restData.add(conmap);
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
                		FragmentHomeAdapter listAdapter = new FragmentHomeAdapter(getActivity(), restData ,R.layout.row_restaurant);
    					restListview.setAdapter(listAdapter);
    					rlblank.setVisibility(View.GONE);
    					rlerror.setVisibility(View.GONE);
                	
                	}
                	else {
			   			ct.showToastError("Limited Internet Connectivity");
			   			rlblank.setVisibility(View.VISIBLE);
    					rlerror.setVisibility(View.VISIBLE);
                	}

         }
	}
	
    
    
    /*
     * Loading popup data
     */

    class LoadDialogData extends AsyncTask<String, String, String>{

    	CustomProgressDialog pDialog;
    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new CustomProgressDialog(getActivity());
			
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			try {
				cityList = new ArrayList<HashMap<String, String>>();
				
				String[] str_city = {"Hyderabad", "Bangalore"};
				
				for (int i = 0; i < str_city.length; i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("CITY_ID", ""+i);
					map.put("CITY_NAME", str_city[i]);

					cityList.add(map);
				}
				
				String[] str_category = {"Bar", "Restaurant", "lounge", "Bakery", "Cafe", "Pubs"};
				
				categoryList = new ArrayList<HashMap<String, String>>();
				for (int i = 0; i < str_category.length; i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("CATEGORY_ID", ""+i);
					map.put("CATEGORY_NAME", str_category[i]);

					categoryList.add(map);
				}
				

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}
		
		protected void onPostExecute(String file_url) {

			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					
					
					pDialog.dismiss();
				}
			});
		}
    }
    /*
     * 
     */

public void customgpscheck()
{
	LocationManager locationManager = (LocationManager) getActivity()
			.getSystemService(getActivity().LOCATION_SERVICE);
	if (locationManager
			.isProviderEnabled(LocationManager.GPS_PROVIDER)
			&& locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
		
		
	        	 getLocation();
	    
	}
else {
	customalertgps("GPS is Disable in your\n device Would you like\n to Enable");
}
}


public void getLocation()
{
     
    // create class object
    gps = new GPSTracker(getActivity());

    // check if GPS enabled     
    if(gps.canGetLocation()){
     
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
     CurrentLat=latitude;
     CurrentLong=longitude;
        //-----------
if(CurrentLat!=0.0)
{

	strCurrent_latitude=String.valueOf(latitude);
	strCurrent_longitude=String.valueOf(longitude);
	new LoadHomeData().execute();
		Log.d("old", "lat :  " + latitude);
		Log.d("old", "long :  " + longitude);
		System.out.println("shivram good location  "+latitude+" longi "+longitude);
		// Enable / Disable zooming controls
		
		//----------SHIVRAM----
		final Geocoder geocoder = new Geocoder(getActivity());
		Address startAddress = null;

		String LocationName = "";
		
		try {
			startAddress = geocoder.getFromLocation(latitude,
					longitude, 1).get(0);

			
			LocationName = StringAddress.asString(startAddress);

		} catch (Exception e) {

		
		}

		
		

}
else
{

	customalertbtn("Location not find Try again");
}
     }else{
        // can't get location
        // GPS or Network is not enabled
        // Ask user to enable GPS/network in settings
    	customalertgps("GPS is Disable in your\n device Would you like\n to Enable");
    }
 

}

protected void customalertgps(String Msg)
{
final Dialog dialog = new Dialog(getActivity());
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
		getActivity().startActivity(gpsOptionsIntent);
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


protected void customalertbtn(String Msg)
{
final Dialog dialog = new Dialog(getActivity());
dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
dialog.setContentView(R.layout.custom_alertmsg1);
TextView text = (TextView) dialog.findViewById(R.id.AlertMsgTextView);
text.setText(Msg);
Button dialogButton = (Button) dialog.findViewById(R.id.AlertMsgOk);
dialogButton.setOnClickListener(new OnClickListener() {
	@Override
	public void onClick(View v) {
		
		dialog.dismiss();
		getLocation();
	}
});
dialog.show();
}

public void customalertfilter()
{


	RangeBar rangebar;
	ImageView btDismiss;
	final TextView txtMinVal;
	final TextView txtMaxVal;
	
	 dialogfilter = new Dialog(getActivity());
	dialogfilter.requestWindowFeature(Window.FEATURE_NO_TITLE);
	dialogfilter.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	dialogfilter.setContentView(R.layout.dialog_filter2);
	dialogfilter.setCancelable(false);
	
	
	Button btn_apply_filter=(Button)dialogfilter.findViewById(R.id.btn_apply_filter);
	rangebar 	 = (RangeBar)dialogfilter.findViewById(R.id.rangebar2);
	btDismiss	 = (ImageView)dialogfilter.findViewById(R.id.dialog_btn_close);
	txtMinVal 	 = (TextView)dialogfilter.findViewById(R.id.dialog_label_min_radius_km);
	txtMaxVal 	 = (TextView)dialogfilter.findViewById(R.id.dialog_label_max_radius_km);
	fchkbar		= (CheckBox)dialogfilter.findViewById(R.id.chkbar);
	fchkBakery		= (CheckBox)dialogfilter.findViewById(R.id.chkBakery);
	fchkCafe		= (CheckBox)dialogfilter.findViewById(R.id.chkCafe);
	fchklounge		= (CheckBox)dialogfilter.findViewById(R.id.chklounge);
	fchkPubs	= (CheckBox)dialogfilter.findViewById(R.id.chkPubs);
	fchkrestorent		= (CheckBox)dialogfilter.findViewById(R.id.chkrestorent);
	fchkcityHyderabad		= (CheckBox)dialogfilter.findViewById(R.id.chkcityHyderabad);
	fchkcityBangalore		= (CheckBox)dialogfilter.findViewById(R.id.chkcityBangalore);
	
	rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
		 
        @Override
        public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {

          //  leftIndexValue.setText("" + leftThumbIndex);
         //   rightIndexValue.setText("" + rightThumbIndex);
        	txtMinVal.setText(leftThumbIndex+" KM");
        	txtMaxVal.setText(rightThumbIndex+1+" KM");
        	fromrange=leftThumbIndex;
        	torange=rightThumbIndex;
        }
    });
	
	fchkbar.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			
			if(fchkbar.isChecked())
			{
				dbf.AddFilter("Bar","Category");
				FilterListArray=dbf.getFilterArray();
				//FilterApplyHome();
			}
			if(!fchkbar.isChecked())
			{
				dbf.RemoveOneFilter("Bar");
				FilterListArray=dbf.getFilterArray();
				//FilterApplyHome();
			}
			
		}
	});
	
	fchkBakery.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			
			if(fchkBakery.isChecked())
			{
				dbf.AddFilter("Bakery","Category");
				FilterListArray=dbf.getFilterArray();
				//FilterApplyHome();
			}
			if(!fchkBakery.isChecked())
			{
				dbf.RemoveOneFilter("Bakery");
				FilterListArray=dbf.getFilterArray();
				//FilterApplyHome();
			}
			
		}
	});
	fchkCafe.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			
			if(fchkCafe.isChecked())
			{
				dbf.AddFilter("Cafe","Category");
				FilterListArray=dbf.getFilterArray();
				//FilterApplyHome();
			}
			if(!fchkCafe.isChecked())
			{
				dbf.RemoveOneFilter("Cafe");
				FilterListArray=dbf.getFilterArray();
				//FilterApplyHome();
			}
			
		}
	});
	fchklounge.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			
			if(fchklounge.isChecked())
			{
				dbf.AddFilter("Lounge","Category");
				FilterListArray=dbf.getFilterArray();
				//FilterApplyHome();
			}
			if(!fchklounge.isChecked())
			{
				dbf.RemoveOneFilter("Lounge");
				FilterListArray=dbf.getFilterArray();
				//FilterApplyHome();
			}
			
		}
	});
	fchkrestorent.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			
			if(fchkrestorent.isChecked())
			{
				dbf.AddFilter("Restaurent","Category");
				FilterListArray=dbf.getFilterArray();
				//FilterApplyHome();
			}
			if(!fchkrestorent.isChecked())
			{
				dbf.RemoveOneFilter("Restaurent");
				FilterListArray=dbf.getFilterArray();
				//FilterApplyHome();
			}
			
		}
	});
fchkPubs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			
			if(fchkPubs.isChecked())
			{
				dbf.AddFilter("Pubs","Category");
				FilterListArray=dbf.getFilterArray();
				//FilterApplyHome();
			}
			if(!fchkPubs.isChecked())
			{
				dbf.RemoveOneFilter("Pubs");
				FilterListArray=dbf.getFilterArray();
				//FilterApplyHome();
			}
			
		}
	});
fchkcityHyderabad.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		
		if(fchkcityHyderabad.isChecked())
		{
			dbf.AddFilter("Hyderabad","City");
			FilterListArray=dbf.getFilterArray();
			///FilterApplyHome();
		}
		if(!fchkcityHyderabad.isChecked())
		{
			dbf.RemoveOneFilter("Hyderabad");
			FilterListArray=dbf.getFilterArray();
		//	FilterApplyHome();
		}
		
	}
});
fchkcityBangalore.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		
		if(fchkcityBangalore.isChecked())
		{
			dbf.AddFilter("Bangalore","City");
			FilterListArray=dbf.getFilterArray();
			//FilterApply();
		}
		if(!fchkcityBangalore.isChecked())
		{
			dbf.RemoveOneFilter("Bangalore");
			FilterListArray=dbf.getFilterArray();
			//FilterApply();
		}
		
	}
});

	 try {
			new LoadDialogData().execute();
		} catch (Exception e) {
			// TODO: handle exception
		}
		btDismiss.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				dialogfilter.dismiss();
				   
			}
		});
		btn_apply_filter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FilterApplyHome();
				dialogfilter.dismiss();
				
			}
		});
	

}

public void FilterApplyHome()
{  

	//categoryList;
	HashMap<String, String> catlistValue = new HashMap<String, String>();
	FillterHasMapArray1 = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>>	FillterHasMapArraytemp = new ArrayList<HashMap<String, String>>();
	 ArrayList<String>	totalfilterlength=new ArrayList<String>();
	String fcategorycheck="false",fcitycheck="false",fcusioncheck="false",fcattypecheck="false",frimcheck="false",fbrandcheck="false",fcolorcheck="false";
	AllreadyFiltered=new ArrayList<String>();
	AllreadyFiltered2=new ArrayList<String>();
	
		
	
	if(FilterListArray.length()>0)
	{
		for(int i=0;i<restData.size();i++)
		{
		catlistValue     					  = restData.get(i);
		for(int fi=0;fi<FilterListArray.length();fi++)
		{
			try
			{
		if(FilterListArray.has("categoryfilter"))
		{
		ArrCategory =FilterListArray.getJSONArray("categoryfilter");	
		}
		
		if(FilterListArray.has("cityfilter"))
		{
		ArrCity =FilterListArray.getJSONArray("cityfilter");	
		}	
		
		
		 if(ArrCategory.length()>0)
		{
		
		 for(int ict=0;ict<ArrCategory.length();ict++){
				
			 JSONObject jsonobdata = ArrCategory.getJSONObject(ict);
			 HashMap<String, String> map = new HashMap<String, String>();
			 if(jsonobdata.has("filtername"))
			 {
				 String filtername=jsonobdata.getString("filtername");
				 if(filtername.equals(catlistValue.get(Key_hrestaurant_type)) )
					{
					 	map.put(Key_hrestaurant_id, catlistValue.get(Key_hrestaurant_id));
					 	map.put(Key_hrestaurant_name, catlistValue.get(Key_hrestaurant_name));
					 	map.put(Key_hrestaurant_address, catlistValue.get(Key_hrestaurant_address));
					 	map.put(Key_hrestaurant_longitude, catlistValue.get(Key_hrestaurant_longitude));
					 	map.put(Key_hrestaurant_latitude, catlistValue.get(Key_hrestaurant_latitude));
					 	map.put(Key_hrestaurant_cover_image, catlistValue.get(Key_hrestaurant_cover_image));
					 	map.put(Key_hrestaurant_type, catlistValue.get(Key_hrestaurant_type));
					 	map.put(Key_hrestorentdistance, catlistValue.get(Key_hrestorentdistance));
					 	map.put(Key_hrestaurant_city, catlistValue.get(Key_hrestaurant_city));
						
					   fcategorycheck="true";
					   double restdist=Double.parseDouble(catlistValue.get(Key_hrestorentdistance));
					   double fromranges=fromrange;
					   double torangess=torange;
					   if(Double.parseDouble(catlistValue.get(Key_hrestorentdistance))>fromrange && Double.parseDouble(catlistValue.get(Key_hrestorentdistance))<torange )
						{
						   if(AllreadyFiltered.contains(catlistValue.get(Key_hrestaurant_id).toString()))
				 	   	   {
				 	   		   
				 	   	   }
				 	   	   else
				 	   	   {
				 	   		 FillterHasMapArray1.add(map);
				 	   		 AllreadyFiltered.add(catlistValue.get(Key_hrestaurant_id).toString());
				 	   		 
				 	   	   }
						}
		      	 	
			 }
			 
			}
			 
		 }
			
		}
		else if(ArrCity.length()>0)
		{
		
		 for(int ict=0;ict<ArrCity.length();ict++){
				
			 JSONObject jsonobdata = ArrCity.getJSONObject(ict);
			 HashMap<String, String> map = new HashMap<String, String>();
			 if(jsonobdata.has("filtername"))
			 {

				 String filtername=jsonobdata.getString("filtername");
				 if(filtername.equals(catlistValue.get(Key_hrestaurant_city)))
					{
					 	map.put(Key_hrestaurant_id, catlistValue.get(Key_hrestaurant_id));
					 	map.put(Key_hrestaurant_name, catlistValue.get(Key_hrestaurant_name));
					 	map.put(Key_hrestaurant_address, catlistValue.get(Key_hrestaurant_address));
					 	map.put(Key_hrestaurant_longitude, catlistValue.get(Key_hrestaurant_longitude));
					 	map.put(Key_hrestaurant_latitude, catlistValue.get(Key_hrestaurant_latitude));
					 	map.put(Key_hrestaurant_cover_image, catlistValue.get(Key_hrestaurant_cover_image));
					 	map.put(Key_hrestaurant_type, catlistValue.get(Key_hrestaurant_type));
					 	map.put(Key_hrestorentdistance, catlistValue.get(Key_hrestorentdistance));
					 	map.put(Key_hrestaurant_city, catlistValue.get(Key_hrestaurant_city));
						
					   fcitycheck="true";
					   if(Double.parseDouble(catlistValue.get(Key_hrestorentdistance))>fromrange && Double.parseDouble(catlistValue.get(Key_hrestorentdistance))<torange )
						{
							if(AllreadyFiltered.contains(catlistValue.get(Key_hrestaurant_id).toString()))
					 	   	   {
					 	   		   
					 	   	   }
					 	   	   else
					 	   	   {
					 	   		 FillterHasMapArray1.add(map);
					 	   		 AllreadyFiltered.add(catlistValue.get(Key_hrestaurant_id).toString());
					 	   		 
					 	   	   }
						}
		      	 
			 }
			 
			
				 
				 
			 }
			 
		 }
			
		}
		
		
			}
			catch(Exception ex)
			{
			ex.printStackTrace();	
			}
	}
		
		
	
	}
//----------Second filter

{
	 if(ArrCategory.length()==0&&ArrCity.length()==0)
	 {
		 FragmentHomeAdapter listAdapter = new FragmentHomeAdapter(getActivity(), restData ,R.layout.row_restaurant);
			restListview.setAdapter(listAdapter);
			rlblank.setVisibility(View.GONE);
			rlerror.setVisibility(View.GONE);	 
	 }
	 else
	 {
			FragmentHomeAdapter listAdapter = new FragmentHomeAdapter(getActivity(), FillterHasMapArray1 ,R.layout.row_restaurant);
			restListview.setAdapter(listAdapter);
			rlblank.setVisibility(View.GONE);
			rlerror.setVisibility(View.GONE);
	 }

}
	}
	else
	{
		for(int i=0;i<restData.size();i++)
		{
		catlistValue     					  = restData.get(i);
		
		 HashMap<String, String> map = new HashMap<String, String>();
		 if(Double.parseDouble(catlistValue.get(Key_hrestorentdistance))>fromrange && Double.parseDouble(catlistValue.get(Key_hrestorentdistance))<torange )
			{
			 
			 	
			 	map.put(Key_hrestaurant_id, catlistValue.get(Key_hrestaurant_id));
			 	map.put(Key_hrestaurant_name, catlistValue.get(Key_hrestaurant_name));
			 	map.put(Key_hrestaurant_address, catlistValue.get(Key_hrestaurant_address));
			 	map.put(Key_hrestaurant_longitude, catlistValue.get(Key_hrestaurant_longitude));
			 	map.put(Key_hrestaurant_latitude, catlistValue.get(Key_hrestaurant_latitude));
			 	map.put(Key_hrestaurant_cover_image, catlistValue.get(Key_hrestaurant_cover_image));
			 	map.put(Key_hrestaurant_type, catlistValue.get(Key_hrestaurant_type));
			 	map.put(Key_hrestorentdistance, catlistValue.get(Key_hrestorentdistance));
			 	map.put(Key_hrestaurant_city, catlistValue.get(Key_hrestaurant_city));
				
			   fcategorycheck="true";
     	 	if(AllreadyFiltered.contains(catlistValue.get(Key_hrestaurant_id).toString()))
	   	   {
	   		   
	   	   }
	   	   else
	   	   {
	   		 FillterHasMapArray1.add(map);
	   		 AllreadyFiltered.add(catlistValue.get(Key_hrestaurant_id).toString());
	   		 
	   	   }
	 }
	 
	}
		
		FragmentHomeAdapter listAdapter = new FragmentHomeAdapter(getActivity(), FillterHasMapArray1 ,R.layout.row_restaurant);
		restListview.setAdapter(listAdapter);
		rlblank.setVisibility(View.GONE);
		rlerror.setVisibility(View.GONE);
	}
	}


}
