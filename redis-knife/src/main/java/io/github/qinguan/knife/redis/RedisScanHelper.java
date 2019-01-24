package io.github.qinguan.knife.redis;

import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/*******************************************************
 * Copyright (C) 2018 iQIYI.COM - All Rights Reserved
 *
 * <p>This file is part of java-knife.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 *
 * <p>Author(s): Xu Guojun
 * Created: 2018/11/23 21:10
 *******************************************************/
public class RedisScanHelper {

    private static Logger Log = LoggerFactory.getLogger(RedisScanHelper.class);

    static RateLimiter rateLimiter = RateLimiter.create(50);

    public static Jedis getJedis(String host, int port, String password, int keyspace) {
        Jedis jedis = new Jedis(host, port);
        if (keyspace >-1) {
            jedis.select(keyspace);
        }
        if (StringUtils.isNotBlank(password)) {
            jedis.auth(password);
        }
        return jedis;
    }

    public static void scanToWriter(Jedis jedis, BufferedWriter bw, String patten) throws IOException {
        ScanParams scanParams = new ScanParams().count(100);
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
                bw.write(r);
                bw.newLine();
            }
            if ("0".equals(cur)) {
                cycleIsFinished = true;
            }
            rateLimiter.acquire();
        }
        Log.info("entity count:{}",count);
    }

    public static void scanRedisKeyToFile(String host,
                                          int port,
                                          int keyspace,
                                          String password,
                                          String keyPatten,
                                          String filePath) {
        if (StringUtils.isBlank(filePath)) {
            filePath = "keys.txt";
        }

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            Log.error("", e);
        }
        if (outputStream == null) {
            Log.error("write file failed.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"))){
            Jedis redis = getJedis(host, port, password, keyspace);
            scanToWriter(redis, bw, keyPatten);
            outputStream.close();
        } catch (Exception e){
            Log.error("", e);
        }
    }

    public static void main(String[] args) {
        scanRedisKeyToFile("127.0.0.1", 17289, 4,null, null,"test.txt");
    }
}
