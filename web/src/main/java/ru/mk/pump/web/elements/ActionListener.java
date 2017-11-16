package ru.mk.pump.web.elements;

public interface ActionListener {

    void onAfter(Action action);

    void onFinallyAfter(Action action);

    void onFail(Action action, Throwable throwable);

    void onSuccess(Action action, Object result);
}
