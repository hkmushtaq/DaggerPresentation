package io.hkmushtaq.sampledagger.ui;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import io.hkmushtaq.sampledagger.models.Photo;
import io.hkmushtaq.sampledagger.network.FlickrApi;
import io.hkmushtaq.sampledagger.network.PhotoSearchResponse;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MainPresenter {

    private final Context mContext;
    private final MainView mMainView;

    private FlickrApi mFlickrApi;
    private Scheduler mMainThreadScheduler;
    private CompositeDisposable mCompositeDisposable;

    @Inject
    public MainPresenter(Context context, MainView mainView, FlickrApi flickrApi,
            Scheduler mainThreadScheduler) {
        this.mContext = context;
        this.mMainView = mainView;
        this.mMainThreadScheduler = mainThreadScheduler;
        this.mFlickrApi = flickrApi;

        this.mCompositeDisposable = new CompositeDisposable();
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
