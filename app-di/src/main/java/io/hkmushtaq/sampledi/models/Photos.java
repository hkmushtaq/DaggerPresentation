package io.hkmushtaq.sampledi.models;

import java.util.List;

public class Photos {

    private final List<Photo> photo;

    public Photos(List<Photo> photo) {
        this.photo = photo;
    }

    public List<Photo> getPhoto() {
        return photo;
    }
}
