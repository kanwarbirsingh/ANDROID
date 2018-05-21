package com.isoft.foodspoty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginoptionActivity extends Activity{
	 
	Button btnSignup, btnLogin;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginoption);
        
        btnSignup = (Button)findViewById(R.id.btnSignup);
        btnLogin  = (Button)findViewById(R.id.btnSignin);
        
        btnSignup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
				intent.putExtra("name", "");
				intent.putExtra("email", "");
				intent.putExtra("userpic", "");
				startActivity(intent);
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

}
