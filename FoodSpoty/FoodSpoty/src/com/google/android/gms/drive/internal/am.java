// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.google.android.gms.drive.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;

// Referenced classes of package com.google.android.gms.drive.internal:
//            OnDeviceUsagePreferenceResponse, FileUploadPreferencesImpl

public class am
    implements android.os.Parcelable.Creator
{

    public am()
    {
    }

    static void a(OnDeviceUsagePreferenceResponse ondeviceusagepreferenceresponse, Parcel parcel, int i)
    {
        int j = b.H(parcel);
        b.c(parcel, 1, ondeviceusagepreferenceresponse.CK);
        b.a(parcel, 2, ondeviceusagepreferenceresponse.QK, i, false);
        b.H(parcel, j);
    }

    public OnDeviceUsagePreferenceResponse ar(Parcel parcel)
    {
        int j = com.google.android.gms.common.internal.safeparcel.a.G(parcel);
        int i = 0;
        FileUploadPreferencesImpl fileuploadpreferencesimpl = null;
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
                    fileuploadpreferencesimpl = (FileUploadPreferencesImpl)com.google.android.gms.common.internal.safeparcel.a.a(parcel, k, FileUploadPreferencesImpl.CREATOR);
                    break;
                }
            } else
            if (parcel.dataPosition() != j)
            {
                throw new com.google.android.gms.common.internal.safeparcel.a.a((new StringBuilder()).append("Overread allowed size end=").append(j).toString(), parcel);
            } else
            {
                return new OnDeviceUsagePreferenceResponse(i, fileuploadpreferencesimpl);
            }
        } while (true);
    }

    public OnDeviceUsagePreferenceResponse[] bG(int i)
    {
        return new OnDeviceUsagePreferenceResponse[i];
    }

    public Object createFromParcel(Parcel parcel)
    {
        return ar(parcel);
    }

    public Object[] newArray(int i)
    {
        return bG(i);
    }
}
