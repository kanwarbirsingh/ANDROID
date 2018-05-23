// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.facebook.android;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieSyncManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

// Referenced classes of package com.facebook.android:
//            FacebookError, DialogError, Util, FbDialog

public class Facebook
{
    public static interface DialogListener
    {

        public abstract void onCancel();

        public abstract void onComplete(Bundle bundle);

        public abstract void onError(DialogError dialogerror);

        public abstract void onFacebookError(FacebookError facebookerror);
    }

    public static interface ServiceListener
    {

        public abstract void onComplete(Bundle bundle);

        public abstract void onError(Error error);

        public abstract void onFacebookError(FacebookError facebookerror);
    }

    private class TokenRefreshServiceConnection
        implements ServiceConnection
    {

        final Context applicationsContext;
        final Messenger messageReceiver = new Messenger(new _cls1());
        Messenger messageSender;
        final ServiceListener serviceListener;
        final Facebook this$0;

        private void refreshToken()
        {
            Bundle bundle = new Bundle();
            bundle.putString("access_token", mAccessToken);
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
                serviceListener.onError(new Error("Service connection error"));
            }
        }

        public void onServiceConnected(ComponentName componentname, IBinder ibinder)
        {
            messageSender = new Messenger(ibinder);
            refreshToken();
        }

        public void onServiceDisconnected(ComponentName componentname)
        {
            serviceListener.onError(new Error("Service disconnected"));
            mAuthActivity.unbindService(this);
        }


        public TokenRefreshServiceConnection(Context context, ServiceListener servicelistener)
        {
            this$0 = Facebook.this;
            super();
            messageSender = null;
            applicationsContext = context;
            serviceListener = servicelistener;
        }
    }


    public static final String CANCEL_URI = "fbconnect://cancel";
    private static final int DEFAULT_AUTH_ACTIVITY_CODE = 32665;
    protected static String DIALOG_BASE_URL = "https://m.facebook.com/dialog/";
    public static final String EXPIRES = "expires_in";
    public static final String FB_APP_SIGNATURE = "abcxyz";
    public static final int FORCE_DIALOG_AUTH = -1;
    protected static String GRAPH_BASE_URL = "https://graph.facebook.com/";
    private static final String LOGIN = "oauth";
    public static final String REDIRECT_URI = "fbconnect://success";
    protected static String RESTSERVER_URL = "https://api.facebook.com/restserver.php";
    public static final String SINGLE_SIGN_ON_DISABLED = "service_disabled";
    public static final String TOKEN = "access_token";
    private final long REFRESH_TOKEN_BARRIER = 0x5265c00L;
    private long mAccessExpires;
    private String mAccessToken;
    private String mAppId;
    private Activity mAuthActivity;
    private int mAuthActivityCode;
    private DialogListener mAuthDialogListener;
    private String mAuthPermissions[];
    private long mLastAccessUpdate;

    public Facebook(String s)
    {
        mAccessToken = null;
        mLastAccessUpdate = 0L;
        mAccessExpires = 0L;
        if (s == null)
        {
            throw new IllegalArgumentException("You must specify your application ID when instantiating a Facebook object. See README for details.");
        } else
        {
            mAppId = s;
            return;
        }
    }

    private void startDialogAuth(Activity activity, String as[])
    {
        Bundle bundle = new Bundle();
        if (as.length > 0)
        {
            bundle.putString("scope", TextUtils.join(",", as));
        }
        CookieSyncManager.createInstance(activity);
        dialog(activity, "oauth", bundle, new DialogListener() {

            final Facebook this$0;

            public void onCancel()
            {
                Log.d("Facebook-authorize", "Login canceled");
                mAuthDialogListener.onCancel();
            }

            public void onComplete(Bundle bundle1)
            {
                CookieSyncManager.getInstance().sync();
                setAccessToken(bundle1.getString("access_token"));
                setAccessExpiresIn(bundle1.getString("expires_in"));
                if (isSessionValid())
                {
                    Log.d("Facebook-authorize", (new StringBuilder("Login Success! access_token=")).append(getAccessToken()).append(" expires=").append(getAccessExpires()).toString());
                    mAuthDialogListener.onComplete(bundle1);
                    return;
                } else
                {
                    mAuthDialogListener.onFacebookError(new FacebookError("Failed to receive access token."));
                    return;
                }
            }

            public void onError(DialogError dialogerror)
            {
                Log.d("Facebook-authorize", (new StringBuilder("Login failed: ")).append(dialogerror).toString());
                mAuthDialogListener.onError(dialogerror);
            }

            public void onFacebookError(FacebookError facebookerror)
            {
                Log.d("Facebook-authorize", (new StringBuilder("Login failed: ")).append(facebookerror).toString());
                mAuthDialogListener.onFacebookError(facebookerror);
            }

            
            {
                this$0 = Facebook.this;
                super();
            }
        });
    }

    private boolean startSingleSignOn(Activity activity, String s, String as[], int i)
    {
        boolean flag = true;
        Intent intent = new Intent();
        intent.setClassName("com.facebook.katana", "com.facebook.katana.ProxyAuth");
        intent.putExtra("client_id", s);
        if (as.length > 0)
        {
            intent.putExtra("scope", TextUtils.join(",", as));
        }
        if (!validateAppSignatureForIntent(activity, intent))
        {
            return false;
        }
        mAuthActivity = activity;
        mAuthPermissions = as;
        mAuthActivityCode = i;
        try
        {
            activity.startActivityForResult(intent, i);
        }
        // Misplaced declaration of an exception variable
        catch (Activity activity)
        {
            flag = false;
        }
        return flag;
    }

    private boolean validateAppSignatureForIntent(Context context, Intent intent)
    {
        intent = context.getPackageManager().resolveActivity(intent, 0);
        if (intent != null)
        {
            intent = ((ResolveInfo) (intent)).activityInfo.packageName;
            int i;
            int j;
            try
            {
                context = context.getPackageManager().getPackageInfo(intent, 64);
            }
            // Misplaced declaration of an exception variable
            catch (Context context)
            {
                return false;
            }
            context = ((PackageInfo) (context)).signatures;
            j = context.length;
            i = 0;
            while (i < j) 
            {
                if (context[i].toCharsString().equals("abcxyz"))
                {
                    return true;
                }
                i++;
            }
        }
        return false;
    }

    public void authorize(Activity activity, DialogListener dialoglistener)
    {
        authorize(activity, new String[0], 32665, dialoglistener);
    }

    public void authorize(Activity activity, String as[], int i, DialogListener dialoglistener)
    {
        boolean flag = false;
        mAuthDialogListener = dialoglistener;
        if (i >= 0)
        {
            flag = startSingleSignOn(activity, mAppId, as, i);
        }
        if (!flag)
        {
            startDialogAuth(activity, as);
        }
    }

    public void authorize(Activity activity, String as[], DialogListener dialoglistener)
    {
        authorize(activity, as, 32665, dialoglistener);
    }

    public void authorizeCallback(int i, int j, Intent intent)
    {
        if (i != mAuthActivityCode) goto _L2; else goto _L1
_L1:
        if (j != -1) goto _L4; else goto _L3
_L3:
        String s;
        String s1 = intent.getStringExtra("error");
        s = s1;
        if (s1 == null)
        {
            s = intent.getStringExtra("error_type");
        }
        if (s == null) goto _L6; else goto _L5
_L5:
        if (!s.equals("service_disabled") && !s.equals("AndroidAuthKillSwitchException")) goto _L8; else goto _L7
_L7:
        Log.d("Facebook-authorize", "Hosted auth currently disabled. Retrying dialog auth...");
        startDialogAuth(mAuthActivity, mAuthPermissions);
_L2:
        return;
_L8:
        if (s.equals("access_denied") || s.equals("OAuthAccessDeniedException"))
        {
            Log.d("Facebook-authorize", "Login canceled by user.");
            mAuthDialogListener.onCancel();
            return;
        }
        String s2 = intent.getStringExtra("error_description");
        intent = s;
        if (s2 != null)
        {
            intent = (new StringBuilder(String.valueOf(s))).append(":").append(s2).toString();
        }
        Log.d("Facebook-authorize", (new StringBuilder("Login failed: ")).append(intent).toString());
        mAuthDialogListener.onFacebookError(new FacebookError(intent));
        return;
_L6:
        setAccessToken(intent.getStringExtra("access_token"));
        setAccessExpiresIn(intent.getStringExtra("expires_in"));
        if (isSessionValid())
        {
            Log.d("Facebook-authorize", (new StringBuilder("Login Success! access_token=")).append(getAccessToken()).append(" expires=").append(getAccessExpires()).toString());
            mAuthDialogListener.onComplete(intent.getExtras());
            return;
        } else
        {
            mAuthDialogListener.onFacebookError(new FacebookError("Failed to receive access token."));
            return;
        }
_L4:
        if (j == 0)
        {
            if (intent != null)
            {
                Log.d("Facebook-authorize", (new StringBuilder("Login failed: ")).append(intent.getStringExtra("error")).toString());
                mAuthDialogListener.onError(new DialogError(intent.getStringExtra("error"), intent.getIntExtra("error_code", -1), intent.getStringExtra("failing_url")));
                return;
            } else
            {
                Log.d("Facebook-authorize", "Login canceled by user.");
                mAuthDialogListener.onCancel();
                return;
            }
        }
        if (true) goto _L2; else goto _L9
_L9:
    }

    public void dialog(Context context, String s, Bundle bundle, DialogListener dialoglistener)
    {
        String s1 = (new StringBuilder(String.valueOf(DIALOG_BASE_URL))).append(s).toString();
        bundle.putString("display", "touch");
        bundle.putString("redirect_uri", "fbconnect://success");
        if (s.equals("oauth"))
        {
            bundle.putString("type", "user_agent");
            bundle.putString("client_id", mAppId);
        } else
        {
            bundle.putString("app_id", mAppId);
        }
        if (isSessionValid())
        {
            bundle.putString("access_token", getAccessToken());
        }
        s = (new StringBuilder(String.valueOf(s1))).append("?").append(Util.encodeUrl(bundle)).toString();
        if (context.checkCallingOrSelfPermission("android.permission.INTERNET") != 0)
        {
            Util.showAlert(context, "Error", "Application requires permission to access the Internet");
            return;
        } else
        {
            (new FbDialog(context, s, dialoglistener)).show();
            return;
        }
    }

    public void dialog(Context context, String s, DialogListener dialoglistener)
    {
        dialog(context, s, new Bundle(), dialoglistener);
    }

    public boolean extendAccessToken(Context context, ServiceListener servicelistener)
    {
        Intent intent = new Intent();
        intent.setClassName("com.facebook.katana", "com.facebook.katana.platform.TokenRefreshService");
        if (!validateAppSignatureForIntent(context, intent))
        {
            return false;
        } else
        {
            return context.bindService(intent, new TokenRefreshServiceConnection(context, servicelistener), 1);
        }
    }

    public boolean extendAccessTokenIfNeeded(Context context, ServiceListener servicelistener)
    {
        if (shouldExtendAccessToken())
        {
            return extendAccessToken(context, servicelistener);
        } else
        {
            return true;
        }
    }

    public long getAccessExpires()
    {
        return mAccessExpires;
    }

    public String getAccessToken()
    {
        return mAccessToken;
    }

    public String getAppId()
    {
        return mAppId;
    }

    public boolean isSessionValid()
    {
        return getAccessToken() != null && (getAccessExpires() == 0L || System.currentTimeMillis() < getAccessExpires());
    }

    public String logout(Context context)
        throws MalformedURLException, IOException
    {
        Util.clearCookies(context);
        context = new Bundle();
        context.putString("method", "auth.expireSession");
        context = request(context);
        setAccessToken(null);
        setAccessExpires(0L);
        return context;
    }

    public String request(Bundle bundle)
        throws MalformedURLException, IOException
    {
        if (!bundle.containsKey("method"))
        {
            throw new IllegalArgumentException("API method must be specified. (parameters must contain key \"method\" and value). See http://developers.facebook.com/docs/reference/rest/");
        } else
        {
            return request(null, bundle, "GET");
        }
    }

    public String request(String s)
        throws MalformedURLException, IOException
    {
        return request(s, new Bundle(), "GET");
    }

    public String request(String s, Bundle bundle)
        throws MalformedURLException, IOException
    {
        return request(s, bundle, "GET");
    }

    public String request(String s, Bundle bundle, String s1)
        throws FileNotFoundException, MalformedURLException, IOException
    {
        bundle.putString("format", "json");
        if (isSessionValid())
        {
            bundle.putString("access_token", getAccessToken());
        }
        if (s != null)
        {
            s = (new StringBuilder(String.valueOf(GRAPH_BASE_URL))).append(s).toString();
        } else
        {
            s = RESTSERVER_URL;
        }
        return Util.openUrl(s, s1, bundle);
    }

    public void setAccessExpires(long l)
    {
        mAccessExpires = l;
    }

    public void setAccessExpiresIn(String s)
    {
        if (s != null)
        {
            long l;
            if (s.equals("0"))
            {
                l = 0L;
            } else
            {
                l = System.currentTimeMillis() + Long.parseLong(s) * 1000L;
            }
            setAccessExpires(l);
        }
    }

    public void setAccessToken(String s)
    {
        mAccessToken = s;
        mLastAccessUpdate = System.currentTimeMillis();
    }

    public void setAppId(String s)
    {
        mAppId = s;
    }

    public boolean shouldExtendAccessToken()
    {
        return isSessionValid() && System.currentTimeMillis() - mLastAccessUpdate >= 0x5265c00L;
    }





    // Unreferenced inner class com/facebook/android/Facebook$TokenRefreshServiceConnection$1

/* anonymous class */
    class TokenRefreshServiceConnection._cls1 extends Handler
    {

        final TokenRefreshServiceConnection this$1;

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
            applicationsContext.unbindService(TokenRefreshServiceConnection.this);
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
                    ServiceListener servicelistener = serviceListener;
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
                this$1 = TokenRefreshServiceConnection.this;
                super();
            }
    }

}
