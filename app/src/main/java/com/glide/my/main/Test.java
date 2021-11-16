package com.glide.my.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.glide.my.Key;
import com.glide.my.bitmap.BitmapPool;
import com.glide.my.bitmap.BitmapPoolImpl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @ProjectName: Glide_Lib
 * @Package: com.glide.my.main
 * @ClassName: Test
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/9 18:04
 */
public class Test {
    private static final String URL = "https://cn.bing.com/sa/simg/hpb/LaDigue_EN-CA1115245085_1920x1080.jpg";
    private static BitmapPool bitmapPool = new BitmapPoolImpl(1024 * 1024 * 60);

    public static void main(String[] args){
        Key key = new Key(URL);

        new Thread(new Runnable() {

            @Override
            public void run() {
                InputStream inputStream = null;
                try {
                    URL url = new URL(URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    int responseCode = connection.getResponseCode();

                    if (HttpURLConnection.HTTP_OK == responseCode) {
                        inputStream = connection.getInputStream();

                        // TODO 获取原始图片的宽高等参数
                        // 创建 options
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        // 设置只拿到周围信息，outXXX， outW，outH
                        // 此时options里面还是没有数据
                        options.inJustDecodeBounds = true;
                        // inputStream转换成bitmap，信息存入options，options里面就有数据了
                        BitmapFactory.decodeStream(inputStream,null,options);
                        // 拿到图片宽和高
                        int w = options.outWidth;
                        int h = options.outHeight;

                        // TODO 再创建一个options
                        BitmapFactory.Options options1 = new BitmapFactory.Options();
                        Bitmap pBitmap = bitmapPool.get(w,h, Bitmap.Config.RGB_565);
//                Bitmap pBitmap = bitmapPool.get(w,h, options.inPreferredConfig);
                        // 从复用池里取出的 Bitmap赋值给 inBitmap
                        // 设置这个才能复用，而不是去申请内存
                        options1.inBitmap = pBitmap;
                        options1.inPreferredConfig = Bitmap.Config.RGB_565;
//                options1.inPreferredConfig = options.inPreferredConfig;
                        options1.inJustDecodeBounds = false;
                        options1.inMutable = true;

                        // TODO inputStream转换
                        final Bitmap resultBitmap = BitmapFactory.decodeStream(inputStream,null,options1);
                        // 添加到复用池
                        bitmapPool.put(resultBitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
