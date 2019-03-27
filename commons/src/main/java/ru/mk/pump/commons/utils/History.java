package ru.mk.pump.commons.utils;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import ru.mk.pump.commons.interfaces.PrettyPrinter;
import ru.mk.pump.commons.interfaces.StrictInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("UnusedReturnValue")
@ToString
//TODO:add compareTo
public class History<T> implements PrettyPrinter {

    private final Queue<Info<T>> history;

    @Getter
    private final int capacity;

    @SuppressWarnings("unused")
    @Getter
    public static class Info<T> implements PrettyPrinter, StrictInfo, Comparable<Info<T>> {

        private static ThreadLocal<Boolean> considerCreateDate = InheritableThreadLocal.withInitial(() -> true);

        private final String id;

        private final LocalDateTime createDate;

        private final T payload;

        private Info(String id, T payload, LocalDateTime createDate) {
            this.id = id;
            this.payload = payload;
            this.createDate = createDate;
        }

        public static <T> Info<T> of(String id, T payload) {
            return new Info<>(id, payload, LocalDateTime.now());
        }

        public static <T> Info<T> of(T payload) {
            return new Info<>(UUID.randomUUID().toString(), payload, LocalDateTime.now());
        }

        public static void setConsiderCreateDate(boolean considerCreateDate) {
            Info.considerCreateDate.set(considerCreateDate);
        }

        //region EQUALS
        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            if (considerCreateDate.get()) {
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

            return (id != null ? id.equals(info.id) : info.id == null) && (!considerCreateDate.get() || (createDate != null ? createDate.equals(info.createDate)
                    : info.createDate == null));
        }

        @Override
        public String toPrettyString() {
            return Str.toPrettyString(getInfo());
        }

        @Override
        public Map<String, String> getInfo() {
            return StrictInfo.infoBuilder("info")
                    .put("id", id)
                    .put("create date", Str.toString(createDate))
                    .put("payload", Str.toString(payload))
                    .build();
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public int compareTo(@NonNull Info<T> o) {
            return getCreateDate().compareTo(o.getCreateDate());
        }

        static <T> Info<T> of(String id, T payload, LocalDateTime createDate) {
            return new Info<>(id, payload, createDate);
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

    private History(EvictingQueue<Info<T>> history, int capacity) {
        this.capacity = capacity;
        this.history = history;
    }

    public History(int capacity) {
        this(EvictingQueue.create(capacity), capacity);
    }

    public Optional<Info<T>> getLast() {
        if (getAll().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(Iterables.getLast(Ordering.natural().sortedCopy(getAll())));
    }

    public Queue<Info<T>> getAll() {
        return history;
    }

    public int size() {
        return history.size();
    }

    public History<T> add(Info<T> item) {
        history.add(item);
        return this;
    }

    public void clear() {
        history.clear();
    }

    public List<Info<T>> findAfter(@NonNull LocalDateTime dateTime) {
        return history.stream().filter(item -> item.getCreateDate().isAfter(dateTime)).collect(Collectors.toList());
    }

    //TODO:Add sort by time

    public List<Info<T>> findBefore(@NonNull LocalDateTime dateTime) {
        return history.stream().filter(item -> item.getCreateDate().isBefore(dateTime)).collect(Collectors.toList());
    }

    public List<Info<T>> findById(@NonNull String id) {
        return history.stream().filter(item -> id.equals(item.getId())).collect(Collectors.toList());
    }

    public Optional<Info<T>> findLastById(@NonNull String id) {
        return history.stream().filter(item -> id.equals(item.getId())).reduce((first, second) -> second);
    }

    /**
     * @return ImmutableList
     */
    public List<Info<T>> asList() {
        return ImmutableList.copyOf(history);
    }

    @Override
    public String toPrettyString() {
        return Str.toPrettyString(getAll());
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public History<T> clone() {
        final EvictingQueue<Info<T>> result = EvictingQueue.create(capacity);
        result.addAll(history);
        return new History<>(result, capacity);
    }


}
