// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.google.android.gms.internal;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

public class am
    implements android.app.Application.ActivityLifecycleCallbacks
{

    private Context mContext;
    private final Object mH = new Object();
    private Activity nB;

    public am(Application application, Activity activity)
    {
        application.registerActivityLifecycleCallbacks(this);
        setActivity(activity);
        mContext = application.getApplicationContext();
    }

    private void setActivity(Activity activity)
    {
        synchronized (mH)
        {
            if (!activity.getClass().getName().startsWith("com.google.android.gms.ads"))
            {
                nB = activity;
            }
        }
        return;
        activity;
        obj;
        JVM INSTR monitorexit ;
        throw activity;
    }

    public Activity getActivity()
    {
        return nB;
    }

    public Context getContext()
    {
        return mContext;
    }

    public void onActivityCreated(Activity activity, Bundle bundle)
    {
    }

    public void onActivityDestroyed(Activity activity)
    {
label0:
        {
            synchronized (mH)
            {
                if (nB != null)
                {
                    break label0;
                }
            }
            return;
        }
        if (nB.equals(activity))
        {
            nB = null;
        }
        obj;
        JVM INSTR monitorexit ;
        return;
        activity;
        obj;
        JVM INSTR monitorexit ;
        throw activity;
    }

    public void onActivityPaused(Activity activity)
    {
        setActivity(activity);
    }

    public void onActivityResumed(Activity activity)
    {
        setActivity(activity);
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle)
    {
    }

    public void onActivityStarted(Activity activity)
    {
        setActivity(activity);
    }

    public void onActivityStopped(Activity activity)
    {
    }
}
