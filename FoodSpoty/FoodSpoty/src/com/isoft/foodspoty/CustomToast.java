package com.isoft.foodspoty;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToast {

	// Context
    Context _context;
    Activity _activity;
    LayoutInflater li;
    
    // Constructor
    public CustomToast(Context context){
        this._context = context;
        _activity = (Activity)context;
        
       /* //Creating the LayoutInflater instance  
        LayoutInflater li = getLayoutInflater();  
        //Getting the View object as defined in the customtoast.xml file  
        View layout = li.inflate(R.layout.customtoast,(ViewGroup)findViewById(R.id.custom_toast_layout));  
       
        //Creating the Toast object   
        Toast toast = new Toast(_context);  
        toast.setDuration(Toast.LENGTH_SHORT);  
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);  
        toast.setView(layout);//setting the view of custom toast layout  
        toast.show(); */ 
        
        //Creating the LayoutInflater instance  
        
        li = _activity.getLayoutInflater();  
    }
    
    public void showToastError(String toastmsg){
    	//Getting the View object as defined in the customtoast.xml file 
        //View layout = li.inflate(R.layout.customtoast,(ViewGroup)findViewById(R.id.custom_toast_layout));
        View layout = li.inflate(R.layout.customtoast,null);  
       
        TextView toastMsg = (TextView)layout.findViewById(R.id.custom_toast_message);
        ImageView toastIcon = (ImageView)layout.findViewById(R.id.custom_toast_image);
        
        layout.setBackgroundResource(R.drawable.withoutround);
        
        toastMsg.setText(toastmsg);
        int toasticon=R.drawable.ic_error;
        toastIcon.setBackgroundResource(toasticon);
        //Creating the Toast object   
        Toast toast = new Toast(_activity);  
        toast.setDuration(Toast.LENGTH_SHORT); 
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);  
        toast.setView(layout);//setting the view of custom toast layout  
        toast.show(); 
    }
    

    public void showToastSuccess(String toastmsg){
    	//Getting the View object as defined in the customtoast.xml file 
        //View layout = li.inflate(R.layout.customtoast,(ViewGroup)findViewById(R.id.custom_toast_layout));
        View layout = li.inflate(R.layout.customtoast,null);  
       
        TextView toastMsg = (TextView)layout.findViewById(R.id.custom_toast_message);
        ImageView toastIcon = (ImageView)layout.findViewById(R.id.custom_toast_image);
        
        layout.setBackgroundResource(R.drawable.withoutround);
        
        toastMsg.setText(toastmsg);
        int toasticon2=R.drawable.ic_right;
        toastIcon.setBackgroundResource(toasticon2);
        //Creating the Toast object   
        Toast toast = new Toast(_activity);  
        toast.setDuration(Toast.LENGTH_SHORT); 
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);  
        toast.setView(layout);//setting the view of custom toast layout  
        toast.show(); 
    }
}
