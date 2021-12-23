package com.example.love_dogs.functionality;

import android.util.Log;
import android.widget.TextView;

public class FieldCheck {
    public static boolean checkIsEmpty(TextView text, String error_message){
        if(text.getText().length() <= 0){
            text.setError(error_message);
            return true;
        }
        return false;
    }

    public static boolean checkNonIsEmpty(TextView[] texts, String[] names) throws ArrayIndexOutOfBoundsException{
        if(texts.length != names.length){
            throw new ArrayIndexOutOfBoundsException("names and texts array must be of the same size.");
        }
        for(int i =0;i<texts.length;i++){
            Log.d("firebase", names[i] + " length : " + texts[i].getText().length());
            if(checkIsEmpty(texts[i], names[i] + " cannot be empty!")){
                return false;
            }
        }
        return true;
    }
}
