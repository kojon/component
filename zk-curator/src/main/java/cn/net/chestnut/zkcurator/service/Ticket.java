package cn.net.chestnut.zkcurator.service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description
 * @Author tarzan
 * @Date 2019/7/11 1:47 PM
 **/
public class Ticket {
    //总共20张火车票
    private static AtomicInteger ticket = new AtomicInteger(20);

    public static String use() {
        int ticketNo = (20 - (ticket.decrementAndGet()));
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "火车票编号:" + ticketNo;

    }

    public static void reset(){
        ticket = new AtomicInteger(20);
    }

    public static Integer getCount(){
        return ticket.get();
    }
}
