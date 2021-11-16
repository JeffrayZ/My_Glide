package com.glide.my.cache.disk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;

import com.glide.my.Value;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @ProjectName: Glide_Lib
 * @Package: com.glide.my.cache.disk
 * @ClassName: DiskLruCacheImpl
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/11 15:13
 */
public class DiskLruCacheImpl {
    private final String DISKLRU_CACHE_DIR = "disk_lru_cache_dir"; // 磁盘缓存的目录
    private final long maxSize = 1024 * 1024 * 10;

    private DiskLruCache diskLruCache;

    public DiskLruCacheImpl(Context context){
        File file = getDiskCacheDir(context,DISKLRU_CACHE_DIR);
        try {
            diskLruCache = DiskLruCache.open(file,getAppVersion(context),1,maxSize);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void put(String key, Value value){
        DiskLruCache.Editor editor = null;
        OutputStream os = null;
        try {
            editor = diskLruCache.edit(key);
            // index 不能大于第三个参数 1
            // DiskLruCache.open(file,getAppVersion(context),1,maxSize);
            os = editor.newOutputStream(0);
            Bitmap bitmap = value.getBitmap();
            // bitmap压入 os
            bitmap.compress(Bitmap.CompressFormat.PNG,100,os);
            os.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
            try {
                editor.abort();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("put catch 磁盘缓存失败：" + e.getMessage());
            }
        } finally {
            try {
                editor.commit();
                diskLruCache.flush();
                if(os != null){
                    os.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
                System.out.println("put finally 磁盘缓存失败：" + exception.getMessage());
            }
        }
    }
    
    public Value get(String key){
        Value value = Value.getInstance();
        InputStream inputStream = null;
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            if(snapshot != null){
                inputStream = snapshot.getInputStream(0);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                value.setBitmap(bitmap);
                // 保存 key
                value.setKey(key);
                return value;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                    System.out.println("get finally 磁盘缓存失败：" + exception.getMessage());
                }
            }
        }
        return null;
    }

    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    public long getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                return info.getLongVersionCode();
            } else {
                return info.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
