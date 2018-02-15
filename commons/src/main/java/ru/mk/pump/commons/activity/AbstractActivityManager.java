package ru.mk.pump.commons.activity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Optional;
import java.util.Set;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("WeakerAccess")
@Slf4j
public abstract class AbstractActivityManager implements ActivityManager, ActivityListener {

    private final Map<String, Activity> activityMap;

    private Activity activeCache = null;

    private Activity prevCache = null;

    private Activity cache = null;

    private Set<Class<? extends Activity>> activityClass;

    protected AbstractActivityManager() {
        this(Lists.newArrayList());
    }

    protected AbstractActivityManager(List<Activity> activityList) {
        activityMap = Maps.newHashMap();
        //noinspection unchecked
        activityClass = Sets.newHashSet(Activity.class);
        if (activityList != null) {
            activityList.forEach(this::add);
        }
    }

    @SuppressWarnings("unchecked")
    protected void setFilterActivityClass(Class<? extends Activity>... activityClass) {
        this.activityClass = Sets.newHashSet(activityClass);
    }

    @Override
    public ActivityManager add(Activity activity) {
        if (activity instanceof Observable) {
            ((Observable) activity).addObserver(this);
        }
        if (isTargetActivity(activity)) {
            cache = activityMap.put(activity.getUUID(), activity);
        }
        return this;
    }

    @Override
    public ActivityManager addIfNotContains(Activity activity) {
        if (isTargetActivity(activity)) {
            if (!activityMap.containsKey(activity.getUUID())) {
                add(activity);
            }
        }
        return this;
    }

    @Override
    public Activity addAndActivate(Activity activity) {
        if (isTargetActivity(activity)) {
            add(activity);
            return activity.activate();
        } else {
            return activity;
        }
    }

    @Override
    public ActivityManager releaseAll() {
        final Map<String, Activity> helperMap = Maps.newHashMap(activityMap);
        for (String uuid : helperMap.keySet()) {
            release(uuid);
        }
        return this;
    }

    @Override
    public ActivityManager releaseActive() {
        getActive().ifPresent(item -> {
            try {
                item.close();
            } catch (IOException e) {
                log.warn("Release error on activity {} with uuid {}", item.getUUID(), item.getClass().getSimpleName());
            }
        });
        return this;
    }

    @Override
    public ActivityManager release(String uuid) {
        get(uuid).ifPresent(item -> {
            try {
                item.close();
            } catch (IOException e) {
                log.warn("Release error on activity {} with uuid {}", item.getUUID(), item.getClass().getSimpleName());
            }
        });
        return this;
    }

    @Override
    public boolean hasActive() {
        return getActive().isPresent();
    }

    @Override
    public Optional<Activity> getActive() {
        if (activeCache != null) {
            return Optional.of(activeCache);
        } else {
            final Optional<Activity> result = getAll().stream().filter(Activity::isActive).findFirst();
            result.ifPresent(item -> cache = item);
            return result;
        }
    }

    @Override
    public Optional<Activity> getPrev() {
        return Optional.ofNullable(prevCache);
    }

    @Override
    public List<Activity> getAll() {
        return Lists.newArrayList(activityMap.values());
    }

    @Override
    public Optional<Activity> get(@NonNull String uuid) {
        if (inCache(uuid)) {
            return Optional.of(cache);
        } else {
            return getAndCache(uuid);
        }
    }

    @Override
    public void onClose(NamedEvent namedEvent, Activity activity) {
        if (isTargetActivity(activity)) {
            clearAllCache(activity.getUUID());
            activityMap.remove(activity.getUUID());
        }
    }

    @Override
    public void onActivate(NamedEvent namedEvent, Activity activity) {
        if (isTargetActivity(activity)) {
            if (!inActiveCache(activity.getUUID()) && activeCache != null) {
                prevCache = activeCache;
                prevCache.disable();
            }
            activeCache = activity;
        }
    }

    @Override
    public void onDisable(NamedEvent namedEvent, Activity activity) {
        if (isTargetActivity(activity)) {
            if (inActiveCache(activity.getUUID())) {
                prevCache = activity;
                activeCache = null;
            }
        }
    }

    protected boolean isTargetActivity(Activity activity) {
        return activityClass.stream().anyMatch(item -> item.isAssignableFrom(activity.getClass()));
    }

    //region PRIVATE M
    private Optional<Activity> getAndCache(@NonNull String uuid) {
        return Optional.ofNullable(activityMap.computeIfPresent(uuid, (key, value) -> {
            cache = value;
            return value;
        }));
    }

    private void clearAllCache(String uuid) {
        if (inActiveCache(uuid)) {
            activeCache = prevCache;
        }
        if (inCache(uuid)) {
            cache = null;
        }
        if (inPrevCache(uuid)) {
            prevCache = null;
        }
    }

    private boolean inActiveCache(String uuid) {
        return activeCache != null && uuid.equals(activeCache.getUUID());
    }

    private boolean inCache(String uuid) {
        return cache != null && uuid.equals(cache.getUUID());
    }

    private boolean inPrevCache(String uuid) {
        return prevCache != null && uuid.equals(prevCache.getUUID());
    }
    //endregion
}
