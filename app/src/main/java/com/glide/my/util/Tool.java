package com.glide.my.util;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

/**
 * @ProjectName: Glide_Lib
 * @Package: com.glide.my.util
 * @ClassName: Tool
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/9 16:31
 */
public class Tool {
    private static final char[] HEX_CHAR_ARRAY = "0123456789abcdef".toCharArray();
    private static final char[] SHA_256_CHARS = new char[64];

    @NonNull
    public static String getSHA256String(String str){
        MessageDigest messageDigest;
        String encodeStr="";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            encodeStr = sha256BytesToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    @NonNull
    private static String sha256BytesToHex(@NonNull byte[] bytes) {
        synchronized(SHA_256_CHARS) {
            return bytesToHex(bytes, SHA_256_CHARS);
        }
    }

    @NonNull
    private static String bytesToHex(@NonNull byte[] bytes, @NonNull char[] hexChars) {
        for(int j = 0; j < bytes.length; ++j) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = HEX_CHAR_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_CHAR_ARRAY[v & 15];
        }

        return new String(hexChars);
    }
}
