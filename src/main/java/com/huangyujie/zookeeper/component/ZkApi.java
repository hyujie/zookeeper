package com.huangyujie.zookeeper.component;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ZkApi {
    @Autowired
    private ZooKeeper zkClient;

    /**
     * 获取指定节点的值
     * @param path
     * @return
     */
    public  String getData(String path, Watcher watcher){
        try {
            Stat stat=new Stat();
            byte[] bytes=zkClient.getData(path,watcher,stat);
            return  new String(bytes);
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }

}
