package com.isoftinc.listadapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.helper.adapters.ImageLoaderisoft;
import com.isoft.foodspoty.ProfileActivity;
import com.isoft.foodspoty.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("unused")
public class AdapterProfileCoupons extends BaseAdapter{
    
    private Activity activity;
    private Context mcontext;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoaderisoft imageLoader; 
    private int m_nlayoutID = 0;
    
    public AdapterProfileCoupons(Context context, ArrayList<HashMap<String, String>> d, int listRow) {
    	mcontext = context ;
        activity = (Activity) context;
        data	 = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoaderisoft(activity.getApplicationContext());
        
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

        
        rowOfferTxt.setText(fragValue.get(ProfileActivity.Key_Rdcoupon_code));
        
        //rowBackground.setBackgroundResource(bgImg[position]);
        imageLoader.DisplayImage(fragValue.get(ProfileActivity.Key_Rdcoupon_image),rowOfferBg,R.drawable.temp_coupons);
        return vi;
    }
}