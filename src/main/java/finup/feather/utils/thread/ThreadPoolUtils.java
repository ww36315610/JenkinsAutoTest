package finup.feather.utils.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtils {
    private static ThreadPoolExecutor threadPoolExecutor = null;
    static {
        int lendUtilsThreadPoolMinSize=1;
        int lendUtilsThreadPoolMaxSize=5;
        threadPoolExecutor = new ThreadPoolExecutor(lendUtilsThreadPoolMinSize, lendUtilsThreadPoolMaxSize,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(lendUtilsThreadPoolMinSize+lendUtilsThreadPoolMaxSize),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolExecutor.allowCoreThreadTimeOut(true);
    }
    public static ThreadPoolExecutor getThreadPoolExecutor(){
        return threadPoolExecutor;
    }
}