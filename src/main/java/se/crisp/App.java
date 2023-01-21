package se.crisp;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 * A demo of virtual threads in Java 19
 * The objective is to have a somewhat fair comparison of the performance
 * between a fixed thread pool and virtual threads
 * when doing blocking work (not CPU intensive work)
 */
public class App {

    private static final Logger LOGGER = Logger.getLogger("app");
    private static final int DEFAULT_NUMBER_OF_EXECUTIONS = 50000;

    public static void main(String[] args) throws InterruptedException {
        boolean virtualThreads = args.length > 0 && args[0].equalsIgnoreCase("on");
        int numberOfExecutions = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_NUMBER_OF_EXECUTIONS;
        LOGGER.log(INFO, () -> "Using Virtual Threads: %s".formatted(virtualThreads));
        LOGGER.log(INFO, () -> "Number of executions: %s".formatted(numberOfExecutions));

        try (ExecutorService fixedThreadPoolExecutor = Executors.newFixedThreadPool(100);
             ExecutorService virtualThreadPerTaskExecutor = Executors.newVirtualThreadPerTaskExecutor()) {

            long start = System.currentTimeMillis();

            CountDownLatch countDownLatch = new CountDownLatch(numberOfExecutions);
            Runnable runnable = createRunnable(countDownLatch);
            for (int i = 0; i < numberOfExecutions; i++) {
                if (virtualThreads) {
                    //Thread.startVirtualThread(runnable);
                    virtualThreadPerTaskExecutor.execute(runnable);
                } else {
                    //new Thread(runnable).start();
                    fixedThreadPoolExecutor.execute(runnable);
                }
            }

            boolean status = countDownLatch.await(30, TimeUnit.SECONDS);
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            LOGGER.log(INFO, () -> "Run time: %s, finished successfully: %s".formatted(timeElapsed, status));
        }

    }

    private static Runnable createRunnable(CountDownLatch countDownLatch) {
        return () -> {
            try {
                // Pretend to do some sort of blocking work (or waiting for a response from something)
                Thread.sleep(5);
            } catch (InterruptedException e) {
                LOGGER.log(INFO, "Interrupted");
            } finally {
                countDownLatch.countDown();
            }
        };
    }

}
