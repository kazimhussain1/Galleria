package com.example.galleria.app;

import android.app.Application;
import android.content.IntentFilter;

import com.example.galleria.common.ContextService;

public class GalleriaApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ContextService contextService = ContextService.getInstance();
        contextService.setContext(this);



    }


}
