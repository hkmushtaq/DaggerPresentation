package io.hkmushtaq.sampledi.network;

import io.hkmushtaq.sampledi.models.Photos;

public class PhotoSearchResponse {

    private final Photos photos;
    private final String stat;

    public PhotoSearchResponse(Photos photos, String stat) {
        this.photos = photos;
        this.stat = stat;
    }

    public Photos getPhotos() {
        return photos;
    }

    public String getStat() {
        return stat;
    }
}
