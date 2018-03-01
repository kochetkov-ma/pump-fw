package ru.mk.pump.commons.activity;

import lombok.Setter;
import lombok.ToString;
import ru.mk.pump.commons.helpers.Parameters;

import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

@SuppressWarnings("unused")
@ToString()
public abstract class AbstractActivity extends Observable implements Activity {

    @Setter
    private String uuid;

    private volatile boolean closed = false;

    private volatile boolean activated = false;

    private Parameters param;

    protected AbstractActivity(Observer observer, String uuid) {
        this.uuid = uuid;
        if (observer != null) {
            addObserver(observer);
        }
    }

    protected AbstractActivity(Observer observer) {
        this(observer, UUID.randomUUID().toString());
    }

    protected AbstractActivity(String uuid) {
        this(null, uuid);
    }

    protected AbstractActivity() {
        this(null, UUID.randomUUID().toString());
    }

    public AbstractActivity withParam(Parameters param) {
        this.param = param;
        return this;
    }

    @Override
    public Parameters getParams() {
        return param;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public String getUUID() {
        return uuid;
    }

    @Override
    public boolean isActive() {
        return activated;
    }

    @Override
    public Activity activate() {
        setChanged();
        notifyObservers(ActivityEvent.ACTIVATE.event());
        activated = true;
        return this;
    }

    @Override
    public Activity disable() {
        if (isActive()) {
            setChanged();
            notifyObservers(ActivityEvent.DISABLE.event());
            activated = false;
        }
        return this;
    }

    @Override
    public void close() {
        if (!isClosed()) {
            setChanged();
            notifyObservers(ActivityEvent.CLOSE.event());
            activated = false;
            closed = true;
        }
    }
}
