package com.blamejared.colcol.blocks;


import com.blamejared.colcol.reference.Reference;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.*;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.*;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

import java.io.*;
import java.util.*;

public class CCBlocks {
    
    public static final CreativeTabs TAB = new CreativeTabs("colcol") {
        
        @Override
        public ItemStack createIcon() {
            return new ItemStack(CCBlocks.BLOCKS.get(0).getBase());
        }
        
        @Override
        public void displayAllRelevantItems(NonNullList<ItemStack> list) {
            super.displayAllRelevantItems(list);
            list.sort(Comparator.comparing(ItemStack::getDisplayName));
        }
    };
    
    //The maps are for render and registration purposes.
    //The list is for recipe purposes.
    public static Map<String, Block> RENDER_MAP = new HashMap<>();
    public static Map<String, Block> BLOCK_LIST = new HashMap<>();
    public static List<BlockHolder> BLOCKS = new ArrayList<>();
    
    public static void registerBlocks(IForgeRegistry<Block> registry) {
        for(EnumDyeColor color : EnumDyeColor.values()) {
            //purpur exists!
            if(color == EnumDyeColor.PURPLE) {
                continue;
            }
            String name = color.getTranslationKey().substring(0, 3).toLowerCase();
            Block baseBlock = new Block(Material.ROCK, MapColor.BLOCK_COLORS[color.getDyeDamage()]).setHardness(1.5F).setResistance(10.0F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setTranslationKey(name + name + "_block");
            registerBlock(registry, baseBlock, name + name + "_block");
            
            Block pillar = new BlockColPillar(Material.ROCK, MapColor.BLOCK_COLORS[color.getDyeDamage()]).setHardness(1.5F).setResistance(10.0F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setTranslationKey(name + name + "_pillar");
            registerBlock(registry, pillar, name + name + "_pillar");
            
            Block stairs = new BlockColStairs(baseBlock.getDefaultState()).setTranslationKey(name + name + "_stairs");
            registerBlock(registry, stairs, name + name + "_stairs");
            
            BlockColcolSlab slab = (BlockColcolSlab) new BlockColcolSlab.Half(color).setHardness(2.0F).setResistance(10.0F).setTranslationKey(name + name + "_slab");
            registerBlock(registry, slab, name + name + "_slab", name + name);
            slab.setDropped(slab);
            
            BlockColcolSlab dSlab = (BlockColcolSlab) new BlockColcolSlab.Double(color).setHardness(2.0F).setResistance(10.0F).setTranslationKey(name + name + "_double_slab");
            registerBlock(registry, dSlab, name + name + "_double_slab", name + name);
            dSlab.setDropped(dSlab);
            
            BLOCKS.add(new BlockHolder(color, baseBlock, pillar, stairs, slab, dSlab));
        }
    }
    
    private static void registerBlock(IForgeRegistry<Block> registry, Block block, String key) {
        registerBlock(registry, block, key, key, null, TAB);
    }
    
    private static void registerBlock(IForgeRegistry<Block> registry, Block block, String key, String texture) {
        registerBlock(registry, block, key, texture, null, TAB);
    }
    
    
    private static void registerBlock(IForgeRegistry<Block> registry, Block block, String key, String texture, Class tile, CreativeTabs TAB) {
        block.setTranslationKey(key).setCreativeTab(TAB);
        if(Reference.DEVENV && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            writeFile(key, texture);
        RENDER_MAP.put(texture, block);
        block = block.setRegistryName(new ResourceLocation(Reference.MODID + ":" + key));
        registry.register(block);
        BLOCK_LIST.put(key, block);
        if(tile != null) {
            GameRegistry.registerTileEntity(tile, new ResourceLocation(Reference.MODID, key));
        }
    }
    
    public static void writeFile(String key, String texture) {
        try {
            File outputState = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.MODID + "/blockstates/" + key + ".json");
            File outputBlock = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.MODID + "/models/block/" + key + ".json");
            File outputItem = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.MODID + "/models/item/" + key + ".json");
            
            File baseState = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.MODID + "/templates/baseBlockState.json");
            File baseBlock = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.MODID + "/templates/baseBlockModel.json");
            File baseItem = new File(new File(System.getProperty("user.dir")).getParentFile(), "src/main/resources/assets/" + Reference.MODID + "/templates/baseBlockItem.json");
            
            if(!outputState.exists()) {
                outputState.createNewFile();
                Scanner scan = new Scanner(baseState);
                List<String> content = new ArrayList<>();
                while(scan.hasNextLine()) {
                    String line = scan.nextLine();
                    if(line.contains("%modid%")) {
                        line = line.replace("%modid%", Reference.MODID);
                    }
                    if(line.contains("%key%")) {
                        line = line.replace("%key%", key);
                    }
                    if(line.contains("%texture%")) {
                        line = line.replace("%texture%", texture);
                    }
                    content.add(line);
                }
                scan.close();
                FileWriter write = new FileWriter(outputState);
                for(String s : content) {
                    write.write(s + "\n");
                }
                write.close();
            }
            if(!outputBlock.exists()) {
                outputBlock.createNewFile();
                Scanner scan = new Scanner(baseBlock);
                List<String> content = new ArrayList<>();
                while(scan.hasNextLine()) {
                    String line = scan.nextLine();
                    if(line.contains("%modid%")) {
                        line = line.replace("%modid%", Reference.MODID);
                    }
                    if(line.contains("%key%")) {
                        line = line.replace("%key%", key);
                    }
                    if(line.contains("%texture%")) {
                        line = line.replace("%texture%", texture);
                    }
                    content.add(line);
                }
                scan.close();
                FileWriter write = new FileWriter(outputBlock);
                for(String s : content) {
                    write.write(s + "\n");
                }
                write.close();
            }
            
            if(!outputItem.exists()) {
                outputItem.createNewFile();
                Scanner scan = new Scanner(baseItem);
                List<String> content = new ArrayList<>();
                while(scan.hasNextLine()) {
                    String line = scan.nextLine();
                    if(line.contains("%modid%")) {
                        line = line.replace("%modid%", Reference.MODID);
                    }
                    if(line.contains("%key%")) {
                        line = line.replace("%key%", key);
                    }
                    if(line.contains("%texture%")) {
                        line = line.replace("%texture%", texture);
                    }
                    content.add(line);
                }
                scan.close();
                FileWriter write = new FileWriter(outputItem);
                for(String s : content) {
                    write.write(s + "\n");
                }
                write.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    @SubscribeEvent
    public void regBlocks(RegistryEvent.Register<Block> event) {
        registerBlocks(event.getRegistry());
    }
    
    @SubscribeEvent
    public void regItems(RegistryEvent.Register<Item> event) {
        BLOCK_LIST.forEach((key, val) -> {
            ItemBlock iBlock = new ItemBlock(val);
            if(!(val instanceof BlockColcolSlab)) {
                event.getRegistry().register(iBlock.setRegistryName(val.getRegistryName()));
            }
        });
        for(BlockHolder block : BLOCKS) {
            ItemBlock iBlock = (ItemBlock) new ItemSlab(block.getSlab(), block.getSlab(), block.getDoubleSlab()).setTranslationKey(block.getSlab().getTranslationKey());
            event.getRegistry().register(iBlock.setRegistryName(block.getSlab().getRegistryName()));
        }
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onModelRegistry(ModelRegistryEvent event) {
        for(Map.Entry<String, Block> entry : BLOCK_LIST.entrySet()) {
            Item item = Item.getItemFromBlock(entry.getValue());
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Reference.MODID + ":" + entry.getKey(), "inventory"));
            
        }
    }
    
    
    @SubscribeEvent
    public void onRegistryRegister(RegistryEvent.Register<IRecipe> event) {
        IForgeRegistry<IRecipe> registry = event.getRegistry();
        for(BlockHolder block : BLOCKS) {
            ResourceLocation name = new ResourceLocation(Reference.MODID, "recipe_" + block.getBase().getRegistryName().getPath());
            registry.register(new ShapedOreRecipe(null, new ItemStack(block.getBase(), 8), "xxx", "xpx", "xxx", 'p', new ItemStack(Items.DYE, 1, block.getColor().getDyeDamage()), 'x', new ItemStack(Blocks.PURPUR_BLOCK)).setRegistryName(name));
            name = new ResourceLocation(Reference.MODID, "recipe_pillar_" + block.getBase().getRegistryName().getPath());
            registry.register(new ShapedOreRecipe(null, new ItemStack(block.getPillar()), true, "x", "x", 'x', new ItemStack(block.getSlab())).setRegistryName(name));
            name = new ResourceLocation(Reference.MODID, "recipe_stair_" + block.getBase().getRegistryName().getPath());
            registry.register(new ShapedOreRecipe(null, new ItemStack(block.getStairs(), 4), true, "x  ", "xx ", "xxx", 'x', new ItemStack(block.getBase())).setRegistryName(name));
            name = new ResourceLocation(Reference.MODID, "recipe_slab_" + block.getBase().getRegistryName().getPath());
            registry.register(new ShapedOreRecipe(null, new ItemStack(block.getSlab(), 6), true, "xxx", 'x', new ItemStack(block.getBase())).setRegistryName(name));
        }
    }
    
    
    public static class BlockHolder {
        
        private final EnumDyeColor color;
        private final Block base;
        private final Block pillar;
        private final Block stairs;
        private final BlockSlab slab;
        private final BlockSlab doubleSlab;
        
        public BlockHolder(EnumDyeColor color, Block base, Block pillar, Block stairs, BlockSlab slab, BlockSlab doubleSlab) {
            this.color = color;
            this.base = base;
            this.pillar = pillar;
            this.stairs = stairs;
            this.slab = slab;
            this.doubleSlab = doubleSlab;
        }
        
        
        public EnumDyeColor getColor() {
            return color;
        }
        
        
        public Block getBase() {
            return base;
        }
        
        public Block getPillar() {
            return pillar;
        }
        
        
        public Block getStairs() {
            return stairs;
        }
        
        
        public BlockSlab getSlab() {
            return slab;
        }
        
        public BlockSlab getDoubleSlab() {
            return doubleSlab;
        }
        
        public List<Block> getBlocks() {
            return ImmutableList.of(getBase(), getPillar(), getStairs(), getSlab(), getDoubleSlab());
        }
        
    }
}
