package cn.davickk.rdi.engine.mixin;

import cn.davickk.rdi.engine.utils.HookUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.ResourceLoadProgressGui;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static net.minecraft.client.gui.AbstractGui.fill;
@Mixin(value = ResourceLoadProgressGui.class, priority = 9999)
public class NoRedLoadingScreenMixin{
    @Shadow
    private float currentProgress;

    @Inject(method = "drawProgressBar",at = @At("HEAD"),cancellable = true)
    private void customBar(MatrixStack ms, int minX, int minY, int maxX, int maxY, float p_228181_5_, CallbackInfo ci){
        ci.cancel();
        int length = MathHelper.ceil((maxX - minX - 1) * this.currentProgress);
        fill(ms,minX - 1, minY - 1, maxX + 1, maxY + 1,HookUtils.getBarBackgroundColor(currentProgress));
        fill(ms,minX, minY, maxX, maxY, HookUtils.backColor);
        fill(ms,minX + 1, minY + 1, minX + length, maxY - 1, HookUtils.getProgressColor(currentProgress));
    }

    @ModifyArg(method = "render",at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/ResourceLoadProgressGui;fill(Lcom/mojang/blaze3d/matrix/MatrixStack;IIIII)V"),index = 5)
    private int backgroundColor1(int old){
        return HookUtils.backColor | old<<24;
    }

    @ModifyArgs(method = "render",at = @At(value = "INVOKE",target = "Lcom/mojang/blaze3d/systems/RenderSystem;color4f(FFFF)V"))
    private void logo(Args args){
        float r = (HookUtils.logoColor >> 16 & 0xff)/255f;
        float g = (HookUtils.logoColor >> 8 & 0xff)/255f;
        float b = (HookUtils.logoColor & 0xff)/255f;
        args.setAll(r,g,b,args.get(3));
    }
}
