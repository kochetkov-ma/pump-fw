package ru.mk.pump.cucumber.plugin;

import cucumber.api.event.EventHandler;
import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestCaseFinished;
import cucumber.api.event.TestCaseStarted;
import cucumber.api.event.TestRunFinished;
import cucumber.api.event.TestRunStarted;
import cucumber.api.event.TestSourceRead;
import cucumber.api.event.TestStepFinished;
import cucumber.api.event.TestStepStarted;
import cucumber.api.formatter.Formatter;
import ru.mk.pump.cucumber.CucumberPumpCore;

public class PumpCucumberPlugin implements Formatter {

    private final CucumberMonitor monitor = CucumberPumpCore.instance().getMonitor();

    private final EventHandler<TestRunStarted> testStartedHandler = this::testStarted;

    private final EventHandler<TestSourceRead> featureStartedHandler = this::featureStarted;

    private final EventHandler<TestCaseStarted> caseStartedHandler = this::caseStarted;

    private final EventHandler<TestCaseFinished> caseFinishedHandler = this::caseFinished;

    private final EventHandler<TestStepStarted> stepStartedHandler = this::stepStarted;

    private final EventHandler<TestStepFinished> stepFinishedHandler = this::stepFinished;

    private final EventHandler<TestRunFinished> testFinishedHandler = this::testFinished;

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, testStartedHandler);
        publisher.registerHandlerFor(TestSourceRead.class, featureStartedHandler);

        publisher.registerHandlerFor(TestCaseStarted.class, caseStartedHandler);
        publisher.registerHandlerFor(TestCaseFinished.class, caseFinishedHandler);

        publisher.registerHandlerFor(TestStepStarted.class, stepStartedHandler);
        publisher.registerHandlerFor(TestStepFinished.class, stepFinishedHandler);

        publisher.registerHandlerFor(TestRunFinished.class, testFinishedHandler);
    }

    private void testStarted(final TestRunStarted event) {

    }

    private void testFinished(final TestRunFinished event) {

    }

    private void featureStarted(final TestSourceRead event) {

    }

    private void caseStarted(final TestCaseStarted event) {

    }

    private void caseFinished(final TestCaseFinished event) {

    }

    private void stepStarted(final TestStepStarted event) {

    }

    private void stepFinished(final TestStepFinished event) {

    }


}
