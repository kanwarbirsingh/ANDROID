// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.google.android.gms.games.leaderboard;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.GameEntity;
import com.google.android.gms.internal.jv;
import com.google.android.gms.internal.le;
import java.util.ArrayList;

// Referenced classes of package com.google.android.gms.games.leaderboard:
//            Leaderboard, LeaderboardVariant, LeaderboardVariantEntity

public final class LeaderboardEntity
    implements Leaderboard
{

    private final String OS;
    private final Uri WD;
    private final String WO;
    private final String adn;
    private final int ado;
    private final ArrayList adp;
    private final Game adq;

    public LeaderboardEntity(Leaderboard leaderboard)
    {
        adn = leaderboard.getLeaderboardId();
        OS = leaderboard.getDisplayName();
        WD = leaderboard.getIconImageUri();
        WO = leaderboard.getIconImageUrl();
        ado = leaderboard.getScoreOrder();
        Object obj = leaderboard.getGame();
        int j;
        if (obj == null)
        {
            obj = null;
        } else
        {
            obj = new GameEntity(((Game) (obj)));
        }
        adq = ((Game) (obj));
        leaderboard = leaderboard.getVariants();
        j = leaderboard.size();
        adp = new ArrayList(j);
        for (int i = 0; i < j; i++)
        {
            adp.add((LeaderboardVariantEntity)(LeaderboardVariantEntity)((LeaderboardVariant)leaderboard.get(i)).freeze());
        }

    }

    static int a(Leaderboard leaderboard)
    {
        return jv.hashCode(new Object[] {
            leaderboard.getLeaderboardId(), leaderboard.getDisplayName(), leaderboard.getIconImageUri(), Integer.valueOf(leaderboard.getScoreOrder()), leaderboard.getVariants()
        });
    }

    static boolean a(Leaderboard leaderboard, Object obj)
    {
        boolean flag1 = true;
        if (obj instanceof Leaderboard) goto _L2; else goto _L1
_L1:
        boolean flag = false;
_L4:
        return flag;
_L2:
        flag = flag1;
        if (leaderboard == obj) goto _L4; else goto _L3
_L3:
        obj = (Leaderboard)obj;
        if (!jv.equal(((Leaderboard) (obj)).getLeaderboardId(), leaderboard.getLeaderboardId()) || !jv.equal(((Leaderboard) (obj)).getDisplayName(), leaderboard.getDisplayName()) || !jv.equal(((Leaderboard) (obj)).getIconImageUri(), leaderboard.getIconImageUri()) || !jv.equal(Integer.valueOf(((Leaderboard) (obj)).getScoreOrder()), Integer.valueOf(leaderboard.getScoreOrder())))
        {
            break; /* Loop/switch isn't completed */
        }
        flag = flag1;
        if (jv.equal(((Leaderboard) (obj)).getVariants(), leaderboard.getVariants())) goto _L4; else goto _L5
_L5:
        return false;
    }

    static String b(Leaderboard leaderboard)
    {
        return jv.h(leaderboard).a("LeaderboardId", leaderboard.getLeaderboardId()).a("DisplayName", leaderboard.getDisplayName()).a("IconImageUri", leaderboard.getIconImageUri()).a("IconImageUrl", leaderboard.getIconImageUrl()).a("ScoreOrder", Integer.valueOf(leaderboard.getScoreOrder())).a("Variants", leaderboard.getVariants()).toString();
    }

    public boolean equals(Object obj)
    {
        return a(this, obj);
    }

    public Object freeze()
    {
        return mG();
    }

    public String getDisplayName()
    {
        return OS;
    }

    public void getDisplayName(CharArrayBuffer chararraybuffer)
    {
        le.b(OS, chararraybuffer);
    }

    public Game getGame()
    {
        return adq;
    }

    public Uri getIconImageUri()
    {
        return WD;
    }

    public String getIconImageUrl()
    {
        return WO;
    }

    public String getLeaderboardId()
    {
        return adn;
    }

    public int getScoreOrder()
    {
        return ado;
    }

    public ArrayList getVariants()
    {
        return new ArrayList(adp);
    }

    public int hashCode()
    {
        return a(this);
    }

    public boolean isDataValid()
    {
        return true;
    }

    public Leaderboard mG()
    {
        return this;
    }

    public String toString()
    {
        return b(this);
    }
}
