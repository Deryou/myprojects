package com.zda.daggertest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.zda.daggertest.anotation.Li;
import com.zda.daggertest.anotation.Wang;
import com.zda.daggertest.diffinstance.DaggerPComponent;
import com.zda.daggertest.diffinstance.Person;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.internal.DaggerCollections;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Li
    @Inject
    Person li;

    @Wang
    @Inject
    Person wang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DaggerPComponent.create().inject(this);
        Log.e(TAG, "onCreate: li"+li);
        Log.e(TAG, "onCreate: wang"+wang);
    }
}
