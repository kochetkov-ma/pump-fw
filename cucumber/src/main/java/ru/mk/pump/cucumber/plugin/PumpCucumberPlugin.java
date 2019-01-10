package ru.mk.pump.cucumber.plugin;

import cucumber.api.Plugin;
import cucumber.api.event.ConcurrentEventListener;
import cucumber.api.event.EventHandler;
import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestCaseFinished;
import cucumber.api.event.TestCaseStarted;
import cucumber.api.event.TestRunFinished;
import cucumber.api.event.TestRunStarted;
import cucumber.api.event.TestStepFinished;
import cucumber.api.event.TestStepStarted;
import io.qameta.allure.cucumber3jvm.AllureCucumber3Jvm;
import ru.mk.pump.commons.listener.AbstractNotifier;
import ru.mk.pump.commons.reporter.Reporter;
import ru.mk.pump.cucumber.CucumberCore;
import ru.mk.pump.cucumber.plugin.CucumberListener.TestEvent;

@SuppressWarnings("unused")
public class PumpCucumberPlugin extends AbstractNotifier<CucumberMonitor, TestEvent, CucumberListener> implements ConcurrentEventListener, Plugin {

    private final AllureCucumber3Jvm allureCucumber2Jvm;
    private final Reporter reporter;
    private final CucumberMonitor monitor;
    private final EventHandler<TestRunStarted> testStartedHandler = this::testStarted;
    private final EventHandler<TestCaseStarted> caseStartedHandler = this::caseStarted;
    private final EventHandler<TestCaseFinished> caseFinishedHandler = this::caseFinished;
    private final EventHandler<TestStepStarted> stepStartedHandler = this::stepStarted;
    private final EventHandler<TestStepFinished> stepFinishedHandler = this::stepFinished;
    private final EventHandler<TestRunFinished> testFinishedHandler = this::testFinished;

    public PumpCucumberPlugin() {

        this.reporter = CucumberCore.instance().getReporter();
        this.monitor = CucumberMonitor.newDefault();
        this.allureCucumber2Jvm = new AllureCucumber3Jvm();

        CucumberCore.instance().setMonitor(monitor);
        monitor.started();
        addListeners(monitor.getListeners());
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        if (CucumberCore.instance().getConfig().isLoadPumpPlugin()) {
            publisher.registerHandlerFor(TestRunStarted.class, testStartedHandler);

            publisher.registerHandlerFor(TestCaseFinished.class, caseFinishedHandler);
            publisher.registerHandlerFor(TestRunFinished.class, testFinishedHandler);

            allureCucumber2Jvm.setEventPublisher(publisher);

            publisher.registerHandlerFor(TestCaseStarted.class, caseStartedHandler);
            publisher.registerHandlerFor(TestStepStarted.class, stepStartedHandler);

            publisher.registerHandlerFor(TestStepFinished.class, stepFinishedHandler);
        }
    }

    private void testStarted(final TestRunStarted event) {
        reporter.testStart("EXECUTION INFORMATION",
                "General execution steam of the test and all the service steps not included in the main report");
        notify(event(monitor, TestEvent.START_TEST));
    }

    private void testFinished(final TestRunFinished event) {
        if (monitor.getLastFeature().isPresent()) {
            /*завершить последнюю Feature*/
            monitor.getLastFeature().get().finish();
            notify(event(monitor, TestEvent.FINISH_FEATURE));
        }
        notify(event(monitor, TestEvent.FINISH_TEST));
        monitor.finished();
        reporter.testStop();
    }

    private void caseStarted(final TestCaseStarted event) {
        if (monitor.getLastFeature().isPresent()
                && !monitor.getLastFeature().get().getUrl().equals(event.testCase.getUri())) {
            /*завершить прошлую Feature*/
            monitor.getLastFeature().get().finish();
            notify(event(monitor, TestEvent.FINISH_FEATURE));
            /*начать новую  Feature*/
            monitor.addFeature(new Feature(event.testCase.getUri(), event.testCase.getScenarioDesignation()));
            notify(event(monitor, TestEvent.START_FEATURE));
        } else if (!monitor.getLastFeature().isPresent()) {
            /*начать самую первую Feature*/
            monitor.addFeature(new Feature(event.testCase.getUri(), event.testCase.getScenarioDesignation()));
            notify(event(monitor, TestEvent.START_FEATURE));
        }

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