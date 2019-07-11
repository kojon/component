package cn.net.chestnut.zkcurator.lock.shared.reentrant;

import cn.net.chestnut.zkcurator.common.ClientLock;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 客户端
 */
public class LockingExample {

    private static final int QTY = 5;

    private static final int REPETITIONS = QTY * 10;

    private static final String CONNECTION_STRING = "192.168.1.187:2181";

    private static final String PATH = "/examples/locks";

    //起五个线程，即五个窗口卖票，五个客户端分别有50张票可以卖，先是尝试获取锁，操作资源后，释放锁。
    public static void main(String[] args) throws Exception {

        //FakeLimitedResource模拟某些外部资源，这些外部资源一次只能由一个进程访问
        final FakeLimitedResource resource = new FakeLimitedResource();

        ExecutorService service = Executors.newFixedThreadPool(QTY);
        try {
            for (int i = 0; i < QTY; ++i) {
                final int index = i;
                Callable<Void> task = new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        CuratorFramework client = CuratorFrameworkFactory.newClient(CONNECTION_STRING, new ExponentialBackoffRetry(1000, 3, Integer.MAX_VALUE));
                        try {
                            client.start();
                            ExampleClientThatLocks example = new ExampleClientThatLocks(client, PATH, resource, "Client " + index);
                            for (int j = 0; j < REPETITIONS; ++j) {
                                example.doWork(10, TimeUnit.SECONDS);
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            CloseableUtils.closeQuietly(client);
                        }
                        return null;
                    }
                };
                service.submit(task);
            }

            service.shutdown();
            service.awaitTermination(10, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
