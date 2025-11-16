package net.dogemines.framework.data.database;

import net.dogemines.framework.DogeMinesFramework;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.HashSet;

public class SqlCache<T extends SqlRow> {
    private final HashMap<String, T> cache = new HashMap<>();
    private final GenericDao<T> dao;
    private final SqlTable<T> table;
    private final Runnable saveCallback;

    public static final HashSet<SqlCache<?>> CACHES = new HashSet<>();

    private SqlCache(GenericDao<T> dao, Runnable saveCallback) {
        this.dao = dao;
        this.table = dao.getTable();
        this.saveCallback = saveCallback;

        CACHES.add(this);
    }

    //put a row into the cache by either inserting into db or selecting, if already exists.
    public void initData(T row, String primaryKey) {
        DogeMinesFramework.runTaskAsynchronously(() -> {
            dao.insertAllFields(row);
            dao.selectWherePrimaryKey(primaryKey, row);

            cache.put(primaryKey, row);
        });
    }

    //sync saving methods
    public void saveSync(String key) {
        T data = cache.get(key);
        if (data != null) {
            dao.updateByPrimaryKey(data);
        }
    }
    public void saveAllSync() {
        if (!cache.isEmpty()) {
            dao.updateBatch(cache.values(), table.getAllMutableFields(), table.getPrimaryKey());

            if (saveCallback != null) {
                saveCallback.run();
            }
        }
    }

    //async wrappers
    public void saveAllAsync() {
        DogeMinesFramework.runTaskAsynchronously(this::saveAllSync);
    }
    public void saveAsync(String key) {
        DogeMinesFramework.runTaskAsynchronously(() -> saveSync(key));
    }


    //saves to db and removes a row from the cache
    public void cleanup(String key) {
        if (cache.containsKey(key)) {
            saveAsync(key);
            cache.remove(key);
        }
    }

    public T get(String key) {
        return cache.get(key);
    }

    HashMap<String, T> getMap() {
        return cache;
    }

    public static class Builder<T extends SqlRow> {
        private final GenericDao<T> dao;

        private Runnable saveCallback;
        private int autosaveDelay;

        public Builder(GenericDao<T> dao) {
            this.dao = dao;
        }

        public Builder<T> setAutosaveWithCallback(Runnable callback, int delay) {
            saveCallback = callback;
            autosaveDelay = delay;
            return this;
        }
        public Builder<T> setAutosave(int delay) {
            autosaveDelay = delay;
            return this;
        }

        public SqlCache<T> build() {
            SqlCache<T> cache = new SqlCache<>(dao, saveCallback);

            if (autosaveDelay != 0) {
                //schedule autosave
                Bukkit.getScheduler().scheduleSyncRepeatingTask(
                        DogeMinesFramework.getInstance(), cache::saveAllAsync, autosaveDelay, autosaveDelay
                );
            }

            return cache;
        }
    }

}
