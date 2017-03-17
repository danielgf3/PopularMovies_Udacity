package com.funnycat.popularmovies.utils;

/**
 * Created by daniel on 15/03/2017.
 */

public class CollectionUtils {

    public static String concatIntegerArray(Integer[] array) {
        StringBuilder sb = new StringBuilder();
        for (Integer item : array) sb.append(item).append("|");
        sb.substring(0, sb.length() - 2);
        return sb.toString();
    }

    public static Integer[] splitIntegerArray(String concatArray){
        if(concatArray.length()==0) return new Integer[0];
        String[] arrayS = concatArray.split("|");
        Integer[] arrayI = new Integer[arrayS.length];
        for(int i= 0; i<arrayS.length; i++) {
            try {
                arrayI[i] = Integer.parseInt(arrayS[i]);
            }catch (NumberFormatException e){}
        }
        return arrayI;
    }
}
