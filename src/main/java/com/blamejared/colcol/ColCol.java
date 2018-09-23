package com.blamejared.colcol;

import com.blamejared.colcol.blocks.CCBlocks;
import com.blamejared.colcol.reference.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.*;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class ColCol {
    
    @Mod.EventHandler
    public void onFMLPreInitialization(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new CCBlocks());
    }
    
    @SideOnly(Side.CLIENT)
    @Mod.EventHandler
    public void onClientFMLPreInitialization(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
    }
    
}
