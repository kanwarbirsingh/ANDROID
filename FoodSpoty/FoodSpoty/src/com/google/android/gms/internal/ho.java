// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.google.android.gms.internal;

import java.util.HashMap;
import java.util.Map;

public class ho
{

    private static final String Df[] = {
        "text1", "text2", "icon", "intent_action", "intent_data", "intent_data_id", "intent_extra_data", "suggest_large_icon", "intent_activity"
    };
    private static final Map Dg;

    public static String P(int i)
    {
        if (i < 0 || i >= Df.length)
        {
            return null;
        } else
        {
            return Df[i];
        }
    }

    public static int as(String s)
    {
        Integer integer = (Integer)Dg.get(s);
        if (integer == null)
        {
            throw new IllegalArgumentException((new StringBuilder()).append("[").append(s).append("] is not a valid global search section name").toString());
        } else
        {
            return integer.intValue();
        }
    }

    public static int fF()
    {
        return Df.length;
    }

    static 
    {
        int i = 0;
        Dg = new HashMap(Df.length);
        for (; i < Df.length; i++)
        {
            Dg.put(Df[i], Integer.valueOf(i));
        }

    }
}
