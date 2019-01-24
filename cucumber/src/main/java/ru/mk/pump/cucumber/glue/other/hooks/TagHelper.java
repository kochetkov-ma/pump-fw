package ru.mk.pump.cucumber.glue.other.hooks;

import cucumber.api.Scenario;
import java.util.Collection;
import lombok.Getter;
import lombok.NonNull;

@SuppressWarnings("WeakerAccess")
public class TagHelper {

    /**
     * need browser restarting
     */
    public final static String BROWSER_RESTART = "@BrowserRestart";

    /**
     * execute ONE this scenario if prev is failed
     */
    public final static String NO_SKIP = "@NoSkip";

    private final Collection<String> tags;

    @Getter
    private Scenario scenario;

    public TagHelper(Scenario scenario) {
        this.scenario = scenario;
        this.tags = scenario.getSourceTagNames();
    }

    /**
     * @param tag Tag to find. Can be without '@'
     * @return true if tag exists, using 'equalsIgnoreCase'
     */
    public boolean hasTag(@NonNull String tag) {
        if (!tag.startsWith("@")) {
            tag = "@" + tag;
        }
        return tags.stream().anyMatch(tag::equalsIgnoreCase);
    }

    public boolean isNoSkip() {
        return hasTag(NO_SKIP);
    }

    public boolean isBrowserRestart() {
        return hasTag(BROWSER_RESTART);
    }
}
