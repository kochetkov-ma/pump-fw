package ru.mk.pump.cucumber.plugin;

import com.google.common.collect.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.mk.pump.commons.utils.Str;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import ru.mk.pump.cucumber.CucumberCore;

@SuppressWarnings({"unused", "WeakerAccess"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CucumberMonitor {

    private Set<CucumberListener> listeners = Sets.newHashSet();

    private Set<CucumberListener> runtimeListeners = Sets.newHashSet();

    @Getter
    private boolean started;

    @Getter
    private boolean finished;

    private List<Feature> featureList = Lists.newArrayList();

    public static CucumberMonitor newInactive() {
        return new CucumberMonitor();
    }

    /**
     * @throws IllegalStateException If PumpCucumberPlugin plugin didn't enabled
     */
    public void checkPlugin() {
        if (!isStarted() && !isFinished() && CucumberCore.instance().getConfig().isLoadPumpPlugin()) {
            throw new IllegalStateException(Str.space("You must enable ru.mk.pump.cucumber.plugin.PumpCucumberPlugin in cucumber options. ",
                    "In @CucumberOptions add parameter 'plugin = {\"ru.mk.pump.cucumber.plugin.PumpCucumberPlugin\"}'. ",
                    "Or in java args add parameter '--plugin ru.mk.pump.cucumber.plugin.PumpCucumberPlugin'"));
        }
    }

    public CucumberMonitor addRuntimeListeners(Set<CucumberListener> cucumberListeners) {
        runtimeListeners.addAll(cucumberListeners);
        return this;
    }

    public CucumberMonitor addRuntimeListener(CucumberListener cucumberListener) {
        runtimeListeners.add(cucumberListener);
        return this;
    }

    public Optional<Feature> getLastFeature() {
        if (getAllFeatures().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Iterables.getLast(getAllFeatures()));
    }

    static CucumberMonitor newDefault() {
        return new CucumberMonitor().addListeners(Listeners.defaultListeners(CucumberCore.instance().getConfig()));
    }

    Set<CucumberListener> getRuntimeListeners() {
        return ImmutableSet.copyOf(runtimeListeners);
    }

    Set<CucumberListener> getListeners() {
        return ImmutableSet.copyOf(listeners);
    }

    CucumberMonitor addListeners(Set<CucumberListener> cucumberListeners) {
        listeners.addAll(cucumberListeners);
        return this;
    }

    CucumberMonitor addListener(CucumberListener cucumberListener) {
        listeners.add(cucumberListener);
        return this;
    }

    List<Feature> getAllFeatures() {
        return ImmutableList.copyOf(featureList);
    }

    void started() {
        this.started = true;
    }

    void finished() {
        this.started = false;
        this.finished = true;
    }

    void addFeature(Feature feature) {
        featureList.add(feature);
    }

    void updateFeature(Consumer<Feature> updater) {
        getLastFeature().ifPresent(updater);
    }
}
