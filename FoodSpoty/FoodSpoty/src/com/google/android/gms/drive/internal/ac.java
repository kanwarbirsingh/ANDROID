// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.google.android.gms.drive.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;

// Referenced classes of package com.google.android.gms.drive.internal:
//            GetDriveIdFromUniqueIdentifierRequest

public class ac
    implements android.os.Parcelable.Creator
{

    public ac()
    {
    }

    static void a(GetDriveIdFromUniqueIdentifierRequest getdriveidfromuniqueidentifierrequest, Parcel parcel, int i)
    {
        i = b.H(parcel);
        b.c(parcel, 1, getdriveidfromuniqueidentifierrequest.CK);
        b.a(parcel, 2, getdriveidfromuniqueidentifierrequest.QC, false);
        b.a(parcel, 3, getdriveidfromuniqueidentifierrequest.QD);
        b.H(parcel, i);
    }

    public GetDriveIdFromUniqueIdentifierRequest am(Parcel parcel)
    {
        boolean flag = false;
        int j = com.google.android.gms.common.internal.safeparcel.a.G(parcel);
        String s = null;
        int i = 0;
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
                    s = com.google.android.gms.common.internal.safeparcel.a.o(parcel, k);
                    break;

                case 3: // '\003'
                    flag = com.google.android.gms.common.internal.safeparcel.a.c(parcel, k);
                    break;
                }
            } else
            if (parcel.dataPosition() != j)
            {
                throw new com.google.android.gms.common.internal.safeparcel.a.a((new StringBuilder()).append("Overread allowed size end=").append(j).toString(), parcel);
            } else
            {
                return new GetDriveIdFromUniqueIdentifierRequest(i, s, flag);
            }
        } while (true);
    }

    public GetDriveIdFromUniqueIdentifierRequest[] bB(int i)
    {
        return new GetDriveIdFromUniqueIdentifierRequest[i];
    }

    public Object createFromParcel(Parcel parcel)
    {
        return am(parcel);
    }

    public Object[] newArray(int i)
    {
        return bB(i);
    }
}