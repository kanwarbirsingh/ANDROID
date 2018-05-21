package com.isoftinc.listadapter;

import com.isoft.foodspoty.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


@SuppressWarnings("unused")
public class AdapterSlidemenu extends BaseAdapter{
    private Activity activity;
	private Context mcontext;
    private String[] data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    int[] menu_icon = new int[] {R.drawable.ic_home, R.drawable.ic_offers, R.drawable.ic_redeem, R.drawable.ic_notification, R.drawable.ic_profile2 , R.drawable.ic_invite, R.drawable.ic_contactus, R.drawable.ic_feedback};
    private int m_nlayoutID = 0;
    
    public AdapterSlidemenu(Context a, String[] d, int listRow) {
    	mcontext    = a ;
        activity    = (Activity) a;
        data	    = d;
        inflater    = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
        
        m_nlayoutID = listRow;
    }

    public int getCount() {
        return data.length;
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

        ImageView menuIcon    = (ImageView)vi.findViewById(R.id.menuicon);
        TextView menuText 	  = (TextView)vi.findViewById(R.id.menuTitle);
        String song = data[position];
        
        menuText.setText(song);
        menuIcon.setBackgroundResource(menu_icon[position]);
        return vi;
    }
    
}