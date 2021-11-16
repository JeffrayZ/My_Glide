package com.glide.my.callback;

import com.glide.my.Value;

/**
 * @ProjectName: Glide_Lib
 * @Package: com.glide.my
 * @ClassName: ValueCallback
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/9 16:52
 */
public interface ValueCallback {
    // value不再使用了
    public void valueNoMoreUse(String key, Value value);
}
