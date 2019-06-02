package io.github.qinguan.knife.disruptor;

import org.apache.commons.lang3.time.StopWatch;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;

/*******************************************************
 * Copyright (C) 2018 iQIYI.COM - All Rights Reserved
 *
 * <p>This file is part of java-knife.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 *
 * <p>Author(s): Xu Guojun
 * Created: 2019/4/16 23:48
 *******************************************************/
public class FalseSharing {

    static CacheLineLong[] ta = new CacheLineLong[4];
    static LineLong[] tb = new LineLong[4];
    static {
        for(int i=0;i<4;i++) {
            ta[i] = new CacheLineLong();
            tb[i] = new LineLong();
        }
    }

    public static <T> void checkFalseSharing(T[] ta, BiConsumer<T[], Integer> f){
        ArrayList<Integer> order = new ArrayList<>(10);
        order.add(0);
        order.add(1);
        order.add(2);
        order.add(3);
        order.add(3);
        order.add(3);
        order.add(3);
        order.add(3);
        order.add(3);
        order.add(3);
        StopWatch sw = new StopWatch();
        sw.start();
        order.parallelStream().forEach(v-> {
            for (int i=0;i<1000000;i++) {
                f.accept(ta, v);
            }
        });
        sw.stop();
        System.out.println(sw.getNanoTime());
    }
    static BiConsumer<CacheLineLong[], Integer> taf = (v, i) -> v[i].value ++;
    static BiConsumer<LineLong[], Integer> tbf = (v, i) -> v[i].value++;

    public static void main(String[] args) {
        checkFalseSharing(ta, taf);//13278689
        checkFalseSharing(tb, tbf);//11175903
    }
}
