// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.google.android.gms.internal;

import android.text.TextUtils;
import com.google.android.gms.common.images.WebImage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package com.google.android.gms.internal:
//            is

public class ix
{

    private static final is Hh = new is("MetadataUtils");
    private static final String Iq[] = {
        "Z", "+hh", "+hhmm", "+hh:mm"
    };
    private static final String Ir = (new StringBuilder()).append("yyyyMMdd'T'HHmmss").append(Iq[0]).toString();

    public static String a(Calendar calendar)
    {
        if (calendar == null)
        {
            Hh.b("Calendar object cannot be null", new Object[0]);
            calendar = null;
        } else
        {
            String s = Ir;
            Object obj = s;
            if (calendar.get(11) == 0)
            {
                obj = s;
                if (calendar.get(12) == 0)
                {
                    obj = s;
                    if (calendar.get(13) == 0)
                    {
                        obj = "yyyyMMdd";
                    }
                }
            }
            obj = new SimpleDateFormat(((String) (obj)));
            ((SimpleDateFormat) (obj)).setTimeZone(calendar.getTimeZone());
            obj = ((SimpleDateFormat) (obj)).format(calendar.getTime());
            calendar = ((Calendar) (obj));
            if (((String) (obj)).endsWith("+0000"))
            {
                return ((String) (obj)).replace("+0000", Iq[0]);
            }
        }
        return calendar;
    }

    public static void a(List list, JSONObject jsonobject)
    {
        JSONObject jsonobject1;
        int i;
        int j;
        try
        {
            list.clear();
            jsonobject = jsonobject.getJSONArray("images");
            j = jsonobject.length();
        }
        // Misplaced declaration of an exception variable
        catch (List list)
        {
            break; /* Loop/switch isn't completed */
        }
        i = 0;
_L2:
        if (i >= j)
        {
            break; /* Loop/switch isn't completed */
        }
        jsonobject1 = jsonobject.getJSONObject(i);
        try
        {
            list.add(new WebImage(jsonobject1));
        }
        catch (IllegalArgumentException illegalargumentexception) { }
        i++;
        continue; /* Loop/switch isn't completed */
        if (true) goto _L2; else goto _L1
_L1:
    }

    public static void a(JSONObject jsonobject, List list)
    {
        JSONArray jsonarray;
        if (list == null || list.isEmpty())
        {
            break MISSING_BLOCK_LABEL_65;
        }
        jsonarray = new JSONArray();
        for (list = list.iterator(); list.hasNext(); jsonarray.put(((WebImage)list.next()).toJson())) { }
        jsonobject.put("images", jsonarray);
        return;
        jsonobject;
    }

    public static Calendar aL(String s)
    {
        if (TextUtils.isEmpty(s))
        {
            Hh.b("Input string is empty or null", new Object[0]);
            return null;
        }
        Object obj = aM(s);
        if (TextUtils.isEmpty(((CharSequence) (obj))))
        {
            Hh.b("Invalid date format", new Object[0]);
            return null;
        }
        String s2 = aN(s);
        s = "yyyyMMdd";
        String s1 = ((String) (obj));
        if (!TextUtils.isEmpty(s2))
        {
            s1 = (new StringBuilder()).append(((String) (obj))).append("T").append(s2).toString();
            if (s2.length() == "HHmmss".length())
            {
                s = "yyyyMMdd'T'HHmmss";
            } else
            {
                s = Ir;
            }
        }
        obj = GregorianCalendar.getInstance();
        try
        {
            s = (new SimpleDateFormat(s)).parse(s1);
        }
        // Misplaced declaration of an exception variable
        catch (String s)
        {
            Hh.b("Error parsing string: %s", new Object[] {
                s.getMessage()
            });
            return null;
        }
        ((Calendar) (obj)).setTime(s);
        return ((Calendar) (obj));
    }

    private static String aM(String s)
    {
        if (TextUtils.isEmpty(s))
        {
            Hh.b("Input string is empty or null", new Object[0]);
            return null;
        }
        try
        {
            s = s.substring(0, "yyyyMMdd".length());
        }
        // Misplaced declaration of an exception variable
        catch (String s)
        {
            Hh.c("Error extracting the date: %s", new Object[] {
                s.getMessage()
            });
            return null;
        }
        return s;
    }

    private static String aN(String s)
    {
        if (!TextUtils.isEmpty(s)) goto _L2; else goto _L1
_L1:
        Hh.b("string is empty or null", new Object[0]);
_L7:
        return null;
_L2:
        int i = s.indexOf('T');
        if (i != "yyyyMMdd".length())
        {
            Hh.b("T delimeter is not found", new Object[0]);
            return null;
        }
        try
        {
            s = s.substring(i + 1);
        }
        // Misplaced declaration of an exception variable
        catch (String s)
        {
            Hh.b("Error extracting the time substring: %s", new Object[] {
                s.getMessage()
            });
            return null;
        }
        if (s.length() == "HHmmss".length())
        {
            return s;
        }
        s.charAt("HHmmss".length());
        JVM INSTR lookupswitch 3: default 140
    //                   43: 142
    //                   45: 142
    //                   90: 158;
           goto _L3 _L4 _L4 _L5
_L5:
        continue; /* Loop/switch isn't completed */
_L3:
        return null;
_L4:
        if (!aO(s)) goto _L7; else goto _L6
_L6:
        return s.replaceAll("([\\+\\-]\\d\\d):(\\d\\d)", "$1$2");
        if (s.length() != "HHmmss".length() + Iq[0].length()) goto _L7; else goto _L8
_L8:
        return (new StringBuilder()).append(s.substring(0, s.length() - 1)).append("+0000").toString();
    }

    private static boolean aO(String s)
    {
        int i = s.length();
        int j = "HHmmss".length();
        return i == Iq[1].length() + j || i == Iq[2].length() + j || i == j + Iq[3].length();
    }

}
