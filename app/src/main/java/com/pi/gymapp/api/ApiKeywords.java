package com.pi.gymapp.api;

import android.content.Context;

import com.pi.gymapp.R;

import java.util.Arrays;

public class ApiKeywords {

    private static String[] difficulties = new String[]{"rookie", "beginner", "intermediate",
            "advanced", "expert"};

    private static String[] cycleTypes = new String[]{"warmup", "exercise", "cooldown"};


    public static String getDifficulty(Context context, String keyword){
        int i = Arrays.asList(difficulties).indexOf(keyword);
        return context.getResources().getStringArray(R.array.difficulties)[i];
    }

    public static String getCycleType(Context context, String keyword){
        int i = Arrays.asList(cycleTypes).indexOf(keyword);
        return context.getResources().getStringArray(R.array.cycleTypes)[i];
    }

}
