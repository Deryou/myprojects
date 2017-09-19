package com.mr.mrdetect.event;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by MR on 2017/8/23.
 */

public class StartBrotherEvent {
    public SupportFragment targetFragment;

    public StartBrotherEvent(SupportFragment fragment) {
        this.targetFragment = fragment;
    }
}
