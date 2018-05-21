// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.facebook.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;

// Referenced classes of package com.facebook.android:
//            FbDialog, DialogError, Util, FacebookError

private class <init> extends WebViewClient
{

    final FbDialog this$0;

    public void onPageFinished(WebView webview, String s)
    {
        super.onPageFinished(webview, s);
        FbDialog.access$1(FbDialog.this).dismiss();
        FbDialog.access$2(FbDialog.this).setBackgroundColor(0);
        FbDialog.access$3(FbDialog.this).setVisibility(0);
        FbDialog.access$4(FbDialog.this).setVisibility(0);
    }

    public void onPageStarted(WebView webview, String s, Bitmap bitmap)
    {
        Log.d("Facebook-WebView", (new StringBuilder("Webview loading URL: ")).append(s).toString());
        super.onPageStarted(webview, s, bitmap);
        FbDialog.access$1(FbDialog.this).show();
    }

    public void onReceivedError(WebView webview, int i, String s, String s1)
    {
        super.onReceivedError(webview, i, s, s1);
        FbDialog.access$0(FbDialog.this).nError(new DialogError(s, i, s1));
        dismiss();
    }

    public boolean shouldOverrideUrlLoading(WebView webview, String s)
    {
        Log.d("Facebook-WebView", (new StringBuilder("Redirect URL: ")).append(s).toString());
        if (s.startsWith("fbconnect://success"))
        {
            Bundle bundle = Util.parseUrl(s);
            s = bundle.getString("error");
            webview = s;
            if (s == null)
            {
                webview = bundle.getString("error_type");
            }
            if (webview == null)
            {
                FbDialog.access$0(FbDialog.this).nComplete(bundle);
            } else
            if (webview.equals("access_denied") || webview.equals("OAuthAccessDeniedException"))
            {
                FbDialog.access$0(FbDialog.this).nCancel();
            } else
            {
                FbDialog.access$0(FbDialog.this).nFacebookError(new FacebookError(webview));
            }
            dismiss();
            return true;
        }
        if (s.startsWith("fbconnect://cancel"))
        {
            FbDialog.access$0(FbDialog.this).nCancel();
            dismiss();
            return true;
        }
        if (s.contains("touch"))
        {
            return false;
        } else
        {
            getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(s)));
            return true;
        }
    }

    private ()
    {
        this$0 = FbDialog.this;
        super();
    }

    this._cls0(this._cls0 _pcls0)
    {
        this();
    }
}
