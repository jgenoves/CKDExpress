package com.example.jgenoves.ckdexpress;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EGFRListSingleton {

    private static EGFRListSingleton sEGFRListSingleton;

    private List<EGFREntry> mEntries;

    public static EGFRListSingleton get(Context context){
        if(sEGFRListSingleton == null){
            sEGFRListSingleton = new EGFRListSingleton(context);
        }
        return sEGFRListSingleton;
    }

    private EGFRListSingleton(Context context){
        mEntries = new ArrayList<>();
    }

    private void addEntry(EGFREntry e){ mEntries.add(e); }

    public List<EGFREntry> getEntries() { return mEntries; }

    public EGFREntry getEntry(UUID id){
        for(EGFREntry e:mEntries){
            if(e.getId().equals(id)){
                return e;
            }

        }
        return null;
    }
}
