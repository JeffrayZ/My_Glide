package com.glide.my.bitmap;

import android.graphics.Bitmap;

// 复用池
public interface BitmapPool {

    void put(Bitmap bitmap);

    /**
     * 获取可复用的 Bitmap
     * @param width
     * @param height
     * @param config
     * @return
     */
    Bitmap get(int width, int height, Bitmap.Config config);
}
