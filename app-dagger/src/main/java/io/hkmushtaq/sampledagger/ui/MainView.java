package io.hkmushtaq.sampledagger.ui;

import java.util.List;

import io.hkmushtaq.sampledagger.models.Photo;

public interface MainView {

    void setPhotos(List<Photo> photoList);

    void displayError();

}
