package ru.mk.pump.commons.utils;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.core.ConditionTimeoutException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.exception.ExecutionException;
import ru.mk.pump.commons.exception.PumpMessage;
import ru.mk.pump.commons.exception.TimeoutException;
import ru.mk.pump.commons.exception.VerifyError;


@Slf4j
class WaiterTest {

    private Callable<String> callableString;

    private Callable<Boolean> callableWithEx;

    private Callable<Boolean> callableWithAssert;

    @BeforeEach
    void setUp() {
        callableString = () -> {
            Waiter.sleep(100);
            return "ok";
        };

        callableWithEx = () -> {
            Waiter.sleep(100);
            throw new RuntimeException("Test exception");
        };

        callableWithAssert = () -> {
            Waiter.sleep(100);
            throw new VerifyError(new PumpMessage("Test"));
        };
    }

    @Test
    void testWaitResultPass() {
        final WaitResult<String> waitResult = new Waiter().wait(1, 0, callableString, null);
        assertThat(waitResult.isSuccess()).isTrue();
        assertThat(waitResult.hasCause()).isFalse();
        assertThat(waitResult.hasResult()).isTrue();
        assertThat(waitResult.getCause()).isNull();
        assertThat(waitResult.getResult()).isEqualTo("ok");
        assertThat(waitResult.getElapsedTime()).isBetween(100L, 150L);
        assertThatCode(() -> waitResult.ifHasResult((r) -> Assertions.fail("Фэйл"))).isInstanceOf(AssertionError.class);
        waitResult.ifHasCause((ex) -> Assertions.fail("Фэйл"));
        assertThat(waitResult.getInfo()).containsOnlyKeys("timeout (sec)", "interval (ms)", "elapsed time (ms)", "last result");
        assertThatCode(waitResult::throwExceptionOnFail).doesNotThrowAnyException();
        assertThatCode(() -> waitResult.throwExceptionOnFail(r -> new RuntimeException(r.getCause()))).doesNotThrowAnyException();
    }

    @Test
    void testWaitResultFail() {
        final WaitResult<String> waitResult = new Waiter().wait(1, 0, callableString, Matchers.equalTo("fail"));
        assertThat(waitResult.isSuccess()).isFalse();
        assertThat(waitResult.hasCause()).isTrue();
        assertThat(waitResult.hasResult()).isTrue();
        assertThat(waitResult.getCause()).isInstanceOf(ConditionTimeoutException.class);
        assertThat(waitResult.getResult()).isEqualTo("ok");
        assertThat(waitResult.getElapsedTime()).isBetween(1000L, 1100L);
        assertThatCode(() -> waitResult.ifHasResult((r) -> Assertions.fail("Фэйл"))).isInstanceOf(AssertionError.class);
        assertThat(waitResult.getInfo()).containsOnlyKeys("cause", "timeout (sec)", "interval (ms)", "elapsed time (ms)", "last result");
        assertThatThrownBy(waitResult::throwExceptionOnFail).isInstanceOf(TimeoutException.class);
        assertThatThrownBy(() -> waitResult.throwExceptionOnFail(r -> new ExecutionException(r.getResult(), r.getCause())))
            .isInstanceOf(ExecutionException.class)
            .hasCauseInstanceOf(ConditionTimeoutException.class);
    }

    @Test
    void testWaitResultFailEx() {
        final WaitResult<?> waitResult = new Waiter().waitIgnoreExceptions(1, 0, callableWithEx);
        assertThat(waitResult.isSuccess()).isFalse();
        assertThat(waitResult.hasCause()).isTrue();
        assertThat(waitResult.hasResult()).isFalse();
        assertThat(waitResult.getCause()).isInstanceOf(RuntimeException.class);
        assertThat(waitResult.getResult()).isNull();
        assertThat(waitResult.getElapsedTime()).isBetween(1000L, 1050L);
        waitResult.ifHasResult((r) -> Assertions.fail("Фэйл"));
        assertThatCode(() -> waitResult.ifHasCause((ex) -> Assertions.fail("Фэйл"))).isInstanceOf(AssertionError.class);
        assertThat(waitResult.getInfo()).containsOnlyKeys("timeout (sec)", "interval (ms)", "elapsed time (ms)", "cause");
        assertThatThrownBy(waitResult::throwExceptionOnFail).isInstanceOf(TimeoutException.class);
        assertThatThrownBy(() -> waitResult.throwExceptionOnFail(r -> new ExecutionException("test", r.getCause()))).isInstanceOf(ExecutionException.class);
    }

    @Test
    void waitIgnoreExceptions() {
        assertThatThrownBy(() -> new Waiter().waitIgnoreExceptions(1, 0, callableWithAssert)).isInstanceOf(VerifyError.class);
        assertThatCode(() -> new Waiter().waitIgnoreExceptions(1, 0, callableWithEx)).doesNotThrowAnyException();
    }

    @Test
    void testWait() {
        assertThat(new Waiter().withReThrow(false).wait(1, 0, callableWithEx).getCause()).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> new Waiter().withReThrow(true).wait(1, 0, callableWithEx)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> new Waiter().wait(1, 0, callableWithAssert)).isInstanceOf(VerifyError.class);
        assertThatCode(() -> new Waiter().withIgnoreExceptions(RuntimeException.class).wait(1, 0, callableWithEx)).doesNotThrowAnyException();
        assertThatThrownBy(() -> new Waiter().withIgnoreExceptions(VerifyError.class).wait(1, 0, callableWithAssert)).isInstanceOf(VerifyError.class);
        assertThatCode(() -> new Waiter(false).withIgnoreExceptions(AssertionError.class).wait(1, 0, callableWithAssert)).doesNotThrowAnyException();
        assertThatThrownBy(() -> new Waiter().withIgnoreExceptions(AssertionError.class).wait(1, 0, callableWithAssert)).isInstanceOf(VerifyError.class);
    }

}