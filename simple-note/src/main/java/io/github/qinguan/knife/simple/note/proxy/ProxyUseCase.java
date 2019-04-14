package io.github.qinguan.knife.simple.note.proxy;

import io.github.qinguan.knife.simple.note.Note;
import io.github.qinguan.knife.simple.note.NoteImpl;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;

/*******************************************************
 * Copyright (C) 2018 iQIYI.COM - All Rights Reserved
 *
 * <p>This file is part of java-knife.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 *
 * <p>Author(s): Xu Guojun
 * Created: 2019/4/14 21:31
 *******************************************************/
public class ProxyUseCase {

    public void jdkProxy() {
        Note note = (Note) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[] {Note.class}, new JdkProxyHandler(new NoteImpl()));
        note.sayHello("jdk proxy!");
    }

    public void cglibProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(NoteImpl.class);
        enhancer.setCallback(new CglibProxyMethodInterceptor());

        String note = "cglib proxy!";
        NoteImpl noteImpl = (NoteImpl) enhancer.create();
        noteImpl.sayHello(note);
        noteImpl.sayBye(note);
    }

    public static void main(String[] args) {
        ProxyUseCase proxyCase = new ProxyUseCase();
        proxyCase.jdkProxy();
        proxyCase.cglibProxy();
    }

}
