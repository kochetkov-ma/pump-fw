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
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("WeakerAccess")
@Slf4j
public abstract class AbstractActivityManager implements ActivityManager, ActivityListener {

    public static final String CLOSE_EVENT_NAME = "close";

    public static final String ACTIVATE_EVENT_NAME = "activate";

    public static final String DISABLE_EVENT_NAME = "disable";

    private final Map<UUID, Activity> activityMap;

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
        final Map<UUID, Activity> helperMap = Maps.newHashMap(activityMap);
        for (UUID uuid : helperMap.keySet()) {
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
    public ActivityManager release(UUID uuid) {
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
    public Optional<Activity> get(@NotNull UUID uuid) {
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

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof NamedEvent && o instanceof Activity) {
            final NamedEvent namedEvent = (NamedEvent) arg;
            final Activity activity = (Activity) o;

            if (CLOSE_EVENT_NAME.equals(namedEvent.getName())) {
                onClose(namedEvent, activity);
            } else if (ACTIVATE_EVENT_NAME.equals(namedEvent.getName())) {
                onActivate(namedEvent, activity);
            } else if (DISABLE_EVENT_NAME.equals(namedEvent.getName())) {
                onDisable(namedEvent, activity);
            }
        }
    }

    protected boolean isTargetActivity(Activity activity) {
        return activityClass.stream().anyMatch(item -> item.isAssignableFrom(activity.getClass()));
    }

    //region PRIVATE M
    private Optional<Activity> getAndCache(@NotNull UUID uuid) {
        return Optional.ofNullable(activityMap.computeIfPresent(uuid, (key, value) -> {
            cache = value;
            return value;
        }));
    }

    private void clearAllCache(UUID uuid) {
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

    private boolean inActiveCache(UUID uuid) {
        return activeCache != null && uuid.equals(activeCache.getUUID());
    }

    private boolean inCache(UUID uuid) {
        return cache != null && uuid.equals(cache.getUUID());
    }

    private boolean inPrevCache(UUID uuid) {
        return prevCache != null && uuid.equals(prevCache.getUUID());
    }
    //endregion
}
