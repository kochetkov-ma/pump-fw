package ru.mk.pump.web.common;

import java.util.List;
import java.util.Observer;
import java.util.Optional;
import java.util.UUID;

public interface ActivityManager extends Observer{

    /**
     * add and activate
     *
     * @param activity add and activate
     */
    ActivityManager add(Activity activity);

    Activity addAndActivate(Activity activity);

    ActivityManager releaseAll();

    ActivityManager releaseActive();

    ActivityManager release(UUID uuid);

    boolean hasActive();

    Optional<Activity> getActive();

    Optional<Activity> getPrev();

    List<Activity> getAll();

    Optional<Activity> get(UUID uuid);

}
