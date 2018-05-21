// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.google.android.gms.games.multiplayer;

import android.os.Parcel;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.data.d;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.GameRef;
import com.google.android.gms.internal.jx;
import java.util.ArrayList;

// Referenced classes of package com.google.android.gms.games.multiplayer:
//            Invitation, ParticipantRef, InvitationEntity, Participant

public final class InvitationRef extends d
    implements Invitation
{

    private final ArrayList adV;
    private final ParticipantRef adY;
    private final Game adq;

    InvitationRef(DataHolder dataholder, int i, int j)
    {
        super(dataholder, i);
        adq = new GameRef(dataholder, i);
        adV = new ArrayList(j);
        String s = getString("external_inviter_id");
        i = 0;
        dataholder = null;
        for (; i < j; i++)
        {
            ParticipantRef participantref = new ParticipantRef(JG, KZ + i);
            if (participantref.getParticipantId().equals(s))
            {
                dataholder = participantref;
            }
            adV.add(participantref);
        }

        adY = (ParticipantRef)jx.b(dataholder, "Must have a valid inviter!");
    }

    public int describeContents()
    {
        return 0;
    }

    public boolean equals(Object obj)
    {
        return InvitationEntity.a(this, obj);
    }

    public Invitation freeze()
    {
        return new InvitationEntity(this);
    }

    public volatile Object freeze()
    {
        return freeze();
    }

    public int getAvailableAutoMatchSlots()
    {
        if (!getBoolean("has_automatch_criteria"))
        {
            return 0;
        } else
        {
            return getInteger("automatch_max_players");
        }
    }

    public long getCreationTimestamp()
    {
        return Math.max(getLong("creation_timestamp"), getLong("last_modified_timestamp"));
    }

    public Game getGame()
    {
        return adq;
    }

    public String getInvitationId()
    {
        return getString("external_invitation_id");
    }

    public int getInvitationType()
    {
        return getInteger("type");
    }

    public Participant getInviter()
    {
        return adY;
    }

    public ArrayList getParticipants()
    {
        return adV;
    }

    public int getVariant()
    {
        return getInteger("variant");
    }

    public int hashCode()
    {
        return InvitationEntity.a(this);
    }

    public String toString()
    {
        return InvitationEntity.b(this);
    }

    public void writeToParcel(Parcel parcel, int i)
    {
        ((InvitationEntity)freeze()).writeToParcel(parcel, i);
    }
}
