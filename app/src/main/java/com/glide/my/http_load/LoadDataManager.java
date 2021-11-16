package com.glide.my.http_load;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import com.glide.my.Value;
import com.glide.my.bitmap.BitmapPoolImpl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ProjectName: Glide_Lib
 * @Package: com.glide.my.http_load
 * @ClassName: LoadDataManager
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/15 15:04
 */
public class LoadDataManager implements LoadData,Runnable{
    private String url;
    private Context context;
    private ResponseCallback callback;
    private BitmapPoolImpl bitmapPool;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public Value loadImg(Context context, String url, ResponseCallback callback,BitmapPoolImpl bitmapPool) {
        this.url = url;
        this.context = context;
        this.callback = callback;
        this.bitmapPool = bitmapPool;

        // 加载
        executorService.execute(this);

        // 如果是本地加载，直接返回值
        return null;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url1 = new URL(url);
            httpURLConnection = (HttpURLConnection) url1.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.connect();
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream();
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
                // 从复用池里取出的 Bitmap赋值给 inBitmap
                // 设置这个才能复用，而不是去申请内存
                options1.inBitmap = bitmapPool.get(w,h, Bitmap.Config.RGB_565);
                // 这里注释掉就不复用
//                options1.inPreferredConfig = Bitmap.Config.RGB_565;
//                options1.inPreferredConfig = options.inPreferredConfig;
                options1.inJustDecodeBounds = false;
                options1.inMutable = true;

                // TODO inputStream转换
                final Bitmap resultBitmap = BitmapFactory.decodeStream(inputStream,null,options1);
                // 添加到复用池
                bitmapPool.put(resultBitmap);

                // TODO 创建value
                Value value = new Value();
                // 设置bitmap
                value.setBitmap(resultBitmap);
                // 回调出去  必须在主线程
                mHandler.post(() -> callback.success(value));
            } else {
                // 请求失败
                callback.exception(new IllegalStateException("图片请求失败：" + httpURLConnection.getResponseCode()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            callback.exception(e);
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                    callback.exception(exception);
                }
            }
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
        }
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper());
}
