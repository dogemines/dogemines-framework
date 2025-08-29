// Various methods to help with handling files
// 2023 - Piggy Gaming

package net.dogemines.framework.data;

import com.google.common.io.BaseEncoding;
import net.dogemines.framework.DogeMinesFramework;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.google.common.io.Files.copy;

public class DogeFileUtils {

    private static final JavaPlugin plugin = DogeMinesFramework.getInstance();

    public static void saveInt(String key, Integer value, String file) {

        File configFile = new File(plugin.getDataFolder(), file);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(file, false);
        }

        FileConfiguration config = new YamlConfiguration();
        config = YamlConfiguration.loadConfiguration(configFile);

        config.set(key, value);

        try {
            config.save(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static Integer loadInt(String key, String file) {

        File configFile = new File(plugin.getDataFolder(), file);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(file, false);
        }

        FileConfiguration config = new YamlConfiguration();
        config = YamlConfiguration.loadConfiguration(configFile);

        int value = (int) config.get(key);

        return value;

    }

    public static void saveStr(String key, String value, String file) {

        File configFile = new File(plugin.getDataFolder(), file);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(file, false);
        }

        FileConfiguration config = new YamlConfiguration();
        config = YamlConfiguration.loadConfiguration(configFile);

        config.set(key, value);

        try {
            config.save(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static String loadStr(String key, String file) {

        File configFile = new File(plugin.getDataFolder(), file);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(file, false);
        }

        FileConfiguration config = new YamlConfiguration();
        config = YamlConfiguration.loadConfiguration(configFile);

        String value = (String) config.get(key);

        return value;

    }

    // Source - https://stackoverflow.com/questions/1399126 ///////////////////////////////
    public static void zip(File directory, File zipfile) throws IOException {
        //delete if already exists (overwrite output)
        if (zipfile.exists()) {
            zipfile.delete();
        }

        URI base = directory.toURI();
        Deque<File> queue = new LinkedList<File>();
        queue.push(directory);
        OutputStream out = new FileOutputStream(zipfile);
        Closeable res = out;
        try {
            ZipOutputStream zout = new ZipOutputStream(out);
            res = zout;
            while (!queue.isEmpty()) {
                directory = queue.pop();
                for (File kid : directory.listFiles()) {
                    String name = base.relativize(kid.toURI()).getPath();
                    if (kid.isDirectory()) {
                        queue.push(kid);
                        name = name.endsWith("/") ? name : name + "/";
                        zout.putNextEntry(new ZipEntry(name));
                    } else {
                        zout.putNextEntry(new ZipEntry(name));
                        copy(kid, zout);
                        zout.closeEntry();
                    }
                }
            }
        } finally {
            res.close();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    public static String calcSHA1(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        try (InputStream input = new FileInputStream(file)) {

            byte[] buffer = new byte[8192];
            int len = input.read(buffer);

            while (len != -1) {
                sha1.update(buffer, 0, len);
                len = input.read(buffer);
            }

            return BaseEncoding.base16().encode(sha1.digest());
        }
    }

}