package io.hkmushtaq.sampledi.ui;

import java.util.List;

import io.hkmushtaq.sampledi.models.Photo;

interface MainView {

    void setPhotos(List<Photo> photoList);

    void displayError();

}
