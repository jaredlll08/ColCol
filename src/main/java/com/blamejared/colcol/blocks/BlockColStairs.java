package com.blamejared.colcol.blocks;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;

public class BlockColStairs extends BlockStairs {
    
    public BlockColStairs(IBlockState modelState) {
        super(modelState);
        this.setLightOpacity(255);
        useNeighborBrightness = true;
    }
    
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    
    /**
     * @deprecated call via {@link IBlockState#isFullCube()} whenever possible. Implementing/overriding is fine.
     */
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
}
