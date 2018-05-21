package com.isoftinc.listadapter;

import helper.CustomProgressDialog;
import helper.Helper;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.helper.adapters.ImageLoaderisoft;
import com.isoft.foodspoty.CustomToast;
import com.isoft.foodspoty.PeopleActivity;
import com.isoft.foodspoty.ProfileActivity;
import com.isoft.foodspoty.R;
import com.isoft.foodspoty.Url;
import com.isoftinc.listadapter.AdapterPeople.sendRequest;
import com.session.ProfileSessionManager;
import com.webservices.ServiceHandler;
import com.webservices.Serviceurl;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("unused")
public class AdapterProfileConnection extends BaseAdapter{
    
    private Activity activity;
    private Context mcontext;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoaderisoft imageLoader; 
    private int m_nlayoutID = 0;
    CustomToast ct;
    String clickedPeopleId="";
    String connectionstatus="";
    Button clickedPeopleBtn;
    String SendStatus="";
    ProfileSessionManager profsession;
    public AdapterProfileConnection(Context context, ArrayList<HashMap<String, String>> d, int listRow) {
    	mcontext = context ;
        activity = (Activity) context;
        data	 = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoaderisoft(activity.getApplicationContext());
        ct=new CustomToast(activity);
        profsession=new ProfileSessionManager(activity);
        m_nlayoutID = listRow;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(m_nlayoutID, null);

        ImageView rowUserPic 	= (ImageView)vi.findViewById(R.id.rowUserPic);
        Button btnConnectStatus = (Button)vi.findViewById(R.id.btnConnectStatus);
        TextView rowUserName 	= (TextView)vi.findViewById(R.id.rowUserName);
        TextView rowEarnedPoint = (TextView)vi.findViewById(R.id.rowEarnedPoint); 
        
        HashMap<String, String> fragValue = new HashMap<String, String>();
        fragValue = data.get(position);

        
        rowUserName.setText(fragValue.get(ProfileActivity.Key_Cuser_name));
        rowEarnedPoint.setText("Total Points : "+fragValue.get(ProfileActivity.Key_Ctotal_point));
       if(fragValue.get(ProfileActivity.Key_Cconnection_status).equals("Pending"))
       {
    	   btnConnectStatus.setBackgroundResource(R.drawable.ic_plus); 
       }
       else
       {
    	   btnConnectStatus.setBackgroundResource(R.drawable.ic_correct); 
    	   
       }
        //rowBackground.setBackgroundResource(bgImg[position]);
        imageLoader.DisplayImage(fragValue.get(ProfileActivity.Key_Cuser_pic),rowUserPic,R.drawable.temp_user);
        btnConnectStatus.setOnClickListener(new SendRequestClick(vi, position));
        return vi;
    }
    

    private class SendRequestClick implements OnClickListener {
		View cview;
		int pos;
		public SendRequestClick(View cview,int position) {
			super();
			this.cview=cview;
			pos=position;
		}

		@Override
		public void onClick(View v) {
			
			clickedPeopleBtn=(Button)cview.findViewById(R.id.btnConnectStatus);
			HashMap<String, String> fragValue = new HashMap<String, String>();
	        fragValue     					  = data.get(pos);
	         clickedPeopleId=fragValue.get(ProfileActivity.Key_Cuser_id);
	         connectionstatus=fragValue.get(ProfileActivity.Key_Cconnection_status);
		        
	        		if(clickedPeopleId!=null && !clickedPeopleId.equals("null") && !clickedPeopleId.equals(""))
	        		{
	        			customalertAccept("Reqest Alert");
	        		}
		}
	}
    
    
    class sendRequest extends AsyncTask<String, String, String>
  	 {
    	CustomProgressDialog pDialog;
  	      @Override
  	      protected void onPreExecute() 
  	       {
  	            super.onPreExecute();
  	            pDialog = new CustomProgressDialog(activity);
  	            if(pDialog.isShowing())
  	             {
  	          	    
  	             }
  	             else
  	             {
  	             pDialog.setIndeterminate(false);
  	             pDialog.setCancelable(true);
  	             pDialog.show();
  	             }

  	        }
  	        protected String doInBackground(String... args)
  	        {
  	            String responseStatus = null ;
  	            try {
  	            		   
  	            		ServiceHandler sh = new ServiceHandler();
  				

  	        			String sendrequestUrl = Url.GetUrl(Serviceurl.accept_connection+"user_id="+profsession.GetSharedPreferences(ProfileSessionManager.KEY_ID)+"&from_user_id="+clickedPeopleId+"&status="+SendStatus);
  	 	        	
  						Log.d("Link", sendrequestUrl);
  					
  						String jsonStr = sh.makeServiceCall(sendrequestUrl,ServiceHandler.GET);
		  					if(jsonStr!=null)
		  					{

  						JSONObject jsonObj = new JSONObject(jsonStr);
  						
  						JSONObject response_obj = jsonObj.getJSONObject("response");
  						Log.d("responce", response_obj.toString());

  						
  						String status = response_obj.getString("status");
  						String message = response_obj.getString("message");
  						if(status.equals("1"))
  						{
  							
  							
  						}
  						responseStatus = status + "`" + message;
  					
  					}
  	        	  }catch(Exception ev){
  	            	
  	            }
  	            
  	            return responseStatus;
  	        }
  	 
  	        protected void onPostExecute(final String responseStatus)
  	        {
  	        	activity.runOnUiThread(new Runnable() 
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
  	                		ct.showToastSuccess(getResponseMessage);
  	                		//clickedPeopleBtn.setBackgroundResource(R.drawable.ic_correct);
  	                		new LoadProfiledata().execute();
  	                	}

  	                }
  	            });
  	            
  	         }
  	   }
    
    class LoadProfiledata extends AsyncTask<String, String, String> {
    	CustomProgressDialog pDialog;
    	String webUserId,webUserName,webUserEmail,webMobile,webuserPic,webUserLatitude,webUserLongitude,webTotalPoint,webPassword;
    	String webTotalCoupan="0",webTotalConnection="0";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new CustomProgressDialog(activity);
			
			pDialog.show();

		}

		protected String doInBackground(String... args) {
			String responseStatus = null;
			ProfileActivity.connectionsList = new ArrayList<HashMap<String, String>>();
			ProfileActivity.coupansList = new ArrayList<HashMap<String, String>>();
			
			
			try {
				ServiceHandler sh = new ServiceHandler();
				String struserpicurl=null;
				/*	try {
    				struserpicurl = URLEncoder.encode(struserpic.toString(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
    		
					String loginUrl = Url.GetUrl(Serviceurl.get_profile + "user_id=" + profsession.GetSharedPreferences(ProfileSessionManager.KEY_ID));
					
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
						webTotalPoint=jobjdata.getString("total_point");
						profsession.SetSharedPreferences(ProfileSessionManager.KEY_ID, webUserId);
						profsession.SetSharedPreferences(ProfileSessionManager.KEY_USERNAME, webUserName);
						profsession.SetSharedPreferences(ProfileSessionManager.KEY_EMAIL, webUserEmail);
						profsession.SetSharedPreferences(ProfileSessionManager.KEY_MOBILE, webMobile);
						profsession.SetSharedPreferences(ProfileSessionManager.KEY_UserPic, webuserPic);
						profsession.SetSharedPreferences(ProfileSessionManager.KEY_userlatitude, webUserLatitude);
						profsession.SetSharedPreferences(ProfileSessionManager.KEY_userlongitude, webUserLongitude);
						profsession.SetSharedPreferences(ProfileSessionManager.KEY_userTotalPoint, webTotalPoint);
						profsession.SetSharedPreferences(ProfileSessionManager.KEY_Password, webPassword);
						}
						for(int i=0;i<jarrconnection.length();i++)
						{
						JSONObject jobjcon=jarrconnection.getJSONObject(i);
						HashMap<String , String>conmap=new HashMap<String, String>();
						conmap.put(ProfileActivity.Key_Cuser_id, jobjcon.getString("user_id"));
						conmap.put(ProfileActivity.Key_Cuser_name, jobjcon.getString("user_name"));
						conmap.put(ProfileActivity.Key_Cuser_email, jobjcon.getString("user_email"));
						conmap.put(ProfileActivity.Key_Cmobile, jobjcon.getString("mobile"));
						conmap.put(ProfileActivity.Key_Cpassword, jobjcon.getString("password"));
						conmap.put(ProfileActivity.Key_Cuser_pic, jobjcon.getString("user_pic"));
						conmap.put(ProfileActivity.Key_Cuser_longitude, jobjcon.getString("user_longitude"));
						conmap.put(ProfileActivity.Key_Cuser_latitude, jobjcon.getString("user_latitude"));
						conmap.put(ProfileActivity.Key_Ctotal_point, jobjcon.getString("total_point"));
						conmap.put(ProfileActivity.Key_Cuser_latitude, jobjcon.getString("user_latitude"));
						conmap.put(ProfileActivity.Key_Cconnection_status, jobjcon.getString("connection_status"));
						ProfileActivity.connectionsList.add(conmap);
						}
						
						for(int i=0;i<jarrRedeemCoupon.length();i++)
						{
						JSONObject jobjcoupan=jarrRedeemCoupon.getJSONObject(i);
						HashMap<String , String>coupmap=new HashMap<String, String>();
						coupmap.put(ProfileActivity.Key_Rdcoupon_id, jobjcoupan.getString("coupon_id"));
						coupmap.put(ProfileActivity.Key_Rdrestaurant_id, jobjcoupan.getString("restaurant_id"));
						coupmap.put(ProfileActivity.Key_Rdcoupon_code, jobjcoupan.getString("coupon_code"));
						coupmap.put(ProfileActivity.Key_Rdcoupon_name, jobjcoupan.getString("coupon_name"));
						coupmap.put(ProfileActivity.Key_Rdcoupon_image, jobjcoupan.getString("coupon_image"));
						coupmap.put(ProfileActivity.Key_Rdpoints_required, jobjcoupan.getString("points_required"));
						coupmap.put(ProfileActivity.Key_Rdpoints_in, jobjcoupan.getString("points_in"));
						coupmap.put(ProfileActivity.Key_Rdpoints_taken, jobjcoupan.getString("points_taken"));
						coupmap.put(ProfileActivity.Key_Rddelivery_status, jobjcoupan.getString("delivery_status"));
						ProfileActivity.coupansList.add(coupmap);
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
        	activity.runOnUiThread(new Runnable() 
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
                			AdapterProfileConnection listAdapter = new AdapterProfileConnection(activity, ProfileActivity.connectionsList ,R.layout.row_profile_connection);
                			ProfileActivity.connectedListview.setAdapter(listAdapter);
    						AdapterProfileCoupons listAdaptergrid = new AdapterProfileCoupons(activity, ProfileActivity.coupansList ,R.layout.row_profile_coupons);
    						ProfileActivity.couponGridview.setAdapter(listAdaptergrid);
    						ProfileActivity.txtuserName.setText(webUserName);
    						ProfileActivity.txtuserTotalpoint.setText("TOTAL POINTS: "+webTotalPoint);
    						imageLoader.DisplayImage(webuserPic, ProfileActivity.userPic, R.drawable.ic_user_pic);
    						ProfileActivity.txtConnection.setText(webTotalConnection);
    						ProfileActivity.txtCoupons.setText(webTotalCoupan);
                	
                	}

                }
            });
            
         }
	}
	
  

protected void customalertAccept(String Msg)
{
	final Dialog dialog = new Dialog(activity);
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	dialog.setContentView(R.layout.customrequest);
	TextView text = (TextView) dialog.findViewById(R.id.AlertMsgTextView);
	text.setText(Msg);
	Button btnAccept = (Button) dialog.findViewById(R.id.btnAccept);
	Button btnBlocked = (Button) dialog.findViewById(R.id.btnBlocked);
	Button btnDeleted = (Button) dialog.findViewById(R.id.btnDeleted);
	Button btnPending = (Button) dialog.findViewById(R.id.btnPending);
	if(connectionstatus.equals("Pending"))
	{
		btnAccept.setVisibility(View.VISIBLE);
	}
	else
	{
		btnAccept.setVisibility(View.GONE);
	}
	btnAccept.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			SendStatus="Active";
			if(Helper.checkInternetConnection(activity))
			{
				new sendRequest().execute();	
				dialog.dismiss();
			}
			else
			{
				ct.showToastError(Helper.interneterror);
			}
			
				}
	});
	btnBlocked.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			SendStatus="Blocked";
			if(Helper.checkInternetConnection(activity))
			{
				new sendRequest().execute();	
				dialog.dismiss();
			}
			else
			{
				ct.showToastError(Helper.interneterror);
			}
			
				}
	});
	btnDeleted.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			SendStatus="Deleted";
			if(Helper.checkInternetConnection(activity))
			{
				new sendRequest().execute();
				dialog.dismiss();
			}
			else
			{
				ct.showToastError(Helper.interneterror);
			}
			
				}
	});
	btnPending.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			SendStatus="Pending";
			
			if(Helper.checkInternetConnection(activity))
			{
				new sendRequest().execute();
				dialog.dismiss();
			}
			else
			{
				ct.showToastError(Helper.interneterror);
			}
			
				}
	});
dialog.show();
}

}