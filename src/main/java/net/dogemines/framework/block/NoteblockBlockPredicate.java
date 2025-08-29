package net.dogemines.framework.block;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.dogemines.framework.data.ResourcePack;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;

public record NoteblockBlockPredicate(
        String instrument,
        int note,
        boolean powered

) implements CustomBlock.BlockPredicate {
    public NoteblockBlockPredicate(Instrument instrument, int note, boolean powered) {
        this(ResourcePack.instrumentToVanillaName(instrument), note, powered);
    }

    @Override
    public JsonObject getJson() {
        return new Gson().toJsonTree(this).getAsJsonObject();
    }

    @Override
    public void setBlock(Block block) {
        block.setType(Material.NOTE_BLOCK, false);
        NoteBlock noteblock = (NoteBlock) block.getBlockData();

        noteblock.setInstrument(Instrument.valueOf(instrument));
        noteblock.setNote(new Note(this.note));
        noteblock.setPowered(this.powered);

        block.setBlockData(noteblock, false);
    }
}
