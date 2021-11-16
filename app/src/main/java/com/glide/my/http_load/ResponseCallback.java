package com.glide.my.http_load;

import com.glide.my.Value;

/**
 * 加载外部资源  成功  失败
 */
public interface ResponseCallback {
    void success(Value value);
    void exception(Exception exception);
}
