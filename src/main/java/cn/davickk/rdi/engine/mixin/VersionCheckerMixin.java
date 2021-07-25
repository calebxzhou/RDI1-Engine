package cn.davickk.rdi.engine.mixin;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

@Mixin(VersionChecker.class)
public class VersionCheckerMixin {
    /**
     * @author
     * @reason 干掉sb更新
     */
    @Overwrite
    public static void startVersionCheck()
    {

    }
    /**
     * @author
     * @reason 干掉sb更新
     */
    @Overwrite
    private static List<IModInfo> gatherMods()
    {
        return new LinkedList<>();
    }
}
