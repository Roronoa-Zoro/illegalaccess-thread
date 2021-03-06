package com.illegalaccess.thread.sdk.thread;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by xiao on 2019/12/21.
 */
public class TracedBoundedBlockingDeque<E> extends LinkedBlockingDeque<E> {

    private static final int DEFAULT_CAPACITY = 200;

    public TracedBoundedBlockingDeque() {
        super(DEFAULT_CAPACITY);
    }

    public TracedBoundedBlockingDeque(int capacity) {
        super(capacity);
    }

    public TracedBoundedBlockingDeque(Collection<? extends E> c) {
        super(c.size() * 2);
        c.stream().forEach(element -> super.add(element));
    }
}
