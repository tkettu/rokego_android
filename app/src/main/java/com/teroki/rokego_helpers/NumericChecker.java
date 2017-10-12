package com.teroki.rokego_helpers;

/**
 * Created by Tero on 11.10.2017.
 */

public class NumericChecker {

    public NumericChecker(){

    }

    public boolean isNumeric(String str){
        try{
            double d = Double.parseDouble(str);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }
}
