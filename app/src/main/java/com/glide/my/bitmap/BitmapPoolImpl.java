package com.glide.my.bitmap;


import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LruCache;

import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class BitmapPoolImpl extends LruCache<Integer, Bitmap> implements BitmapPool {

    // 里面有API  可以删选出合适的值 TreeMap里面的 ceilingKey
    private final TreeMap<Integer, String> treeMap = new TreeMap<>();

    public BitmapPoolImpl(int maxSize) {
        super(maxSize);
    }

    @Override
    public void put(Bitmap bitmap) {
        // 复用条件
        // TODO 条件一   bitmap.isMutable() == true
        if (!bitmap.isMutable()) {
            System.out.println("bitmap.isMutable() == true 不满足，不能存入复用池");
            return;
        }
        // TODO 条件二  bitmap大小不能大于maxSize
        int bitmapSize = getBitmapSize(bitmap);
        if (bitmapSize >= maxSize()) {
            System.out.println("bitmap内存太大，不能存入复用池");
        }

        put(bitmapSize, bitmap);
        treeMap.put(bitmapSize, null);
        System.out.println("bitmap 存入复用池");
    }

    /**
     * 获取bitmap大小
     *
     * @param bitmap
     * @return
     */
    private int getBitmapSize(Bitmap bitmap) {
        // 最早期的时候 getRowBytes() * getHeight();

        // Android 3.0 12 API  bitmap.getByteCount()
        // bitmap.getByteCount()

        // Android 4.4 19 API 以后的版本
        // bitmap.getAllocationByteCount();

        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        }
        return bitmap.getByteCount();
    }

    @Override
    public Bitmap get(int width, int height, Bitmap.Config config) {
        /**
         * ALPHA_8  理论上 实际上Android自动做处理的 只有透明度 8位  1个字节
         * w*h*1
         *
         * RGB_565  理论上 实际上Android自动做处理的  R red红色 5， G绿色 6， B蓝色 5   16位 2个字节 没有透明度
         * w*h*2
         *
         * ARGB_4444 理论上 实际上Android自动做处理 A透明度 4位  R red红色4位   16位 2个字节
         *
         * 质量最高的：
         * ARGB_8888 理论上 实际上Android自动做处理  A 8位 1个字节  ，R 8位 1个字节， G 8位 1个字节， B 8位 1个字节
         *
         * 常用的 ARGB_8888  RGB_565
         */
        int getSize = width * height * (config == Bitmap.Config.ARGB_8888 ? 4 : 2);
        // 返回 map里面最小的 大于或者等于 getSize的 key
        Integer key = treeMap.ceilingKey(getSize);
        if(key == null){ // 没找到
            return null;
        }
        // 查找容器取出来的key，必须大于getSize 并且 小于 计算出来的 (getSize * 2)
        // 也可以注释掉
        if(key >= getSize && key <= (getSize * 2)){
            Bitmap reUseBitmap = remove(key);
            System.out.println("从复用池里面取到了bitmap");
            return  reUseBitmap;
        }

        return null;
    }

    // 元素大小
    @Override
    protected int sizeOf(@NonNull Integer key, @NonNull Bitmap value) {
        return getBitmapSize(value);
    }

    // 元素被移除
    @Override
    protected void entryRemoved(boolean evicted, @NonNull Integer key, @NonNull Bitmap oldValue, @Nullable Bitmap newValue) {
        super.entryRemoved(evicted, key, oldValue, newValue);
    }
}
