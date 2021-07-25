package cn.davickk.rdi.engine.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LoadingGui;
import net.minecraft.client.gui.ResourceLoadProgressGui;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.resources.IAsyncReloader;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL40;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL14C.*;
import static org.lwjgl.opengl.GL11C.*;
@Mixin(ResourceLoadProgressGui.class)
public class ResLoadProgressGuiMixin extends LoadingGui {
    @Shadow
    private Minecraft minecraft;
    private long windowId=minecraft.getWindow().getWindow();
    @Shadow
    private float currentProgress;
    @Shadow
    private  IAsyncReloader reload;
    @Shadow
    private  Consumer<Optional<Throwable>> onFinish;


    private static final int BRAND_BACKGROUND = ColorHelper.PackedColor.color(0, 64, 64, 64);
    private static final int BRAND_BACKGROUND_NO_ALPHA = BRAND_BACKGROUND & 16777215;
    private static final ResourceLocation MOJANG_STUDIOS_LOGO_LOCATION = new ResourceLocation("rdiengine:image/loadscreen-black.png");

    private boolean fadeIn=false;
    private long fadeOutStart = -1L;
    private long fadeInStart = -1L;
    /**
     * @author
     */
    @Override
    @Overwrite
    public void render(MatrixStack matrixStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        InputStream imageStream = getClass().getClassLoader().getResourceAsStream("assets/rdiengine/image/loadscreen-black.png");

        int guiScaledWidth = this.minecraft.getWindow().getGuiScaledWidth();
        int guiScaledHeight = this.minecraft.getWindow().getGuiScaledHeight();
        long millis = Util.getMillis();
        if (this.fadeIn && (this.reload.isApplying() || this.minecraft.screen != null) && this.fadeInStart == -1L) {
            this.fadeInStart = millis;
        }

        float f = this.fadeOutStart > -1L ? (float)(millis - this.fadeOutStart) / 1000.0F : -1.0F;
        float f1 = this.fadeInStart > -1L ? (float)(millis - this.fadeInStart) / 500.0F : -1.0F;
        float f2;
        if (f >= 1.0F) {
            if (this.minecraft.screen != null) {
                this.minecraft.screen.render(matrixStack, 0, 0, p_230430_4_);
            }

            int l = MathHelper.ceil((1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F)) * 255.0F);
            fill(matrixStack, 0, 0, guiScaledWidth, guiScaledHeight, BRAND_BACKGROUND_NO_ALPHA | l << 24);
            f2 = 1.0F - MathHelper.clamp(f - 1.0F, 0.0F, 1.0F);
        } else if (this.fadeIn) {
            if (this.minecraft.screen != null && f1 < 1.0F) {
                this.minecraft.screen.render(matrixStack, p_230430_2_, p_230430_3_, p_230430_4_);
            }

            int i2 = MathHelper.ceil(MathHelper.clamp((double)f1, 0.15D, 1.0D) * 255.0D);
            fill(matrixStack, 0, 0, guiScaledWidth, guiScaledHeight, BRAND_BACKGROUND_NO_ALPHA | i2 << 24);
            f2 = MathHelper.clamp(f1, 0.0F, 1.0F);
        } else {
            fill(matrixStack, 0, 0, guiScaledWidth, guiScaledHeight, BRAND_BACKGROUND);
            f2 = 1.0F;
        }

        int halfWidth = (int)((double)this.minecraft.getWindow().getGuiScaledWidth() * 0.5D);
        int halfHeight = (int)((double)this.minecraft.getWindow().getGuiScaledHeight() * 0.5D);
        double d0 = Math.min((double)this.minecraft.getWindow().getGuiScaledWidth() * 0.75D, (double)this.minecraft.getWindow().getGuiScaledHeight()) * 0.25D;
        int j1 = (int)(d0 * 0.5D);
        double d1 = d0 * 4.0D;
        int k1 = (int)(d1 * 0.5D);

        this.minecraft.getTextureManager().bind(MOJANG_STUDIOS_LOGO_LOCATION);
        RenderSystem.enableBlend();
        RenderSystem.blendEquation(GL_FUNC_ADD);
        RenderSystem.blendFunc(GL_SRC_ALPHA, 1);
        RenderSystem.alphaFunc(516, 0.0F);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, f2);
        blit(matrixStack, halfWidth - k1, halfHeight - j1, k1, (int)d0, -0.0625F, 0.0F, 30, 60, 30, 30);
        //blit(matrixStack, halfWidth, halfHeight - j1, k1, (int)d0, 0.0625F, 60.0F, 120, 60, 120, 120);
        //RenderSystem.defaultBlendFunc();
        //RenderSystem.defaultAlphaFunc();
        RenderSystem.disableBlend();
        int l1 = (int)((double)this.minecraft.getWindow().getGuiScaledHeight() * 0.8325D);
        float f3 = this.reload.getActualProgress();
        this.currentProgress = MathHelper.clamp(this.currentProgress * 0.95F + f3 * 0.050000012F, 0.0F, 1.0F);
        net.minecraftforge.fml.client.ClientModLoader.renderProgressText();
        if (f < 1.0F) {
            this.drawProgressBar(matrixStack,
                    guiScaledWidth / 2 - k1,
                    l1 - 5,
                    guiScaledWidth / 2 + k1,
                    l1 + 5,
                    1.0F - MathHelper.clamp(f, 0.0F, 1.0F));
        }

        if (f >= 2.0F) {
            this.minecraft.setOverlay((LoadingGui)null);
        }

        if (this.fadeOutStart == -1L && this.reload.isDone() && (!this.fadeIn || f1 >= 2.0F)) {
            this.fadeOutStart = Util.getMillis(); // Moved up to guard against inf loops caused by callback
            try {
                this.reload.checkExceptions();
                this.onFinish.accept(Optional.empty());
            } catch (Throwable throwable) {
                this.onFinish.accept(Optional.of(throwable));
            }

            if (this.minecraft.screen != null) {
                this.minecraft.screen.init(this.minecraft, this.minecraft.getWindow().getGuiScaledWidth(), this.minecraft.getWindow().getGuiScaledHeight());
            }
        }
    }
    private void drawProgressBar(MatrixStack matrixStack, int p_238629_2_, int p_238629_3_, int p_238629_4_, int p_238629_5_, float p_238629_6_) {
        int i = MathHelper.ceil((float)(p_238629_4_ - p_238629_2_ - 2) * this.currentProgress);
        int j = Math.round(p_238629_6_ * 255.0F);
        int k = ColorHelper.PackedColor.color(j, 255, 255, 255);
        fill(matrixStack, p_238629_2_ + 1, p_238629_3_, p_238629_4_ - 1, p_238629_3_ + 1, k);
        fill(matrixStack, p_238629_2_ + 1, p_238629_5_, p_238629_4_ - 1, p_238629_5_ - 1, k);
        fill(matrixStack, p_238629_2_, p_238629_3_, p_238629_2_ + 1, p_238629_5_, k);
        fill(matrixStack, p_238629_4_, p_238629_3_, p_238629_4_ - 1, p_238629_5_, k);
        fill(matrixStack, p_238629_2_ + 2, p_238629_3_ + 2, p_238629_2_ + i, p_238629_5_ - 2, k);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    @Mixin(MojangLogoTexture.class)
    static class MojangLogoTexture extends SimpleTexture {
        public MojangLogoTexture() {
            super(ResLoadProgressGuiMixin.MOJANG_STUDIOS_LOGO_LOCATION);
        }

        /**
         * @author
         */
        @Override
        @Overwrite
        protected SimpleTexture.TextureData getTextureImage(IResourceManager p_215246_1_) {
            try (InputStream inputstream = getClass().getClassLoader().getResourceAsStream("assets/rdiengine/image/loadscreen-black.png")) {
                return new SimpleTexture.TextureData(new TextureMetadataSection(true, true), NativeImage.read(inputstream));
            } catch (IOException ioexception) {
                return new SimpleTexture.TextureData(ioexception);
            }
        }
    }
}
