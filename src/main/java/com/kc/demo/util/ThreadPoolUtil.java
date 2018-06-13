package com.kc.demo.util;

import com.kc.demo.bean.MyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPoolUtil {
    private ExecutorService fixedThreadPool;

    // 单例逻辑, 全局只有一份fixedThreadPool
    private static ThreadPoolUtil ins;
    private static final ReentrantLock lock = new ReentrantLock();


    private ThreadPoolUtil() {
        this.fixedThreadPool = Executors.newFixedThreadPool(20);
    }

    private static ThreadPoolUtil getInstance() {
        if (ins == null) {
            lock.lock();
            try {
                if (ins == null) {
                    ins = new ThreadPoolUtil();
                }
            } finally {
                lock.unlock();
            }
        }

        return ins;
    }

    /**
     * 提交任务至线程池, 若队列已满则等待
     *
     * @param task
     * @return
     */
    public static Future<Object> submit(Callable<Object> task) {
        return getInstance().fixedThreadPool.submit(task);
    }
}
