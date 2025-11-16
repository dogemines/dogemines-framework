package net.dogemines.framework.block;

import com.google.gson.annotations.Expose;
import net.dogemines.framework.data.resource.ResourcePack;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.NoteBlock;
import org.jetbrains.annotations.NotNull;

public class NoteblockBlockPredicate implements BlockPredicate {

    private static final Material MATERIAL = Material.NOTE_BLOCK;

    private final NoteBlock blockData;
    @Expose private final String instrument;
    @Expose private final int note;
    @Expose private final boolean powered;

    public NoteblockBlockPredicate(Instrument bukkitInstrument, int note, boolean powered) {
        this.powered = powered;
        this.instrument = ResourcePack.instrumentToVanillaName(bukkitInstrument);
        this.note = note;

        blockData = (NoteBlock) MATERIAL.createBlockData();
        blockData.setNote(new Note(note));
        blockData.setInstrument(bukkitInstrument);
        blockData.setPowered(powered);
    }

    @Override
    public void setBlock(@NotNull Block block) {
        block.setBlockData(blockData, false);
    }

    @Override
    public BlockData getBlockData() {
        return blockData;
    }

    @Override
    public Material getMaterial() {
        return MATERIAL;
    }

}
