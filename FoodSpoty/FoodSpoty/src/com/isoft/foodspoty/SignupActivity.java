package com.isoft.foodspoty;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rangebar.CustomProgressDialog;
import com.session.ProfileSessionManager;
import com.webservices.ServiceHandler;

import com.webservices.Serviceurl;

import helper.Helper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends Activity{
	 
	EditText txtEmail,txtName, txtMobile, txtPassword;
	Button btnCreateAccount, btnLogin;
	
	Button btn_slidemenu, btn_menuback, btn_menuSearch, btn_menuFilter;
	TextView headTitle, txtTermsCondition;
	CustomToast ct;
	String webUserID;
	String termsConditions = "By clicking on Create Account I agree to all the <ul>Terms &amp; Conditions</ul>.";
	ProfileSessionManager profsession;
	
	String userEmail="", userMobile="", userPassword="",strname="",strlatitude="28.005",strLongitude="77.4562",struserpic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ct=new CustomToast(SignupActivity.this);
        profsession	=new ProfileSessionManager(SignupActivity.this);
        txtEmail     = (EditText)findViewById(R.id.txtEmail);
        txtName		 = (EditText)findViewById(R.id.txtName);
        txtMobile    = (EditText)findViewById(R.id.txtMobile);
        txtPassword  = (EditText)findViewById(R.id.txtPassword);
        txtTermsCondition = (TextView)findViewById(R.id.txtTermsCondition);
        txtTermsCondition.setText(Html.fromHtml(termsConditions));
        btnCreateAccount    = (Button)findViewById(R.id.btnCreateAccount);
        btnLogin  			= (Button)findViewById(R.id.btnLogin);
        
        btn_slidemenu  = (Button)findViewById(R.id.btn_slidemenu);
        btn_menuback   = (Button)findViewById(R.id.btn_menuback);
        btn_menuSearch = (Button)findViewById(R.id.btn_menuSearch);
        btn_menuFilter = (Button)findViewById(R.id.btn_menuFilter);
        headTitle	   = (TextView)findViewById(R.id.headTitle);

        btn_slidemenu.setVisibility(View.GONE);
        btn_menuback.setVisibility(View.INVISIBLE);
        btn_menuSearch.setVisibility(View.INVISIBLE);
        btn_menuFilter.setVisibility(View.GONE);
        headTitle.setText("Sign Up");
        Intent oIntent = getIntent();
		 
		 try{
			  strname = oIntent.getExtras().getString("name");
			  userEmail = oIntent.getExtras().getString("email");
			  struserpic = oIntent.getExtras().getString("userpic");
			  String tempstrname=oIntent.getExtras().getString("name");
					  String Tempemail= oIntent.getExtras().getString("email");
					  String tempuserpic=oIntent.getExtras().getString("userpic");
			  txtEmail.setText(userEmail);
			  txtName.setText(strname);
			 
		 }catch(Exception ev){
			 
		 }
        GPSTracker gps=new GPSTracker(SignupActivity.this);
        if(gps.canGetLocation)
        {
        	strlatitude=String.valueOf(gps.getLatitude());
        	strLongitude=String.valueOf(gps.getLongitude());
        }
        
        
        btnCreateAccount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				strname		=txtName.getText().toString().trim();
				userEmail	= txtEmail.getText().toString().trim();
				userMobile	= txtMobile.getText().toString().trim();
				userPassword= txtPassword.getText().toString().trim();
				
				if (strname.replace(" ", "").length()==0) {
					//Toast.makeText(getApplicationContext(),"Enter Email", Toast.LENGTH_LONG).show();
					ct.showToastError("Please enter Name");
				}
				else if (userEmail.replace(" ", "").length()==0) {
					//Toast.makeText(getApplicationContext(),"Enter Email", Toast.LENGTH_LONG).show();
					ct.showToastError("Please enter email-id");
				}
				else if (!isValidEmail(userEmail)){
					//Toast.makeText(getApplicationContext(),"Please fill valid email",Toast.LENGTH_LONG).show();
					ct.showToastError("Please enter valid email-id");
				} else if(userMobile.replace(" ", "").length()==0){
					//Toast.makeText(getApplicationContext(), "Enter Mobile", Toast.LENGTH_LONG).show();
					ct.showToastError("Please enter mobile number");
				}
				else if(userMobile.replace(" ", "").length()<10){
					//Toast.makeText(getApplicationContext(), "Enter Mobile", Toast.LENGTH_LONG).show();
					ct.showToastError("Please enter valid mobile number");
				}
				else if(userPassword.replace(" ", "").length()==0){
				//	Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_LONG).show();
					ct.showToastError("Please enter password");
				}else if (userPassword.length() < 3) {
					//Toast.makeText(getApplicationContext(),"Atleast 3 digit",Toast.LENGTH_LONG).show();
					ct.showToastError("Please should be atleast 3 digit");
				}else {
					if (!Helper.checkInternetConnection(getApplicationContext())){
						//Toast.makeText(getApplicationContext(), "Network Unavailable", Toast.LENGTH_LONG).show();
						ct.showToastError(Helper.interneterror);
						
					} else {
						try {
						 	new LoadSignup().execute();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

		});
        
        btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(intent);
			}
		});
        
    }
    
	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}
    
    class LoadSignup extends AsyncTask<String, String, String> {
    	CustomProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new CustomProgressDialog(SignupActivity.this);
			
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
    		
					String loginUrl = Url.GetUrl(Serviceurl.create_profile + "user_name=" + strname+ "&user_email=" + userEmail + "&mobile=" + userMobile + "&password="+userPassword+"&user_pic="+struserpic+"&user_longitude="+strLongitude+"&user_latitude="+strlatitude);
					
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
						webUserID= response_obj.getString("user_id");
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
                		ct.showToastSuccess("Successfully created account");
                		profsession.SetSharedPreferences(ProfileSessionManager.KEY_ID, webUserID);
                		profsession.SetSharedPreferences(ProfileSessionManager.KEY_EMAIL, userEmail);
                		profsession.SetSharedPreferences(ProfileSessionManager.KEY_MOBILE, userMobile);
                		profsession.SetSharedPreferences(ProfileSessionManager.KEY_Password, userPassword);
                		
                		new LoadLogin().execute();
                		
                	     	}

                }
            });
            
         }
	}
	
	class LoadLogin extends AsyncTask<String, String, String> {
    	CustomProgressDialog pDialog;
    	String webUserId,webUserName,webUserEmail,webMobile,webuserPic,webUserLatitude,webUserLongitude,webTotalPoint;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new CustomProgressDialog(SignupActivity.this);
			
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
    		
					String loginUrl = Url.GetUrl(Serviceurl.login + "email=" + userEmail+ "&password=" + userPassword + "&device_token=" + "" + "&device_user="+"Android");
					
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
                		profsession.SetSharedPreferences(ProfileSessionManager.KEY_Password, userPassword);
                		profsession.SetSharedPreferences(ProfileSessionManager.KEY_LoginStatus, "true");
						
                		Intent intent = new Intent(SignupActivity.this,MainActivity.class);
                		startActivity(intent);
                		finish();
                	}

                }
            });
            
         }
	}
	
  
}
