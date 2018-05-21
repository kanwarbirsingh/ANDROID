package com.isoftinc.listadapter;

import helper.CustomProgressDialog;
import helper.Helper;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.helper.adapters.ImageLoaderisoft;
import com.isoft.foodspoty.CustomToast;
import com.isoft.foodspoty.OffersActivity;
import com.isoft.foodspoty.ProfileActivity;
import com.isoft.foodspoty.R;
import com.isoft.foodspoty.RedeemActivity;
import com.isoft.foodspoty.Url;
import com.isoftinc.listadapter.AdapterOffers.LoadProfiledata;
import com.isoftinc.listadapter.AdapterOffers.sendRequest;
import com.session.ProfileSessionManager;
import com.webservices.ServiceHandler;
import com.webservices.Serviceurl;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.SyncStateContract.Helpers;
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
public class AdapterRedeemCoupans extends BaseAdapter{
    
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
    String ClickedCurrentpoints="";
    Button clickedPeopleBtn;
    String SendStatus="";
    ProfileSessionManager profsession;
    public AdapterRedeemCoupans(Context context, ArrayList<HashMap<String, String>> d, int listRow) {
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

        
        rowOfferTxt.setText(fragValue.get(RedeemActivity.Key_coupon_name));
        
        //rowBackground.setBackgroundResource(bgImg[position]);
       
        imageLoader.DisplayImage(fragValue.get(RedeemActivity.Key_coupon_image),rowOfferBg,R.drawable.temp_coupons);
        btnRedeem.setOnClickListener(new SendRequestClick(vi, position));
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
	         ClickedCoupanId=fragValue.get(RedeemActivity.Key_coupon_id);
	         ClickedCoupanCode=fragValue.get(RedeemActivity.Key_coupon_code);
	         Clickedcoupon_name=fragValue.get(RedeemActivity.Key_coupon_name);
	         Clickedpoints_in=fragValue.get(RedeemActivity.Key_points_in);
	         Clickedpoints_required=fragValue.get(RedeemActivity.Key_points_required);
	         ClickedCurrentpoints=fragValue.get(RedeemActivity.Key_currentpoints);
	         if(!ClickedCurrentpoints.equals("")&&!ClickedCurrentpoints.equals("null"))
	         {
	        if(Double.parseDouble(ClickedCurrentpoints)<Double.parseDouble(fragValue.get(RedeemActivity.Key_points_required)))
	        		 {
	        	 customalertbtn("You don't have enough point for use it");
	        		 }
			        else
			        {
	        	if(ClickedCoupanId!=null && !ClickedCoupanId.equals("null") && !ClickedCoupanId.equals(""))
        		{
        			customalertAccept("REDEEM");
        		} 
			        }
	         	}
	         else
	         {
	        	 customalertbtn("You don't have enough point for use it");
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
  	                		new LoadProfiledata().execute();
  	                		customalertbtn("Congratulations you have redeemed "+Clickedpoints_required+" for "+Clickedcoupon_name);
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
	TextView txtrestname=(TextView)dialog.findViewById(R.id.txtrestname);
	txtrestname.setVisibility(View.GONE);
	
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

protected void customalertbtn(String Msg)
{
final Dialog dialog = new Dialog(activity);
dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
dialog.setContentView(R.layout.custom_alertmsg1);
TextView text = (TextView) dialog.findViewById(R.id.AlertMsgTextView);
text.setText(Msg);
Button dialogButton = (Button) dialog.findViewById(R.id.AlertMsgOk);
dialogButton.setOnClickListener(new OnClickListener() {
	@Override
	public void onClick(View v) {
		
		dialog.dismiss();
	//	getLocation();
	}
});
dialog.show();
}
class LoadProfiledata extends AsyncTask<String, String, String> {
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
		RedeemActivity.redeemData = new ArrayList<HashMap<String, String>>();
		
		
		
		try {
			ServiceHandler sh = new ServiceHandler();
			String struserpicurl=null;
			/*	try {
				struserpicurl = URLEncoder.encode(struserpic.toString(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
				String loginUrl = Url.GetUrl(Serviceurl.get_redeem + "user_id=" + profsession.GetSharedPreferences(ProfileSessionManager.KEY_ID));
				
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
						JSONObject jobdata=jarrdata.getJSONObject(i);
						JSONArray jarrcashBack=jobdata.getJSONArray("cashBack");
						
						for(int i1=0;i1<jarrcashBack.length();i1++)
						{
						JSONObject jobcashback=jarrcashBack.getJSONObject(i1);
						HashMap<String , String>cashback=new HashMap<String, String>();
						cashback.put(RedeemActivity.Key_coupon_id, jobcashback.getString("coupon_id"));
						cashback.put(RedeemActivity.Key_coupon_code, jobcashback.getString("coupon_code"));
						cashback.put(RedeemActivity.Key_coupon_name, jobcashback.getString("coupon_name"));
						cashback.put(RedeemActivity.Key_points_required, jobcashback.getString("points_required"));
						cashback.put(RedeemActivity.Key_points_in, jobcashback.getString("points_in"));
						cashback.put(RedeemActivity.Key_coupon_image, jobcashback.getString("coupon_image"));
						//cashback.put(Key_offer_Address, "Address N/A");
						
						RedeemActivity.redeemData.add(cashback);
						}
						
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
            		
            		AdapterRedeemCoupans listAdapter = new AdapterRedeemCoupans(activity, RedeemActivity.redeemData ,R.layout.row_redeem);
            		RedeemActivity.redeemGridview.setAdapter(listAdapter);
					
            	
            	}

            }
        });
        
     }
}

}