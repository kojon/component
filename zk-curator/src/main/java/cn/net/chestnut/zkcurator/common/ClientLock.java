package cn.net.chestnut.zkcurator.common;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

/**
 * 使用锁操作资源
 */
public class ClientLock {

    /** 锁 */
    public final InterProcessMutex lock;
    /** 客户端名称 */
    public final String clientName;

    public ClientLock(CuratorFramework client, String lockPath, String clientName) {
        this.clientName = clientName;
        lock = new InterProcessMutex(client, lockPath);
    }
}
