package com.glide.my.fragment;

import androidx.fragment.app.Fragment;

/**
 * package androidx.fragment.app
 */
public class AndroidXRequestManagerFragment extends Fragment {
    private LifecycleCallback callback;

    public AndroidXRequestManagerFragment(LifecycleCallback callback) {
        this.callback = callback;
    }

    public AndroidXRequestManagerFragment() {
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
