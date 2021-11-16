package com.test.glide_lib;

/**
 * @ProjectName: Glide_Lib
 * @Package: com.test.glide_lib
 * @ClassName: TestMain
 * @Description: java类作用描述
 * @Author: Jeffray
 * @CreateDate: 2021/11/12 16:28
 */
public class TestMain {
    public static void main(String[] args){
        SyncTest4 test41 = new SyncTest4();
        SyncTest4 test42 = new SyncTest4();



//        SyncTest3 test31 = new SyncTest3();
//        SyncTest3 test32 = new SyncTest3();




        SyncTest2 test2 = new SyncTest2();

//        SyncTest test = new SyncTest();

        new Thread(new Runnable() {
            @Override
            public void run() {
//                test.obj1();

//                test2.obj1();

//                test31.obj1();



                test41.obj1();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
//                test.obj3();

//                SyncTest2.obj3();


//                test32.obj3();


                test42.obj3();
            }
        }).start();
    }
}
