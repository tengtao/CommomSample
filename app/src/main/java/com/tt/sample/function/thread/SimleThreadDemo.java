package com.tt.sample.function.thread;


/**
 *
 */
public class SimleThreadDemo {


    /**
     * 最简单的线程执行，
     */
    void simpleThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //执行你的任务
            }
        }).start();
    }

    /**
     * 演示如何停止线程
     * sleep，或者waite？的时候停止不了，需要inteput来停止，
     */
    boolean isWork = true;
    Thread workThread;

    void simpleStopThread() {
        workThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isWork) {
                    //执行你的任务
                }
            }
        });
        workThread.start();
    }

}
