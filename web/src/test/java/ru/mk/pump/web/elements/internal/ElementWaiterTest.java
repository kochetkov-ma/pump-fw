package ru.mk.pump.web.elements.internal;


import static org.assertj.core.api.Assertions.*;

import com.google.common.collect.Iterators;
import java.util.Iterator;
import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.utils.WaitResult;

@Slf4j
class ElementWaiterTest {

    @Test
    void waitPredicate() {
        Iterator iter = Iterators.cycle(1, 1, 1, 2);
        Iterator iterInfinity = Iterators.cycle(1, 1, 1, 1);

        ElementWaiter waiter = ElementWaiter.newWaiterS(1);
        WaitResult res = waiter.waitPredicate((Callable<Object>) iter::next, (prev, next) -> !next.equals(prev));

        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getResult()).isEqualTo(2);

        waiter = ElementWaiter.newWaiterS(1);
        res = waiter.waitPredicate((Callable<Object>) iterInfinity::next, (prev, next) -> !next.equals(prev));

        assertThat(res.isSuccess()).isFalse();
        assertThat(res.getResult()).isEqualTo(1);

    }
}