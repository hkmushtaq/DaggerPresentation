package io.hkmushtaq.sampleapp.ui;

import java.util.List;

import io.hkmushtaq.sampleapp.models.Photo;

interface MainView {

    void setPhotos(List<Photo> photoList);

    void displayError();

}
