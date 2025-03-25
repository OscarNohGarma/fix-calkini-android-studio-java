package com.example.fixcalkini;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    public Context context;

    public static int incre = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }
}
