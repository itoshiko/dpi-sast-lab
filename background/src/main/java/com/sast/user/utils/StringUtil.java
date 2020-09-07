package com.sast.user.utils;

public class StringUtil {
    public static boolean checkPassword(String passwordStr) {
        String regex = "(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z]).{8,30}";
        return passwordStr.matches(regex);
    }
}
