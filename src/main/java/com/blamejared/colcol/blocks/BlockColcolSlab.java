package com.blamejared.colcol.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public abstract class BlockColcolSlab extends BlockSlab {
    
    public static final PropertyEnum<BlockColcolSlab.Variant> VARIANT = PropertyEnum.<BlockColcolSlab.Variant> create("variant", BlockColcolSlab.Variant.class);
    
    public Block dropped;
    
    public BlockColcolSlab(EnumDyeColor colour) {
        super(Material.ROCK, MapColor.getBlockColor(colour));
        IBlockState iblockstate = this.blockState.getBaseState();
        
        if(!this.isDouble()) {
            iblockstate = iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        }
        
        this.setDefaultState(iblockstate.withProperty(VARIANT, BlockColcolSlab.Variant.DEFAULT));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setLightOpacity(255);
        useNeighborBrightness = true;
    }
    
    /**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(dropped);
    }
    
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(dropped);
    }
    
    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, BlockColcolSlab.Variant.DEFAULT);
        
        if(!this.isDouble()) {
            iblockstate = iblockstate.withProperty(HALF, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }
        
        return iblockstate;
    }
    
    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        
        if(!this.isDouble() && state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP) {
            i |= 8;
        }
        
        return i;
    }
    
    public void setDropped(Block dropped) {
        this.dropped = dropped;
    }
    
    protected BlockStateContainer createBlockState() {
        return this.isDouble() ? new BlockStateContainer(this, VARIANT) : new BlockStateContainer(this, HALF, VARIANT);
    }
    
    /**
     * Returns the slab block name with the type associated with it
     */
    public String getTranslationKey(int meta) {
        return super.getTranslationKey();
    }
    
    public IProperty<?> getVariantProperty() {
        return VARIANT;
    }
    
    public Comparable<?> getTypeForItem(ItemStack stack) {
        return BlockColcolSlab.Variant.DEFAULT;
    }
    
    public static class Double extends BlockColcolSlab {
        
        public Double(EnumDyeColor colour) {
            super(colour);
        }
        
        public boolean isDouble() {
            return true;
        }
    }
    
    public static class Half extends BlockColcolSlab {
        
        public Half(EnumDyeColor colour) {
            super(colour);
        }
        
        public boolean isDouble() {
            return false;
        }
    }
    
    public enum Variant implements IStringSerializable {
        DEFAULT;
        
        public String getName() {
            return "default";
        }
    }
}