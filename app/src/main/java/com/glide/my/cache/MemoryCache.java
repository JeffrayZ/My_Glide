package com.glide.my.cache;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LruCache;

import com.glide.my.Value;
import com.glide.my.callback.MemoryCacheCallback;

/**
 * @ProjectName: Glide_Lib
 * @Package: com.glide.my.cache
 * @ClassName: MemoryCache
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/10 16:36
 */
public class MemoryCache extends LruCache<String, Value> {

    private boolean sdRemovedFlag = false; // 手动 remove标识
    private MemoryCacheCallback callback;

    public void setCallback(MemoryCacheCallback callback) {
        this.callback = callback;
    }

    public MemoryCache(int maxSize) {
        super(maxSize);
    }

    /**
     * 手动移除
     * 加这个方法的目的就是为了加这个标识
     * @param key
     * @return
     */
    public Value sdRemoved(String key) {
        sdRemovedFlag = true;
        // 执行这个方法，会回调到 entryRemoved里面
        Value value = remove(key);
        sdRemovedFlag = false;
        return value;
    }

    @Override
    protected int sizeOf(@NonNull String key, @NonNull Value value) {
        Bitmap bitmap = value.getBitmap();

        // 获取 bitmap的大小
//        bitmap.getRowBytes() * bitmap.getHeight();
//        bitmap.getByteCount();
//        int result = bitmap.getAllocationByteCount();

        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        }

        return bitmap.getByteCount();
    }

    // lru算法中，被移除会调用这个方法
    @Override
    protected void entryRemoved(boolean evicted, @NonNull String key, @NonNull Value oldValue, @Nullable Value newValue) {
        super.entryRemoved(evicted, key, oldValue, newValue);
        if(callback != null && !sdRemovedFlag){
            // 手动移除 我们不做回调
            callback.entryRemovedMemoryCache(key,oldValue);
        }
    }
}
