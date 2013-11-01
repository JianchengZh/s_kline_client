package com.android.dazhihui.util;


import android.util.Log;

import com.android.dazhihui.Globe;

import com.android.dazhihui.http.StructRequest;
import com.android.dazhihui.rms.RmsAdapter;

import com.android.dazhihui.wml.Tools;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Vector;

public class Functions
{
  private static String urlBase;

/*  public static boolean LimitCheck(int paramInt, WindowsManager paramWindowsManager)
  {

    return true;
  }*/

  public static void Log(String paramString)
  {
    if (!(Globe.debug))
      return;
    System.out.println(paramString);
  }

  public static void Log(String paramString1, String paramString2)
  {
    if (!(Globe.debug))
      return;
    Log.e(paramString1, paramString2);
  }

  public static byte[] StringToKey(String paramString)
  {
    byte[] arrayOfByte = new byte[8];
    for (int i = 0; ; ++i)
    {
      if (i >= arrayOfByte.length)
        return arrayOfByte;
      arrayOfByte[i] = (byte)Integer.parseInt(paramString.substring(i * 2, 2 + i * 2), 16);
    }
  }

  public static void UpdateStock(String[] paramArrayOfString)
  {}

  public static boolean addFreeStock(String paramString)
  {
    if (Globe.vecFreeStock.size() >= 100)
      return false;
    for (int i = 0; ; ++i)
    {
      if (i >= Globe.vecFreeStock.size())
      {
        Globe.vecFreeStock.add(0, paramString);
        return true;
      }
      if (((String)Globe.vecFreeStock.elementAt(i)).equals(paramString))
        return false;
    }
  }

  public static int byteArrayToInt(byte[] paramArrayOfByte, int paramInt)
  {
    return ((0xFF & paramArrayOfByte[(paramInt + 3)]) << 24 | (0xFF & paramArrayOfByte[(paramInt + 2)]) << 16 | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 8 | 0xFF & paramArrayOfByte[paramInt]);
  }

  public static int byteArrayToShort(byte[] paramArrayOfByte, int paramInt)
  {
    int i = (0xFF & paramArrayOfByte[(paramInt + 1)]) << 8 | 0xFF & paramArrayOfByte[paramInt];
    if (i > 32767)
      return (i - 65536);
    return i;
  }

  public static int byteArrayToWord(byte[] paramArrayOfByte, int paramInt)
  {
    return (0xFFFF & byteArrayToShort(paramArrayOfByte, paramInt));
  }

  public static int byteToUnsigned(byte paramByte)
  {
    return (paramByte & 0xFF);
  }

  public static String bytesToString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramInt2 == 0)
      return "";
    if (paramArrayOfByte[(paramInt1 + paramInt2 - 1)] == 0)
      --paramInt2;
    return new String(paramArrayOfByte, paramInt1, paramInt2);
  }

  public static String bytesToUNIString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramArrayOfByte[(paramInt1 + paramInt2 - 1)] == 0)
      --paramInt2;
    try
    {
      String str = new String(paramArrayOfByte, paramInt1, paramInt2, "UNICODE");
      return str;
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  public static String bytesToUTFString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramArrayOfByte[(paramInt1 + paramInt2 - 1)] == 0)
      --paramInt2;
    try
    {
      String str = new String(paramArrayOfByte, paramInt1, paramInt2, "UTF-8");
      return str;
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  public static void delAllFreeStock()
  {
    Globe.vecFreeStock.removeAllElements();
    saveFreeStock();
  }

  public static void delFreeStock(String paramString)
  {
    Globe.vecFreeStock.removeElement(paramString);
    saveFreeStock();
  }

  public static boolean existFreeStock(String paramString)
  {
    for (int i = 0; ; ++i)
    {
      if (i >= Globe.vecFreeStock.size())
        return false;
      if (((String)Globe.vecFreeStock.elementAt(i)).equals(paramString))
        return true;
    }
  }

/*  public static String format(byte[] paramArrayOfByte)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = 0;
    if (i >= paramArrayOfByte.length)
      label10: return localStringBuffer.toString().toUpperCase();
    String str1 = Integer.toString(0xFF & paramArrayOfByte[i], 16);
    if (str1.length() == 2);
    for (String str2 = str1; ; str2 = "0" + str1)
    {
      localStringBuffer.append(str2);
      ++i;
      break label10:
    }
  }*/

/*  public static String formatNumString(long paramLong)
  {
    if (paramLong == 0L)
      return "-";
    return formatNumString(String.valueOf(paramLong));
  }*/

/*  public static String formatNumString(String paramString)
  {
    String str1 = paramString.trim();
    int i = str1.length();
    if (i <= 7)
      return str1;
    if (i < 10)
    {
      StringBuffer localStringBuffer1 = new StringBuffer();
      localStringBuffer1.append(str1.substring(0, i - 4)).append(".").append(str1.substring(i - 4, i));
      String str2 = localStringBuffer1.toString();
      StringBuffer localStringBuffer2 = new StringBuffer();
      localStringBuffer2.append(str2.substring(0, 5));
      String str3 = localStringBuffer2.toString();
      if (str3.charAt(4) == '.')
      {
        localStringBuffer2 = new StringBuffer();
        localStringBuffer2.append(str3.substring(0, 4)).append("��");
      }
      while (true)
      {
        return localStringBuffer2.toString();
        if (str3.charAt(4) == '0')
        {
          localStringBuffer2 = new StringBuffer();
          localStringBuffer2.append(str3.substring(0, 4)).append("1").append("��");
        }
        localStringBuffer2.append("��");
      }
    }
    StringBuffer localStringBuffer3 = new StringBuffer();
    localStringBuffer3.append(str1.substring(0, i - 8)).append(".").append(str1.substring(i - 8, i));
    String str4 = localStringBuffer3.toString();
    StringBuffer localStringBuffer4 = new StringBuffer();
    localStringBuffer4.append(str4.substring(0, 5));
    String str5 = localStringBuffer4.toString();
    if (str5.charAt(4) == '.')
    {
      localStringBuffer4 = new StringBuffer();
      localStringBuffer4.append(str5.substring(0, 4)).append("��");
    }
    while (true)
    {
      return localStringBuffer4.toString();
      if (str5.charAt(4) == '0')
      {
        localStringBuffer4 = new StringBuffer();
        localStringBuffer4.append(str5.substring(0, 4)).append("1").append("��");
      }
      localStringBuffer4.append("��");
    }
  }*/

  private static String formatNumber(int paramInt)
  {
    if (paramInt < 10)
      return "0" + paramInt;
    return Integer.toString(paramInt);
  }

  public static String formatSpecString(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; ; ++i)
    {
      if (i >= paramString.length())
        return localStringBuffer.toString();
      char c = paramString.charAt(i);
      if (c == '\r')
        continue;
      localStringBuffer.append(c);
    }
  }

/*  public static String formatString(String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (BaseFuction.stringWidthWithSize(paramString, paramInt2) < paramInt1 * 3)
      localStringBuffer.append(paramString);
    int i;
    int j;
    int k;
    while (true)
    {
      return localStringBuffer.toString();
      i = 0;
      j = paramString.length();
      k = 0;
      label45: if ((k < paramInt3) || (i >= paramString.length()))
        break;
      localStringBuffer.append("...");
    }
    for (int l = i; ; ++l)
    {
      if (l > j);
      while (true)
      {
        ++k;
        break label45:
        String str = paramString.substring(i, l);
        if (BaseFuction.stringWidthWithSize(str, paramInt2) + BaseFuction.stringWidthWithSize("��", paramInt2) < paramInt1)
          break;
        localStringBuffer.append(str);
        i = l;
      }
    }
  }*/

/*  public static String[] getAdsPort(String paramString)
  {
    String[] arrayOfString = new String[2];
    for (int i = 0; ; ++i)
    {
      int j = paramString.length();
      int k = 0;
      if (i >= j);
      while (true)
      {
        arrayOfString[0] = paramString.substring(0, k - 1);
        arrayOfString[1] = paramString.substring(k, paramString.length());
        return arrayOfString;
        if (paramString.charAt(i) != ':')
          break;
        k = i + 1;
      }
    }
  }*/

  public static String getDay(int paramInt)
  {
    int[] arrayOfInt = new int[5];
    arrayOfInt[0] = (paramInt & 0x3F);
    arrayOfInt[1] = (0x1F & paramInt >>> 6);
    arrayOfInt[2] = (0x1F & paramInt >>> 11);
    arrayOfInt[3] = (0xF & paramInt >>> 16);
    arrayOfInt[4] = (0xFFF & paramInt >>> 20);
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(formatNumber(arrayOfInt[3])).append(formatNumber(arrayOfInt[2])).append(" ").append(formatNumber(arrayOfInt[1])).append(":").append(formatNumber(arrayOfInt[0]));
    return localStringBuffer.toString();
  }

  public static String getDay_OnlyHM(int paramInt)
  {
    int[] arrayOfInt = new int[5];
    arrayOfInt[0] = (paramInt & 0x3F);
    arrayOfInt[1] = (0x1F & paramInt >>> 6);
    arrayOfInt[2] = (0x1F & paramInt >>> 11);
    arrayOfInt[3] = (0xF & paramInt >>> 16);
    arrayOfInt[4] = (0xFFF & paramInt >>> 20);
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(formatNumber(arrayOfInt[1])).append(":").append(formatNumber(arrayOfInt[0]));
    return localStringBuffer.toString();
  }

  public static String getDay_OnlyMD(int paramInt)
  {
    int[] arrayOfInt = new int[5];
    arrayOfInt[0] = (paramInt & 0x3F);
    arrayOfInt[1] = (0x1F & paramInt >>> 6);
    arrayOfInt[2] = (0x1F & paramInt >>> 11);
    arrayOfInt[3] = (0xF & paramInt >>> 16);
    arrayOfInt[4] = (0xFFF & paramInt >>> 20);
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(formatNumber(arrayOfInt[3])).append("/").append(formatNumber(arrayOfInt[2]));
    return localStringBuffer.toString();
  }

/*  public static String getLoginUserUrl(String paramString)
  {
    String str1 = "-1";
    String str2;
    if (Globe.userName.length() > 0)
    {
      str1 = Globe.userName;
      if (Globe.phoneNumber.length() > 0)
        str2 = Globe.phoneNumber;
    }
    for (String str3 = str1; ; str3 = str1)
    {
      StringBuffer localStringBuffer1 = new StringBuffer();
      Calendar.getInstance();
      String str4 = String.valueOf(Globe.month);
      String str5 = String.valueOf(Globe.day);
      if (Globe.month < 10)
        str4 = "0" + str4;
      if (Globe.day < 10)
        str5 = "0" + str5;
      String str6 = Globe.year + "-" + str4 + "-" + str5;
      localStringBuffer1.append(str3);
      localStringBuffer1.append("3123jklds@#$00");
      localStringBuffer1.append(str6);
      localStringBuffer1.append(str2);
      String str7 = My_MD5.md5s(localStringBuffer1.toString());
      StringBuffer localStringBuffer2 = new StringBuffer();
      localStringBuffer2.append(paramString + "?u=");
      String str8 = str3;
      String str9 = "&m=" + str2 + "&p=";
      localStringBuffer2.append(str8);
      localStringBuffer2.append(str9);
      localStringBuffer2.append(str7);
      String str10 = localStringBuffer2.toString();
      Log("url&& = " + str10);
      return str10;
      str2 = "-1";
    }
  }*/

  public static String getRealCode(String paramString)
  {
    String str1 = paramString;
    if (paramString.length() > 2)
    {
      String str2 = paramString.substring(0, 2);
      if ((str2.equals("SZ")) || (str2.equals("SH")) || (str2.equals("BI")) || (str2.equals("SC")) || (str2.equals("DC")) || (str2.equals("ZC")) || (str2.equals("SF")) || (str2.equals("SG")) || (str2.equals("FE")) || (str2.equals("HK")))
        str1 = paramString.substring(2, paramString.length());
    }
    return str1;
  }

  public static String getTextView(int paramInt)
  {
    if ((paramInt < 0) || (Globe.TEXTVIEW_DATA == null) || (paramInt >= Globe.TEXTVIEW_DATA.length))
      return "";
    if (Globe.TEXTVIEW_DATA[paramInt] == null)
      return "";
    return Globe.TEXTVIEW_DATA[paramInt];
  }



  public static byte[] intToByteArray(int paramInt)
  {
    byte[] arrayOfByte = new byte[4];
    arrayOfByte[0] = (byte)(paramInt & 0xFF);
    arrayOfByte[1] = (byte)((0xFF00 & paramInt) >> 8);
    arrayOfByte[2] = (byte)((0xFF0000 & paramInt) >> 16);
    arrayOfByte[3] = (byte)((0xFF000000 & paramInt) >> 24);
    return arrayOfByte;
  }

  public static void killProcess()
  {

  }

  public static void removeAllScreen()
  {}

  public static void saveFreeStock()
  {}

  public static void saveLaterStock()
  {}

/*  public static final Vector splitStr(String paramString, int paramInt)
  {
    if (paramString == null)
      return null;
    int i = (int)BaseFuction.mPaint.getTextSize();
    if (paramInt < i)
      paramInt = i;
    int j = 0;
    int k = 0;
    Vector localVector = new Vector(89, 89);
    label39: int i1;
    int l;
    while (true)
    {
      if (paramString.length() <= j)
      {
        localVector.addElement(paramString);
        return localVector;
      }
      i1 = paramString.charAt(j);
      switch (i1)
      {
      default:
        if (k + i <= paramInt)
          break;
        localVector.addElement(paramString.substring(0, j - 1));
        paramString = paramString.substring(j - 1);
        l = 0;
        j = 0;
        break;
      case 10:
        localVector.addElement(paramString.substring(0, j));
        paramString = paramString.substring(j + 1);
        l = 0;
        j = 0;
        break;
      case 9:
        StringBuffer localStringBuffer3 = new StringBuffer(paramString);
        localStringBuffer3.deleteCharAt(j);
        localStringBuffer3.insert(j, "");
        paramString = localStringBuffer3.toString();
        break;
      case 13:
        StringBuffer localStringBuffer2 = new StringBuffer(paramString);
        localStringBuffer2.deleteCharAt(j);
        localStringBuffer2.insert(j, "");
        paramString = localStringBuffer2.toString();
        break;
      case 0:
      }
      StringBuffer localStringBuffer1 = new StringBuffer(paramString);
      localStringBuffer1.deleteCharAt(j);
      localStringBuffer1.insert(j, "");
      paramString = localStringBuffer1.toString();
    }
    if (i1 == 32)
      l += (i >> 2);
    while (true)
    {
      ++j;
      break label39:
      l += i;
    }
  }
*/
  public static byte[] wordToByteArray(int paramInt)
  {
    byte[] arrayOfByte = new byte[2];
    arrayOfByte[1] = (byte)((0xFF00 & paramInt) >> 8);
    arrayOfByte[0] = (byte)(paramInt & 0xFF);
    return arrayOfByte;
  }
}

/* Location:           D:\android\Git\dzh_585\dzh_585\libs\dazhihui_5_85_dex2jar.jar
 * Qualified Name:     com.android.dazhihui.util.Functions
 * Java Class Version: 6 (50.0)
 * JD-Core Version:    0.5.3
 */