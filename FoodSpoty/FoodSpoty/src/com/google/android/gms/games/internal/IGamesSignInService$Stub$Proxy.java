// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.google.android.gms.games.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

// Referenced classes of package com.google.android.gms.games.internal:
//            IGamesSignInService, IGamesSignInCallbacks

private static class 
    implements IGamesSignInService
{

    private IBinder le;

    public void a(IGamesSignInCallbacks igamessignincallbacks, String s)
        throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();
        parcel.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInService");
        if (igamessignincallbacks == null)
        {
            break MISSING_BLOCK_LABEL_68;
        }
        igamessignincallbacks = igamessignincallbacks.asBinder();
_L1:
        parcel.writeStrongBinder(igamessignincallbacks);
        parcel.writeString(s);
        le.transact(14001, parcel, parcel1, 0);
        parcel1.readException();
        parcel1.recycle();
        parcel.recycle();
        return;
        igamessignincallbacks = null;
          goto _L1
        igamessignincallbacks;
        parcel1.recycle();
        parcel.recycle();
        throw igamessignincallbacks;
    }

    public void a(IGamesSignInCallbacks igamessignincallbacks, String s, String s1)
        throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();
        parcel.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInService");
        if (igamessignincallbacks == null)
        {
            break MISSING_BLOCK_LABEL_80;
        }
        igamessignincallbacks = igamessignincallbacks.asBinder();
_L1:
        parcel.writeStrongBinder(igamessignincallbacks);
        parcel.writeString(s);
        parcel.writeString(s1);
        le.transact(5006, parcel, parcel1, 0);
        parcel1.readException();
        parcel1.recycle();
        parcel.recycle();
        return;
        igamessignincallbacks = null;
          goto _L1
        igamessignincallbacks;
        parcel1.recycle();
        parcel.recycle();
        throw igamessignincallbacks;
    }

    public void a(IGamesSignInCallbacks igamessignincallbacks, String s, String s1, String s2)
        throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();
        parcel.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInService");
        if (igamessignincallbacks == null)
        {
            break MISSING_BLOCK_LABEL_87;
        }
        igamessignincallbacks = igamessignincallbacks.asBinder();
_L1:
        parcel.writeStrongBinder(igamessignincallbacks);
        parcel.writeString(s);
        parcel.writeString(s1);
        parcel.writeString(s2);
        le.transact(5005, parcel, parcel1, 0);
        parcel1.readException();
        parcel1.recycle();
        parcel.recycle();
        return;
        igamessignincallbacks = null;
          goto _L1
        igamessignincallbacks;
        parcel1.recycle();
        parcel.recycle();
        throw igamessignincallbacks;
    }

    public void a(IGamesSignInCallbacks igamessignincallbacks, String s, String s1, String s2, String as[])
        throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();
        parcel.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInService");
        if (igamessignincallbacks == null)
        {
            break MISSING_BLOCK_LABEL_94;
        }
        igamessignincallbacks = igamessignincallbacks.asBinder();
_L1:
        parcel.writeStrongBinder(igamessignincallbacks);
        parcel.writeString(s);
        parcel.writeString(s1);
        parcel.writeString(s2);
        parcel.writeStringArray(as);
        le.transact(5008, parcel, parcel1, 0);
        parcel1.readException();
        parcel1.recycle();
        parcel.recycle();
        return;
        igamessignincallbacks = null;
          goto _L1
        igamessignincallbacks;
        parcel1.recycle();
        parcel.recycle();
        throw igamessignincallbacks;
    }

    public void a(IGamesSignInCallbacks igamessignincallbacks, String s, String s1, String as[])
        throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();
        parcel.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInService");
        if (igamessignincallbacks == null)
        {
            break MISSING_BLOCK_LABEL_87;
        }
        igamessignincallbacks = igamessignincallbacks.asBinder();
_L1:
        parcel.writeStrongBinder(igamessignincallbacks);
        parcel.writeString(s);
        parcel.writeString(s1);
        parcel.writeStringArray(as);
        le.transact(5004, parcel, parcel1, 0);
        parcel1.readException();
        parcel1.recycle();
        parcel.recycle();
        return;
        igamessignincallbacks = null;
          goto _L1
        igamessignincallbacks;
        parcel1.recycle();
        parcel.recycle();
        throw igamessignincallbacks;
    }

    public void a(IGamesSignInCallbacks igamessignincallbacks, String s, String s1, String as[], String s2)
        throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();
        parcel.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInService");
        if (igamessignincallbacks == null)
        {
            break MISSING_BLOCK_LABEL_94;
        }
        igamessignincallbacks = igamessignincallbacks.asBinder();
_L1:
        parcel.writeStrongBinder(igamessignincallbacks);
        parcel.writeString(s);
        parcel.writeString(s1);
        parcel.writeStringArray(as);
        parcel.writeString(s2);
        le.transact(5003, parcel, parcel1, 0);
        parcel1.readException();
        parcel1.recycle();
        parcel.recycle();
        return;
        igamessignincallbacks = null;
          goto _L1
        igamessignincallbacks;
        parcel1.recycle();
        parcel.recycle();
        throw igamessignincallbacks;
    }

    public IBinder asBinder()
    {
        return le;
    }

    public void b(IGamesSignInCallbacks igamessignincallbacks, String s, String s1, String s2)
        throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();
        parcel.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInService");
        if (igamessignincallbacks == null)
        {
            break MISSING_BLOCK_LABEL_87;
        }
        igamessignincallbacks = igamessignincallbacks.asBinder();
_L1:
        parcel.writeStrongBinder(igamessignincallbacks);
        parcel.writeString(s);
        parcel.writeString(s1);
        parcel.writeString(s2);
        le.transact(5007, parcel, parcel1, 0);
        parcel1.readException();
        parcel1.recycle();
        parcel.recycle();
        return;
        igamessignincallbacks = null;
          goto _L1
        igamessignincallbacks;
        parcel1.recycle();
        parcel.recycle();
        throw igamessignincallbacks;
    }

    public String bO(String s)
        throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();
        parcel.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInService");
        parcel.writeString(s);
        le.transact(5001, parcel, parcel1, 0);
        parcel1.readException();
        s = parcel1.readString();
        parcel1.recycle();
        parcel.recycle();
        return s;
        s;
        parcel1.recycle();
        parcel.recycle();
        throw s;
    }

    public String bP(String s)
        throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();
        parcel.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInService");
        parcel.writeString(s);
        le.transact(5002, parcel, parcel1, 0);
        parcel1.readException();
        s = parcel1.readString();
        parcel1.recycle();
        parcel.recycle();
        return s;
        s;
        parcel1.recycle();
        parcel.recycle();
        throw s;
    }

    public String i(String s, boolean flag)
        throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        int j;
        j = 0;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();
        parcel.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInService");
        parcel.writeString(s);
        if (flag)
        {
            j = 1;
        }
        parcel.writeInt(j);
        le.transact(5009, parcel, parcel1, 0);
        parcel1.readException();
        s = parcel1.readString();
        parcel1.recycle();
        parcel.recycle();
        return s;
        s;
        parcel1.recycle();
        parcel.recycle();
        throw s;
    }

    public void v(String s, String s1)
        throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();
        parcel.writeInterfaceToken("com.google.android.gms.games.internal.IGamesSignInService");
        parcel.writeString(s);
        parcel.writeString(s1);
        le.transact(9001, parcel, parcel1, 0);
        parcel1.readException();
        parcel1.recycle();
        parcel.recycle();
        return;
        s;
        parcel1.recycle();
        parcel.recycle();
        throw s;
    }
}
