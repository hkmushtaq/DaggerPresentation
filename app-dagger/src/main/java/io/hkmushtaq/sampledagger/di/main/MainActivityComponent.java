package io.hkmushtaq.sampledagger.di.main;

import dagger.Subcomponent;
import io.hkmushtaq.sampledagger.di.scopes.MainActivityScope;
import io.hkmushtaq.sampledagger.ui.MainActivity;

@MainActivityScope
@Subcomponent(modules = MainActivityModule.class)
public interface MainActivityComponent {

    void inject(MainActivity mainActivity);

}
