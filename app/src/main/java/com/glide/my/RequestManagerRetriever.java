package com.glide.my;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

/**
 * @ProjectName: Glide_Lib
 * @Package: com.glide.my
 * @ClassName: RequestManagerRetriever
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/12 10:43
 */
public class RequestManagerRetriever {
    public RequestManager get(FragmentActivity activity) {
        return new RequestManager(activity);
    }

    public RequestManager get(Activity activity) {
        return new RequestManager(activity);
    }

    public RequestManager get(Context context) {
        return new RequestManager(context);
    }
}
