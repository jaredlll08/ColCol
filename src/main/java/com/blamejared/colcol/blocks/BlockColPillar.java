package com.blamejared.colcol.blocks;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.*;

public class BlockColPillar extends BlockRotatedPillar {
    
    protected BlockColPillar(Material materialIn) {
        super(materialIn);
    }
    
    protected BlockColPillar(Material materialIn, MapColor color) {
        super(materialIn, color);
    }
}
