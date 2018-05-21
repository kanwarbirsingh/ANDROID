// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.facebook.android;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

// Referenced classes of package com.facebook.android:
//            Facebook, FacebookError

private class serviceListener
    implements ServiceConnection
{

    final Context applicationsContext;
    final Messenger messageReceiver = new Messenger(new Handler() {

        final Facebook.TokenRefreshServiceConnection this$1;

        public void handleMessage(Message message)
        {
            String s;
            Bundle bundle;
            long l;
            s = message.getData().getString("access_token");
            l = message.getData().getLong("expires_in") * 1000L;
            bundle = (Bundle)message.getData().clone();
            bundle.putLong("expires_in", l);
            if (s == null) goto _L2; else goto _L1
_L1:
            setAccessToken(s);
            setAccessExpires(l);
            if (serviceListener != null)
            {
                serviceListener.onComplete(bundle);
            }
_L4:
            applicationsContext.unbindService(Facebook.TokenRefreshServiceConnection.this);
            return;
_L2:
            if (serviceListener != null)
            {
                String s1 = message.getData().getString("error");
                if (message.getData().containsKey("error_code"))
                {
                    int i = message.getData().getInt("error_code");
                    serviceListener.onFacebookError(new FacebookError(s1, null, i));
                } else
                {
                    Facebook.ServiceListener servicelistener = serviceListener;
                    if (s1 != null)
                    {
                        message = s1;
                    } else
                    {
                        message = "Unknown service error";
                    }
                    servicelistener.onError(new Error(message));
                }
            }
            if (true) goto _L4; else goto _L3
_L3:
        }

            
            {
                this$1 = Facebook.TokenRefreshServiceConnection.this;
                super();
            }
    });
    Messenger messageSender;
    final serviceListener serviceListener;
    final Facebook this$0;

    private void refreshToken()
    {
        Bundle bundle = new Bundle();
        bundle.putString("access_token", Facebook.access$1(Facebook.this));
        Message message = Message.obtain();
        message.setData(bundle);
        message.replyTo = messageReceiver;
        try
        {
            messageSender.send(message);
            return;
        }
        catch (RemoteException remoteexception)
        {
            serviceListener.serviceListener(new Error("Service connection error"));
        }
    }

    public void onServiceConnected(ComponentName componentname, IBinder ibinder)
    {
        messageSender = new Messenger(ibinder);
        refreshToken();
    }

    public void onServiceDisconnected(ComponentName componentname)
    {
        serviceListener.serviceListener(new Error("Service disconnected"));
        Facebook.access$0(Facebook.this).unbindService(this);
    }


    public _cls1.this._cls1(Context context, _cls1.this._cls1 _pcls1)
    {
        this$0 = Facebook.this;
        super();
        messageSender = null;
        applicationsContext = context;
        serviceListener = _pcls1;
    }
}
