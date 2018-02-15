package ru.mk.pump.commons.activity;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("UnusedReturnValue")
public interface ActivityManager {

    /**
     * add and activate
     *
     * @param activity add and activate
     */
    ActivityManager add(Activity activity);

    ActivityManager addIfNotContains(Activity activity);

    Activity addAndActivate(Activity activity);

    ActivityManager releaseAll();

    ActivityManager releaseActive();

    ActivityManager release(String uuid);

    boolean hasActive();

    Optional<Activity> getActive();

    Optional<Activity> getPrev();

    List<Activity> getAll();

    Optional<Activity> get(String uuid);
}
