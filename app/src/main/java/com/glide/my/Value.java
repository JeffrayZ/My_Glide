package com.glide.my;

import android.graphics.Bitmap;

import com.glide.my.callback.ValueCallback;

/**
 * @ProjectName: Glide_Lib
 * @Package: com.glide.my
 * @ClassName: Value
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/9 16:42
 */
public class Value {

    private static Value value;
    public static Value getInstance(){
        if(value == null){
            synchronized (Value.class){
                if(value == null){
                    value = new Value();
                }
            }
        }
        return value;
    }

    private ValueCallback callback;

    private String key;
    private Bitmap mBitmap;

    // 使用计数 使用一次就+1
    private int count;

    public void useAction() {
        if (mBitmap == null || mBitmap.isRecycled()) {
            System.out.println("bitmap 被回收，计数器不用+1");
            return;
        }
        count++;
        System.out.println("value 正在被使用，引用计数器count是：" + count);
    }

    /**
     * value 已经被使用了
     */
    public void noMoreUseAction() {
        if (count-- <= 0 && callback != null) {
            // 回调告诉外界不再使用
            callback.valueNoMoreUse(key, this);
        }
        System.out.println("value 使用完毕，引用计数器count是：" + count);
    }

    public void recycleBitmap() {
        if (count > 0) {
            System.out.println("引用计数器大于0，Bitmap还在使用......");
            return;
        }
        if (mBitmap.isRecycled()) {
            System.out.println("Bitmap被回收了，不需要回收了");
            return;
        }
        mBitmap.recycle();
        value = null;
        System.gc();
    }

    public ValueCallback getCallback() {
        return callback;
    }

    public void setCallback(ValueCallback callback) {
        this.callback = callback;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
