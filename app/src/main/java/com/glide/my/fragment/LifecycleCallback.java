package com.glide.my.fragment;

/**
 * 生命周期的回调
 */
public interface LifecycleCallback {
    void glideInitAction();

    void glideStopAction();

    void glideDestroyAction();
}
