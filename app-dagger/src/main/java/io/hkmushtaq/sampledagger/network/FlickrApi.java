package io.hkmushtaq.sampledagger.network;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrApi {

    @GET("/services/rest/?method=flickr.photos.search")
    Single<PhotoSearchResponse> getPhotosWithTag(@Query("tags") String tag);

}
