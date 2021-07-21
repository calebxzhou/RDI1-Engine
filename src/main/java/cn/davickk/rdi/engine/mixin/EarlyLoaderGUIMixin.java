package cn.davickk.rdi.engine.mixin;

import cn.davickk.rdi.engine.utils.HookUtils;
import net.minecraftforge.fml.client.EarlyLoaderGUI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = EarlyLoaderGUI.class,priority = 9999)
public class EarlyLoaderGUIMixin {

    //red green blue alpha
    @ModifyArgs(method = "renderTick", remap = false, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clearColor(FFFF)V"))
    private void setBackgroundColor(Args args) {
        float r = (HookUtils.backColor >> 16 & 0xff)/255f;
        float g = (HookUtils.backColor >> 8 & 0xff)/255f;
        float b = (HookUtils.backColor & 0xff)/255f;
        args.setAll(r,g,b,args.get(3));
    }
}