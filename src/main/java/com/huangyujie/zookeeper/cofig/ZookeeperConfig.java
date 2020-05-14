package com.huangyujie.zookeeper.cofig;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Configuration
public class ZookeeperConfig {
    @Value("${zookeeper.address}")
    private  String connectString;

    @Value("${zookeeper.timeout}")
    private  int timeout;

    @Bean
    public ZooKeeper zooKeeper(){
        ZooKeeper zooKeeper = null;

        final CountDownLatch countDown = new CountDownLatch(1);
        timeout = 3000;
        try {
            ZooKeeper finalZooKeeper = zooKeeper;
            zooKeeper = new ZooKeeper(connectString, timeout, event -> {
               if(event.getState() == Watcher.Event.KeeperState.SyncConnected){
                   countDown.countDown();
                   if(event.getType() == Watcher.Event.EventType.NodeDataChanged){
                       try {
                           System.out.println("参数变了1："+new String(finalZooKeeper.getData(event.getPath(),true,new Stat())));
                       } catch (KeeperException | InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
               }

            });
            countDown.await();
            System.out.println("zookeeper建立连接成功！！");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return zooKeeper;
    }
    @Autowired
    private ZooKeeper zooKeeper;
//    @PostConstruct
    public void test() throws KeeperException, InterruptedException {
        byte[] str = zooKeeper.getData("/hello", event -> {
            if(event.getState() == Watcher.Event.KeeperState.SyncConnected){
                if(event.getType() == Watcher.Event.EventType.NodeDataChanged){
                    try {
                        System.out.println("参数变了2："+new String(zooKeeper.getData(event.getPath(),true,new Stat())));
                    } catch (KeeperException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },new Stat());
        System.out.println("str:"+new String(str));
    }


}
