package com.glide.my.http_load;

import android.content.Context;

import com.glide.my.Value;
import com.glide.my.bitmap.BitmapPoolImpl;

/**
 * 加载外部资源   网络资源
 */
public interface LoadData {
    Value loadImg(Context context, String url, ResponseCallback callback, BitmapPoolImpl bitmapPool);
}
