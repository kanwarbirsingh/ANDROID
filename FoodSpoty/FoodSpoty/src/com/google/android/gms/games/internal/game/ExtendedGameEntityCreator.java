// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.google.android.gms.games.internal.game;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import com.google.android.gms.games.GameEntity;
import com.google.android.gms.games.snapshot.SnapshotMetadataEntity;

// Referenced classes of package com.google.android.gms.games.internal.game:
//            ExtendedGameEntity, GameBadgeEntity

public class ExtendedGameEntityCreator
    implements android.os.Parcelable.Creator
{

    public ExtendedGameEntityCreator()
    {
    }

    static void a(ExtendedGameEntity extendedgameentity, Parcel parcel, int i)
    {
        int j = b.H(parcel);
        b.a(parcel, 1, extendedgameentity.mi(), i, false);
        b.c(parcel, 1000, extendedgameentity.getVersionCode());
        b.c(parcel, 2, extendedgameentity.lZ());
        b.a(parcel, 3, extendedgameentity.ma());
        b.c(parcel, 4, extendedgameentity.mb());
        b.a(parcel, 5, extendedgameentity.mc());
        b.a(parcel, 6, extendedgameentity.md());
        b.a(parcel, 7, extendedgameentity.me(), false);
        b.a(parcel, 8, extendedgameentity.mf());
        b.a(parcel, 9, extendedgameentity.mg(), false);
        b.c(parcel, 10, extendedgameentity.lY(), false);
        b.a(parcel, 11, extendedgameentity.mh(), i, false);
        b.H(parcel, j);
    }

    public Object createFromParcel(Parcel parcel)
    {
        return cu(parcel);
    }

    public ExtendedGameEntity cu(Parcel parcel)
    {
        int l = com.google.android.gms.common.internal.safeparcel.a.G(parcel);
        int k = 0;
        GameEntity gameentity = null;
        int j = 0;
        boolean flag = false;
        int i = 0;
        long l3 = 0L;
        long l2 = 0L;
        String s1 = null;
        long l1 = 0L;
        String s = null;
        java.util.ArrayList arraylist = null;
        SnapshotMetadataEntity snapshotmetadataentity = null;
        do
        {
            if (parcel.dataPosition() < l)
            {
                int i1 = com.google.android.gms.common.internal.safeparcel.a.F(parcel);
                switch (com.google.android.gms.common.internal.safeparcel.a.aH(i1))
                {
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i1);
                    break;

                case 1: // '\001'
                    gameentity = (GameEntity)com.google.android.gms.common.internal.safeparcel.a.a(parcel, i1, GameEntity.CREATOR);
                    break;

                case 1000: 
                    k = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i1);
                    break;

                case 2: // '\002'
                    j = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i1);
                    break;

                case 3: // '\003'
                    flag = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i1);
                    break;

                case 4: // '\004'
                    i = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i1);
                    break;

                case 5: // '\005'
                    l3 = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i1);
                    break;

                case 6: // '\006'
                    l2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i1);
                    break;

                case 7: // '\007'
                    s1 = com.google.android.gms.common.internal.safeparcel.a.o(parcel, i1);
                    break;

                case 8: // '\b'
                    l1 = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i1);
                    break;

                case 9: // '\t'
                    s = com.google.android.gms.common.internal.safeparcel.a.o(parcel, i1);
                    break;

                case 10: // '\n'
                    arraylist = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i1, GameBadgeEntity.CREATOR);
                    break;

                case 11: // '\013'
                    snapshotmetadataentity = (SnapshotMetadataEntity)com.google.android.gms.common.internal.safeparcel.a.a(parcel, i1, SnapshotMetadataEntity.CREATOR);
                    break;
                }
            } else
            if (parcel.dataPosition() != l)
            {
                throw new com.google.android.gms.common.internal.safeparcel.a.a((new StringBuilder()).append("Overread allowed size end=").append(l).toString(), parcel);
            } else
            {
                return new ExtendedGameEntity(k, gameentity, j, flag, i, l3, l2, s1, l1, s, arraylist, snapshotmetadataentity);
            }
        } while (true);
    }

    public ExtendedGameEntity[] eb(int i)
    {
        return new ExtendedGameEntity[i];
    }

    public Object[] newArray(int i)
    {
        return eb(i);
    }
}
