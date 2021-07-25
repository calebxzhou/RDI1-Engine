package cn.davickk.rdi.engine.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.ForgeI18n;
import net.minecraftforge.fml.client.ClientHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Arrays;
import java.util.stream.Collectors;

@Mixin(ClientHooks.class)
public class ClientHooksMixin {
    @Shadow
    private static ResourceLocation iconSheet;
    /**
     * @author
     * @reason 去掉服务器界面那个红X，永远都是绿勾
     */
    @Overwrite
    public static void drawForgePingInfo(MultiplayerScreen gui, ServerData target, MatrixStack mStack, int x, int y, int width, int relativeMouseX, int relativeMouseY) {
        int idx;
        String tooltip;
        if (target.forgeData == null)
            return;
        idx = 0;
        tooltip = ForgeI18n.parseMessage("fml.menu.multiplayer.compatible", target.forgeData.numberOfMods);
        Minecraft.getInstance().getTextureManager().bind(iconSheet);
        AbstractGui.blit(mStack, x + width - 18, y + 10, 16, 16, 0, idx, 16, 16, 256, 256);

        if(relativeMouseX > width - 15 && relativeMouseX < width && relativeMouseY > 10 && relativeMouseY < 26) {
            //this is not the most proper way to do it,
            //but works best here and has the least maintenance overhead
            gui.setToolTip(Arrays.stream(tooltip.split("\n")).map(StringTextComponent::new).collect(Collectors.toList()));
        }
    }
}
