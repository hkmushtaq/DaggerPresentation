package io.hkmushtaq.sampledagger.di.main;

import dagger.Module;
import dagger.Provides;
import io.hkmushtaq.sampledagger.di.scopes.MainActivityScope;
import io.hkmushtaq.sampledagger.ui.MainActivity;
import io.hkmushtaq.sampledagger.ui.MainView;

@Module
public class MainActivityModule {

    private final MainActivity mMainActivity;

    public MainActivityModule(MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
    }

    @Provides
    @MainActivityScope
    MainView provideMainView() {
        return mMainActivity;
    }

}
