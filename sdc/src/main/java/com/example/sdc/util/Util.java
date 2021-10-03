package com.example.sdc.util;

import com.example.sdc.exception.CustomizedRequestException;

public class Util {
    public static String stackTrace(Throwable ex) {
        StackTraceElement[] ste = ex.getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append(ex.getMessage());
        for (int i = 0; i < ste.length; i++) {
            sb.append(ste[i].toString());
            sb.append("\n");
            if (i == 10) {
                break;
            }
        }
        return sb.toString();
    }
    public void check(String type){
        switch (type){
            case "A": break;
            case "L": break;
            default: throw new CustomizedRequestException("Issue_type Aplication(A) or Letter(L) contains only one character", 21);
        }
    }
}
