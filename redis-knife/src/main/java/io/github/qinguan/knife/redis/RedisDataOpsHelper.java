package io.github.qinguan.knife.redis;

import com.google.common.util.concurrent.RateLimiter;
import io.github.qinguan.knife.redis.common.JedisHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

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
public class RedisDataOpsHelper {
    private static Logger Log = LoggerFactory.getLogger(RedisDataOpsHelper.class);

    /**
     * 按给定匹配模式删除数据
     * @param jedis
     * @param patten
     * @throws IOException
     */
    public static void delByPatten(Jedis jedis, String patten) throws IOException {
        int affect = JedisHelper.scanByConsumer(jedis, patten, RateLimiter.create(50), r->jedis.del(r));
        Log.info("entity delete count:{}", affect);
    }

    /**
     * 按给定匹配模式导出key数据
     * @param redis
     * @param keyPatten
     * @param filePath
     */
    public static void scanKeyToFile(Jedis redis, String keyPatten, String filePath) {
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
            int affect = JedisHelper.scanByConsumer(redis, keyPatten, RateLimiter.create(50), (String v) -> {
                try {
                    bw.write(v);
                    bw.newLine();
                } catch (IOException e) {
                    Log.error("", e);
                }
            });
            Log.info("entity count:{}", affect);
            outputStream.close();
        } catch (Exception e){
            Log.error("", e);
        }
    }

    public static void main(String[] args) {
        Jedis jedis = JedisHelper.getJedis("127.0.0.1", 6910, null, 2);
        try {
            delByPatten(jedis, "");
            //scanKeyToFile(jedis, null,"test.txt");
        } catch (Exception e) {
            Log.error("", e);
        }
    }
}
