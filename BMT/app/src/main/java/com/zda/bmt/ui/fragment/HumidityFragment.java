package com.zda.bmt.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zda.bmt.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HumidityFragment extends Fragment {


    public HumidityFragment() {
        // Required empty public constructor
    }

    public static HumidityFragment newInstance() {
        HumidityFragment fragment = new HumidityFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_humidity, container, false);
    }

}
