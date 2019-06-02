package io.github.qinguan.knife.disruptor;

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
public class CacheLineLong {
    public volatile long value = 0L;
    public long pad1,pad2,pad3,pad4,pad5,pad6,pad7;
}
