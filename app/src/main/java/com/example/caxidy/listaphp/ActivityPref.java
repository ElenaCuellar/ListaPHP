package com.example.caxidy.listaphp;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ActivityPref extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
