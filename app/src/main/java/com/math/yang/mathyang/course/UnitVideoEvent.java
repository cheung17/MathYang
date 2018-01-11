package com.math.yang.mathyang.course;

import com.math.yang.mathyang.model.UnitVideo;

/**
 * @author ztx
 */
public interface UnitVideoEvent {
    void onVideoClickListener(UnitVideo unitVideo, int position, boolean playNow);
}
