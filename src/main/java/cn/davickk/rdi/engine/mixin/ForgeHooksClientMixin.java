package cn.davickk.rdi.engine.mixin;

import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.loading.progress.StartupMessageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Set;

@Mixin(ForgeHooksClient.class)
public class ForgeHooksClientMixin {
    /**
     * @author
     */
    @Overwrite(remap = false)
    public static void onTextureStitchedPre(AtlasTexture map, Set<ResourceLocation> resourceLocations)
    {
        StartupMessageManager.mcLoaderConsumer().ifPresent(c->c.accept("load special block "+map.location().toString()));
        ModLoader.get().postEvent(new TextureStitchEvent.Pre(map, resourceLocations));
        Atlases.SIGN_MATERIALS.values().stream()
                .filter(rm -> rm.atlasLocation().equals(map.location()))
                .forEach(rm -> resourceLocations.add(rm.texture()));
    }
}
