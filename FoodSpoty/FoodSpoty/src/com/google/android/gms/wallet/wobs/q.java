// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.google.android.gms.wallet.wobs;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;

// Referenced classes of package com.google.android.gms.wallet.wobs:
//            p, l, n

public class q
    implements android.os.Parcelable.Creator
{

    public q()
    {
    }

    static void a(p p1, Parcel parcel, int i)
    {
        int j = b.H(parcel);
        b.c(parcel, 1, p1.getVersionCode());
        b.a(parcel, 2, p1.awT, false);
        b.a(parcel, 3, p1.tU, false);
        b.a(parcel, 4, p1.awX, i, false);
        b.a(parcel, 5, p1.awY, i, false);
        b.a(parcel, 6, p1.awZ, i, false);
        b.H(parcel, j);
    }

    public Object createFromParcel(Parcel parcel)
    {
        return ek(parcel);
    }

    public p ek(Parcel parcel)
    {
        n n1 = null;
        int j = com.google.android.gms.common.internal.safeparcel.a.G(parcel);
        int i = 0;
        n n2 = null;
        l l1 = null;
        String s = null;
        String s1 = null;
        do
        {
            if (parcel.dataPosition() < j)
            {
                int k = com.google.android.gms.common.internal.safeparcel.a.F(parcel);
                switch (com.google.android.gms.common.internal.safeparcel.a.aH(k))
                {
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, k);
                    break;

                case 1: // '\001'
                    i = com.google.android.gms.common.internal.safeparcel.a.g(parcel, k);
                    break;

                case 2: // '\002'
                    s1 = com.google.android.gms.common.internal.safeparcel.a.o(parcel, k);
                    break;

                case 3: // '\003'
                    s = com.google.android.gms.common.internal.safeparcel.a.o(parcel, k);
                    break;

                case 4: // '\004'
                    l1 = (l)com.google.android.gms.common.internal.safeparcel.a.a(parcel, k, l.CREATOR);
                    break;

                case 5: // '\005'
                    n2 = (n)com.google.android.gms.common.internal.safeparcel.a.a(parcel, k, n.CREATOR);
                    break;

                case 6: // '\006'
                    n1 = (n)com.google.android.gms.common.internal.safeparcel.a.a(parcel, k, n.CREATOR);
                    break;
                }
            } else
            if (parcel.dataPosition() != j)
            {
                throw new com.google.android.gms.common.internal.safeparcel.a.a((new StringBuilder()).append("Overread allowed size end=").append(j).toString(), parcel);
            } else
            {
                return new p(i, s1, s, l1, n2, n1);
            }
        } while (true);
    }

    public p[] gt(int i)
    {
        return new p[i];
    }

    public Object[] newArray(int i)
    {
        return gt(i);
    }
}
