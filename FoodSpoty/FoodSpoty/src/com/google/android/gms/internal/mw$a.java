// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.google.android.gms.internal;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;

// Referenced classes of package com.google.android.gms.internal:
//            mw

public static final class nB extends nB
{

    private final int My;
    private Activity nB;

    static void a(nB nb, Activity activity)
    {
        nb.setActivity(activity);
    }

    private void setActivity(Activity activity)
    {
        nB = activity;
    }

    public void g(int i, Bundle bundle)
    {
        if (i != 1) goto _L2; else goto _L1
_L1:
        Intent intent = new Intent();
        intent.putExtras(bundle);
        bundle = nB.createPendingResult(My, intent, 0x40000000);
        if (bundle != null) goto _L4; else goto _L3
_L3:
        return;
_L4:
        try
        {
            bundle.send(1);
            return;
        }
        // Misplaced declaration of an exception variable
        catch (Bundle bundle)
        {
            Log.w("AddressClientImpl", "Exception settng pending result", bundle);
        }
        return;
_L2:
        PendingIntent pendingintent = null;
        if (bundle != null)
        {
            pendingintent = (PendingIntent)bundle.getParcelable("com.google.android.gms.identity.intents.EXTRA_PENDING_INTENT");
        }
        bundle = new ConnectionResult(i, pendingintent);
        if (bundle.hasResolution())
        {
            try
            {
                bundle.startResolutionForResult(nB, My);
                return;
            }
            // Misplaced declaration of an exception variable
            catch (Bundle bundle)
            {
                Log.w("AddressClientImpl", "Exception starting pending intent", bundle);
            }
            return;
        }
        bundle = nB.createPendingResult(My, new Intent(), 0x40000000);
        if (bundle != null)
        {
            try
            {
                bundle.send(1);
                return;
            }
            // Misplaced declaration of an exception variable
            catch (Bundle bundle)
            {
                Log.w("AddressClientImpl", "Exception setting pending result", bundle);
            }
            return;
        }
        if (true) goto _L3; else goto _L5
_L5:
    }

    public eption(int i, Activity activity)
    {
        My = i;
        nB = activity;
    }
}
