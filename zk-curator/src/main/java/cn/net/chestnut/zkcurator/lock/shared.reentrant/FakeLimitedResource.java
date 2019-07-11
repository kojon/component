package cn.net.chestnut.zkcurator.lock.shared.reentrant;

/**
 * 共享资源
 */
public class FakeLimitedResource {

    //总共250张火车票
    private Integer ticket = 250;

    public void use() throws InterruptedException {
        try {
            System.out.println("火车票还剩"+(--ticket)+"张！");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
