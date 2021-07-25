package cn.davickk.rdi.engine.utils;

import cn.davickk.rdi.engine.RDIEngine;
import net.minecraft.util.ResourceLocation;

public class ResourceLocationUtils {
    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(RDIEngine.MOD_ID, path);
    }
}
