package com.glide.my;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;


/**
 * @ProjectName: Glide_Lib
 * @Package: com.glide.my
 * @ClassName: Glide
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/12 10:38
 */
public class Glide {
    private final RequestManagerRetriever requestManagerRetriever;

    public Glide(RequestManagerRetriever requestManagerRetriever){
        this.requestManagerRetriever = requestManagerRetriever;
    }

    public static RequestManager with(FragmentActivity activity){
        return getRetriever(activity).get(activity);
    }

    public static RequestManager with(Activity activity){
        return getRetriever(activity).get(activity);
    }

    public static RequestManager with(Context context){
        return getRetriever(context).get(context);
    }

    /**
     * RequestManager 由 RequestManagerRetriever创建
     * @param context
     * @return
     */
    private static RequestManagerRetriever getRetriever(Context context){
        return Glide.get(context).getRequestManagerRetriever();
    }

    private RequestManagerRetriever getRequestManagerRetriever() {
        return requestManagerRetriever;
    }

    /**
     * Glide 是 new出来的，建造者模式
     * @param context
     * @return
     */
    public static Glide get(Context context){
        return new GlideBuilder().build();
    }
}
