package com.isoft.foodspoty;

import static com.isoft.foodspoty.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.isoft.foodspoty.CommonUtilities.EXTRA_MESSAGE;
import static com.isoft.foodspoty.CommonUtilities.SENDER_ID;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;  
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import helper.CustomProgressDialog;
import helper.Helper;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.isoft.foodspoty.WakeLocker;
import com.session.ProfileSessionManager;
import com.webservices.ServiceHandler;
import com.webservices.Serviceurl;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.IntentSender.SendIntentException;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("unused")
public class LoginActivity extends Activity implements OnClickListener,ConnectionCallbacks, OnConnectionFailedListener{
	 
	EditText txtEmail, txtPassword;
	Button btnLogin, btnFacebookLogin, btnGoogleLogin, btnCreateAccount;
	String strUserPic,strusername,struseremail;
	Button btn_slidemenu, btn_menuback, btn_menuSearch, btn_menuFilter;
	TextView headTitle;
	CustomToast ct;
	private static String APP_ID = "XXXXX"; // Replace with your App ID
	// Instance of Facebook Class
	private Facebook facebook = new Facebook(APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	/*
	 * Google plus login
	 */
	private static final int RC_SIGN_IN = 0;
	// Logcat tag
	private static final String TAG = "MainActivity";
	// Profile pic image size in pixels
	private static final int PROFILE_PIC_SIZE = 400;
	// Google client to interact with Google API
	private GoogleApiClient mGoogleApiClient;
	
	/**
	 * A flag indicating that a PendingIntent is in progress and prevents us
	 * from starting further intents.
	 */
	private boolean mIntentInProgress;
	private ConnectionResult mConnectionResult;
	int googlePlusClick = 1 ;
	int facebookClick = 0 ;
	private boolean mSignInClicked;
	String userEmail ="";
	String userPass  ="";
	
	AsyncTask<Void, Void, Void> mRegisterTask; // gcm integration
	String regId="";
	ArrayList<HashMap<String, String>> userDetail;
	ProfileSessionManager profileSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ct=new CustomToast(LoginActivity.this);
        profileSession=new ProfileSessionManager(LoginActivity.this);
        txtEmail     = (EditText)findViewById(R.id.txtEmail);
        txtPassword  = (EditText)findViewById(R.id.txtPassword);
        
        btnLogin  			= (Button)findViewById(R.id.btnLogin);
        btnFacebookLogin    = (Button)findViewById(R.id.btnFacebookLogin);
        btnGoogleLogin      = (Button)findViewById(R.id.btnGoogleLogin);
        btnCreateAccount    = (Button)findViewById(R.id.btnCreateAccount);
        
        btn_slidemenu  = (Button)findViewById(R.id.btn_slidemenu);
        btn_menuback   = (Button)findViewById(R.id.btn_menuback);
        btn_menuSearch = (Button)findViewById(R.id.btn_menuSearch);
        btn_menuFilter = (Button)findViewById(R.id.btn_menuFilter);
        headTitle	   = (TextView)findViewById(R.id.headTitle);

        btn_slidemenu.setVisibility(View.GONE);
        btn_menuback.setVisibility(View.INVISIBLE);
        btn_menuSearch.setVisibility(View.INVISIBLE);
        btn_menuFilter.setVisibility(View.GONE);
        headTitle.setText("Login");
        
       

        btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				 userEmail = txtEmail.getText().toString().trim();
				 userPass  = txtPassword.getText().toString().trim();
				if (userEmail.replace(" ", "").length() == 0) {
					//Toast.makeText(getApplicationContext(), "Enter Username", Toast.LENGTH_LONG).show();
					ct.showToastError("Please enter email-id");
				} else if(!isValidEmail(userEmail)){
					ct.showToastError("Please enter valid email id");
					//Toast.makeText(getApplicationContext(), "Not a valid Email", Toast.LENGTH_LONG).show();
				} else if (userPass.replace(" ", "").length() == 0) {
					ct.showToastError("Please enter password");
					//Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_LONG).show();
				} else{
					// Cheaking Internet Connectivity 
	 				if (!Helper.checkInternetConnection(getApplicationContext())){
	 					//Toast.makeText(getApplicationContext(), "Check Internet connection", Toast.LENGTH_LONG).show();
	 					ct.showToastError(Helper.interneterror);
		 			} else {
		 				try {
		 						new LoadLogin().execute();
							} catch (Exception e) {
									e.printStackTrace();
							}
		 					
		 			}
				}
				
			}
		});
        

        btnCreateAccount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
				intent.putExtra("name", "");
				intent.putExtra("email", "");
				intent.putExtra("userpic", "");
				startActivity(intent);
			}
		});
        
        mAsyncRunner = new AsyncFacebookRunner(facebook);
        
        btnFacebookLogin.setOnClickListener(new OnClickListener() {
			
   				@Override
   				public void onClick(View v) {
   							facebookClick = 1 ;
   				    googlePlusClick = 0 ;
   					loginToFacebook();
   				
   				
   					
   				}
   			});
   	        
   	       
   	        
        btnGoogleLogin.setOnClickListener(new OnClickListener() {
   				
   				@Override
   				public void onClick(View v) {
   					facebookClick = 0 ;
   				    googlePlusClick = 1 ;
   					signInWithGplus();
   					
   				}
   			});
   	        
        mGoogleApiClient = new GoogleApiClient.Builder(this)
	    .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
	    .addApi(Plus.API, new Plus.PlusOptions.Builder().build()) // note the options
	    .addScope(Plus.SCOPE_PLUS_LOGIN).build();
		
		strUserPic=  profileSession.GetSharedPreferences(ProfileSessionManager.KEY_UserPic); 
			// setting Toogle button Statuss
	      String  temp=  profileSession.GetSharedPreferences(ProfileSessionManager.KEY_UserPic); 
	      txtEmail.setText(profileSession.GetSharedPreferences(ProfileSessionManager.KEY_EMAIL));
	        txtPassword.setText(profileSession.GetSharedPreferences(ProfileSessionManager.KEY_Password));
	     
	      if(profileSession.GetSharedPreferences(ProfileSessionManager.KEY_LoginStatus).equals("true")&& !profileSession.GetSharedPreferences(ProfileSessionManager.KEY_ID).equals("") && !profileSession.GetSharedPreferences(ProfileSessionManager.KEY_ID).equals("null"))
	      {
	    		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
	    		startActivity(intent);
				finish();
	      }
   	      
    }
    public void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

    public void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			
			mGoogleApiClient.disconnect();
		}
		
		
	}
	
	public void onDestory(){
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();
		}
		
		
		
		
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
			 if(regId!=null|| !regId.equals(""))
			 {
				 String tempregid2=regId;
				
			 }
		//	Toast.makeText(getApplicationContext(), "Device token:- \n"+regId, Toast.LENGTH_LONG).show();
		} else {
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
	
	
	
	
	
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		 if (resultCode == RESULT_OK) {
	           
	            	if(facebookClick == 1){
	        			facebook.authorizeCallback(requestCode, resultCode, data);
	        			
	        			//String getUserEmailId = getEmail();
	        			//ProSessionManager.createProfileSession("1", "", "", "", getUserEmailId);
	        			//new PostRegistration().execute();
	        		}
	        		
	        		if (requestCode == RC_SIGN_IN && googlePlusClick == 1) {
	        			if (resultCode != RESULT_OK) {
	        				mSignInClicked = false;
	        			}

	        			mIntentInProgress = false;

	        			if (!mGoogleApiClient.isConnecting()) {
	        				mGoogleApiClient.connect();
	        			}
	        		}
	        }
	    			}
	

		class LoadLogin extends AsyncTask<String, String, String> {
	    	CustomProgressDialog pDialog;
	    	String webUserId,webUserName,webUserEmail,webMobile,webuserPic,webUserLatitude,webUserLongitude,webTotalPoint;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new CustomProgressDialog(LoginActivity.this);
				
				pDialog.show();

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
	    		
						String loginUrl = Url.GetUrl(Serviceurl.login + "email=" + userEmail+ "&password=" + userPass + "&device_token=" +regId + "&device_user="+"Android");
						
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
							profileSession.SetSharedPreferences(ProfileSessionManager.KEY_ID, webUserId);
							profileSession.SetSharedPreferences(ProfileSessionManager.KEY_USERNAME, webUserName);
							profileSession.SetSharedPreferences(ProfileSessionManager.KEY_EMAIL, webUserEmail);
							profileSession.SetSharedPreferences(ProfileSessionManager.KEY_MOBILE, webMobile);
							profileSession.SetSharedPreferences(ProfileSessionManager.KEY_UserPic, webuserPic);
							profileSession.SetSharedPreferences(ProfileSessionManager.KEY_userlatitude, webUserLatitude);
							profileSession.SetSharedPreferences(ProfileSessionManager.KEY_userlongitude, webUserLongitude);
							profileSession.SetSharedPreferences(ProfileSessionManager.KEY_userTotalPoint, webTotalPoint);
							profileSession.SetSharedPreferences(ProfileSessionManager.KEY_Password, userPass);
							
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
	                		ct.showToastSuccess("Successfully Login");
	                		profileSession.SetSharedPreferences(ProfileSessionManager.KEY_LoginStatus, "true");
							
	                		Intent intent = new Intent(LoginActivity.this,MainActivity.class);
	                		startActivity(intent);
	                		finish();
	                	}

	                }
	            });
	            
	         }
		}
		
	  
    
    public final static boolean isValidEmail(CharSequence target) {
	    if (target == null) {
	        return false;
	    } else {
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    }
	}
    /************************************************** login  with facebook ******************************************/
	 /**
		 * Function to login into facebook
		 * */
		public void loginToFacebook() {

			mPrefs = getPreferences(MODE_PRIVATE);
			String access_token = mPrefs.getString("access_token", null);
			long expires = mPrefs.getLong("access_expires", 0);

			if (access_token != null) {
				facebook.setAccessToken(access_token);
				getFacebookProfileInformation();
				Log.d("FB Sessions", "" + facebook.isSessionValid());
			}

			if (expires != 0) {
				facebook.setAccessExpires(expires);
			}

			if (!facebook.isSessionValid()) {
				facebook.authorize(this,
						new String[] { "email", "publish_stream" },
						new DialogListener() {

							@Override
							public void onCancel() {
								// Function to handle cancel event
							}

							@Override
							public void onComplete(Bundle values) {
								// Function to handle complete event
								// Edit Preferences and update facebook acess_token
								SharedPreferences.Editor editor = mPrefs.edit();
								editor.putString("access_token",facebook.getAccessToken());
								editor.putLong("access_expires",facebook.getAccessExpires());
								editor.commit();
								
								getFacebookProfileInformation();
							}

							@Override
							public void onError(DialogError error) {
								// Function to handle error

							}

							@Override
							public void onFacebookError(FacebookError fberror) {
								// Function to handle Facebook errors

							}

						});
			}
		}
		
		/**
		 * Get Profile information by making request to Facebook Graph API
		 * */
		public void getFacebookProfileInformation() {
			mAsyncRunner.request("me", new RequestListener() {
				@Override
				public void onComplete(String response, Object state) {
					Log.d("Profile", response);
					String json = response;
					try {
						// Facebook Profile JSON data
						JSONObject profile = new JSONObject(json);
						
						// getting name of the user
						final String name = profile.getString("name");
						final String facebookid = profile.getString("id");
						//final String url=profile.getString(name);
						
						final String email = "";//profile.getString("email");
						  strUserPic = "https://graph.facebook.com/" + facebookid + "/picture?type=large";
						if(facebookid!=null && !facebookid.equals(""))
						{
							// profileSession.SetSharedPreferences(ProfileSessionManager.KEY_UserPic, strUserPic);
									
						}
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								loginFunction(name, email,strUserPic);
								//Toast.makeText(getApplicationContext(), "Name: " + name + "\nEmail: " + email, Toast.LENGTH_LONG).show();
							}

						});

						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onIOException(IOException e, Object state) {
				}

				@Override
				public void onFileNotFoundException(FileNotFoundException e,
						Object state) {
				}

				@Override
				public void onMalformedURLException(MalformedURLException e,
						Object state) {
				}

				@Override
				public void onFacebookError(FacebookError e, Object state) {
				}
			});
		}
		
		private void loginFunction(String name, String email,String userpic){
			
			String tempname=name;
			String tempemail=email;
			if(googlePlusClick==1)
			{
				
				if(profileSession.GetSharedPreferences(ProfileSessionManager.KEY_ID)!=null && !profileSession.GetSharedPreferences(ProfileSessionManager.KEY_ID).equals("") && !profileSession.GetSharedPreferences(ProfileSessionManager.KEY_ID).equals("null")   && profileSession.GetSharedPreferences(ProfileSessionManager.KEY_EMAIL)!=null && !profileSession.GetSharedPreferences(ProfileSessionManager.KEY_EMAIL).equals("")&&!profileSession.GetSharedPreferences(ProfileSessionManager.KEY_EMAIL).equals("null") )
				
					{
					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
					intent.putExtra("name", name);
					intent.putExtra("email", email);
					intent.putExtra("userpic", userpic);
					startActivity(intent);
						}
				else
				{
					Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
					intent.putExtra("name", name);
					intent.putExtra("email", email);
					intent.putExtra("userpic", userpic);
					startActivity(intent);
				}
				profileSession.SetSharedPreferences(ProfileSessionManager.KEY_LoginWith, "gplus");
				finish();
			}
			if(facebookClick==1)
			{
				profileSession.SetSharedPreferences(ProfileSessionManager.KEY_LoginWith, "fb");
				if(profileSession.GetSharedPreferences(ProfileSessionManager.KEY_ID)!=null && !profileSession.GetSharedPreferences(ProfileSessionManager.KEY_ID).equals("") && !profileSession.GetSharedPreferences(ProfileSessionManager.KEY_ID).equals("null")   && profileSession.GetSharedPreferences(ProfileSessionManager.KEY_EMAIL)!=null && !profileSession.GetSharedPreferences(ProfileSessionManager.KEY_EMAIL).equals("")&&!profileSession.GetSharedPreferences(ProfileSessionManager.KEY_EMAIL).equals("null") )
					
				{
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.putExtra("name", name);
				intent.putExtra("email", email);
				intent.putExtra("userpic", userpic);
				startActivity(intent);
				
					}
			else
			{
				Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
				intent.putExtra("name", name);
				intent.putExtra("email", email);
				intent.putExtra("userpic", userpic);
				startActivity(intent);
			}
				finish();
				
			}
			
		
		}

		
		/************************************************** end login with facebook ***************************************/
		
		/******************************************* google plus login ********************************************/
		
		
		/**
		 * Method to resolve any signin errors
		 * */
		private void resolveSignInError() {
			if (mConnectionResult.hasResolution()) {
				try {
					mIntentInProgress = true;
					mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
				} catch (SendIntentException e) {
					mIntentInProgress = false;
					mGoogleApiClient.connect();
				}
			}
		}

		@Override
		public void onConnectionFailed(ConnectionResult result) {
			if (!result.hasResolution()) {
				GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
						0).show();
				return;
			}

			if (!mIntentInProgress) {
				// Store the ConnectionResult for later usage
				mConnectionResult = result;

				if (mSignInClicked) {
					// The user has already clicked 'sign-in' so we attempt to
					// resolve all
					// errors until the user is signed in, or they cancel.
					resolveSignInError();
				}
			}

		}
		
		@Override
		public void onConnected(Bundle arg0) {
			mSignInClicked = false;
			Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

			// Get user's information
			
				getProfileInformation();
				
			

		}

		/**
		 * Fetching user's information name, email, profile pic
		 * */
		private void getProfileInformation() {
			try {
				if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
					Person currentPerson = Plus.PeopleApi
							.getCurrentPerson(mGoogleApiClient);
					String personName = currentPerson.getDisplayName();
					String personPhotoUrl = currentPerson.getImage().getUrl();
					String personGooglePlusProfile = currentPerson.getUrl();
					String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

					Log.e(TAG, "Name: " + personName + ", plusProfile: "
							+ personGooglePlusProfile + ", email: " + email
							+ ", Image: " + personPhotoUrl);

					//personPhotoUrl = personPhotoUrl.substring(0,personPhotoUrl.length() - 2)+ PROFILE_PIC_SIZE;

					//new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);
					strUserPic = personPhotoUrl.substring(0,
							personPhotoUrl.length() - 2)
							+ PROFILE_PIC_SIZE;
				/*	profileSession.SetSharedPreferences(ProfileSessionManager.KEY_UserPic, personPhotoUrl);
					profileSession.SetSharedPreferences(ProfileSessionManager.KEY_USERNAME, personName);
				*/	//profileSession.createProfileSession("1", personName, "", personPhotoUrl, email);
					loginFunction(personName, email,strUserPic);

				} else {
					Toast.makeText(getApplicationContext(),"Person information is null", Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onConnectionSuspended(int arg0) {
			mGoogleApiClient.connect();
		}
		
		
		/**
		 * Sign-in into google
		 * */
		private void signInWithGplus() {
			if (!mGoogleApiClient.isConnecting()) {
				mSignInClicked = true;
				resolveSignInError();
				
			}
		}
		
		/**
		 * Sign-out from google
		 * */
		@SuppressWarnings("unused")
		private void signOutFromGplus() {
			if (mGoogleApiClient.isConnected()) {
				Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
				mGoogleApiClient.disconnect();
				mGoogleApiClient.connect();
			}
		}

		/**
		 * Revoking access from google
		 * */
		@SuppressWarnings("unused")
		private void revokeGplusAccess() {
			if (mGoogleApiClient.isConnected()) {
				Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
				Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
						.setResultCallback(new ResultCallback<Status>() {
							@Override
							public void onResult(Status arg0) {
								Log.e(TAG, "User access revoked!");
								mGoogleApiClient.connect();
							}

						});
			}
		}
		
		
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
}
