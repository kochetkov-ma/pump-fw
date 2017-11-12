package ru.mk.pump.commons.activity;

interface ActivityListener {

    void onClose(NamedEvent namedEvent, Activity activity);

    void onActivate(NamedEvent namedEvent, Activity activity);

    void onDisable(NamedEvent namedEvent, Activity activity);

}
