package io.hkmushtaq.sampleapp.ui;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hkmushtaq.sampleapp.R;
import io.hkmushtaq.sampleapp.models.Photo;

public class MainActivity extends AppCompatActivity implements MainView {

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

        mMainPresenter = new MainPresenter(this, this);
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
