package io.hkmushtaq.sampledagger;

import android.app.Application;

import io.hkmushtaq.sampledagger.di.AppComponent;
import io.hkmushtaq.sampledagger.di.AppModule;
import io.hkmushtaq.sampledagger.di.DaggerAppComponent;

public class SampleApplication extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        mAppComponent.inject(this);
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
