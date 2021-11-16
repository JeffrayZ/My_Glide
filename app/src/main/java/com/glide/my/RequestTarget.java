package com.glide.my;

import android.content.Context;
import android.os.Looper;
import android.widget.ImageView;

import com.glide.my.bitmap.BitmapPoolImpl;
import com.glide.my.cache.ActiveCache;
import com.glide.my.cache.MemoryCache;
import com.glide.my.cache.disk.DiskLruCacheImpl;
import com.glide.my.callback.MemoryCacheCallback;
import com.glide.my.callback.ValueCallback;
import com.glide.my.fragment.LifecycleCallback;
import com.glide.my.http_load.LoadDataManager;
import com.glide.my.http_load.ResponseCallback;

// 图片加载的核心类， 实现了生命周期接口，从而与Activity或者FragmentActivity的生命周期绑定在一起。
public class RequestTarget implements LifecycleCallback, ValueCallback, MemoryCacheCallback, ResponseCallback {

    private ActiveCache activeCache; // 活动缓存
    private MemoryCache memoryCache; // 内存缓存
    private DiskLruCacheImpl diskLruCache; // 磁盘缓存

    // 复用池~~~
    private BitmapPoolImpl lruBitmapPool;

    private final int MEMORY__MAX_SIZE = 1024 * 1024 * 60;
    private Context context;
    private String url;

    // 图片连接对应的key
    private String key;
    private ImageView imageView;

    public RequestTarget(Context context) {
        if (activeCache == null) {
            activeCache = new ActiveCache(this);
        }
        if (memoryCache == null) {
            memoryCache = new MemoryCache(MEMORY__MAX_SIZE);
            memoryCache.setCallback(this);
        }
        if (diskLruCache == null) {
            diskLruCache = new DiskLruCacheImpl(context);
        }
        if (lruBitmapPool == null) {
            lruBitmapPool = new BitmapPoolImpl(1024 * 1024 * 10);
        }
    }

    @Override
    public void glideInitAction() {
        System.out.println("生命周期初始化");
    }

    @Override
    public void glideStopAction() {
        System.out.println("生命周期停止");
    }

    @Override
    public void glideDestroyAction() {
        System.out.println("生命周期销毁");
        // 释放资源
        if (activeCache != null) {
            activeCache.closeThread();
        }
    }

    public void loadValue(String url, Context context) {
        this.url = url;
        this.context = context;
        // 编码后的key，String类型
        key = new Key(url).getKey();
    }

    public void into(ImageView imageView) {
        this.imageView = imageView;
        if (Looper.myLooper() != Looper.getMainLooper()) {
            System.out.println("当前不是在主线程中操作，请检查~");
            return;
        }
        Value value = cacheAction();
        if (value != null) {
            // 手机上的图片   会在这里显示
            imageView.setImageBitmap(value.getBitmap());
            // 标识value不再使用
            value.noMoreUseAction();
        }
    }

    /**
     * TODO 真正去加载~~~
     */
    private Value cacheAction() {
        // TODO 第一步  如果活动缓存有，就加载  不然就找内存缓存
        Value value = activeCache.get(key);
        if (value != null) {
            System.out.println("本次加载是活动缓存");
            // 返回代表使用了一次 要加 1
            value.useAction();
            return value;
        }

        // TODO 第二步  从内存缓存找，如果找到，就加载，并把内存缓存转移到活动缓存  不然就找磁盘缓存
        value = memoryCache.get(key);
        if (value != null) {
            System.out.println("本次加载是内存缓存");
            // 移除内存缓存
            memoryCache.sdRemoved(key);
            // 把内存缓存中的元素加入到活动缓存   -----    移动操作
            activeCache.put(key, value);
            value.useAction();
            return value;
        }

        // TODO 第三步  从磁盘缓存中找，找到就使用，并把磁盘缓存转移到内存缓存
        value = diskLruCache.get(key);
        if (value != null) {
            System.out.println("本次加载是磁盘缓存");
            // 把磁盘缓存加入到内存缓存中
            activeCache.put(key, value);
            value.useAction();
            return value;
        }

        // TODO 第四步   真正去加载网络资源
        System.out.println("本次加载是网络资源~");
        value = new LoadDataManager().loadImg(context, url, this,lruBitmapPool);
        return value;
    }

    // 活动缓存不再使用，使用完毕
    @Override
    public void valueNoMoreUse(String key, Value value) {
        // TODO 活动缓存转移到内存缓存
        if (value != null && key != null) {
            memoryCache.put(key, value);
        }
    }

    @Override
    public void entryRemovedMemoryCache(String key, Value value) {
        // 添加到复用池
        lruBitmapPool.put(value.getBitmap());
    }

    @Override
    public void success(Value value) {
        // 网络加载  会回调到这里
        if (value != null) {
            value.noMoreUseAction();
            imageView.setImageBitmap(value.getBitmap());
        }

        // 设置key   ---- 唯一标识
        value.setKey(key);
        // 保存到活动缓存
        activeCache.put(key, value);
    }

    @Override
    public void exception(Exception exception) {
        System.out.println("回调错误出来了：" + exception.getMessage());
    }
}
