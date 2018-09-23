package com.blamejared.colcol.reference;

import net.minecraft.launchwrapper.Launch;

public class Reference {
    
    public static final String MODID = "colcol";
    public static final String NAME = "ColCol";
    public static final String VERSION = "1.0.0";
    
    public static boolean DEVENV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    
}
