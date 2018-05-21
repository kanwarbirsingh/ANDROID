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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("unused")
public class AdapterSearchCity extends BaseAdapter{
    
    private Activity activity;
    private Context mcontext;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    private int m_nlayoutID = 0;
    
    
    public AdapterSearchCity(Context context, ArrayList<HashMap<String, String>> d, int listRow) {
    	mcontext = context ;
        activity = (Activity) context;
        data	 = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
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

        CheckBox text         = (CheckBox)vi.findViewById(R.id.cheakbox);
        
        HashMap<String, String> fragValue = new HashMap<String, String>();
        fragValue = data.get(position);

        text.setText(fragValue.get("CITY_NAME"));
       /* if(position == 0){
        	topLine.setVisibility(View.INVISIBLE);
        }else{
        	topLine.setVisibility(View.VISIBLE);
        }*/
        return vi;
    }
}