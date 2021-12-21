package com.example.love_dogs.functionality;

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
