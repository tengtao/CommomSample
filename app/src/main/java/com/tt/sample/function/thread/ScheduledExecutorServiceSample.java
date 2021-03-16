package com.tt.sample.function.thread;


import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 周期执行线程池
 * ScheduledThreadPoolExecutor
 * 核心线程2个，只会用两个线程来执行线程池
 * 要防止提交过多线程使程序崩溃
 * <p>
 * //
 * scheduleAtFixedDelay()：
 * 这个方法是从上一个任务执行开始就计算下次任务执行时间。
 * 如果方法执行超过延时的时间，那么任务做完就马上执行下一个任务
 * 没超过执行时间，那么会延时到延时时间结束再执行，
 * 所以这个方法可能会有两个同时执行，
 * <p>
 * scheduleWithFixedDelay()：
 * 这个方法是从上一个任务执行结束后才开始计算下次任务执行时间。
 * 没做完就等到做完，，然后还要延时一段时间才执行
 */
public class ScheduledExecutorServiceSample {
    public static void main(String[] args) {
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(2);
        //
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("================" + Thread.currentThread().getName());
                //触发异常,这个周期任务就不会再执行了
//                throw new RuntimeException();
            }
        }, 1000, 5000, TimeUnit.MILLISECONDS);

        //
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                ///每隔一段时间打印系统时间，证明两者是互不影响的
                System.out.println(System.nanoTime() + "======="
                        + Thread.currentThread().getName());
            }
        }, 1000, 2000, TimeUnit.MILLISECONDS);
    }
}
