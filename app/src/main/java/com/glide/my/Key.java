package com.glide.my;

import com.glide.my.util.Tool;

/**
 * @ProjectName: Glide_Lib
 * @Package: com.glide.my
 * @ClassName: Key
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/9 16:21
 */
public class Key {
    // https://cn.bing.com/sa/simg/hpb/LaDigue_EN-CA1115245085_1920x1080.jpg
    private String key;

    public Key(String key) {
        this.key = Tool.getSHA256String(key);
    }

    /**
     * 返回密文形式的key
     * @return
     */
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
