package io.github.qinguan.knife.simple.note;

/*******************************************************
 * Copyright (C) 2018 iQIYI.COM - All Rights Reserved
 *
 * <p>This file is part of java-knife.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 *
 * <p>Author(s): Xu Guojun
 * Created: 2019/4/14 21:25
 *******************************************************/
public class NoteImpl implements Note {

    @Override
    public String sayHello(String note) {
        String res = "hello " + note;
        System.out.println(res);
        return res;
    }

    public String sayBye(String note) {
        String res = "bye " + note;
        System.out.println(res);
        return res;
    }
}
