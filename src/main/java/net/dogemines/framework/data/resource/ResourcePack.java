// Handles creation and uploading of the doge mines resource pack.
// 2023 - Piggy Gaming

package net.dogemines.framework.data.resource;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.dogemines.framework.DogeMinesFramework;
import net.dogemines.framework.block.BlockPredicate;
import net.dogemines.framework.block.CustomBlock;
import net.dogemines.framework.block.CustomBlockState;
import net.dogemines.framework.block.MultipleFacingPredicate;
import net.dogemines.framework.data.DogeFileUtils;
import net.dogemines.framework.data.registry.Registries;
import net.dogemines.framework.data.registry.RegistryObject;
import net.dogemines.framework.item.BlockItem;
import net.dogemines.framework.item.CustomItem;
import net.dogemines.framework.sound.CustomSound;
import net.dogemines.framework.sound.CustomSoundEvent;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.codehaus.plexus.util.FileUtils;
import org.jetbrains.annotations.ApiStatus;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Logger;

@ApiStatus.Internal
public class ResourcePack {

    private final HostingMethod hostingMethod;

    private File assets;
    private File zipfile;
    private byte[] sha1;

    public ResourcePack(HostingMethod hostingMethod) {
        this.hostingMethod = hostingMethod;
        generate();
    }

    public void generate() {
        JavaPlugin plugin = DogeMinesFramework.getInstance();
        Logger log = plugin.getLogger();
        File datafolder = plugin.getDataFolder();

        log.info("generating resource pack. please wait...");
        this.assets = new File(datafolder, "assets");

        File resourceoutput = new File(datafolder.getPath() + "/resourceoutput/pack");
        if (resourceoutput.exists()) {
            try {
                FileUtils.deleteDirectory(resourceoutput);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        resourceoutput.mkdir();

        try {
            generateInternal(resourceoutput);
        } catch (IOException e) {
            log.warning("could not generate resource pack");
            throw new RuntimeException(e);
        }

        log.info("done generating resource pack!");

        //host resource pack
        log.info("hosting resource pack...");
        hostingMethod.hostPack(this.zipfile);
    }

    public HostingMethod getHostingMethod() {
        return this.hostingMethod;
    }

    public File getFile() {
        return zipfile;
    }

    public byte[] getSha1() {
        return sha1;
    }

    private File copyFile(String path, File output) throws IOException {
        File file1 = new File(assets.getPath() + "/" + path);
        File file2 = new File(output.getPath() + "/" + path);
        if (file1.exists()) {
            FileUtils.copyFile(file1, file2);
            return file2;
        }
        else {
            return null;
        }
    }

    private static File newDirectory(File parent, String directory) {
        File newDir = new File(parent.getPath() + "/" + directory);
        newDir.mkdirs();
        return newDir;
    }

    private static File combinePaths(File parent, String path) {
        File file = new File(parent.getPath() + path);
        return file;
    }


    // source - https://github.com/oraxen/oraxen - pack.generation.PredicatesGenerator
    private final static String[] tools = new String[]{"PICKAXE", "SWORD", "HOE", "AXE", "SHOVEL"};
    public static String getParent(final Material material, boolean isBlock) {
        if (material.isBlock() && isBlock)
            return "block/cube_all";
        if (Arrays.stream(tools).anyMatch(tool -> material.toString().contains(tool)))
            return "item/handheld";
        if (material == Material.FISHING_ROD)
            return "item/handheld_rod";
        if (material == Material.SHIELD)
            return "builtin/entity";
        return "item/generated";
    }

    /*public static String getVanillaModelName(final Material material) {
        return getVanillaTextureName(material, true);
    }

    public static String getVanillaTextureName(final Material material, final boolean model) {
        if (!model)
            if (material.isBlock()) return "block/" + material.toString().toLowerCase(Locale.ENGLISH);
            else if (material == Material.CROSSBOW) return "item/crossbow_standby";
        return "item/" + material.toString().toLowerCase(Locale.ENGLISH);
    }*/
    //----------------------------------------------------------------------------------------------------


    private static boolean isBlock(RegistryObject<?> toCheck) {
        return toCheck.getValue() instanceof CustomBlock || toCheck.getValue() instanceof BlockItem;
    }
    public static String getDogeminesPath(RegistryObject<?> item) {
        return getDogeminesPath(item.getKey(), isBlock(item));
    }
    public static String getDogeminesPath(NamespacedKey itemId, boolean isBlock) {
        if (isBlock) {
            return itemId.getNamespace() + ":block/" + itemId.getKey();
        } else {
            return itemId.getNamespace() + ":item/" + itemId.getKey();
        }
    }

    public static String instrumentToVanillaName(Instrument instrument) {
        if (instrument.equals(Instrument.PIANO)) {
            return "harp";
        }
        else if (instrument.equals(Instrument.BASS_DRUM)) {
            return "basedrum";
        }
        else if (instrument.equals(Instrument.SNARE_DRUM)) {
            return "snare";
        }
        else if (instrument.equals(Instrument.STICKS)) {
            return "hat";
        }
        else if (instrument.equals(Instrument.BASS_GUITAR)) {
            return "bass";
        }
        else if (instrument.equals(Instrument.ZOMBIE)) {
            return "imitate.zombie";
        }
        else if (instrument.equals(Instrument.SKELETON)) {
            return "imitate.skeleton";
        }
        else if (instrument.equals(Instrument.CREEPER)) {
            return "imitate.creeper";
        }
        else if (instrument.equals(Instrument.DRAGON)) {
            return "imitate.dragon";
        }
        else if (instrument.equals(Instrument.WITHER_SKELETON)) {
            return "imitate.wither_skeleton";
        }
        else if (instrument.equals(Instrument.PIGLIN)) {
            return "imitate.piglin";
        }
        else if (instrument.equals(Instrument.CUSTOM_HEAD)) {
            return "";
        } else {
            return instrument.name().toLowerCase();
        }
    }

    private record NamespacedDirectoryStructure(File mainDir, File modelItemDir, File modelBlockDir, File itemsDir, File soundsFile) { }

    private void setupNamespacedDir(String namespace) throws IOException {
        File newMainDir = newDirectory(newAssets, namespace);
        FileUtils.copyDirectoryStructure(new File(assets, namespace), newMainDir);

        File newModelItemDir = newDirectory(newMainDir, "models/item");
        File newModelBlockDir = newDirectory(newMainDir, "models/block");
        File newItemsDir = newDirectory(newMainDir, "items");
        File soundsFile = new File(newMainDir, "sounds.json");

        namespacedDirs.put(namespace, new NamespacedDirectoryStructure(
                newMainDir,
                newModelItemDir,
                newModelBlockDir,
                newItemsDir,
                soundsFile
        ));
    }

    private File newAssets;
    private File newMinecraftDir;
    private final HashMap<String, NamespacedDirectoryStructure> namespacedDirs = new HashMap<>();

    private void generateInternal(File output) throws IOException {

        final Gson gsonbuild = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        this.newAssets = newDirectory(output, "assets");
        this.newMinecraftDir = newDirectory(newAssets, "minecraft");

        final File newMcMeta = copyFile("pack.mcmeta", output);
        final File newPackImage = copyFile("pack.png", output);
        copyFile("README.txt", output);

        FileUtils.copyDirectoryStructure(new File(assets, "minecraft"), newMinecraftDir);

        final File mcModelItemDir = newDirectory(newMinecraftDir, "models/item");
        final File blockstateDir = newDirectory(newMinecraftDir, "blockstates");

        final File fontDir = newDirectory(newMinecraftDir, "font");
        final File fontFile = new File(fontDir, "default.json");

        //setup namespaced directory
        for (String namespace : Registries.getAllNamespaces()) {
            setupNamespacedDir(namespace);
        }


        //-----------------------------
        //     vanilla item models
        //-----------------------------
        //Redundant since items can have custom model property without using CustomModelData.

        /*for (Map.Entry<Material, List<CustomItem>> entry : ItemRegistry.baseItemUses.entrySet()) {
            Material baseitem = entry.getKey();
            List<DogeItem> uses = entry.getValue();

            File modelFile = new File(mcModelItemDir, baseitem.filename().toLowerCase(Locale.ENGLISH) + ".json");


            //create a json object for the baseitem
            JsonObject modeljson = new JsonObject();


            //parent
            modeljson.addProperty("parent", getParent(baseitem));

            //textures
            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", getVanillaTextureName(baseitem, false));
            modeljson.add("textures", textures);

            //loop through item uses and add to overrides object
            JsonArray overrides = new JsonArray();
            for (DogeItem item : uses) {
                JsonObject predicate = new JsonObject();

                JsonObject predicateProperties = new JsonObject();
                predicateProperties.addProperty("custom_model_data", item.CustomModelData);
                predicate.add("predicate", predicateProperties);

                predicate.addProperty("model", getDogeminesPath(item));

                overrides.add(predicate);
            }
            modeljson.add("overrides", overrides);

            if (modelFile.createNewFile()) {
                FileUtils.fileWrite(modelFile, gsonbuild.toJson(modeljson));
            } else {
                DogeMines.getPlugin(DogeMines.class).getLogger().warning("error when generating resource pack: unable to create file for " + baseitem.toString());
            }

        }*/


        //-----------------------
        //  item texture models
        //-----------------------
        for (RegistryObject<CustomItem> itemEntry : Registries.ITEM.getPairs()) {
            CustomItem customItem = itemEntry.getValue();
            if (customItem.hasModel()) {
                boolean isBlock = isBlock(itemEntry);

                //TODO: old code, adapt to new BasicModel class
                File modelFile;
                String fileName = itemEntry.getId().toLowerCase(Locale.ENGLISH) + ".json";

                JsonObject modelJSON = new JsonObject();

                //parent
                modelJSON.addProperty("parent", getParent(customItem.getMaterial(), isBlock));

                //textures
                JsonObject textures = new JsonObject();

                NamespacedDirectoryStructure namespacedDir = namespacedDirs.get(itemEntry.getNamespace());

                if (isBlock) {
                    modelFile = new File(namespacedDir.modelBlockDir, fileName);
                    textures.addProperty("all", getDogeminesPath(itemEntry));
                } else {
                    modelFile = new File(namespacedDir.modelItemDir, fileName);
                    textures.addProperty("layer0", getDogeminesPath(itemEntry));
                }
                modelJSON.add("textures", textures);

                if (customItem instanceof CustomItem.modelMiddleware modelMiddleware) {
                    modelMiddleware.modifyModelJSON(modelJSON);
                }

                if (modelFile.createNewFile()) {
                    FileUtils.fileWrite(modelFile, gsonbuild.toJson(modelJSON));
                }

                //file in items directory
                //--------------------------------------------
                File itemFile = new File(namespacedDir.itemsDir, fileName);
                JsonObject itemJSON = new JsonObject();

                //model
                JsonObject model = new JsonObject();

                model.addProperty("type", "minecraft:model");
                model.addProperty("model", getDogeminesPath(itemEntry));

                itemJSON.add("model", model);

                if (customItem instanceof CustomItem.itemMiddleware itemMiddleware) {
                    itemMiddleware.modifyItemJSON(itemJSON);
                }
                if (itemFile.createNewFile()) {
                    FileUtils.fileWrite(itemFile, gsonbuild.toJson(itemJSON));
                }

            }

        }

        //---------------------------
        //   blockstate predicates
        //---------------------------
        final Map<Material, JsonObject> BLOCKSTATE_JSON = new HashMap<>();
        final Map<Material, JsonArray> MULTIPART_JSON = new HashMap<>();
        final Map<Material, File> BLOCKSTATE_FILES = new HashMap<>();

        //fill maps
        for (Material material : MultipleFacingPredicate.TYPES) {
            JsonObject blockstate = new JsonObject();
            JsonArray multipart = new JsonArray();
            blockstate.add("multipart", multipart);

            BLOCKSTATE_JSON.put(material, blockstate);
            MULTIPART_JSON.put(material, multipart);
            BLOCKSTATE_FILES.put(material, new File(blockstateDir, material.name().toLowerCase() + ".json"));
        }


        //create json
        for (RegistryObject<CustomBlock> blockObject : Registries.BLOCK.getPairs()) {
            CustomBlock block = blockObject.getValue();

            for (CustomBlockState blockState : block.getBlockStates()) {
                BlockPredicate predicate = blockState.getPredicate();

                MULTIPART_JSON.get(predicate.getMaterial())
                        .add(blockState.getJson(blockObject.getKey()));

            }
        }


        //write files
        for (Map.Entry<Material, File> entry : BLOCKSTATE_FILES.entrySet()) {
            FileUtils.fileWrite(
                    entry.getValue(),
                    gsonbuild.toJson(BLOCKSTATE_JSON.get(entry.getKey()))
            );
        }

        //------------------
        //   sounds.json
        //------------------
        final HashMap<String, JsonObject> soundsJsons = new HashMap<>();
        for (String namespace : Registries.getAllNamespaces()) {
            soundsJsons.put(namespace, new JsonObject());
        }

        for (RegistryObject<CustomSoundEvent> sound : Registries.SOUND_EVENT.getPairs()) {
            JsonObject soundsJson = soundsJsons.get(sound.getNamespace());

            CustomSoundEvent soundEvent = sound.getValue();
            JsonArray soundEventArray = new JsonArray();

            for (CustomSound customSound : soundEvent.getSounds()) {
                soundEventArray.add(customSound.generateSoundObject());
            }

            JsonObject soundEventJson = new JsonObject();
            soundEventJson.add("sounds", soundEventArray);
            soundsJson.add(sound.getId(), soundEventJson);
        }

        for (Map.Entry<String, JsonObject> entry : soundsJsons.entrySet()) {
            NamespacedDirectoryStructure namespacedDir = namespacedDirs.get(entry.getKey());
            FileUtils.fileWrite(namespacedDir.soundsFile, gsonbuild.toJson(entry.getValue()));
        }


        //--------------------------
        //   custom unicode chars
        //--------------------------
        final JsonObject fontJson = new JsonObject();
        final JsonArray providers = new JsonArray();

        for (RegistryObject<UnicodeChar> charObject : Registries.UNICODE_CHAR.getPairs()) {
            final UnicodeChar unicodeChar = charObject.getValue();
            if (unicodeChar instanceof BitmapUnicodeChar bitmapChar) {
                providers.add(bitmapChar.getJson(charObject.getNamespace()));
            }
        }
        //add entire space advance block
        providers.add(BitmapUnicodeChar.SpaceAdvance.getEntireJson());

        fontJson.add("providers", providers);
        FileUtils.fileWrite(fontFile, gsonbuild.toJson(fontJson)
                .replace("\\\\u", "\\u")); //because gson escapes all unicode characters, we need to do some post-processing on the json string


        //------------------
        //   zipping pack
        //------------------

        File zos = new File(output.getParentFile(), "dogemines-resources.zip");
        DogeFileUtils.zip(output, zos);
        zipfile = zos;

        //calculate sha1 of zip file
        try {
            String sha1String = DogeFileUtils.calcSHA1(zipfile);
            sha1 = BaseEncoding.base16().decode(sha1String.toUpperCase());
        } catch (NoSuchAlgorithmException e) {
            DogeMinesFramework.getInstance().getLogger().warning(e.toString());
        }
    }


}
