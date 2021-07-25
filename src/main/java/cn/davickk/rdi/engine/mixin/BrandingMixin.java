package cn.davickk.rdi.engine.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.BrandingControl;
import net.minecraftforge.fml.ForgeI18n;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.versions.forge.ForgeVersion;
import net.minecraftforge.versions.mcp.MCPVersion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(BrandingControl.class)
public class BrandingMixin {
    @Shadow
    private static List<String> brandings;
    @Shadow
    private static List<String> brandingsNoMC;
    @Shadow
    private static List<String> overCopyrightBrandings;
    /**
     * @author
     */
    @Overwrite
    private static void computeBranding()
    {
        if (brandings == null)
        {
            ImmutableList.Builder<String> brd = ImmutableList.builder();
            int tModCount = ModList.get().size();
            brd.add(MCPVersion.getMCVersion()+"/" + ForgeVersion.getVersion()+"/"+tModCount+"Mods");
            brandings = brd.build();
            brandingsNoMC = brandings.subList(1, brandings.size());
        }
    }
    /**
     * @author
     */
    @Overwrite
    public static String getClientBranding() {
        return "RDI Headquarter & Forge";
    }
    /**
     * @author
     */
    @Overwrite
    public static String getServerBranding() {
        return "RDI Headquarter";
    }

}
