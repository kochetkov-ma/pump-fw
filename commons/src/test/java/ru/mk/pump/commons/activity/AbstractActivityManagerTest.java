package ru.mk.pump.commons.activity;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("ConstantConditions")
@Slf4j
class AbstractActivityManagerTest {

    private ActivityManager activityManager;

    private String lastUuid;

    @BeforeEach
    void setUp() {
        final List<Activity> activityList = Lists.newArrayList(activity(), activity(), activity());
        this.activityManager = manager(activityList);
    }

    @Test
    void add() {
        activityManager.add(activity());
        Assertions.assertEquals(4, activityManager.getAll().size(), "Size is not 3");
    }

    @Test
    void addAndActivate() {
        Assertions.assertFalse(activityManager.hasActive());
        activityManager.addAndActivate(activity());
        Assertions.assertTrue(activityManager.hasActive());
        Assertions.assertEquals(lastUuid, activityManager.getActive().get().getUUID());
        Assertions.assertTrue(activityManager.get(lastUuid).get().isActive());
    }

    @Test
    void releaseAll() {
        activityManager.add(activity());
        activityManager.add(activity());
        activityManager.releaseAll();
        Assertions.assertEquals(0, activityManager.getAll().size());
        Assertions.assertFalse(activityManager.hasActive());
        Assertions.assertFalse(activityManager.getPrev().isPresent());

    }

    @Test
    void releaseActive() {
        activityManager.addAndActivate(activity());
        activityManager.addAndActivate(activity());
        log.info(String.valueOf(activityManager.getActive().isPresent()));
        log.info(String.valueOf(activityManager.getPrev().isPresent()));
        log.info(String.valueOf(activityManager.getAll().size()));
        activityManager.releaseActive();
        activityManager.releaseActive();
        log.info(String.valueOf(activityManager.getAll().size()));
        log.info(String.valueOf(activityManager.getActive().isPresent()));
        log.info(String.valueOf(activityManager.getPrev().isPresent()));
        log.info(String.valueOf(activityManager.getAll().size()));
    }

    @Test
    void activate() {
        Activity activity = activityManager.getAll().get(0);
        activity.activate();
        log.info(String.valueOf(activityManager.getActive().isPresent()));
        log.info(String.valueOf(activityManager.getPrev().isPresent()));
        activity.activate();
        log.info(String.valueOf(activityManager.getActive().isPresent()));
        log.info(String.valueOf(activityManager.getPrev().isPresent()));

        activity = activityManager.getAll().get(1);
        activity.activate();
        log.info(String.valueOf(activityManager.getActive().get().getUUID()));
        log.info(String.valueOf(activityManager.getPrev().get().getUUID()));
        activityManager.getPrev().get().activate();
        log.info(String.valueOf(activityManager.getPrev().get().getUUID()));
    }

    @Test
    void getHasActive() {
        activityManager.add(activity());
        Assertions.assertFalse(activityManager.getActive().isPresent());
        Assertions.assertFalse(activityManager.hasActive());

        activityManager.get(lastUuid).get().activate();
        Assertions.assertEquals(lastUuid, activityManager.getActive().get().getUUID());
        Assertions.assertTrue(activityManager.hasActive());
    }

    @Test
    void getPrev() {
        Activity activity = activity();
        activityManager.add(activity);
        Assertions.assertFalse(activityManager.getPrev().isPresent());
        activity.activate();
        Assertions.assertTrue(activityManager.getActive().get().isActive());
        activityManager.addAndActivate(activity());
        Assertions.assertEquals(activity.getUUID(), activityManager.getPrev().get().getUUID());
        Assertions.assertFalse(activityManager.getPrev().get().isActive());
    }

    @Test
    void getAll() {
        Assertions.assertEquals(3, activityManager.getAll().size());
    }

    @Test
    void get() {
        Assertions.assertEquals(lastUuid, activityManager.get(lastUuid).get().getUUID());
    }

    private Activity activity() {
        lastUuid = UUID.randomUUID().toString();
        return new AbstractActivity(lastUuid) {
        };
    }

    private ActivityManager manager(List<Activity> activities) {
        return new AbstractActivityManager(activities) {
        };
    }
}