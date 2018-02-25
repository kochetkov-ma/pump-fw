package ru.mk.pump.cucumber.plugin;

import cucumber.api.event.*;
import cucumber.api.formatter.Formatter;
import io.qameta.allure.cucumber2jvm.AllureCucumber2Jvm;
import io.qameta.allure.cucumber2jvm.CucumberSourceUtils;
import ru.mk.pump.commons.listener.AbstractNotifier;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.cucumber.CucumberCore;
import ru.mk.pump.cucumber.plugin.CucumberListener.TestEvent;

@SuppressWarnings("unused")
public class PumpCucumberPlugin extends AbstractNotifier<CucumberMonitor, TestEvent, CucumberListener> implements Formatter {

    private final CucumberSourceUtils cucumberSourceUtils = new CucumberSourceUtils();
    private final AllureCucumber2Jvm allureCucumber2Jvm;
    private final Reporter reporter;
    private final CucumberMonitor monitor;
    private final EventHandler<TestRunStarted> testStartedHandler = this::testStarted;
    private final EventHandler<TestSourceRead> featureStartedHandler = this::featureStarted;
    private final EventHandler<TestCaseStarted> caseStartedHandler = this::caseStarted;
    private final EventHandler<TestCaseFinished> caseFinishedHandler = this::caseFinished;
    private final EventHandler<TestStepStarted> stepStartedHandler = this::stepStarted;
    private final EventHandler<TestStepFinished> stepFinishedHandler = this::stepFinished;
    private final EventHandler<TestRunFinished> testFinishedHandler = this::testFinished;

    public PumpCucumberPlugin() {

        this.reporter = CucumberCore.instance().getReporter();
        this.monitor = CucumberMonitor.newDefault();
        this.allureCucumber2Jvm = new AllureCucumber2Jvm();

        CucumberCore.instance().setMonitor(monitor);
        monitor.started();
        addListeners(monitor.getListeners());
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, testStartedHandler);
        publisher.registerHandlerFor(TestSourceRead.class, featureStartedHandler);

        publisher.registerHandlerFor(TestCaseFinished.class, caseFinishedHandler);
        publisher.registerHandlerFor(TestRunFinished.class, testFinishedHandler);

        allureCucumber2Jvm.setEventPublisher(publisher);

        publisher.registerHandlerFor(TestCaseStarted.class, caseStartedHandler);
        publisher.registerHandlerFor(TestStepStarted.class, stepStartedHandler);

        publisher.registerHandlerFor(TestStepFinished.class, stepFinishedHandler);

    }

    private void testStarted(final TestRunStarted event) {
        reporter.testStart("EXECUTION INFORMATION",
                "General execution steam of the test and all the service steps not included in the main report");
        notify(event(monitor, TestEvent.START_TEST));
    }

    private void testFinished(final TestRunFinished event) {
        if (monitor.getLastFeature().isPresent()) {
            notify(event(monitor, TestEvent.FINISH_FEATURE));
        }
        notify(event(monitor, TestEvent.FINISH_TEST));
        monitor.finished();
        reporter.testStop();
    }

    private void featureStarted(final TestSourceRead event) {
        if (monitor.getLastFeature().isPresent()) {
            notify(event(monitor, TestEvent.FINISH_FEATURE));
        }
        monitor.addFeature(new Feature(event.uri, event.source));
        notify(event(monitor, TestEvent.START_FEATURE));
    }

    private void caseStarted(final TestCaseStarted event) {
        monitor.updateFeature(feature -> feature.startScenario(new Scenario(event.testCase)));
        notify(event(monitor, TestEvent.START_SCENARIO));
    }

    private void caseFinished(final TestCaseFinished event) {
        monitor.updateFeature(feature -> feature.releaseScenario(event.result));
        notify(event(monitor, TestEvent.FINISH_SCENARIO));
    }

    private void stepStarted(final TestStepStarted event) {
        /*nothing*/
    }

    private void stepFinished(final TestStepFinished event) {
        /*nothing*/
    }
}