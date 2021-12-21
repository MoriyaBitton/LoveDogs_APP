package com.example.love_dogs.functionality;

import android.content.Context;
import android.content.Intent;

import com.example.love_dogs.posts.ViewPostActivity;

import java.util.Iterator;
import java.util.LinkedList;

public class FragmentManager {
    public static IFragmentBackable latest;

    private static LinkedList<FragmentExtended> list = new LinkedList<>();

    public static void AddToStack(FragmentExtended fragment){
        list.push(fragment);
    }

    public static boolean BackOnce(){
        if(list.size() > 0){
            FragmentExtended last = list.pop();
            last.OnBackPressed();
            return true;
        }
        return false;
    }

    public static void GoToRoot(Context context){
        list = new LinkedList<>();
        latest = null;

        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void RemoveSelf(FragmentExtended fragment){
        if(fragment.IsInQueue()){
            list.remove(fragment);
        }
    }

    public static void BackAllTillSelf(FragmentExtended fragment){
        if(fragment.IsInQueue()){
            Iterator<FragmentExtended> iter = list.iterator();
            while (iter.hasNext()){
                FragmentExtended current = iter.next();
                if(current == fragment){
                    break;
                }
                current.OnBackPressed();
            }
        }
    }
}
