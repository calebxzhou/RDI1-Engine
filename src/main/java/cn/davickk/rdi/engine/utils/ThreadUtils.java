package cn.davickk.rdi.engine.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtils {
    public static void startThread(Runnable task){
        ExecutorService exe = Executors.newCachedThreadPool();
        exe.execute(task);
    }
}
