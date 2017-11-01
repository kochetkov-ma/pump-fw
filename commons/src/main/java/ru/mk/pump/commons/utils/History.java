package ru.mk.pump.commons.utils;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.ForwardingQueue;
import com.google.common.collect.ImmutableList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import ru.mk.pump.commons.utils.History.Info;

@ToString
public class History<T> extends ForwardingQueue<Info<T>> {


    private final Queue<Info<T>> history;

    @Getter
    private final int maxSize;

    private History(EvictingQueue<Info<T>> history) {
        this.maxSize = history.remainingCapacity();
        this.history = history;
    }

    public History(int maxSize) {
        this(EvictingQueue.create(maxSize));
    }

    @Override
    protected Queue<Info<T>> delegate() {
        return history;
    }

    public Optional<Info<T>> findLastById(@NotNull String id) {
        return history.stream().filter(item -> id.equals(item.getId())).reduce((first, second) -> second);
    }

    public List<Info<T>> asList() {
        return ImmutableList.copyOf(history);
    }

    @Getter
    public static class Info<T> {

        @Setter
        private static boolean considerCreateDate = true;

        private final String id;

        private final LocalDateTime createDate;

        private final T payload;

        private Info(String id, T payload) {
            this.id = id;
            this.payload = payload;
            createDate = LocalDateTime.now();
        }

        public static <T> Info<T> of(String id, T payload) {
            return new Info<>(id, payload);
        }

        public static <T> Info<T> of(T payload) {
            return new Info<>(UUID.randomUUID().toString(), payload);
        }

        //region EQUALS
        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            if (considerCreateDate) {
                result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
            }
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Info)) {
                return false;
            }

            Info<?> info = (Info<?>) o;

            if (id != null ? !id.equals(info.id) : info.id != null) {
                return false;
            }
            return !considerCreateDate || (createDate != null ? createDate.equals(info.createDate) : info.createDate == null);
        }

        @Override
        public String toString() {
            return "Info(" +
                "id='" + id + '\'' +
                ", createDate=" + createDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSSS")) +
                ", payload=" + payload +
                ')';
        }
        //endregion
    }

    @Override
    public History<T> clone() {
        final EvictingQueue<Info<T>> result = EvictingQueue.create(maxSize);
        result.addAll(history);
        return new History<>(result);
    }


}
