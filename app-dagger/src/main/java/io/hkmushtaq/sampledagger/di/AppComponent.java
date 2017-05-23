package io.hkmushtaq.sampledagger.di;

import dagger.Component;
import io.hkmushtaq.sampledagger.SampleApplication;
import io.hkmushtaq.sampledagger.di.main.MainActivityComponent;
import io.hkmushtaq.sampledagger.di.main.MainActivityModule;
import io.hkmushtaq.sampledagger.di.scopes.ApplicationScope;

@ApplicationScope
@Component(modules = AppModule.class)
public interface AppComponent {

    MainActivityComponent plus(MainActivityModule mainActivityModule);

    void inject(SampleApplication application);
}
