// Handles creation and uploading of the doge mines resource pack.
// 2023 - Piggy Gaming

package net.dogemines.framework.data;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.dogemines.framework.DogeMinesFramework;
import net.dogemines.framework.block.CustomBlock;
import net.dogemines.framework.data.registry.Registries;
import net.dogemines.framework.data.registry.RegistryObject;
import net.dogemines.framework.item.BlockItem;
import net.dogemines.framework.item.CustomItem;
import net.dogemines.framework.sound.CustomSound;
import net.dogemines.framework.sound.CustomSoundEvent;
import org.bukkit.Instrument;
import org.bukkit.Material;
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

    private final File assets;
    private File zipfile;
    private byte[] sha1;

    public ResourcePack() {
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
            generate(resourceoutput);
        } catch (IOException e) {
            log.warning("could not generate resource pack");
            throw new RuntimeException(e);
        }

        log.info("done generating resource pack!");
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
        FileUtils.copyFile(file1, file2);
        return file2;
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
    private static String getParent(final Material material, boolean isItem) {
        if (material.isBlock() && !isItem)
            return "block/cube_all";
        if (Arrays.stream(tools).anyMatch(tool -> material.toString().contains(tool)))
            return "item/handheld";
        if (material == Material.FISHING_ROD)
            return "item/handheld_rod";
        if (material == Material.SHIELD)
            return "builtin/entity";
        return "item/generated";
    }

    private static String getVanillaModelName(final Material material) {
        return getVanillaTextureName(material, true);
    }

    private static String getVanillaTextureName(final Material material, final boolean model) {
        if (!model)
            if (material.isBlock()) return "block/" + material.toString().toLowerCase(Locale.ENGLISH);
            else if (material == Material.CROSSBOW) return "item/crossbow_standby";
        return "item/" + material.toString().toLowerCase(Locale.ENGLISH);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private static String getDogeminesPath(RegistryObject<?> item) {
        if (item.getValue() instanceof CustomBlock) {
            return "dogemines:block/" + item.getId().toLowerCase(Locale.ENGLISH);
        } else {
            return "dogemines:item/" + item.getId().toLowerCase(Locale.ENGLISH);
        }
    }

    public static String instrumentToVanillaName(Instrument instrument) {
        if (instrument.equals(Instrument.PIANO)) {
            return "harp";
        }
        if (instrument.equals(Instrument.BASS_DRUM)) {
            return "basedrum";
        }
        if (instrument.equals(Instrument.SNARE_DRUM)) {
            return "snare";
        }
        if (instrument.equals(Instrument.STICKS)) {
            return "hat";
        }
        if (instrument.equals(Instrument.BASS_GUITAR)) {
            return "bass";
        }
        if (instrument.equals(Instrument.ZOMBIE)) {
            return "imitate.zombie";
        }
        if (instrument.equals(Instrument.SKELETON)) {
            return "imitate.skeleton";
        }
        if (instrument.equals(Instrument.CREEPER)) {
            return "imitate.creeper";
        }
        if (instrument.equals(Instrument.DRAGON)) {
            return "imitate.dragon";
        }
        if (instrument.equals(Instrument.WITHER_SKELETON)) {
            return "imitate.wither_skeleton";
        }
        if (instrument.equals(Instrument.PIGLIN)) {
            return "imitate.piglin";
        }
        if (instrument.equals(Instrument.CUSTOM_HEAD)) {
            return "";
        } else {
            return instrument.name().toLowerCase();
        }
    }

    public File generate(File output) throws IOException {

        Gson gsonbuild = new GsonBuilder().setPrettyPrinting().create();

        File newAssets = newDirectory(output, "assets");
        File newMinecraftDir = newDirectory(newAssets, "minecraft");
        File newDogeDirectory = newDirectory(newAssets, "dogemines");

        File newMcMeta = copyFile("pack.mcmeta", output);
        File newPackImage = copyFile("pack.png", output);

        FileUtils.copyDirectoryStructure(new File(assets, "minecraft"), newMinecraftDir);
        FileUtils.copyDirectoryStructure(new File(assets, "dogemines"), newDogeDirectory);

        File mcModelItemDir = newDirectory(newMinecraftDir, "models/item");

        File dogeModelItemDir = newDirectory(newDogeDirectory, "models/item");
        File dogeModelBlockDir = newDirectory(newDogeDirectory, "models/block");

        File dogeItemsDir = newDirectory(newDogeDirectory, "items");

        File blockstateDir = newDirectory(newMinecraftDir, "blockstates");


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
            if (customItem.getSettings().hasModel()) {

                File modelFile;
                String fileName = itemEntry.getId().toLowerCase(Locale.ENGLISH) + ".json";

                JsonObject modelJSON = new JsonObject();

                //parent
                modelJSON.addProperty("parent", getParent(customItem.getMaterial(), true));

                //textures
                JsonObject textures = new JsonObject();

                if (customItem instanceof BlockItem) {
                    modelFile = new File(dogeModelBlockDir, fileName);
                    textures.addProperty("all", getDogeminesPath(itemEntry));
                } else {
                    modelFile = new File(dogeModelItemDir, fileName);
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
                File itemFile = new File(dogeItemsDir, fileName);
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

        JsonObject mushroomJson = new JsonObject();
        JsonObject noteblockJson = new JsonObject();

        //create child JsonObjects for both parents
        JsonArray multipart_mushroom = new JsonArray();
        JsonArray multipart_noteblock = new JsonArray();

        //file objects
        File mushroomFile = new File(blockstateDir, "brown_mushroom_block.json");
        File noteblockFile = new File(blockstateDir, "note_block.json");

        //loop through all blocks
        /*for (DogeBlock block : ItemRegistry.blocks.values()) {

            //brown_mushroom_block
            if (block.type == DogeBlock.CustomBlockType.brownMushroom) {

                JsonObject data = new JsonObject();
                JsonObject when = new JsonObject();

                when.addProperty("down", block.down);
                when.addProperty("east", block.east);
                when.addProperty("north", block.north);
                when.addProperty("south", block.south);
                when.addProperty("up", block.up);
                when.addProperty("west", block.west);

                data.add("when", when);

                JsonObject apply = new JsonObject();
                apply.addProperty("model", getDogeminesPath(block.DogeBlockItem));
                data.add("apply", apply);

                multipart_mushroom.add(data);

            }

            //note_block
            if (block.type == DogeBlock.CustomBlockType.noteblock) {

                JsonObject data = new JsonObject();
                JsonObject when = new JsonObject();

                when.addProperty("instrument", instrumentToVanillaName(block.instrument));
                when.addProperty("note", block.note);
                when.addProperty("powered", block.powered);

                data.add("when", when);

                JsonObject apply = new JsonObject();
                apply.addProperty("model", getDogeminesPath(block.DogeBlockItem));
                data.add("apply", apply);

                multipart_noteblock.add(data);

            }


        }

        //add the multipart json to the main json object
        mushroomJson.add("multipart", multipart_mushroom);
        noteblockJson.add("multipart", multipart_noteblock);

        //create the mushroom block file
        if (mushroomFile.createNewFile()) {
            FileUtils.fileWrite(mushroomFile, gsonbuild.toJson(mushroomJson));
        } else {
            DogeMines.getPlugin(DogeMines.class).getLogger().warning("error when generating resource pack: unable to create blockstates file for mushroom blocks");
        }

        //create the note block file
        if (noteblockFile.createNewFile()) {
            FileUtils.fileWrite(noteblockFile, gsonbuild.toJson(noteblockJson));
        } else {
            DogeMines.getPlugin(DogeMines.class).getLogger().warning("error when generating resource pack: unable to create blockstates file for note blocks");
        }*/

        //------------------
        //   sounds.json
        //------------------
        File soundsFile = new File(newMinecraftDir, "sounds.json");
        JsonObject soundsJson = new JsonObject();

        for (RegistryObject<CustomSoundEvent> sound : Registries.SOUND_EVENTS.getPairs()) {
            CustomSoundEvent soundEvent = sound.getValue();
            JsonArray soundEventArray = new JsonArray();

            for (CustomSound customSound : soundEvent.getSounds()) {
                soundEventArray.add(customSound.generateSoundObject());
            }

            JsonObject soundEventJson = new JsonObject();
            soundEventJson.add("sounds", soundEventArray);
            soundsJson.add(sound.getId(), soundEventJson);
        }

        FileUtils.fileWrite(soundsFile, gsonbuild.toJson(soundsJson));


        //------------------
        //   zipping pack
        //------------------

        File zos = new File(output.getParentFile(), "dogemines-rp-" + DogeMinesFramework.VERSION + ".zip");
        DogeFileUtils.zip(output, zos);
        zipfile = zos;

        //calculate sha1 of zip file
        try {
            String sha1String = DogeFileUtils.calcSHA1(zipfile);
            sha1 = BaseEncoding.base16().decode(sha1String.toUpperCase());
        } catch (NoSuchAlgorithmException e) {
            DogeMinesFramework.getInstance().getLogger().warning(e.toString());
        }
        return zipfile;
    }


}
