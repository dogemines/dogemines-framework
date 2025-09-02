package net.dogemines.framework.item;

import net.dogemines.framework.block.CustomBlock;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class ToolItem extends CustomItem {
    private final int miningSpeed;
    private final int breakingPower;
    private final Type toolType;

    public ToolItem(Material material, Component name, ItemMetaDecorator metaDecorator, int miningSpeed, int breakingPower, Type toolType) {
        super(material, name, metaDecorator);
        this.miningSpeed = miningSpeed;
        this.breakingPower = breakingPower;
        this.toolType = toolType;
    }
    public ToolItem(Material material, Component name, int miningSpeed, int breakingPower, Type toolType) {
        this(material, name, ItemMetaDecorator.DEFAULT, miningSpeed, breakingPower, toolType);
    }

    public int getMiningSpeed(boolean isHindered) {
        if (isHindered) {
            return miningSpeed / 5;
        }
        return miningSpeed;
    }

    public int getTicksToMine(int blockStrength, boolean isHindered) {
        int ticks = Math.round((float) (30 * blockStrength) / (getMiningSpeed(isHindered)));
        return Math.max(ticks, 4);
    }

    public boolean canMine(CustomBlock block) {
        return breakingPower >= 5;
    }

    public static enum Type {
        ALL,
        PICKAXE,
        AXE,
        SHOVEL
    }
}
