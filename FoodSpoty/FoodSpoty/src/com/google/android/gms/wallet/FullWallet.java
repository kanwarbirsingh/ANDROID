// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.google.android.gms.wallet;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.identity.intents.model.UserAddress;

// Referenced classes of package com.google.android.gms.wallet:
//            f, ProxyCard, Address, InstrumentInfo

public final class FullWallet
    implements SafeParcelable
{

    public static final android.os.Parcelable.Creator CREATOR = new f();
    private final int CK;
    String auL;
    String auM;
    ProxyCard auN;
    String auO;
    Address auP;
    Address auQ;
    String auR[];
    UserAddress auS;
    UserAddress auT;
    InstrumentInfo auU[];

    private FullWallet()
    {
        CK = 1;
    }

    FullWallet(int i, String s, String s1, ProxyCard proxycard, String s2, Address address, Address address1, 
            String as[], UserAddress useraddress, UserAddress useraddress1, InstrumentInfo ainstrumentinfo[])
    {
        CK = i;
        auL = s;
        auM = s1;
        auN = proxycard;
        auO = s2;
        auP = address;
        auQ = address1;
        auR = as;
        auS = useraddress;
        auT = useraddress1;
        auU = ainstrumentinfo;
    }

    public int describeContents()
    {
        return 0;
    }

    public Address getBillingAddress()
    {
        return auP;
    }

    public UserAddress getBuyerBillingAddress()
    {
        return auS;
    }

    public UserAddress getBuyerShippingAddress()
    {
        return auT;
    }

    public String getEmail()
    {
        return auO;
    }

    public String getGoogleTransactionId()
    {
        return auL;
    }

    public InstrumentInfo[] getInstrumentInfos()
    {
        return auU;
    }

    public String getMerchantTransactionId()
    {
        return auM;
    }

    public String[] getPaymentDescriptions()
    {
        return auR;
    }

    public ProxyCard getProxyCard()
    {
        return auN;
    }

    public Address getShippingAddress()
    {
        return auQ;
    }

    public int getVersionCode()
    {
        return CK;
    }

    public void writeToParcel(Parcel parcel, int i)
    {
        f.a(this, parcel, i);
    }

}
