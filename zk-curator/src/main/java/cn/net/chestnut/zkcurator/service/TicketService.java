package cn.net.chestnut.zkcurator.service;

import cn.net.chestnut.zkcurator.common.ClientLock;
import cn.net.chestnut.zkcurator.common.ZkTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author tarzan
 * @Date 2019/7/11 11:20 AM
 **/
@Service
@Slf4j
public class TicketService {

    //10分钟后活动结束
    private Date expireTime;

    public TicketService() {
        getAfterTime();
    }

    public void reset() {
        Ticket.reset();
        getAfterTime();
    }

    private void getAfterTime() {
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(10);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        expireTime = Date.from(instant);
    }

    public String start() {
        ZkTools zk = ZkTools.getZkTools();
        try {
            if (new Date().compareTo(expireTime) > 0 && zk.getClient().getState() != CuratorFrameworkState.STOPPED) {
                zk.closeQuietly();
                return "活动已过期";
            } else {
                if (zk.getClient().getState() == CuratorFrameworkState.LATENT) {
                    zk.getClient().start();
                }
                ClientLock lock = new ClientLock(zk.getClient(), ZkTools.PATH, System.currentTimeMillis() + "");
                return doWork(lock, 10, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private String doWork(ClientLock clientLock, long time, TimeUnit unit) throws Exception {
        //指定时间内获取锁
        if (!clientLock.lock.acquire(time, unit)) {
            throw new IllegalStateException(clientLock.clientName + " could not acquire the lock");
        }
        try {
            log.info(clientLock.clientName + " has the lock");
            //操作资源
            return Ticket.use();
        } finally {
            log.info(clientLock.clientName + " releasing the lock");
            clientLock.lock.release(); //总是在Final块中释放锁。
        }
    }

}
