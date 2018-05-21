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
import com.isoft.foodspoty.Url;
import com.isoftinc.listadapter.AdapterProfileConnection.LoadProfiledata;
import com.isoftinc.listadapter.AdapterProfileConnection.sendRequest;
import com.session.ProfileSessionManager;
import com.webservices.ServiceHandler;
import com.webservices.Serviceurl;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("unused")
public class AdapterOffers extends BaseAdapter{
    
    private Activity activity;
    private Context mcontext;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoaderisoft imageLoader; 
    
    private int m_nlayoutID = 0;
    private int[] bgImg = {R.drawable.temp_restaurant_1,R.drawable.temp_restaurant_2,R.drawable.temp_restaurant_3, R.drawable.temp_restaurant_1};
    CustomToast ct;
    String ClickedOfferID="";
    String ClickedOfferCode="";
    String ClickedOfferIn="";
    String ClickedOfferamt="";
    String ClickedOfferrestname="";
    TextView clickedPeopleBtn;
    String SendStatus="";
    ProfileSessionManager profsession;
    public AdapterOffers(Context context, ArrayList<HashMap<String, String>> d, int listRow) {
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

        ImageView rowBackground = (ImageView)vi.findViewById(R.id.rowBackground);
        Button btnFavorite      = (Button)vi.findViewById(R.id.btnFavorite);
        EditText offerName 	    = (EditText)vi.findViewById(R.id.offerName);
        EditText restAddress    = (EditText)vi.findViewById(R.id.restAddress); 
        TextView btnGrabit 	    = (TextView)vi.findViewById(R.id.btnGrabit); 
        
        HashMap<String, String> fragValue = new HashMap<String, String>();
        fragValue = data.get(position);
   
        
        offerName.setText(fragValue.get(OffersActivity.Key_offer_title));
        restAddress.setText(fragValue.get(OffersActivity.Key_offer_Address));
        btnGrabit.setText("GRAB IT");  
        //rowBackground.setBackgroundResource(bgImg[position]);
        imageLoader.DisplayImage(fragValue.get(OffersActivity.Key_offer_image), rowBackground,R.drawable.temp_restaurant_1);
        btnGrabit.setOnClickListener(new SendRequestClick(vi, position));
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
			
			clickedPeopleBtn=(TextView)cview.findViewById(R.id.btnGrabit);
			HashMap<String, String> fragValue = new HashMap<String, String>();
	        fragValue     					  = data.get(pos);
	         ClickedOfferID=fragValue.get(OffersActivity.Key_offer_id);
	         ClickedOfferCode=fragValue.get(OffersActivity.Key_offer_code);
	         
	         ClickedOfferIn=fragValue.get(OffersActivity.Key_discount_in);
	         ClickedOfferamt=fragValue.get(OffersActivity.Key_offer_discount);
	         ClickedOfferrestname=fragValue.get(OffersActivity.key_restname);
	         
	     //    ClickedOfferamt=fragValue.get(OffersActivity.Key_offer_discount);
	             
	        		if(ClickedOfferID!=null && !ClickedOfferID.equals("null") && !ClickedOfferID.equals(""))
	        		{
	        			customalertAccept("GRAB IT");
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
  				

  	        			String sendrequestUrl = Url.GetUrl(Serviceurl.grab_redeem_it+"user_id="+profsession.GetSharedPreferences(ProfileSessionManager.KEY_ID)+"&offer_coupon_id="+ClickedOfferID)+"&offer_type="+"offers";
  	 	        	
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
	TextView txtrestname=(TextView)dialog.findViewById(R.id.txtrestname);
	txtrestname.setText(ClickedOfferrestname);
	txtofferamt.setText(ClickedOfferamt);
	txtcoupancode.setText(ClickedOfferCode);
	Button btnGrabit = (Button) dialog.findViewById(R.id.btnGrabit);
	if(ClickedOfferIn.equals("rs"))
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
		OffersActivity.Cashbacklist = new ArrayList<HashMap<String, String>>();
		OffersActivity.Dailydeallist = new ArrayList<HashMap<String, String>>();
		OffersActivity.Discountlist = new ArrayList<HashMap<String, String>>();
	
		
		
		try {
			ServiceHandler sh = new ServiceHandler();
			String struserpicurl=null;
			/*	try {
				struserpicurl = URLEncoder.encode(struserpic.toString(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
				String loginUrl = Url.GetUrl(Serviceurl.get_offer + "user_id=" + profsession.GetSharedPreferences(ProfileSessionManager.KEY_ID));
				
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
						JSONArray jarrdiscount=jobdata.getJSONArray("discount");
						JSONArray jarrdailyDeals=jobdata.getJSONArray("dailyDeals");
						
						for(int i1=0;i1<jarrcashBack.length();i1++)
						{
						JSONObject jobcashback=jarrcashBack.getJSONObject(i1);
						HashMap<String , String>cashback=new HashMap<String, String>();
						cashback.put(OffersActivity.Key_offer_id, jobcashback.getString("offer_id"));
						cashback.put(OffersActivity.Key_offer_code, jobcashback.getString("offer_code"));
						cashback.put(OffersActivity.Key_offer_discount, jobcashback.getString("offer_discount"));
						cashback.put(OffersActivity.Key_discount_in, jobcashback.getString("discount_in"));
						cashback.put(OffersActivity.Key_offer_image, jobcashback.getString("offer_image"));
						cashback.put(OffersActivity.Key_offer_title, jobcashback.getString("offer_title"));
						cashback.put(OffersActivity.Key_offer_Address, "Address N/A");
						
						OffersActivity.Cashbacklist.add(cashback);
						}
						for(int id=0;id<jarrdiscount.length();id++)
						{
						JSONObject jobdiscount=jarrdiscount.getJSONObject(id);
						HashMap<String , String>cashback=new HashMap<String, String>();
						cashback.put(OffersActivity.Key_offer_id, jobdiscount.getString("offer_id"));
						cashback.put(OffersActivity.Key_offer_code, jobdiscount.getString("offer_code"));
						cashback.put(OffersActivity.Key_offer_discount, jobdiscount.getString("offer_discount"));
						cashback.put(OffersActivity.Key_discount_in, jobdiscount.getString("discount_in"));
						cashback.put(OffersActivity.Key_offer_image, jobdiscount.getString("offer_image"));
						cashback.put(OffersActivity.Key_offer_title, jobdiscount.getString("offer_title"));
						cashback.put(OffersActivity.Key_offer_Address, "Address N/A");
						
						OffersActivity.Discountlist.add(cashback);
						}
						
						for(int idd=0;idd<jarrdailyDeals.length();idd++)
						{
						JSONObject jobdailyDeals=jarrdailyDeals.getJSONObject(idd);
						HashMap<String , String>cashback=new HashMap<String, String>();
						cashback.put(OffersActivity.Key_offer_id, jobdailyDeals.getString("offer_id"));
						cashback.put(OffersActivity.Key_offer_code, jobdailyDeals.getString("offer_code"));
						cashback.put(OffersActivity.Key_offer_discount, jobdailyDeals.getString("offer_discount"));
						cashback.put(OffersActivity.Key_discount_in, jobdailyDeals.getString("discount_in"));
						cashback.put(OffersActivity.Key_offer_image, jobdailyDeals.getString("offer_image"));
						cashback.put(OffersActivity.Key_offer_title, jobdailyDeals.getString("offer_title"));
						cashback.put(OffersActivity.Key_offer_Address, "Address N/A");
						
						OffersActivity.Dailydeallist.add(cashback);
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
            		
            		if( Helper.SelectedOffertab.equals("Daily"))
            		{
            			AdapterOffers listAdapter = new AdapterOffers(activity, OffersActivity.Dailydeallist ,R.layout.row_offers);
            			OffersActivity.offersListview.setAdapter(listAdapter);
                	
            		}
            		else if( Helper.SelectedOffertab.equals("Discount"))
            		{
            			AdapterOffers listAdapter = new AdapterOffers(activity, OffersActivity.Discountlist ,R.layout.row_offers);
            			OffersActivity.offersListview.setAdapter(listAdapter);
                	
            		}
            		else if( Helper.SelectedOffertab.equals("Cashback"))
            		{
            			AdapterOffers listAdapter = new AdapterOffers(activity, OffersActivity.Cashbacklist ,R.layout.row_offers);
            			OffersActivity.offersListview.setAdapter(listAdapter);
                	
            		}
            	
            	}

            }
        });
        
     }
}


}