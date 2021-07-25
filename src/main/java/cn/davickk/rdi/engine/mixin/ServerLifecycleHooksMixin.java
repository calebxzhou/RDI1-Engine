package cn.davickk.rdi.engine.mixin;

import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ServerLifecycleHooks.class)
public class ServerLifecycleHooksMixin {

    /**
     * @author
     * @reason ±¿¿£≤ªÕÀ”Œœ∑
     */
    @Overwrite
    public static void handleExit(int retVal)
    {
        System.out.println(retVal);
    }
}
