package com.isoftinc.listadapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.helper.adapters.ImageLoaderisoft;
import com.isoft.foodspoty.NotificationActivity;
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
public class AdapterNotification extends BaseAdapter{
    
    private Activity activity;
    private Context mcontext;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoaderisoft imageLoader; 
    private int m_nlayoutID = 0;
    
    private int[] socialIcon = {R.drawable.icon_fb, R.drawable.icon_twitter, R.drawable.icon_googleplus, R.drawable.icon_mail};
    
    public AdapterNotification(Context context, ArrayList<HashMap<String, String>> d, int listRow) {
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

        TextView topLine         = (TextView)vi.findViewById(R.id.verticalLineTop);
        ImageView notiUserPic 	 = (ImageView)vi.findViewById(R.id.notiUserPic);
        TextView notiDescription = (TextView)vi.findViewById(R.id.notiDescription);
        TextView notiTime = (TextView)vi.findViewById(R.id.notiTime);
        View dotView			=(View)vi.findViewById(R.id.dotView);
        HashMap<String, String> fragValue = new HashMap<String, String>();
        fragValue = data.get(position);

        if(position == 0){
        	topLine.setVisibility(View.INVISIBLE);
        }else{
        	topLine.setVisibility(View.VISIBLE);
        }
       /* if(fragValue.get(NotificationActivity.Key_notification_status).equals("Unread"))
        {
        	dotView.setBackgroundResource(R.drawable.circle_green);
        }
        else
        {
        	dotView.setBackgroundResource(R.drawable.circle_green);
            
        }*/
        notiTime.setText(fragValue.get(NotificationActivity.Key_notification_send_date));
        notiDescription.setText(fragValue.get(NotificationActivity.Key_notification_message));
         imageLoader.DisplayImage(fragValue.get(NotificationActivity.Key_pic), notiUserPic,R.drawable.app_icon);
        return vi;
    }
}