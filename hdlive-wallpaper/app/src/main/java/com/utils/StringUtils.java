package com.utils;

public class StringUtils {

    public static boolean isValidString(String string)
    {
        return string != null && string.trim().length() > 0;
    }
    public static String setString(String string)
    {
        if(string !=null && string.trim().length() > 0)
            return string;
        else
            return "";
    }
}
