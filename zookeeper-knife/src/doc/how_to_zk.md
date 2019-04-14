1. 验证ConnectionStateListener及模拟session timeout功能
    - 启动CuratorZookeeperClient的main方法，检查zkui上，可以看到注册了/test1节点，写入first
    - 执行zkServer stop，kill掉zookeeper，连接LOST
    - 执行zkServer start，启动zookeeper，客户端会重连，写入second
