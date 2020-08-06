package com.sast.user.utils;

public class StringUtil {
    public static boolean checkPassword(String passwordStr) {
        String regexZ = "\\d*";
        String regexS = "[a-zA-Z]+";
        String regexT = "\\W+$";
        String regexZT = "\\D*";
        String regexST = "[\\d\\W]*";
        String regexZS = "\\w*";
        String regexZST = "[\\w\\W]*";

        if (passwordStr.matches(regexZ)) {
            return false;
        }
        if (passwordStr.matches(regexS)) {
            return false;
        }
        if (passwordStr.matches(regexT)) {
            return false;
        }
        if (passwordStr.matches(regexZT)) {
            return true;
        }
        if (passwordStr.matches(regexST)) {
            return true;
        }
        if (passwordStr.matches(regexZS)) {
            return true;
        }
        if (passwordStr.matches(regexZST)) {
            return true;
        }
        return true;

    }
}
