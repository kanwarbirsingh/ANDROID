// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.maps.model.internal.l;

public interface d
    extends IInterface
{
    public static abstract class a extends Binder
        implements d
    {

        public static d aU(IBinder ibinder)
        {
            if (ibinder == null)
            {
                return null;
            }
            IInterface iinterface = ibinder.queryLocalInterface("com.google.android.gms.maps.internal.IInfoWindowAdapter");
            if (iinterface != null && (iinterface instanceof d))
            {
                return (d)iinterface;
            } else
            {
                return new a(ibinder);
            }
        }

        public IBinder asBinder()
        {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel1, int j)
            throws RemoteException
        {
            com.google.android.gms.dynamic.d d2 = null;
            com.google.android.gms.dynamic.d d1 = null;
            switch (i)
            {
            default:
                return super.onTransact(i, parcel, parcel1, j);

            case 1598968902: 
                parcel1.writeString("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                return true;

            case 1: // '\001'
                parcel.enforceInterface("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                d2 = f(com.google.android.gms.maps.model.internal.l.a.bA(parcel.readStrongBinder()));
                parcel1.writeNoException();
                parcel = d1;
                if (d2 != null)
                {
                    parcel = d2.asBinder();
                }
                parcel1.writeStrongBinder(parcel);
                return true;

            case 2: // '\002'
                parcel.enforceInterface("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                d1 = g(com.google.android.gms.maps.model.internal.l.a.bA(parcel.readStrongBinder()));
                parcel1.writeNoException();
                parcel = d2;
                break;
            }
            if (d1 != null)
            {
                parcel = d1.asBinder();
            }
            parcel1.writeStrongBinder(parcel);
            return true;
        }

        public a()
        {
            attachInterface(this, "com.google.android.gms.maps.internal.IInfoWindowAdapter");
        }
    }

    private static class a.a
        implements d
    {

        private IBinder le;

        public IBinder asBinder()
        {
            return le;
        }

        public com.google.android.gms.dynamic.d f(l l1)
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("com.google.android.gms.maps.internal.IInfoWindowAdapter");
            if (l1 == null)
            {
                break MISSING_BLOCK_LABEL_66;
            }
            l1 = l1.asBinder();
_L1:
            parcel.writeStrongBinder(l1);
            le.transact(1, parcel, parcel1, 0);
            parcel1.readException();
            l1 = com.google.android.gms.dynamic.a.ap(parcel1.readStrongBinder());
            parcel1.recycle();
            parcel.recycle();
            return l1;
            l1 = null;
              goto _L1
            l1;
            parcel1.recycle();
            parcel.recycle();
            throw l1;
        }

        public com.google.android.gms.dynamic.d g(l l1)
            throws RemoteException
        {
            Parcel parcel;
            Parcel parcel1;
            parcel = Parcel.obtain();
            parcel1 = Parcel.obtain();
            parcel.writeInterfaceToken("com.google.android.gms.maps.internal.IInfoWindowAdapter");
            if (l1 == null)
            {
                break MISSING_BLOCK_LABEL_66;
            }
            l1 = l1.asBinder();
_L1:
            parcel.writeStrongBinder(l1);
            le.transact(2, parcel, parcel1, 0);
            parcel1.readException();
            l1 = com.google.android.gms.dynamic.a.ap(parcel1.readStrongBinder());
            parcel1.recycle();
            parcel.recycle();
            return l1;
            l1 = null;
              goto _L1
            l1;
            parcel1.recycle();
            parcel.recycle();
            throw l1;
        }

        a.a(IBinder ibinder)
        {
            le = ibinder;
        }
    }


    public abstract com.google.android.gms.dynamic.d f(l l)
        throws RemoteException;

    public abstract com.google.android.gms.dynamic.d g(l l)
        throws RemoteException;
}
