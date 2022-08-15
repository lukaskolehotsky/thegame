package com.example.tetris;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import com.example.tetris.constants.SharedPreferencesConstants;

public class SharedPreferencesService {

    public void storePositions(Context context, View view) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putFloat(SharedPreferencesConstants.X, view.getX());
        prefsEditor.putFloat(SharedPreferencesConstants.Y, view.getY());
        prefsEditor.apply();
    }

    public Float retrievePosition(Context context, String position) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getFloat(position, -1);
    }

    public Boolean existsPositions(Context context) {
        Float x = retrievePosition(context, SharedPreferencesConstants.X);
        Float y = retrievePosition(context, SharedPreferencesConstants.Y);

        return x != -1 && y != -1;
    }

    public void clearSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.clear();
        prefsEditor.apply();
    }

}
