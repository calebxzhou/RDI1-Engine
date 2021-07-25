package cn.davickk.rdi.engine.mixin;

import cn.davickk.rdi.engine.music.BgmTracks;
import cn.davickk.rdi.engine.screen.SupportUsScreen;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.RenderSkybox;
import net.minecraft.client.renderer.RenderSkyboxCube;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridgeScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(MainMenuScreen.class)
public class TitleScreenMixin extends Screen {
    private static final RenderSkyboxCube CUBE = new RenderSkyboxCube(new ResourceLocation("rdiengine:panorama/"));
    private final RenderSkybox panorama = new RenderSkybox(CUBE);

    private final String right="RDI Liberation Engine 1.0 / Game by Mojang";
    @Shadow
    private boolean fading;
    @Shadow
    private long fadeInStart;
    @Shadow
    private static ResourceLocation PANORAMA_OVERLAY;
    @Shadow
    private static ResourceLocation MINECRAFT_LOGO;
    @Shadow
    private static ResourceLocation MINECRAFT_EDITION;
    @Shadow
    private int copyrightWidth;
    @Shadow
    private int copyrightX;
    protected TitleScreenMixin(ITextComponent p_i51108_1_) {
        super(p_i51108_1_);
    }


    /**
     * @author
     * @reason 更换全景图片和左下角右下角
     */
    @Override
    @Overwrite
    public void render(MatrixStack matrixStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        if (this.fadeInStart == 0L && this.fading) {
            this.fadeInStart = Util.getMillis();
        }

        float f = this.fading ? (float)(Util.getMillis() - this.fadeInStart) / 1000.0F : 1.0F;
        fill(matrixStack, 0, 0, this.width, this.height, -1);
        //下面第一个参数是全景图片旋转速度
        this.panorama.render(2, MathHelper.clamp(f, 0.0F, 1.0F));
        int i = 274;
        int j = this.width / 2 - 137;
        int k = 30;
        this.minecraft.getTextureManager().bind(PANORAMA_OVERLAY);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.fading ? (float)MathHelper.ceil(MathHelper.clamp(f, 0.0F, 1.0F)) : 1.0F);
        blit(matrixStack, 0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);
        float f1 = this.fading ? MathHelper.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;
        int l = MathHelper.ceil(f1 * 255.0F) << 24;
        if ((l & -67108864) != 0) {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, f1);
            blit(matrixStack, j + 88, 67, 0.0F, 0.0F, 98, 14, 128, 16);
            net.minecraftforge.client.ForgeHooksClient.renderMainMenu((MainMenuScreen) (Object)this, matrixStack, this.font, this.width, this.height, l);
            net.minecraftforge.fml.BrandingControl.forEachLine(true, true, (brdline, brd) ->
                    drawString(matrixStack, this.font, brd, 2, this.height - ( 10 + brdline * (this.font.lineHeight + 1)), 16777215 | l)
            );

            net.minecraftforge.fml.BrandingControl.forEachAboveCopyrightLine((brdline, brd) ->
                    drawString(matrixStack, this.font, brd, this.width - font.width(brd), this.height - (10 + (brdline + 1) * ( this.font.lineHeight + 1)), 16777215 | l)
            );
            //像素栈，字体font renderer，内容字符串，x，y，
            drawString(matrixStack, this.font, right, this.copyrightX, this.height - 10, 16777215 | l);
            if (p_230430_2_ > this.copyrightX && p_230430_2_ < this.copyrightX + this.copyrightWidth && p_230430_3_ > this.height - 10 && p_230430_3_ < this.height) {
                fill(matrixStack, this.copyrightX, this.height - 1, this.copyrightX + this.copyrightWidth, this.height, 16777215 | l);
            }

            for(Widget widget : this.buttons) {
                widget.setAlpha(f1);
            }

            super.render(matrixStack, p_230430_2_, p_230430_3_, p_230430_4_);

        }
    }

    /**
     * @author
     * @reason 更换领域按钮为赞助rdi按钮
     */
    @Overwrite
    private void realmsButtonClicked() {
        Minecraft.getInstance().setScreen(new SupportUsScreen());
    }
    @ModifyConstant(method = "init",constant =
    @Constant(stringValue =                         "Copyright Mojang AB. Do not distribute!"))
    private String changerightwidth(String old) {
        return right;
    }
/*
    @ModifyConstant(method = "render",constant =
    @Constant(stringValue =                         "Copyright Mojang AB. Do not distribute!"))
    private String changeright(String old) {
        return right;
    }*/
/*
    @ModifyConstant(method = "init",remap = false,constant =
    @Constant(stringValue = "fml.menu.mods"))
    private String changeMods(String old) {
        return "模组列表";
    }*/

    @ModifyConstant(method = "createNormalMenuOptions",constant =
    @Constant(stringValue = "menu.online"))
    private String changeRealmsButton(String old) {
        return "支持RDI运营";
    }

    @ModifyConstant(method = "createNormalMenuOptions",constant =
    @Constant(stringValue = "menu.multiplayer"))
    private String changeMPButton(String old) {
        return "连接服务器";
    }
    /*@Inject(method = "init", at = @At("HEAD"))
    private void onInit(CallbackInfo cbi){
        int j = this.height / 4 + 48;
        this.addButton(new Button(this.width / 2 - 20, j + 72 + 12,
                98, 20, new TranslationTextComponent("支持RDI维持营运"), (p_213096_1_) -> {
            this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
        }));


    }*/
}
