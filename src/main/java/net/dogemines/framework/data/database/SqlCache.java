package net.dogemines.framework.data.database;

import net.dogemines.framework.DogeMinesFramework;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.HashSet;

public class SqlCache<T extends SqlRow> {
    private final HashMap<String, T> cache = new HashMap<>();
    private final GenericDao<T> dao;

    public static final HashSet<SqlCache<?>> CACHES = new HashSet<>();

    public SqlCache(GenericDao<T> dao) {
        this.dao = dao;
        CACHES.add(this);
    }

    public SqlCache<T> setAutosave(Runnable callback, int delay) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(DogeMinesFramework.getInstance(), () -> {
            saveAll(callback);
        }, delay, delay);

        return this;
    }

    public void initData(T data, String primaryKey) {
        DogeMinesFramework.runTaskAsynchronously(() -> {
            dao.insertAllFields(data);
            dao.selectWherePrimaryKey(primaryKey, data);

            cache.put(primaryKey, data);
        });
    }
    public void save(String key) {
        T data = cache.get(key);
        if (data != null) {
            DogeMinesFramework.runTaskAsynchronously(() -> {
                dao.updateByPrimaryKey(data);
            });
        }
    }
    public void saveAll(Runnable callback) {
        DogeMinesFramework.runTaskAsynchronously(() -> {
            saveAllSync();
            if (callback != null) {
                callback.run();
            }
        });
    }
    public void saveAllSync() {
        if (!cache.isEmpty()) {
            dao.updateBatch(this);
        }
    }

    public void cleanup(String key) {
        if (cache.containsKey(key)) {
            save(key);
            cache.remove(key);
        }
    }

    public T get(String key) {
        return cache.get(key);
    }

    HashMap<String, T> getMap() {
        return cache;
    }

}
