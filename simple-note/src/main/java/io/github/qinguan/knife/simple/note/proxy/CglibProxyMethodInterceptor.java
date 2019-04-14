package io.github.qinguan.knife.simple.note.proxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/*******************************************************
 * Copyright (C) 2018 iQIYI.COM - All Rights Reserved
 *
 * <p>This file is part of java-knife.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 *
 * More refer: https://dzone.com/articles/cglib-missing-manual
 *
 * Proxy For non-interface types based on Enhancer which dynamically creates a subclass of a given type.
 *
 * The Enhancer class cannot instrument constructors. Neither can it instrument static or final classes.
 *
 * <p>Author(s): Xu Guojun
 * Created: 2019/4/14 21:43
 *******************************************************/
public class CglibProxyMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("cglib proxy method:" + method.getName());
        return methodProxy.invokeSuper(o, objects);
    }
}
