package com.aries.template.xiaoyu.meeting;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.ainemo.sdk.otf.OpenGLTextureView;
import com.ainemo.sdk.otf.VideoInfo;

public class MeetingVideoCell extends OpenGLTextureView {

    public MeetingVideoCell(Context context) {
        this(context, null);
    }

    public MeetingVideoCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        startRender();
    }

    public void setVideoInfo(VideoInfo videoInfo) {
        //设置渲染视频的资源ID。
        String sourceID = videoInfo.getDataSourceID();

        if (TextUtils.isEmpty(sourceID)) {
            return;
        }
        super.setSourceID(sourceID);
        startRender();
    }

    /**
     * 渲染
     */
    Runnable renderRunnable = () -> {
        String sourceID = getSourceID();
        if (TextUtils.isEmpty(sourceID)) {
            return;
        }
        // 渲染一帧视频画面，因此一般都是循环调用该方法来渲染视频，需要停止渲染时退出循环条件即可。
        requestRender();
        startRender();
    };

    /**
     * 实际情况下可以根据页面onResume/onStop等生命周期状态来确定是否需要渲染
     */
    public void startRender() {
        postDelayed(renderRunnable, 1000/16);
    }


}
