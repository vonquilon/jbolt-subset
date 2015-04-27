package com.syas.jbolt.model.user;


public class Utils {

    public static final String JSON = "com.syas.jbolt.JSON";

    private static final float INCHESPERCM = 0.39370f;
    private static final int INCHESPERFT = 12;

    static int inchesToCms(int inches) {
        return (int)(inches/INCHESPERCM);
    }

    static String cmsToInches(int cms) {
        int totalInches = Math.round(cms * INCHESPERCM);
        int feet = totalInches / INCHESPERFT;
        int inches = totalInches % INCHESPERFT;

        return feet + "\'" + inches + "\"";
    }
}
