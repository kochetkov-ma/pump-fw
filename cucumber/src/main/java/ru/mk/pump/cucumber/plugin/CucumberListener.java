package ru.mk.pump.cucumber.plugin;

import ru.mk.pump.commons.listener.Event;
import ru.mk.pump.commons.listener.Listener;
import ru.mk.pump.cucumber.plugin.CucumberListener.TestEvent;

public interface CucumberListener extends Listener<CucumberMonitor, TestEvent> {

    @Override
    default void on(Event<CucumberMonitor, TestEvent> event, Object... args) {
        switch (event.name()) {
            case START_TEST:
                onStartTest(event.get());
                break;
            case START_FEATURE:
                onStartFeature(event.get());
                break;
            case START_SCENARIO:
                onStartScenario(event.get());
                break;
            case FINISH_TEST:
                onFinishTest(event.get());
                break;
            case FINISH_FEATURE:
                onFinishFeature(event.get());
                break;
            case FINISH_SCENARIO:
                onFinishScenario(event.get());
                break;
        }
    }

    void onStartTest(CucumberMonitor monitor);

    void onStartFeature(CucumberMonitor monitor);

    void onStartScenario(CucumberMonitor monitor);

    void onFinishTest(CucumberMonitor monitor);

    void onFinishFeature(CucumberMonitor monitor);

    void onFinishScenario(CucumberMonitor monitor);

    enum TestEvent {
        START_TEST, START_FEATURE, START_SCENARIO, FINISH_FEATURE, FINISH_SCENARIO, FINISH_TEST
    }
}
