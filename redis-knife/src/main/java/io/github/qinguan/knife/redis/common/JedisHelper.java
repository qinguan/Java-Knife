package io.github.qinguan.knife.redis.common;

import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.io.IOException;
import java.util.function.Consumer;

/*******************************************************
 * Copyright (C) 2018 iQIYI.COM - All Rights Reserved
 *
 * <p>This file is part of java-knife.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 *
 * <p>Author(s): Xu Guojun
 * Created: 2019/2/2 13:46
 *******************************************************/
public final class JedisHelper {

    static int connectionTimeout = 2000;
    static int socketTimeout = 3000;
    static int scanStep = 500;

    /**
     * 根据redis信息，获取jedis实例
     * @param host
     * @param port
     * @param password
     * @param keyspace
     * @return
     */
    public static Jedis getJedis(String host, int port, String password, int keyspace) {
        Jedis jedis = new Jedis(host, port, connectionTimeout, socketTimeout);
        if (keyspace >-1) {
            jedis.select(keyspace);
        }
        if (StringUtils.isNotBlank(password)) {
            jedis.auth(password);
        }
        return jedis;
    }

    /**
     * 以limter的速率按patten匹配redis数据，针对每一条扫描出来的目标数据，执行consumer操作
     * @param jedis
     * @param patten
     * @param limiter
     * @param consumer
     * @return
     * @throws IOException
     */
    public static int scanByConsumer(Jedis jedis, String patten, RateLimiter limiter, Consumer<String> consumer) throws IOException {
        ScanParams scanParams = new ScanParams().count(scanStep);
        if (StringUtils.isNotBlank(patten)) {
            scanParams = scanParams.match(patten);
        }
        String cur = redis.clients.jedis.ScanParams.SCAN_POINTER_START;

        int count = 0;
        boolean cycleIsFinished = false;
        while(!cycleIsFinished){
            ScanResult<String> scanResult = jedis.scan(cur, scanParams);
            cur = scanResult.getStringCursor();
            count += scanResult.getResult().size();
            for (String r:scanResult.getResult()) {
                consumer.accept(r);
            }
            /**
             * 扫描结束时cur返回字符串0
             */
            if ("0".equals(cur)) {
                cycleIsFinished = true;
            }
            limiter.acquire();
        }
        return count;
    }
}
