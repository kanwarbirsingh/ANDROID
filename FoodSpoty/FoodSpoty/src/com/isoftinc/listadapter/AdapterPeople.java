package com.isoftinc.listadapter;

import helper.CustomProgressDialog;

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
import com.session.ProfileSessionManager;
import com.webservices.ServiceHandler;
import com.webservices.Serviceurl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("unused")
public class AdapterPeople extends BaseAdapter{
    
    private Activity activity;
    private Context mcontext;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoaderisoft imageLoader; 
    private int m_nlayoutID = 0;
    CustomToast ct;
    String clickedPeopleId="";
    Button clickedPeopleBtn;
    ProfileSessionManager profsession;
    public AdapterPeople(Context context, ArrayList<HashMap<String, String>> d, int listRow) {
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
        
        HashMap<String, String> fragValue = new HashMap<String, String>();
        fragValue = data.get(position);

        
        rowUserName.setText(fragValue.get(PeopleActivity.Key_Puser_name));
        
        if(fragValue.get(PeopleActivity.Key_Pconnection_status).equals("no request"))
        {
        	btnConnectStatus.setBackgroundResource(R.drawable.ic_plus);
        		
        }
        else
        {
        	btnConnectStatus.setBackgroundResource(R.drawable.ic_correct);
        }
     	imageLoader.DisplayImage(fragValue.get(PeopleActivity.Key_Puser_pic),rowUserPic,R.drawable.ic_user_pic);
        
        btnConnectStatus.setOnClickListener(new SendRequestClick(vi, position));
        //rowBackground.setBackgroundResource(bgImg[position]);
        //
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
	        clickedPeopleId=fragValue.get(PeopleActivity.Key_Puser_id);
	        if(fragValue.get(PeopleActivity.Key_Pconnection_status).equals("no request"))
	        {
	        	if(clickedPeopleId!=null && !clickedPeopleId.equals("null") && !clickedPeopleId.equals(""))
        		{
        			new sendRequest().execute();
        		}	
	        }
	        else
	        {
	        	ct.showToastError("Allready send friend request");
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
  				

  	        			String sendrequestUrl = Url.GetUrl(Serviceurl.sendrequestpeople_connection+"request_to_user_id="+clickedPeopleId+"&request_from_user_id="+profsession.GetSharedPreferences(ProfileSessionManager.KEY_ID));
  	 	        	
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
  	                		ct.showToastError(getResponseMessage);
  	                		clickedPeopleBtn.setBackgroundResource(R.drawable.ic_correct);
  	                	
  	                	}

  	                }
  	            });
  	            
  	         }
  	   }
    
}