package com.isoftinc.listadapter;

import java.util.ArrayList;
import java.util.HashMap;

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
public class AdapterInvite extends BaseAdapter{
    
    private Activity activity;
	private Context mcontext;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    private int m_nlayoutID = 0;
    
    private int[] socialIcon = {R.drawable.icon_fb, R.drawable.icon_twitter, R.drawable.icon_googleplus, R.drawable.icon_mail};
    
    public AdapterInvite(Context context, ArrayList<HashMap<String, String>> d, int listRow) {
    	mcontext = context ;
        activity = (Activity) context;
        data	 = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
        
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

        ImageView socialPic 	= (ImageView)vi.findViewById(R.id.socialPic);
        TextView socialTxt 	= (TextView)vi.findViewById(R.id.socialTxt);
        
        HashMap<String, String> fragValue = new HashMap<String, String>();
        fragValue = data.get(position);

        
        socialTxt.setText(fragValue.get("SOCIAL_TITLE"));
    	socialPic.setBackgroundResource(socialIcon[position]);
        
        //rowBackground.setBackgroundResource(bgImg[position]);
        //imageLoader.DisplayImage(song.get(IndiaMagazineActivity.KEY_THUMB), thumb_image);
        return vi;
    }
}