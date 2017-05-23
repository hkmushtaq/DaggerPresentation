package io.hkmushtaq.sampleapp.ui;

import android.content.Context;
import android.text.Html;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.hkmushtaq.sampleapp.R;
import io.hkmushtaq.sampleapp.models.Photo;
import io.hkmushtaq.sampleapp.network.FlickrApi;
import io.hkmushtaq.sampleapp.network.PhotoSearchResponse;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
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

public class MainPresenter {

    private static final int MAX_CACHE_SIZE = 10 * 1000 * 1000;

    private final Context mContext;
    private final MainView mMainView;

    private FlickrApi mFlickrApi;
    private Scheduler mMainThreadScheduler;
    private CompositeDisposable mCompositeDisposable;

    public MainPresenter(Context context, MainView mainView) {
        this.mContext = context;
        this.mMainView = mainView;

        this.mMainThreadScheduler = AndroidSchedulers.mainThread();
        this.mCompositeDisposable = new CompositeDisposable();

        File cacheFile = new File(mContext.getCacheDir(), "okhttp_log");
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

        String baseUrl = mContext.getString(R.string.flickr_api_base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(String.valueOf(Html.fromHtml(baseUrl)))
                .client(client)
                .addCallAdapterFactory(
                        RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();

        this.mFlickrApi = retrofit.create(FlickrApi.class);
    }

    public void onResume() {
        fetchPhotos();
    }

    private void fetchPhotos() {
        mCompositeDisposable.add(
                mFlickrApi.getPhotosWithTag("landscape")
                        .observeOn(mMainThreadScheduler)
                        .subscribe(onPhotosLoaded(), onPhotosError())
        );
    }

    public void onPause() {
        mCompositeDisposable.dispose();
    }

    private Consumer<PhotoSearchResponse> onPhotosLoaded() {
        return new Consumer<PhotoSearchResponse>() {
            @Override
            public void accept(@NonNull PhotoSearchResponse photoSearchResponse) throws Exception {
                List<Photo> photos = photoSearchResponse.getPhotos().getPhoto();
                mMainView.setPhotos(photos);
            }
        };
    }

    private Consumer<? super Throwable> onPhotosError() {
        return new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                mMainView.displayError();
            }
        };
    }

}
