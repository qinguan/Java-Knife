package io.github.qinguan.knife.zookeeper;

import com.google.common.util.concurrent.Uninterruptibles;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/*******************************************************
 * Copyright (C) 2018 iQIYI.COM - All Rights Reserved
 *
 * <p>This file is part of java-knife.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 *
 * <p>Author(s): Xu Guojun
 * Created: 2019/4/9 23:28
 *******************************************************/
public class CuratorZookeeperClient {

    private static final Logger LOG = LoggerFactory.getLogger(CuratorZookeeperClient.class);
    private final CuratorFramework client;

    public CuratorZookeeperClient() {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                .retryPolicy(new ExponentialBackoffRetry(500, Integer.MAX_VALUE, 3000))
                .sessionTimeoutMs(3000)
                .connectionTimeoutMs(2000);

        client = builder.build();
        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                LOG.info("state1:" + connectionState);
                if (connectionState == ConnectionState.LOST) {
                    LOG.info("state2:" + connectionState);
                }
                if (connectionState == ConnectionState.RECONNECTED) {
                    try {
                        LOG.info("state3:" + connectionState);
                        register("second");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        client.start();
    }

    public CuratorFramework getClient() {
        return client;
    }

    public String createPersistentNode(String path, byte[] payload) throws Exception {
        return getClient().create()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.PERSISTENT).forPath(path, payload);
    }

    public String createEphemeralNode(String path, byte[] payload) throws Exception {
        return getClient().create()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.EPHEMERAL).forPath(path, payload);
    }

    public String register(String content) throws Exception {
        if (null != getClient().checkExists().forPath(testPath)) {
            getClient().delete().forPath(testPath);
        }
        String res = createEphemeralNode(testPath, content.getBytes());
        return res;
    }

    public static String testPath = "/test1";

    public static void main(String[] args) throws Exception {
        CuratorZookeeperClient c = new CuratorZookeeperClient();
        c.register("first");
        Uninterruptibles.sleepUninterruptibly(50, TimeUnit.SECONDS);

    }
}
