package com.isoftinc.listadapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.helper.adapters.ImageLoaderisoft;
import com.isoft.foodspoty.CustomToast;
import com.isoft.foodspoty.OffersActivity;
import com.isoft.foodspoty.R;
import com.isoftinc.fragment.FragmentMain;
import com.session.ProfileSessionManager;
import com.webservices.DatabaseHandlerFav;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressWarnings("unused")
public class FragmentHomeAdapter extends BaseAdapter{
    
    private Activity activity;
    private Context mcontext;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoaderisoft imageLoader; 
    DatabaseHandlerFav dbfav;
    private int m_nlayoutID = 0;
    private int[] bgImg = {R.drawable.temp_restaurant_1,R.drawable.temp_restaurant_2,R.drawable.temp_restaurant_3, R.drawable.temp_restaurant_1};
    CustomToast ct;
    String ClickedrestID="";
    String Clickedrestname="";
    String Clickedrestlat="";
    String Clickedrestlong="";
    Button clickedfavbutton;
    String SendStatus="";
    ProfileSessionManager profsession;
    public FragmentHomeAdapter(Context context, ArrayList<HashMap<String, String>> d, int listRow) {
    	mcontext = context ;
        activity = (Activity) context;
        data	 = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoaderisoft(activity.getApplicationContext());
        dbfav=new DatabaseHandlerFav(activity);
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
        RelativeLayout rlfav	=(RelativeLayout)vi.findViewById(R.id.rlfav);
        TextView restName 	    = (TextView)vi.findViewById(R.id.restName);
        TextView restAddress    = (TextView)vi.findViewById(R.id.restAddress); 
        TextView restType	    = (TextView)vi.findViewById(R.id.restType); 
        TextView distance 	    = (TextView)vi.findViewById(R.id.distance); 
        
        HashMap<String, String> fragValue = new HashMap<String, String>();
        fragValue = data.get(position);

        if(dbfav.CheckExistItemOrNot(fragValue.get(FragmentMain.Key_hrestaurant_id))==1)
        {
        	btnFavorite.setBackgroundResource(R.drawable.ic_heartgreen);
        		
        }
        else     
        {   
        	btnFavorite.setBackgroundResource(R.drawable.ic_heart);
        }
        restName.setText(fragValue.get(FragmentMain.Key_hrestaurant_name));
        restAddress.setText(fragValue.get(FragmentMain.Key_hrestaurant_address));
        restType.setText(fragValue.get(FragmentMain.Key_hrestaurant_type));
        distance.setText(fragValue.get(FragmentMain.Key_hrestorentdistance)+" KMS");
        
        //rowBackground.setBackgroundResource(bgImg[position]);
      imageLoader.DisplayImage(fragValue.get(FragmentMain.Key_hrestaurant_cover_image), rowBackground,R.drawable.temp_restaurant_1);
      btnFavorite.setOnClickListener(new SendRequestClick(vi, position));
      rlfav.setOnClickListener(new SendRequestClick(vi, position));
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
			
			clickedfavbutton=(Button)cview.findViewById(R.id.btnFavorite);
			HashMap<String, String> fragValue = new HashMap<String, String>();
	        fragValue     					  = data.get(pos);
	         ClickedrestID=fragValue.get(FragmentMain.Key_hrestaurant_id);
	         Clickedrestname=fragValue.get(FragmentMain.Key_hrestaurant_name);
	         Clickedrestlong=fragValue.get(FragmentMain.Key_hrestaurant_longitude);
	         Clickedrestlat=fragValue.get(FragmentMain.Key_hrestaurant_latitude);
	         if(dbfav.CheckExistItemOrNot(ClickedrestID)==0)
	         {
	        	 dbfav.AddFilter(ClickedrestID, Clickedrestname, Clickedrestlat, Clickedrestlong);
	        	 clickedfavbutton.setBackgroundResource(R.drawable.ic_heartgreen);
	         }
	         else
	         {
	        	dbfav.DeleteOneService(ClickedrestID);
	        	
	        		 clickedfavbutton.setBackgroundResource(R.drawable.ic_heart);
	        	
	         }
	       
		}
	}
    
    
   
}