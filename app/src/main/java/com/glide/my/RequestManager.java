package com.glide.my;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.glide.my.fragment.AndroidRequestManagerFragment;
import com.glide.my.fragment.AndroidXRequestManagerFragment;

/**
 * @ProjectName: Glide_Lib
 * @Package: com.glide.my
 * @ClassName: RequestManager
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/12 10:39
 */
public class RequestManager {
    private static final String FRAGMENT_TAG_1 = "Fragment_Activity_Name";
    private static final String FRAGMENT_TAG_2 = "Activity_Name";
    private static final int ID_REMOVE_FRAGMENT_MANAGER = 1;
    private Context context;
    private static RequestTarget target;
    // 构造代码块，不用所有构造方法都去写
    {
        if(target == null){
            target = new RequestTarget(context);
        }
    }

    /**
     * 可以管理声明周期
     * @param activity
     */
    public RequestManager(FragmentActivity activity) {
        this.context = activity;
        FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
        Fragment fragmentByTag = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG_1);
        if(fragmentByTag == null){
            // target fragment生命周期与target关联
            fragmentByTag = new AndroidXRequestManagerFragment(target);
            supportFragmentManager.beginTransaction().add(fragmentByTag,FRAGMENT_TAG_1).commitAllowingStateLoss();
        }
        mHandler.sendEmptyMessage(ID_REMOVE_FRAGMENT_MANAGER);
    }

    /**
     * 可以管理声明周期
     * @param activity
     */
    public RequestManager(Activity activity) {
        this.context = activity;
        android.app.FragmentManager fragmentManager = activity.getFragmentManager();
        android.app.Fragment fragmentByTag = fragmentManager.findFragmentByTag(FRAGMENT_TAG_2);
        if(fragmentByTag == null){
            // target fragment生命周期与target关联
            fragmentByTag = new AndroidRequestManagerFragment(target);
            fragmentManager.beginTransaction().add(fragmentByTag,FRAGMENT_TAG_2).commitAllowingStateLoss();
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            return false;
        }
    });

    /**
     * 这个方法无法管理声明周期
     * 因为 Application无法管理
     * @param context
     */
    public RequestManager(Context context) {
        this.context = context;
    }

    public RequestTarget load(String url){
        mHandler.removeMessages(ID_REMOVE_FRAGMENT_MANAGER);
        target.loadValue(url,context);
        return target;
    }
}
