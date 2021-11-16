package com.test.glide_lib;

/**
 * @ProjectName: Glide_Lib
 * @Package: com.test.glide_lib
 * @ClassName: SyncTest
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/12 16:24
 */
public class SyncTest {
    public synchronized void obj3() {
        int i = 5;
        while (i-- > 0) {
            System.out.println(Thread.currentThread().getName() + " : " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
            }
        }
    }

    public void obj2() {
        synchronized (this) {
            int i = 5;
            while (i-- > 0) {
                System.out.println(Thread.currentThread().getName() + " : " + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                }
            }
        }
    }
}
