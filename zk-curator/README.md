# 快速入门

### 简介

基于zookeeper，使用curator框架中的分布式可重入锁实现高并发秒杀场景。

### 参考

apache-zookeeper

https://zookeeper.apache.org/

apache-curator

http://curator.apache.org/getting-started.html

### 分布式锁原理

![](https://hlvan-st.oss-cn-beijing.aliyuncs.com/property/upload/20190711175937.png)

原理说明

- 每个请求发送到服务器后，服务器与zookeeper建立连接
- 在/ticket/lock节点下添加node节点，并添加watcher事件
- 对所有子节点名称排序，找到自己上一个节点
- 如果上一个节点存在，则上一个节点获取到了锁，自己进入等待，在指定时间内未获取锁则请求失败。
- 如果自己就是第一个节点或者上节点已经释放锁，则开始更新数据
- 查看数据资源是否还有数据可消费
- 如果数据资源已经使用完，则请求失败
- 更新数据后，删除当前节点

![](https://hlvan-st.oss-cn-beijing.aliyuncs.com/property/upload/zk-分布式锁原理.jpeg)

### 锁的类型与使用

##### Shared Reentrant Lock(分布式可重入锁)

使用类：InterProcessMutex

方法：

- public boolean acquire(long time, TimeUnit unit) throws Exception;	

  指定时间内获取锁

- release(）释放锁

  使用release(）方法释放锁
  线程通过acquire()获取锁时，可通过release()进行释放，如果该线程多次调用 了acquire()获取锁，则如果只调用 一次release()该锁仍然会被该线程持有。

- makeRevocable()  撤销锁

注意：同一个线程中InterProcessMutex实例是可重用的，也就是不需要在每次获取锁的时候都new一个InterProcessMutex实例，用同一个实例就好。