// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.facebook.android;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

// Referenced classes of package com.facebook.android:
//            DialogError, Util, FacebookError

public class FbDialog extends Dialog
{
    private class FbWebViewClient extends WebViewClient
    {

        final FbDialog this$0;

        public void onPageFinished(WebView webview, String s)
        {
            super.onPageFinished(webview, s);
            mSpinner.dismiss();
            mContent.setBackgroundColor(0);
            mWebView.setVisibility(0);
            mCrossImage.setVisibility(0);
        }

        public void onPageStarted(WebView webview, String s, Bitmap bitmap)
        {
            Log.d("Facebook-WebView", (new StringBuilder("Webview loading URL: ")).append(s).toString());
            super.onPageStarted(webview, s, bitmap);
            mSpinner.show();
        }

        public void onReceivedError(WebView webview, int i, String s, String s1)
        {
            super.onReceivedError(webview, i, s, s1);
            mListener.onError(new DialogError(s, i, s1));
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
                    mListener.onComplete(bundle);
                } else
                if (webview.equals("access_denied") || webview.equals("OAuthAccessDeniedException"))
                {
                    mListener.onCancel();
                } else
                {
                    mListener.onFacebookError(new FacebookError(webview));
                }
                dismiss();
                return true;
            }
            if (s.startsWith("fbconnect://cancel"))
            {
                mListener.onCancel();
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

        private FbWebViewClient()
        {
            this$0 = FbDialog.this;
            super();
        }

        FbWebViewClient(FbWebViewClient fbwebviewclient)
        {
            this();
        }
    }


    static final float DIMENSIONS_DIFF_LANDSCAPE[] = {
        20F, 60F
    };
    static final float DIMENSIONS_DIFF_PORTRAIT[] = {
        40F, 60F
    };
    static final String DISPLAY_STRING = "touch";
    static final int FB_BLUE = 0xff6d84b4;
    static final String FB_ICON = "icon.png";
    static final android.widget.FrameLayout.LayoutParams FILL = new android.widget.FrameLayout.LayoutParams(-1, -1);
    static final int MARGIN = 4;
    static final int PADDING = 2;
    private FrameLayout mContent;
    private ImageView mCrossImage;
    private Facebook.DialogListener mListener;
    private ProgressDialog mSpinner;
    private String mUrl;
    private WebView mWebView;

    public FbDialog(Context context, String s, Facebook.DialogListener dialoglistener)
    {
        super(context, 0x1030010);
        mUrl = s;
        mListener = dialoglistener;
    }

    private void createCrossImage()
    {
        mCrossImage = new ImageView(getContext());
        mCrossImage.setOnClickListener(new android.view.View.OnClickListener() {

            final FbDialog this$0;

            public void onClick(View view)
            {
                mListener.onCancel();
                dismiss();
            }

            
            {
                this$0 = FbDialog.this;
                super();
            }
        });
        Drawable drawable = getContext().getResources().getDrawable(R.drawable.close);
        mCrossImage.setImageDrawable(drawable);
        mCrossImage.setVisibility(4);
    }

    private void setUpWebView(int i)
    {
        LinearLayout linearlayout = new LinearLayout(getContext());
        mWebView = new WebView(getContext());
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new FbWebViewClient(null));
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);
        mWebView.setLayoutParams(FILL);
        mWebView.setVisibility(4);
        linearlayout.setPadding(i, i, i, i);
        linearlayout.addView(mWebView);
        mContent.addView(linearlayout);
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        mSpinner = new ProgressDialog(getContext());
        mSpinner.requestWindowFeature(1);
        mSpinner.setMessage("Loading...");
        requestWindowFeature(1);
        mContent = new FrameLayout(getContext());
        createCrossImage();
        setUpWebView(mCrossImage.getDrawable().getIntrinsicWidth() / 2);
        mContent.addView(mCrossImage, new android.view.ViewGroup.LayoutParams(-2, -2));
        addContentView(mContent, new android.view.ViewGroup.LayoutParams(-1, -1));
    }






}
