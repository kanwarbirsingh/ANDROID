package com.isoft.foodspoty;

import static com.isoft.foodspoty.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.isoft.foodspoty.CommonUtilities.EXTRA_MESSAGE;
import static com.isoft.foodspoty.CommonUtilities.SENDER_ID;
import helper.CustomProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;
import com.helper.adapters.ImageLoaderisoft;
import com.isoftinc.fragment.FragmentMain;
import com.isoftinc.layout.CommonFunctions;
import com.isoftinc.layout.MainLayout;
import com.isoftinc.listadapter.AdapterSlidemenu;
import com.session.ProfileSessionManager;
import com.webservices.ServiceHandler;
import com.webservices.Serviceurl;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

// DK 
// This is my studying about Sliding Menu following Youtube video
@SuppressLint("InflateParams") 
public class MainActivity extends FragmentActivity {

    // The MainLayout which will hold both the sliding menu and our main content
    // Main content will holds our Fragment respectively
    MainLayout mainLayout;
   // FrameLayout fragmentBody;
   // ListView menu
    private ListView lvMenu;
    private String[] lvMenuItems;
    
    private LinearLayout profileBtn;
    ImageLoaderisoft imageloder;
    // Menu button
    Button btMenu;//, btFilter;
    RelativeLayout rllogout;
    // Title according to fragment
    TextView tvTitle;
    ImageView userPic;
    TextView userName;
    TextView userEmail;
    String strEmailid="";
    String strpassword="";
    ProfileSessionManager profsession;
    Button btn_logout;
    Button btn_menuSearch;
    RelativeLayout rlsearch;
    ImageView search_search_icon,search_back_icon;
    AutoCompleteTextView search_etxt;
    AsyncTask<Void, Void, Void> mRegisterTask; // gcm integration
	String regId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profsession=new ProfileSessionManager(MainActivity.this);
        // Inflate the mainLayout
        mainLayout = (MainLayout)this.getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(mainLayout);
        imageloder=new ImageLoaderisoft(MainActivity.this);
        CommonFunctions.hideKeyboard(this);
        //fragmentBody = (FrameLayout)findViewById(R.id.activity_main_content_fragment);
        // Init menu
        rlsearch=(RelativeLayout)findViewById(R.id.rlsearch);
        rlsearch.setVisibility(View.GONE);
        search_search_icon=(ImageView)findViewById(R.id.search_back_icon);
        search_back_icon=(ImageView)findViewById(R.id.search_back_icon);
        btn_menuSearch=(Button)findViewById(R.id.btn_menuSearch);
        search_etxt=(AutoCompleteTextView)findViewById(R.id.search_etxt);
        lvMenuItems = getResources().getStringArray(R.array.menu_items);
        lvMenu = (ListView) findViewById(R.id.activity_main_menu_listview);
        userPic=(ImageView)	findViewById(R.id.userPic);
        userName=(TextView)findViewById(R.id.userName);
        userEmail=(TextView)findViewById(R.id.userEmail);
        profileBtn = (LinearLayout)findViewById(R.id.userProfileLayout);
        rllogout	=(RelativeLayout)findViewById(R.id.logoutLayout);
        btn_logout	=(Button)findViewById(R.id.btn_logout);
        //lvMenu.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, lvMenuItems));
        AdapterSlidemenu slidemenuAdapter = new AdapterSlidemenu(this, lvMenuItems, R.layout.row_slidemenu);
        lvMenu.setAdapter(slidemenuAdapter);
        lvMenu.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onMenuItemClick(parent, view, position, id);
            }
            
        });
       	
 
        imageloder.DisplayImage(profsession.GetSharedPreferences(ProfileSessionManager.KEY_UserPic), userPic, R.drawable.ic_user_pic);
        userName.setText(profsession.GetSharedPreferences(ProfileSessionManager.KEY_USERNAME));
        userEmail.setText(profsession.GetSharedPreferences(ProfileSessionManager.KEY_EMAIL));
        strEmailid=profsession.GetSharedPreferences(ProfileSessionManager.KEY_EMAIL);
        strpassword=profsession.GetSharedPreferences(ProfileSessionManager.KEY_Password);
        rllogout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				profsession.SetSharedPreferences(ProfileSessionManager.KEY_LoginStatus, "false");
				
				Intent in=new Intent(MainActivity.this,LoginActivity.class);
				startActivity(in);
				finish();
			}
		});
        btn_logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				profsession.SetSharedPreferences(ProfileSessionManager.KEY_LoginStatus, "false");
				
				Intent in=new Intent(MainActivity.this,LoginActivity.class);
				startActivity(in);
				finish();
			}
		});
        /*btFilter = (Button)findViewById(R.id.btn_menuFilter);
        
        btFilter.setOnClickListener(new OnClickListener() {
        	 RangeBar rangebar;
        	 ImageView btDismiss;
        	 TextView txtMinVal, txtMaxVal;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				final Dialog dialog = new Dialog(MainActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				dialog.setContentView(R.layout.dialog_filter);
				dialog.setCancelable(false);
				
				dialog.show();
				
				rangebar 	= (RangeBar)dialog.findViewById(R.id.rangebar2);
				btDismiss	= (ImageView)dialog.findViewById(R.id.dialog_btn_close);
				lvSearchCity= (ListView)dialog.findViewById(R.id.dialog_city_list);
				gridCategory= (GridView)dialog.findViewById(R.id.dialog_grid_view);
				dialog.show();
				
				txtMinVal = (TextView)dialog.findViewById(R.id.dialog_label_min_radius_km);
				txtMaxVal = (TextView)dialog.findViewById(R.id.dialog_label_max_radius_km);
				
				rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
					 
			            @Override
			            public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {

			              //  leftIndexValue.setText("" + leftThumbIndex);
			             //   rightIndexValue.setText("" + rightThumbIndex);
			            	txtMinVal.setText(leftThumbIndex+" KM");
			            	txtMaxVal.setText(rightThumbIndex+1+" KM");
			            }
			        });
				 

				gridCategory.setOnTouchListener(new OnTouchListener(){

				   
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if(event.getAction() == MotionEvent.ACTION_MOVE){
				            return false;
				        }
						return false;
					}

				});
				
				lvSearchCity.setOnTouchListener(new OnTouchListener(){

					   
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if(event.getAction() == MotionEvent.ACTION_MOVE){
				            return false;
				        }
						return false;
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
						
						dialog.dismiss();
						
					}
				});
			}
		});*/
        
        // Get menu button
        btMenu = (Button) findViewById(R.id.btn_slidemenu);
        btMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show/hide the menu
                toggleMenu(v);
            }
        });
       
        profileBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
				startActivity(intent);
			}
		});
        
        // Get title textview
        tvTitle = (TextView) findViewById(R.id.headTitle);
        
        
        // Add FragmentMain as the initial fragment       
        FragmentManager fm = MainActivity.this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        
        FragmentMain fragment = new FragmentMain();
        ft.add(R.id.activity_main_content_fragment, fragment);
        ft.commit();   
        
        /*fragmentBody.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mainLayout.isMenuShown()) {
		            mainLayout.toggleMenu();
		        }else{
		        	mainLayout.toggleMenu();
		        }
				Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();
			}
		});*/
        /*
		 *  Start GCM integration
		 */
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		//lblMessage = (TextView) findViewById(R.id.lblMessage);
		
		registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));
		
		// Get GCM registration id
		 regId = GCMRegistrar.getRegistrationId(this);
		 String tempregid=regId;
	//	Toast.makeText(getApplicationContext(), "Device id:- \n"+regId, Toast.LENGTH_LONG).show();
		// Check if regid already presents
		if (regId ==null || regId.equals("")) {
			// Registration is not present, register now with GCM			
			GCMRegistrar.register(this, SENDER_ID);
			 regId = GCMRegistrar.getRegistrationId(this);
			 String tempregid1=regId;
			 if(regId!=null&& !regId.equals(""))
			 {
				 String tempregid2=regId;
				 new LoadLogin().execute();
				
			 }
		//	Toast.makeText(getApplicationContext(), "Device token:- \n"+regId, Toast.LENGTH_LONG).show();
		} else {
			
			
			if(regId!=null & !regId.equals(""))
			{
				 new LoadLogin().execute();
			}
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
					} else {
					final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
												
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}
					
		/*
		 *  End GCM integration
		 */
		
		
	}
   	
	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());
			
			/**
			 * Take appropriate action on this message
			 * depending upon your app requirement
			 * For now i am just displaying it on the screen
			 * */
			
			// Showing received message		
		//	Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
			
			// Releasing wake lock
			WakeLocker.release();
		}
	};
	
	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}
	
	
	
    
    
    
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void toggleMenu(View v){
        mainLayout.toggleMenu();
    }
    
    // Perform action when a menu item is clicked
    private void onMenuItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = lvMenuItems[position];
        String currentItem = tvTitle.getText().toString();
        
        // Do nothing if selectedItem is currentItem
        if(selectedItem.compareTo(currentItem) == 0) {
            mainLayout.toggleMenu();
            return;
        }
            
        FragmentManager fm = MainActivity.this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = null;

        
        if(selectedItem.compareTo("Home") == 0) {
            fragment = new FragmentMain();
        } else if(selectedItem.compareTo("Offers") == 0) {
        	Intent intent = new Intent(getApplicationContext(), OffersActivity.class);
            startActivity(intent);
        } else if(selectedItem.compareTo("Redeem") == 0) {
        	Intent intent = new Intent(getApplicationContext(), RedeemActivity.class);
            startActivity(intent);
        } else if(selectedItem.compareTo("Notifications") == 0) {
        	Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
            startActivity(intent);
        } else if(selectedItem.compareTo("People") == 0) {
           // fragment = new FragmentImageView();
        	Intent intent = new Intent(getApplicationContext(), PeopleActivity.class);
            startActivity(intent);
        } else if(selectedItem.compareTo("Invite Friend") == 0) {
        	Intent intent = new Intent(getApplicationContext(), InviteActivity.class);
            startActivity(intent);
        } else if(selectedItem.compareTo("Contact Us") == 0) {
        	
        }else if(selectedItem.compareTo("Feedback") == 0) {
         // fragment = new FragmentRadioButton();
        } /*else if(selectedItem.compareTo("TextView") == 0) {
            fragment = new FragmentTextView();
            Bundle b = new Bundle();
            b.putString("KEY_STRING", "Please display this text");
            fragment.setArguments(b);
        } else if(selectedItem.compareTo("ToggleButton") == 0) {
            fragment = new FragmentToggleButton();
        } else if(selectedItem.compareTo("WebView") == 0) {
            fragment = new FragmentWebView();
        }*/
        
        if(fragment != null) {
            // Replace current fragment by this new one
            ft.replace(R.id.activity_main_content_fragment, fragment);
            ft.commit();
            
            tvTitle.setText(selectedItem);
        }
        
        // Hide menu anyway
        mainLayout.toggleMenu();
    }
    
    @Override
    public void onBackPressed() {
        if (mainLayout.isMenuShown()) {
            mainLayout.toggleMenu();
        }
        else {
            super.onBackPressed();
        }
    }
    
    
    class LoadLogin extends AsyncTask<String, String, String> {
    	String webUserId,webUserName,webUserEmail,webMobile,webuserPic,webUserLatitude,webUserLongitude,webTotalPoint;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		}

		protected String doInBackground(String... args) {
			String responseStatus = null;
			
			try {
				ServiceHandler sh = new ServiceHandler();
				String struserpicurl=null;
				/*	try {
    				struserpicurl = URLEncoder.encode(struserpic.toString(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
    		
					String loginUrl = Url.GetUrl(Serviceurl.login + "email=" + strEmailid+ "&password=" + strpassword + "&device_token=" +regId + "&device_user="+"Android");
					
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
						JSONObject jobjdata=jarrdata.getJSONObject(i);
						webUserId=jobjdata.getString("user_id");
						webUserName=jobjdata.getString("user_name");
						webUserEmail=jobjdata.getString("user_email");
						webMobile=jobjdata.getString("mobile");
						webuserPic=jobjdata.getString("user_pic");
						webUserLongitude=jobjdata.getString("user_longitude");
						webUserLatitude=jobjdata.getString("user_latitude");
						webTotalPoint=jobjdata.getString("total_point");
						profsession.SetSharedPreferences(ProfileSessionManager.KEY_ID, webUserId);
						profsession.SetSharedPreferences(ProfileSessionManager.KEY_USERNAME, webUserName);
						profsession.SetSharedPreferences(ProfileSessionManager.KEY_EMAIL, webUserEmail);
						profsession.SetSharedPreferences(ProfileSessionManager.KEY_MOBILE, webMobile);
						profsession.SetSharedPreferences(ProfileSessionManager.KEY_UserPic, webuserPic);
						profsession.SetSharedPreferences(ProfileSessionManager.KEY_userlatitude, webUserLatitude);
						profsession.SetSharedPreferences(ProfileSessionManager.KEY_userlongitude, webUserLongitude);
						profsession.SetSharedPreferences(ProfileSessionManager.KEY_userTotalPoint, webTotalPoint);
						
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
        	
            
         }
	}
	
 
}
