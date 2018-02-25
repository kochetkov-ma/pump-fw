package ru.mk.pump.commons.utils;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mk.pump.commons.utils.History.Info;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class HistoryTest {

    private History<String> history;

    private LocalDateTime intermediateDate;

    @BeforeEach
    void setUp() {

        history = new History<>(3);
        history.add(Info.of("id-1", "test-1"));

        Waiter.sleep(50);
        intermediateDate = LocalDateTime.now();
        Waiter.sleep(50);
        history.add(Info.of("id-2", "test-2"));

        Waiter.sleep(50);
        history.add(Info.of("id-3", "test-3"));
    }

    @Test
    void getAll() {
        assertThat(history.getAll())
                .hasOnlyElementsOfType(Info.class)
                .hasSize(3);
    }

    @Test
    void prettyPrinter() {
        log.info(history.toPrettyString());
    }

    @Test
    void getLast() {
        history.add(Info.of("id-5", "test-5"));
        assertThat(history.getLast().get().getPayload()).isEqualTo("test-5");

        Info<String> info = Info.of(UUID.randomUUID().toString(), "test-4", LocalDateTime.now().minusSeconds(1));
        history.add(info);
        assertThat(history.getLast().get().getPayload()).isEqualTo("test-5");
    }

    @Test
    void add() {
        Info<String> info = Info.of("test-4");
        history.add(info);
        assertThat(history.getAll()).contains(info);
    }

    @Test
    void clear() {
        history.clear();
        assertThat(history.getAll()).isEmpty();
        assertThat(history.asList()).isEmpty();
        assertThat(history.clone().getAll()).isEmpty();
    }

    @Test
    void findAfter() {
        assertThat(history.findAfter(intermediateDate)).hasSize(2);
    }

    @Test
    void findBefore() {
        assertThat(history.findBefore(intermediateDate)).hasSize(1);
    }

    @Test
    void findById() {
        history.add(Info.of("id-3", "test-4"));
        assertThat(history.findById("id-3"));
    }

    @Test
    void findLastById() {
        LocalDateTime time = LocalDateTime.now();
        history.add(Info.of("id-3", "test-4"));
        Optional<Info<String>> info = history.findLastById("id-3");
        assertThat(info).isPresent();
        info.ifPresent(i -> assertThat(i.getCreateDate()).isAfterOrEqualTo(time));

    }

    @Test
    void asList() {
        assertThat(history.asList())
                .hasOnlyElementsOfType(Info.class)
                .hasSize(3);
    }

    @Test
    void testClone() {
        final History<String> newHistory = history.clone();
        assertThat(newHistory.getAll()).contains(history.getAll().poll(), history.getAll().poll(), history.getAll().poll());
    }

    @Test
    void getMaxSize() {
        final History<String> newHistory = history.clone();

        assertThat(history.getCapacity()).isEqualTo(3);
        assertThat(newHistory.getCapacity()).isEqualTo(3);
    }

    @Test
    void testInfo() {
        final Info<String> info = Info.of("test1");
        assertThat(info.getCreateDate()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(info.getId()).matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
        assertThat(info.getPayload()).isEqualTo("test1");

        Info.setConsiderCreateDate(false);
        Info<String> infoExpected = Info.of("id", "test1");
        Waiter.sleep(50);
        Info<String> infoActual = Info.of("id", "test2");
        assertThat(infoActual).isEqualTo(infoExpected);

        Info.setConsiderCreateDate(true);
        infoExpected = Info.of("id", "test1");
        Waiter.sleep(50);
        infoActual = Info.of("id", "test2");
        assertThat(infoActual).isNotEqualTo(infoExpected);
    }
}