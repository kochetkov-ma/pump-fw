package ru.mk.pump.commons.activity;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.mk.pump.commons.activity.AbstractActivity;
import ru.mk.pump.commons.activity.AbstractActivityManager;
import ru.mk.pump.commons.activity.Activity;
import ru.mk.pump.commons.activity.ActivityManager;

@Slf4j
public class AbstractActivityManagerTest {

    private ActivityManager activityManager;

    private UUID lastUuid;

    @Before
    public void setUp() {
        final List<Activity> activityList = Lists.newArrayList(activity(), activity(), activity());
        this.activityManager = manager(activityList);
    }

    @Test
    public void add() {
        activityManager.add(activity());
        Assert.assertEquals("Size is not 3", 4, activityManager.getAll().size());
    }

    @Test
    public void addAndActivate() {
        Assert.assertFalse("Не должно быть активных", activityManager.hasActive());
        activityManager.addAndActivate(activity());
        Assert.assertTrue("Новый элемент активен", activityManager.hasActive());
        Assert.assertEquals("Активный элемент не последний UUID", lastUuid, activityManager.getActive().get().getUUID());
        Assert.assertTrue("Активный элемент не последний UUID", activityManager.get(lastUuid).get().isActive());
    }

    @Test
    public void releaseAll() {
        activityManager.add(activity());
        activityManager.add(activity());
        activityManager.releaseAll();
        Assert.assertEquals("Размер активити не 0", 0, activityManager.getAll().size());
        Assert.assertFalse("Есть активные", activityManager.hasActive());
        Assert.assertFalse("Есть предыдущий", activityManager.getPrev().isPresent());

    }

    @Test
    public void releaseActive() {
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
    public void activate() {
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
    public void getHasActive() {
        activityManager.add(activity());
        Assert.assertFalse(activityManager.getActive().isPresent());
        Assert.assertFalse(activityManager.hasActive());

        activityManager.get(lastUuid).get().activate();
        Assert.assertEquals(lastUuid, activityManager.getActive().get().getUUID());
        Assert.assertTrue(activityManager.hasActive());
    }

    @Test
    public void getPrev() {
        Activity activity = activity();
        activityManager.add(activity);
        Assert.assertFalse(activityManager.getPrev().isPresent());
        activity.activate();
        Assert.assertTrue(activityManager.getActive().get().isActive());
        activityManager.addAndActivate(activity());
        Assert.assertEquals(activity.getUUID(), activityManager.getPrev().get().getUUID());
        Assert.assertFalse(activityManager.getPrev().get().isActive());
    }

    @Test
    public void getAll() {
        Assert.assertEquals(3, activityManager.getAll().size());
    }

    @Test
    public void get() {
        Assert.assertEquals(lastUuid, activityManager.get(lastUuid).get().getUUID());
    }

    private Activity activity() {
        lastUuid = UUID.randomUUID();
        return new AbstractActivity(lastUuid) {
        };
    }

    private ActivityManager manager(List<Activity> activities) {
        return new AbstractActivityManager(activities) {
        };
    }
}