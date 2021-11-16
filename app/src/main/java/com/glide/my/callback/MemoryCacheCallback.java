package com.glide.my.callback;

import com.glide.my.Value;

/**
 * @ProjectName: Glide_Lib
 * @Package: com.glide.my.callback
 * @ClassName: MemoryCacheCallback
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/10 17:08
 */
public interface MemoryCacheCallback {
    // 内存缓存中移除
    public void entryRemovedMemoryCache(String key, Value value);
}
