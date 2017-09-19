package com.mr.mrdetect.di.component;

import android.app.Activity;

import com.mr.mrdetect.ui.fragment.FirstFragment;
import com.mr.mrdetect.ui.fragment.FourthFragment;
import com.mr.mrdetect.ui.fragment.ThirdFragment;
import com.mr.mrdetect.ui.fragment.quipment.ControlerFragment;
import com.mr.mrdetect.ui.fragment.quipment.ProductFragment;
import com.mr.mrdetect.ui.fragment.quipment.SensorFragment;

/**
 * Created by MR on 2017/9/12.
 */

//@FragmentScope
//@Component(dependencies = AppComponent.class,modules = FragmentModule.class)
public interface FragmentComponent {
    Activity getActivity();

    void inject(FirstFragment firstFragment);

    void inject(ThirdFragment thirdFragment);

    void inject(FourthFragment fourthFragment);

    void inject(ProductFragment productFragment);

    void inject(ControlerFragment controlerFragment);

    void inject(SensorFragment sensorFragment);
}
