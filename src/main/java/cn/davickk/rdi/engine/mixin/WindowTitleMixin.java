package cn.davickk.rdi.engine.mixin;

import cn.davickk.rdi.engine.music.BgmTracks;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Minecraft.class)
public class WindowTitleMixin {
    @Overwrite
    private String createTitle(){


        return "RDI Celestial Technology 2.94";
    }
}
