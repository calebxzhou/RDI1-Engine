package cn.davickk.rdi.engine.mixin;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.loading.progress.StartupMessageManager;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.GameData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(GameData.class)
public class GameDataMixin {
    /**
     * @author
     */
    @Overwrite(remap = false)
    public static CompletableFuture<List<Throwable>> preRegistryEventDispatch(final Executor executor, final ModLoadingStage.EventGenerator<? extends RegistryEvent.Register<?>> eventGenerator) {
        return CompletableFuture.runAsync(()-> {
            final RegistryEvent.Register<?> event = eventGenerator.apply(null);
            final ResourceLocation rl = event.getName();
            ForgeRegistry<?> fr = (ForgeRegistry<?>) event.getRegistry();
            StartupMessageManager.modLoaderConsumer().ifPresent(s -> s.accept("add block " + rl));
            fr.unfreeze();
        }, executor).thenApply(v-> Collections.emptyList());
    }
}
