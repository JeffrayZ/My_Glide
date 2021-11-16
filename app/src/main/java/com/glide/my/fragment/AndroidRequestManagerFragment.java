package com.glide.my.fragment;

import android.app.Fragment;

/**
 * package android.app;
 */
public class AndroidRequestManagerFragment extends Fragment {
    private LifecycleCallback callback;

    public AndroidRequestManagerFragment() {
    }

    public AndroidRequestManagerFragment(LifecycleCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (callback != null) {
            callback.glideInitAction();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (callback != null) {
            callback.glideStopAction();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (callback != null) {
            callback.glideDestroyAction();
        }
    }
}
