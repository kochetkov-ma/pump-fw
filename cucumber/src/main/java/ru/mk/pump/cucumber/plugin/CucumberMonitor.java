package ru.mk.pump.cucumber.plugin;

import lombok.NoArgsConstructor;
import ru.mk.pump.commons.listener.AbstractNotifier;
import ru.mk.pump.cucumber.plugin.CucumberListener.TestEvent;

@NoArgsConstructor
public class CucumberMonitor extends AbstractNotifier<CucumberMonitor, TestEvent, CucumberListener> {

}
