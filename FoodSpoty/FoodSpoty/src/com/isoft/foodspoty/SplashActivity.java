package com.isoft.foodspoty;

import com.session.ProfileSessionManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity{
	RelativeLayout rlparent;
	 // Splash screen timer
    private static int SPLASH_TIME_OUT = 500;
    TextView txtwebsitelink;
	ProgressDialog pDialog ;
 ProfileSessionManager profileSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rlparent=(RelativeLayout)findViewById(R.id.rlparent);
        txtwebsitelink=(TextView)findViewById(R.id.txtwebsitelink);
        profileSession=new ProfileSessionManager(SplashActivity.this);
        txtwebsitelink.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(profileSession.isNetworkAvailable()){
					Uri uri = (Uri.parse(Url.GetUrl("http://www.spotye.com")));
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				}else{
				 Toast.makeText(SplashActivity.this,"Network not available", 1).show();
			  }		
			}
		});
        rlparent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				 if(profileSession.GetSharedPreferences(ProfileSessionManager.KEY_LoginStatus).equals("true")&& !profileSession.GetSharedPreferences(ProfileSessionManager.KEY_ID).equals("") && !profileSession.GetSharedPreferences(ProfileSessionManager.KEY_ID).equals("null"))
            	 {
       	    		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
       	    		intent.putExtra("ActivityName", "HomeActivity");
       	    		startActivity(intent);
       				finish();
       	      	}
            	 else
            	 {
            		   Intent intent = new Intent(SplashActivity.this, LoginoptionActivity.class);
                       intent.putExtra("ActivityName", "HomeActivity");
                       startActivity(intent);
                       finish();
            	 }
			}
		});
        new Handler().postDelayed(new Runnable() {
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
            	 if(profileSession.GetSharedPreferences(ProfileSessionManager.KEY_LoginStatus).equals("true")&& !profileSession.GetSharedPreferences(ProfileSessionManager.KEY_ID).equals("") && !profileSession.GetSharedPreferences(ProfileSessionManager.KEY_ID).equals("null"))
            	 {
       	    		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
       	    		intent.putExtra("ActivityName", "HomeActivity");
       	    		startActivity(intent);
       				finish();
       	      	}
            	 else
            	 {
            		   Intent intent = new Intent(SplashActivity.this, LoginoptionActivity.class);
                       intent.putExtra("ActivityName", "HomeActivity");
                       startActivity(intent);
                       finish();
            	 }
             
            }
        }, SPLASH_TIME_OUT);
    }
    
}
