package io.hkmushtaq.sampledagger.di;

import android.app.Application;
import android.content.Context;
import android.text.Html;

import java.io.File;
import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import io.hkmushtaq.sampledagger.R;
import io.hkmushtaq.sampledagger.di.scopes.ApplicationScope;
import io.hkmushtaq.sampledagger.network.FlickrApi;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public class AppModule {

    private static final int MAX_CACHE_SIZE = 10 * 1000 * 1000;

    private final Application mApplication;

    public AppModule(Application application) {
        this.mApplication = application;
    }

    @Provides
    @ApplicationScope
    Context providesContext() {
        return mApplication;
    }

    @Provides
    @ApplicationScope
    FlickrApi providesFlickrApi(Retrofit retrofit) {
        return retrofit.create(FlickrApi.class);
    }

    @Provides
    @ApplicationScope
    Retrofit providesRetrofit() {
        File cacheFile = new File(mApplication.getCacheDir(), "okhttp_log");
        cacheFile.mkdir();

        Cache cache = new Cache(cacheFile, MAX_CACHE_SIZE);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        HttpUrl newRequestUrl = chain.request().url().newBuilder()
                                .addQueryParameter("api_key", "cc98251169b8b9895710b692b07790d7")
                                .addQueryParameter("format", "json")
                                .addQueryParameter("nojsoncallback", String.valueOf(1))
                                .build();
                        return chain.proceed(
                                chain.request().newBuilder().url(newRequestUrl).build());
                    }
                })
                .cache(cache)
                .build();

        String baseUrl = mApplication.getString(R.string.flickr_api_base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(String.valueOf(Html.fromHtml(baseUrl)))
                .client(client)
                .addCallAdapterFactory(
                        RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();

        return retrofit;
    }

    @Provides
    @ApplicationScope
    Scheduler providesMainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

}
