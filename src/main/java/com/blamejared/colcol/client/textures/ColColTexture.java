package com.blamejared.colcol.client.textures;

import com.google.common.collect.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.Collection;
import java.util.function.Function;

public class ColColTexture extends TextureAtlasSprite {
    
    private final ResourceLocation texDep;
    private final int colour;
    
    public ColColTexture(ResourceLocation textureDependency, ResourceLocation spriteName, int colour) {
        super(spriteName.toString());
        this.texDep = textureDependency;
        this.colour = colour;
    }
    
    @Override
    public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
        return true;
    }
    
    @Override
    public boolean load(IResourceManager manager, ResourceLocation location, Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
        final TextureAtlasSprite sprite = textureGetter.apply(texDep);
        copyFrom(sprite);
        this.framesTextureData = Lists.newArrayList();
        for(int i = 0; i < sprite.getFrameCount(); i++) {
            final int[][] textureFrom = sprite.getFrameTextureData(i).clone();
            int[][] textureTo = new int[textureFrom.length][];
            for(int j = 0; j < textureFrom.length; j++) {
                textureTo[j] = new int[textureFrom[j].length];
                System.arraycopy(textureFrom[j], 0, textureTo[j], 0, textureFrom[j].length);
            }
            this.framesTextureData.add(i, textureTo);
        }
        
        for(int[][] texture : framesTextureData) {
            for(int l = 0; l < texture.length; l++) {
                for(int j = 0; j < texture[l].length; j++) {
                    Color color = new Color(colour);
                    Color texCol = new Color(texture[l][j]);
                    int gLevel = (texCol.getRed() + texCol.getGreen() + texCol.getBlue()) / 3;
                    Color gray = new Color((gLevel << 16) + (gLevel << 8) + gLevel);
                    float r = (gray.getRed() / 255f) * (color.getRed() / 255f);
                    float g = (gray.getGreen() / 255f) * (color.getGreen() / 255f);
                    float b = (gray.getBlue() / 255f) * (color.getBlue() / 255f);
                    texture[l][j] = new Color(r, g, b, 1).getRGB();
                }
            }
        }
        
        return false;
    }
    
    @Override
    public Collection<ResourceLocation> getDependencies() {
        return ImmutableList.of(texDep);
    }
}