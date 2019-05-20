package com.william.parksystem.CaptureTheError;

import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();
        CrashHandlerUtil.getInstance().init(this);
    }
}
