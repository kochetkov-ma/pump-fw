package ru.mk.pump.commons.activity;

import java.util.Observable;
import java.util.Observer;

public interface ActivityListener extends Observer {

    void onClose(NamedEvent namedEvent, Activity activity);

    void onActivate(NamedEvent namedEvent, Activity activity);

    void onDisable(NamedEvent namedEvent, Activity activity);

    default void update(Observable o, Object arg) {
        if (arg instanceof NamedEvent && o instanceof Activity) {
            final NamedEvent namedEvent = (NamedEvent) arg;
            final Activity activity = (Activity) o;

            if (ActivityEvent.CLOSE.check(namedEvent)) {
                onClose(namedEvent, activity);
            } else if (ActivityEvent.ACTIVATE.check(namedEvent)) {
                onActivate(namedEvent, activity);
            } else if (ActivityEvent.DISABLE.check(namedEvent)) {
                onDisable(namedEvent, activity);
            }
        }
    }

}
