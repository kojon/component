package cn.net.chestnut.zkcurator.common;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

/**
 * @Description
 * @Author tarzan
 * @Date 2019/7/11 10:41 AM
 **/
public class ZkTools {
    private static ZkTools zkTools=new ZkTools();

    private static final String CONNECTION_STRING = "192.168.1.185:2181,192.168.1.186:2181,192.168.1.187:2181";
    public static final String PATH = "/ticket/lock";

    private CuratorFramework client = null;

    private ZkTools(){
        client=CuratorFrameworkFactory.newClient(CONNECTION_STRING, new ExponentialBackoffRetry(1000, 3, Integer.MAX_VALUE));
    }

    public static ZkTools getZkTools(){
        return zkTools;
    }


    public CuratorFramework getClient() {
        return client;
    }

    public void closeQuietly(){
        CloseableUtils.closeQuietly(client);
    }
}