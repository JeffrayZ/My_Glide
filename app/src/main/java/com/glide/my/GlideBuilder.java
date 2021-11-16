package com.glide.my;

/**
 * @ProjectName: Glide_Lib
 * @Package: com.glide.my
 * @ClassName: GlideBuilder
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/12 10:51
 */
public class GlideBuilder {

    public Glide build() {
        Glide glide = new Glide(new RequestManagerRetriever());
        return glide;
    }
}
