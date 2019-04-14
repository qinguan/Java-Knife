package io.github.qinguan.knife.netty.demo.echo;

/*******************************************************
 * Copyright (C) 2018 iQIYI.COM - All Rights Reserved
 *
 * <p>This file is part of java-knife.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 *
 * <p>Author(s): Xu Guojun
 * Created: 2019/2/5 14:19
 *******************************************************/
public class Demo {

    public static boolean isOutOfBounds1(int index, int length, int capacity) {
        return capacity - (index + length) < 0;
    }

    public static boolean isOutOfBounds(int index, int length, int capacity) {
        return (index | length | (index + length) | (capacity - (index + length))) < 0;
    }

    public static void main(String[] args) {
        System.out.println(isOutOfBounds1(2,0,2));
        System.out.println(isOutOfBounds(2,0,2));
    }
}
