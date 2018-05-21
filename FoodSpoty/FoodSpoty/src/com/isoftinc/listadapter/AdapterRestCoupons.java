package com.isoftinc.listadapter;

import helper.CustomProgressDialog;
import helper.Helper;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.internal.di;
import com.helper.adapters.ImageLoaderisoft;
import com.isoft.foodspoty.CustomToast;
import com.isoft.foodspoty.ProfileActivity;
import com.isoft.foodspoty.R;
import com.isoft.foodspoty.RedeemActivity;
import com.isoft.foodspoty.RestaurantProfileActivity;
import com.isoft.foodspoty.Url;
import com.isoftinc.listadapter.AdapterRedeemCoupans.LoadProfiledata;
import com.isoftinc.listadapter.AdapterRedeemCoupans.sendRequest;
import com.session.ProfileSessionManager;
import com.webservices.ServiceHandler;
import com.webservices.Serviceurl;

import android.app.Activity;
import android.app.Dialog;
import android.app.backup.RestoreObserver;
import android.content.Context;
import android.os.AsyncTask;
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
public class AdapterRestCoupons extends BaseAdapter{
    
    private Activity activity;
    private Context mcontext;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoaderisoft imageLoader; 
    private int m_nlayoutID = 0;
    CustomToast ct;
    String ClickedCoupanId="";
    String ClickedCoupanCode="";
    String Clickedcoupon_name="";
    String Clickedpoints_in="";
    String Clickedpoints_required="";
    String Clickedrestname="";
    Button clickedPeopleBtn;
    String SendStatus="";
    ProfileSessionManager profsession;
    public AdapterRestCoupons(Context context, ArrayList<HashMap<String, String>> d, int listRow) {
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

        ImageView rowOfferBg 	= (ImageView)vi.findViewById(R.id.rowOfferBg);
        Button btnRedeem 		= (Button)vi.findViewById(R.id.btnRedeem);
        TextView rowOfferTxt 	= (TextView)vi.findViewById(R.id.rowOfferTxt);
        
        HashMap<String, String> fragValue = new HashMap<String, String>();
        fragValue = data.get(position);

        
        rowOfferTxt.setText(fragValue.get(RestaurantProfileActivity.Key_Rdcoupon_name));
        
        //rowBackground.setBackgroundResource(bgImg[position]);
        btnRedeem.setOnClickListener(new SendRequestClick(vi, position));
        imageLoader.DisplayImage(fragValue.get(RestaurantProfileActivity.Key_Rdcoupon_image),rowOfferBg,R.drawable.temp_coupons);
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
			
			clickedPeopleBtn=(Button)cview.findViewById(R.id.btnRedeem);
			HashMap<String, String> fragValue = new HashMap<String, String>();
	        fragValue     					  = data.get(pos);
	         ClickedCoupanId=fragValue.get(RestaurantProfileActivity.Key_Rdcoupon_id);
	         ClickedCoupanCode=fragValue.get(RestaurantProfileActivity.Key_Rdcoupon_code);
	         Clickedcoupon_name=fragValue.get(RestaurantProfileActivity.Key_Rdcoupon_name);
	         
	         Clickedpoints_in=fragValue.get(RestaurantProfileActivity.Key_Rdpoints_in);
	         Clickedpoints_required=fragValue.get(RestaurantProfileActivity.Key_Rdpoints_required);
	         Clickedrestname=fragValue.get(RestaurantProfileActivity.Key_RdRestName);
	          
	        		if(ClickedCoupanId!=null && !ClickedCoupanId.equals("null") && !ClickedCoupanId.equals(""))
	        		{
	        			customalertAccept("REDEEM");
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
  				

  	        			String sendrequestUrl = Url.GetUrl(Serviceurl.grab_redeem_it+"user_id="+profsession.GetSharedPreferences(ProfileSessionManager.KEY_ID)+"&offer_coupon_id="+ClickedCoupanId)+"&offer_type="+"coupons";
  	 	        	
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
  	                		new LoadHomeData().execute();
  	                	}

  	                }
  	            });
  	            
  	         }
  	   }
    
   
  
protected void customalertAccept(String Msg)
{
	final Dialog dialog = new Dialog(activity);
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	dialog.setContentView(R.layout.customgrabit);
	TextView text = (TextView) dialog.findViewById(R.id.AlertMsgTextView);
	text.setText(Msg);
	TextView txtcoupancode = (TextView) dialog.findViewById(R.id.txtcoupancode);
	TextView txtofferamt = (TextView) dialog.findViewById(R.id.txtofferamt);
	TextView txtofferinpercent = (TextView) dialog.findViewById(R.id.txtofferinpercent);
	TextView txtofferinrs = (TextView) dialog.findViewById(R.id.txtofferinrs);
	TextView txtlblcoupancode = (TextView) dialog.findViewById(R.id.txtlblcoupancode);
	TextView txtrestname	=(TextView)dialog.findViewById(R.id.txtrestname);
	txtrestname.setText(Clickedrestname);
	txtofferamt.setText(Clickedpoints_required);
	txtcoupancode.setText(Clickedcoupon_name);
	Button btnGrabit = (Button) dialog.findViewById(R.id.btnGrabit);
	btnGrabit.setText("REDEEM");
	txtlblcoupancode.setText("Reedeem Offer");
	if(Clickedpoints_in.equals("rs"))
	{
		txtofferinpercent.setVisibility(View.GONE);
		txtofferinrs.setVisibility(View.VISIBLE);
		
	}
	else
	{
		txtofferinpercent.setVisibility(View.VISIBLE);
		txtofferinrs.setVisibility(View.GONE);
		
	}
	btnGrabit.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			SendStatus="Grab";
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
class LoadHomeData extends AsyncTask<String, String, String> {
	CustomProgressDialog pDialog;
	String webTotalCoupan="0",webTotalConnection="0";
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new CustomProgressDialog(activity);
		
		pDialog.show();

	}

	protected String doInBackground(String... args) {
		String responseStatus = null;
		RestaurantProfileActivity.profileData = new ArrayList<HashMap<String, String>>();
		
		try {
			ServiceHandler sh = new ServiceHandler();
			String struserpicurl=null;
			/*	try {
				struserpicurl = URLEncoder.encode(struserpic.toString(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
				String loginUrl = Url.GetUrl(Serviceurl.restaurant_detail+"restaurant_id="+profsession.GetSharedPreferences(ProfileSessionManager.KEY_SelectedRestorent));
				
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
					RestaurantProfileActivity.webrestaurant_id=jobjdata.getString("restaurant_id");
					RestaurantProfileActivity.webrestaurant_name=jobjdata.getString("restaurant_name");
					RestaurantProfileActivity.webrestaurant_address=jobjdata.getString("restaurant_address");
					RestaurantProfileActivity.webrestaurant_longitude=jobjdata.getString("restaurant_longitude");
					RestaurantProfileActivity.webrestaurant_latitude=jobjdata.getString("restaurant_latitude");
					RestaurantProfileActivity.restlong=jobjdata.getString("restaurant_longitude");
					RestaurantProfileActivity.restlat=jobjdata.getString("restaurant_latitude");
					RestaurantProfileActivity.webrestaurant_opentime=jobjdata.getString("restaurant_opentime");
					RestaurantProfileActivity.webrestaurant_closetime=jobjdata.getString("restaurant_closetime");
					RestaurantProfileActivity.webrestaurant_rating=jobjdata.getString("restaurant_rating");
					
					RestaurantProfileActivity.webrestaurant_mobile1=jobjdata.getString("restaurant_mobile1");
					RestaurantProfileActivity.webrestaurant_contact_mail=jobjdata.getString("restaurant_contact_mail");
					RestaurantProfileActivity.webrestaurant_profile_image=jobjdata.getString("restaurant_cover_image");
					RestaurantProfileActivity.webrestaurant_id=jobjdata.getString("restaurant_id");
					
					
					
					
					}
					for(int i=0;i<jarrResCoupon.length();i++)
					{
						
					JSONObject jobjResCoupan=jarrResCoupon.getJSONObject(i);
				
					HashMap<String , String>conmap=new HashMap<String, String>();
					conmap.put(RestaurantProfileActivity.Key_Rdcoupon_id, jobjResCoupan.getString("coupon_id"));
					conmap.put(RestaurantProfileActivity.Key_Rdcoupon_code, jobjResCoupan.getString("coupon_code"));
					conmap.put(RestaurantProfileActivity.Key_Rdcoupon_name, jobjResCoupan.getString("coupon_name"));
					conmap.put(RestaurantProfileActivity.Key_Rdcoupon_image, jobjResCoupan.getString("coupon_image"));
					conmap.put(RestaurantProfileActivity.Key_Rdpoints_required, jobjResCoupan.getString("points_required"));
					conmap.put(RestaurantProfileActivity.Key_Rdpoints_in, jobjResCoupan.getString("points_in"));
					conmap.put(RestaurantProfileActivity.Key_Rdcoupon_status, jobjResCoupan.getString("coupon_status"));
					conmap.put(RestaurantProfileActivity.Key_Rdcoupon_start_date, jobjResCoupan.getString("coupon_start_date"));
					conmap.put(RestaurantProfileActivity.Key_Rdcoupon_expiry_date, jobjResCoupan.getString("coupon_expiry_date"));
					RestaurantProfileActivity.profileData.add(conmap);
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
            		
            		
            			AdapterRestCoupons listAdapter = new AdapterRestCoupons(activity, RestaurantProfileActivity.profileData ,R.layout.row_profile_coupons);
            			RestaurantProfileActivity.couponGridview.setAdapter(listAdapter);
            		
            	
            	}
            	else {
		   			ct.showToastError("Limited Internet Connectivity");
		   			
            	}

     }
}

}