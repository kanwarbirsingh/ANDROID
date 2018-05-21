// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.facebook.android;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

// Referenced classes of package com.facebook.android:
//            Facebook, FacebookError

class this._cls1 extends Handler
{

    final rviceListener this$1;

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
        cess._mth0(this._cls1.this).setAccessToken(s);
        cess._mth0(this._cls1.this).setAccessExpires(l);
        if (rviceListener != null)
        {
            rviceListener.rviceListener(bundle);
        }
_L4:
        plicationsContext.unbindService(this._cls1.this);
        return;
_L2:
        if (rviceListener != null)
        {
            String s1 = message.getData().getString("error");
            if (message.getData().containsKey("error_code"))
            {
                int i = message.getData().getInt("error_code");
                rviceListener.rviceListener(new FacebookError(s1, null, i));
            } else
            {
                this._cls1 _lcls1 = rviceListener;
                if (s1 != null)
                {
                    message = s1;
                } else
                {
                    message = "Unknown service error";
                }
                _lcls1.rviceListener(new Error(message));
            }
        }
        if (true) goto _L4; else goto _L3
_L3:
    }

    ()
    {
        this$1 = this._cls1.this;
        super();
    }
}
