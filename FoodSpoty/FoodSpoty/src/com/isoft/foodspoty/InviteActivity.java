package com.isoft.foodspoty;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.plus.PlusShare;

import com.isoftinc.listadapter.AdapterInvite;
import com.isoftinc.listadapter.AdapterPeople;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("unused")
public class InviteActivity extends Activity{
	 
	ListView inviteListview;
    private ProgressDialog pDialog;
	private ArrayList<HashMap<String, String>> socialData;
	
	Button btn_slidemenu, btn_menuback, btn_menuSearch, btn_menuFilter;
	TextView headTitle;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
                
        inviteListview = (ListView)findViewById(R.id.inviteListview);

        btn_slidemenu  = (Button)findViewById(R.id.btn_slidemenu);
        btn_menuback   = (Button)findViewById(R.id.btn_menuback);
        btn_menuSearch = (Button)findViewById(R.id.btn_menuSearch);
        btn_menuFilter = (Button)findViewById(R.id.btn_menuFilter);
        headTitle	   = (TextView)findViewById(R.id.headTitle);

        btn_slidemenu.setVisibility(View.GONE);
        btn_menuback.setVisibility(View.VISIBLE);
        btn_menuSearch.setVisibility(View.INVISIBLE);
        btn_menuFilter.setVisibility(View.GONE);
        headTitle.setText("Invite People");
        
        new LoadData().execute();
        
        btn_menuback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
        
        inviteListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				if(pos==0)
				{
					sendingFacebook();
				}
				else if(pos==1)
				{
 
			       String msg = "Hi , your friend has invited you to sign up at www.foodspoty.com for booking food online. Thanks!";
			      /*  Uri uri = Uri
			        .parse("android.resource://com.code2care.example.sharetextandimagetwitter/drawable/mona");
			      */  Intent intent = new Intent();
			        intent.setAction(Intent.ACTION_SEND);
			        intent.putExtra(Intent.EXTRA_TEXT, msg);
			        intent.setType("text/plain");
			    //    intent.putExtra(Intent.EXTRA_STREAM, uri);
			        intent.setType("image/jpeg");
			        intent.setPackage("com.twitter.android");
			        startActivity(intent);
				}
				else if(pos==2)
				{
					  Intent shareIntent = new PlusShare.Builder(InviteActivity.this)
			          .setType("text/plain")
			          .setText("Hi , your friend has invited you to sign up at www.foodspoty.com for booking food online. Thanks!")
			          .setContentUrl(Uri.parse("https://www.i-softinc.com/+/"))
			          .getIntent();

			      startActivityForResult(shareIntent, 0);

				}
				else if(pos==3)
				{
					sendingEmail();
				}
			}
		});
    }

    protected void sendingFacebook() {
		
	
	
		String text = "Hi , your friend has invited you to sign up at www.i-softinc.com for booking food online. Thanks!";
		String uri = "https://www.facebook.com"+text;
		
	    String shareString = ""+ Uri.parse(uri);
	   
	   
	    
	     try {
	      Intent i = new Intent(android.content.Intent.ACTION_SEND);
	      i.setType("text/plain");
	      i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Food Spoty");
	      i.putExtra(android.content.Intent.EXTRA_TEXT, "" + Uri.parse(uri));
	  	
	      i.setPackage("com.facebook.katana");
		startActivity(i);
	      startActivity(Intent.createChooser(i, Html.fromHtml("Share")));
	     } catch (Exception e) {
	      e.printStackTrace();
	      Toast.makeText(InviteActivity.this, "Not Supported", Toast.LENGTH_LONG).show();
	     }
		
		
	
	
	
	}
	public void sendingEmail() {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_SUBJECT, "Regarding to School Connect App");
		i.putExtra(Intent.EXTRA_TEXT,
				"");
		try {
			startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(InviteActivity.this, "There are no email clients installed.",
					Toast.LENGTH_SHORT).show();
		}

	}

	
    /*
     * Start server data load for tab selection.
     * 
     */
    class LoadData extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(InviteActivity.this);
			pDialog.setMessage("Loading. Please wait ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			try {
					socialData = new ArrayList<HashMap<String, String>>();
					
					String[] socialTitle = {"Facebook", "Twitter", "Google Plus", "Email"};
					for (int i = 0; i < socialTitle.length; i++) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("SOCIAL_ID", ""+i);
						map.put("SOCIAL_PIC", "");
						map.put("SOCIAL_TITLE", socialTitle[i]);
	
						socialData.add(map);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			return null;
		}

		protected void onPostExecute(String file_url) {

			runOnUiThread(new Runnable() {
				public void run() {
					
					AdapterInvite listAdapter = new AdapterInvite(InviteActivity.this, socialData ,R.layout.row_invite);
					inviteListview.setAdapter(listAdapter);
					pDialog.dismiss();
				}
			});
		}
		
	}
    
    /*
     * 
     */
    
}
