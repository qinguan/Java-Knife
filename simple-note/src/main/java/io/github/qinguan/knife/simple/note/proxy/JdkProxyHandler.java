package io.github.qinguan.knife.simple.note.proxy;

import io.github.qinguan.knife.simple.note.Note;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/*******************************************************
 * Copyright (C) 2018 iQIYI.COM - All Rights Reserved
 *
 * <p>This file is part of java-knife.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 *
 *
 * More refer: https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Proxy.html
 *
 * Proxy For interface types
 *
 * <p>Author(s): Xu Guojun
 * Created: 2019/4/14 21:27
 *******************************************************/
public class JdkProxyHandler implements InvocationHandler {

    private Note note;

    public JdkProxyHandler(Note note) {
        this.note = note;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("jdk proxy method:" + method.getName());
        return method.invoke(note, args);
    }
}
