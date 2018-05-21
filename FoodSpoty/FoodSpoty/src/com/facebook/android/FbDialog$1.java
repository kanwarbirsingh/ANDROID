// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.facebook.android;

import android.view.View;

// Referenced classes of package com.facebook.android:
//            FbDialog

class this._cls0
    implements android.view.kListener
{

    final FbDialog this$0;

    public void onClick(View view)
    {
        FbDialog.access$0(FbDialog.this).onCancel();
        dismiss();
    }

    alogListener()
    {
        this$0 = FbDialog.this;
        super();
    }
}
