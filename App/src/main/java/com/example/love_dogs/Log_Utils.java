package com.example.love_dogs;

import android.provider.ContactsContract;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Log_Utils {

    //This Class Contains Functions to process register and login inputs//

    public static boolean valid_email(String email){
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(email);
        return m.matches();
    }


    public static boolean valid_pass(String pass){
        String pPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8}$" ;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pPattern);
        java.util.regex.Matcher m = p.matcher(pass);
        return m.matches();
    }


}
