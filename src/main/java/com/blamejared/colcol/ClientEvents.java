package com.blamejared.colcol;

import com.blamejared.colcol.client.textures.ColColTexture;
import com.blamejared.colcol.reference.Reference;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientEvents {
    
    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent event) {
        TextureMap map = event.getMap();
        for(EnumDyeColor color : EnumDyeColor.values()) {
            String name = color.getTranslationKey().substring(0, 3).toLowerCase();
            String key = name + name + "_block";
            map.setTextureEntry(new ColColTexture(new ResourceLocation("blocks/purpur_block"), new ResourceLocation(Reference.MODID, "blocks/" + key), color.getColorValue()));
            map.setTextureEntry(new ColColTexture(new ResourceLocation("blocks/purpur_pillar"), new ResourceLocation(Reference.MODID, "blocks/" + name + name + "_pillar"), color.getColorValue()));
            map.setTextureEntry(new ColColTexture(new ResourceLocation("blocks/purpur_pillar_top"), new ResourceLocation(Reference.MODID, "blocks/" + name + name + "_pillar_top"), color.getColorValue()));
        }
    }
    
}
