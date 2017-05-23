package io.hkmushtaq.sampledi.ui;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hkmushtaq.sampledi.R;
import io.hkmushtaq.sampledi.models.Photo;
import io.hkmushtaq.sampledi.network.FlickrApi;
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

public class MainActivity extends AppCompatActivity implements MainView {

    private static final int MAX_CACHE_SIZE = 10 * 1000 * 1000;

    private MainPresenter mMainPresenter;
    private PhotosListAdapter mPhotosListAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Set up the Recycler View with an empty array
        mPhotosListAdapter = new PhotosListAdapter(this, Collections.<Photo>emptyList());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mPhotosListAdapter);

        // Set up dependencies for Presenter
        File cacheFile = new File(getCacheDir(), "okhttp_log");
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

        String baseUrl = getString(R.string.flickr_api_base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(String.valueOf(Html.fromHtml(baseUrl)))
                .client(client)
                .addCallAdapterFactory(
                        RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();

        FlickrApi flickrApi = retrofit.create(FlickrApi.class);

        mMainPresenter = new MainPresenter(this, this, flickrApi, AndroidSchedulers.mainThread());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMainPresenter.onPause();
    }

    @Override
    public void setPhotos(List<Photo> photoList) {
        mPhotosListAdapter.setPhotosList(photoList);
    }

    @Override
    public void displayError() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("There was an error")
                .create();
        alertDialog.show();
    }
}
