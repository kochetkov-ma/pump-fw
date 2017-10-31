package ru.mk.pump.web.common;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;
import lombok.Setter;
import lombok.ToString;

@ToString(exclude = "prevActivity")
public abstract class AbstractActivity extends Observable implements Activity {

    @Setter
    private UUID uuid;

    private volatile boolean closed = false;

    private volatile boolean activated = false;

    private Map<String, Parameter> param;

    private Activity prevActivity;

    protected AbstractActivity(Observer observer, UUID uuid) {
        this.uuid = uuid;
        if (observer != null) {
            addObserver(observer);
        }
    }

    protected AbstractActivity(Observer observer) {
        this(observer, UUID.randomUUID());
    }

    protected AbstractActivity(UUID uuid) {
        this(null, uuid);
    }

    protected AbstractActivity() {
        this(null, UUID.randomUUID());
    }

    public AbstractActivity withParam(Map<String, Parameter> param) {
        this.param = param;
        return this;
    }

    @Override
    public Map<String, Parameter> getParams() {
        return param;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean isActive() {
        return activated;
    }

    @Override
    public Activity activate() {
        setChanged();
        notifyObservers(NamedEvent.of("activate"));
        activated = true;
        clearChanged();
        return this;
    }

    @Override
    public Activity disable() {
        if (isActive()) {
            setChanged();
            notifyObservers(NamedEvent.of("disable"));
            activated = false;
            clearChanged();
        }
        return this;
    }

    @Override
    public void close() {
        if (!isClosed()) {
            setChanged();
            notifyObservers(NamedEvent.of("close"));
            activated = false;
            closed = true;
            clearChanged();
        }
    }
}
