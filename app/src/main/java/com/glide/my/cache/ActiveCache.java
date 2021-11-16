package com.glide.my.cache;

import com.glide.my.Value;
import com.glide.my.callback.ValueCallback;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ProjectName: Glide_Lib
 * @Package: com.glide.my.cache
 * @ClassName: ActivrCache
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/9 18:11
 */
public class ActiveCache {
    // 活动缓存---------------正在被使用的资源
    private ConcurrentHashMap<String, WeakReference<Value>> map = new ConcurrentHashMap<>();

    // 为了监听弱引用是否被回收
    private final ReferenceQueue<Value> resourceReferenceQueue = new ReferenceQueue<>();

    private boolean isCloseThread = false;
    private Thread mThread = null;
    private ValueCallback callback;
//    private boolean sdRemoveFlag = false;

    public ActiveCache(ValueCallback callback) {
        this.callback = callback;
        // 开启线程  检测 Value 的被动移除
        mThread = new Thread() {
            @Override
            public void run() {
                super.run();
                while (!isCloseThread) {
//                    if(!sdRemoveFlag){ // 手动移除了，就不执行这里的方法
                        try {
                            // 如果被回收了，就会执行到这个方法
                            // 阻塞式
                            Reference<? extends Value> removeValue = resourceReferenceQueue.remove();
                            // 强转
                            MyWeakReference reference = (MyWeakReference) removeValue;
                            // 移除
                            if (map != null && !map.isEmpty()) {
                                map.remove(reference.key);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                    }
                }
            }
        };
        mThread.start();
    }


    /**
     * 用户关闭线程
     */
    public void closeThread() {
        isCloseThread = true;

        // 下面这个方法有可能中断不成功，先不使用
//        if(mThread != null){
//            try {
//                // 中断线程
//                mThread.interrupt();
//                // 为了让线程安稳停止
//                mThread.join(TimeUnit.SECONDS.toMillis(5));
//                if(mThread.isAlive()) {
//                    throw new IllegalStateException("活动缓存线程没有停下来");
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        // 下面这种方法更好
        map.clear();
        System.gc();
    }

    /**
     * 添加活动缓存
     *
     * @param key
     * @param value
     */
    public void put(String key, Value value) {
        //
        value.setCallback(callback);
        map.put(key, new MyWeakReference<>(value, resourceReferenceQueue, key));
    }

    /**
     * 给外界获取Value使用
     *
     * @param key
     * @return
     */
    public Value get(String key) {
        WeakReference<Value> valueWeakReference = map.get(key);
        if (valueWeakReference != null) {
            return valueWeakReference.get();
        }
        return null;
    }

    /**
     * 手动移除 Value，也就是不等 Gc
     *
     * @param key
     * @return
     */
    public Value sdRemove(String key) {
//        sdRemoveFlag = true;
        WeakReference<Value> valueWeakReference = map.remove(key);
//        sdRemoveFlag = false;
        if (valueWeakReference != null) {
            return valueWeakReference.get();
        }
        return null;
    }

    // 监听弱引用 需要成为弱引用的子类
    public static class MyWeakReference<V> extends WeakReference<V> {
        private String key;

        public MyWeakReference(V referent, ReferenceQueue<? super V> q, String key) {
            super(referent, q);
            this.key = key;
        }
    }

}
