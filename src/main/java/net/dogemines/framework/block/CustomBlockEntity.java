package net.dogemines.framework.block;

import java.util.List;

public class CustomBlockEntity extends CustomBlock {
    public CustomBlockEntity(List<CustomBlockState> blockstates, int blockStrength) {
        super(blockstates, blockStrength);
    }
    public CustomBlockEntity(CustomBlockState defaultState, int blockStrength) {
        super(defaultState, blockStrength);
    }
}
