package cn.jarlen.richcommon.jwebview.jsbridge;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HandlerThreadExecutor implements Executor {

    private static final int INITIAL_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;

    /**
     * Sets the amount of time an idle thread waits before terminating
     */
    private static final int KEEP_ALIVE_TIME = 10;

    private final BlockingQueue<Runnable> workQueue;

    private final ThreadPoolExecutor threadPoolExecutor;

    private final ThreadFactory threadFactory;

    private volatile static HandlerThreadExecutor threadExecutor;

    public HandlerThreadExecutor() {
        this.workQueue = new LinkedBlockingQueue<>();
        this.threadFactory = new JobThreadFactory();
        this.threadPoolExecutor = new ThreadPoolExecutor(
                INITIAL_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                workQueue,
                threadFactory);
    }

    public static HandlerThreadExecutor getInstance() {
        if (threadExecutor == null) {
            synchronized (HandlerThreadExecutor.class) {
                if (threadExecutor == null) {
                    threadExecutor = new HandlerThreadExecutor();
                }
            }
        }
        return threadExecutor;
    }

    @Override
    public void execute(Runnable runnable) {
        if (runnable == null) {
            throw new IllegalArgumentException("Runnable to execute cannot be null");
        }
        this.threadPoolExecutor.execute(runnable);
    }

    public void submit(Runnable runnable) {
        if (runnable == null) {
            throw new IllegalArgumentException("Runnable to submite cannot be null");
        }
        this.threadPoolExecutor.submit(runnable);
    }

    public Future submit(Callable callable) {
        if (callable == null) {
            throw new IllegalArgumentException("callable to submit cannot be null");
        }
        return this.threadPoolExecutor.submit(callable);
    }

    private static class JobThreadFactory implements ThreadFactory {
        private static final String THREAD_NAME = "EoaWebThread_";
        private int counter = 0;

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, THREAD_NAME + counter);
        }
    }
}
