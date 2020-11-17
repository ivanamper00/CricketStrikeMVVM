package com.etitgib.cricketstrikemvvm.repositories;

public class Presets {
    public static String seriesId = "2693";
    public static String teamId = "0";
    public static String matchId = "0";
//    49296
    public static int teamCount = 0;

    public static String nullable(String string){
        return string == null || string.equals("") ?  "N/A"  : string;
    }
}
