package cn.davickk.rdi.engine.mixin;

import cn.davickk.rdi.engine.utils.FontUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.EarlyLoaderGUI;
import net.minecraftforge.fml.loading.progress.StartupMessageManager;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL31;
import org.lwjgl.stb.STBEasyFont;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.nio.ByteBuffer;
import java.util.List;

@Mixin(value = EarlyLoaderGUI.class,priority = Integer.MAX_VALUE)
public class EarlyLoaderGUIMixin {
@Shadow
private MainWindow window;
private long windowId;
private final FontRenderer fontRenderer=new FontUtils().createFont();
private final MatrixStack matrixStack=new MatrixStack();

    /**
     * @author
     */

    @Overwrite(remap = false)
    private void renderBackground() {
        GL11.glBegin(GL11.GL_QUADS);
        //深灰色的
        GL11.glColor4f(64F / 255F, 64F / 255F, 64F / 255F, 0F / 255F);
        GL11.glVertex3f(0, 0, -10);
        GL11.glVertex3f(0, window.getGuiScaledHeight(), -10);
        GL11.glVertex3f(window.getGuiScaledWidth(), window.getGuiScaledHeight(), -10);
        GL11.glVertex3f(window.getGuiScaledWidth(), 0, -10);
        GL11.glEnd();
        windowId=window.getWindow();

    }

    /**
     * @author
     */
    @Overwrite(remap = false)
    private void renderMessages() {
        List<Pair<Integer, StartupMessageManager.Message>> messages = StartupMessageManager.getMessages();
        for (int i = 0; i < messages.size(); i++) {
            boolean nofade = i == 0;
            final Pair<Integer, StartupMessageManager.Message> pair = messages.get(i);
            /*final float fade = MathHelper.clamp((4000.0f - (float) pair.getLeft() - ( i - 4 ) * 1000.0f) / 5000.0f, 0.0f, 1.0f);
            if (fade <0.01f && !nofade) continue;*/
            StartupMessageManager.Message msg = pair.getRight();
            renderMessage(msg.getText(), msg.getTypeColour(), ((window.getGuiScaledHeight() - 15) / 10) - i + 1, /*nofade ? */1.0f /*: fade*/);
        }
        renderMemoryInfo();
    }
    private static final float[] memorycolour = new float[] { 255f, 255f, 255f};

    private void renderMemoryInfo() {
        final MemoryUsage heapusage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        final MemoryUsage offheapusage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
        final float pctmemory = (float) heapusage.getUsed() / heapusage.getMax();
        String memory = String.format("RAM %d / %dMB (%.1f%%) ", heapusage.getUsed() >> 20, heapusage.getMax() >> 20, pctmemory * 100.0);

        /*final int i = MathHelper.hsvToRgb((1.0f - (float)Math.pow(pctmemory, 1.5f)) / 3f, 1.0f, 0.5f);
        memorycolour[2] = ((i) & 0xFF) / 255.0f;
        memorycolour[1] = ((i >> 8 ) & 0xFF) / 255.0f;
        memorycolour[0] = ((i >> 16 ) & 0xFF) / 255.0f;*/
        renderMessage(memory, memorycolour, 1, 1.0f);
        //GLFW.glfwSetWindowTitle(windowId,"RDI Libe Engine/正在启动 Forge模组加载器");
    }

    @SuppressWarnings("deprecation")
    void renderMessage(final String message, final float[] colour, int line, float alpha) {
        GlStateManager._enableClientState(GL11.GL_VERTEX_ARRAY);
        ByteBuffer charBuffer = MemoryUtil.memAlloc(message.length() * 270);
        int quads = STBEasyFont.stb_easy_font_print(0, 0, message, null, charBuffer);
        GL14.glVertexPointer(2, GL11.GL_FLOAT, 16, charBuffer);

        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        // STBEasyFont's quads are in reverse order or what OGGL expects, so it gets culled for facing the wrong way.
        // So Disable culling https://github.com/MinecraftForge/MinecraftForge/pull/6824
        RenderSystem.disableCull();
        GL14.glBlendColor(0,0,0, alpha);
        RenderSystem.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
        RenderSystem.color3f(colour[0],colour[1],colour[2]);
        RenderSystem.pushMatrix();
        RenderSystem.translatef(10, line * 10, 0);
        RenderSystem.scalef(1, 1, 0);
        RenderSystem.drawArrays(GL11.GL_QUADS, 0, quads * 4);
        RenderSystem.popMatrix();

        RenderSystem.enableCull();
        GlStateManager._disableClientState(GL11.GL_VERTEX_ARRAY);
        MemoryUtil.memFree(charBuffer);
    }
  /*  @ModifyConstant(method = "renderMemoryInfo",remap = false,constant =
    @Constant(stringValue = "Memory Heap: %d / %d MB (%.1f%%)  OffHeap: %d MB"))
    private String memory(String old){
        return "Mem %d / %dMB (%.1f%%) , %d MB free";
    }
*/
    //red green blue alpha
    /*@ModifyArgs(method = "renderTick", remap = false, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clearColor(FFFF)V"))
    private void setBackgroundColor(Args args) {
        float r = (HookUtils.backColor >> 16 & 0xff)/255f;
        float g = (HookUtils.backColor >> 8 & 0xff)/255f;
        float b = (HookUtils.backColor & 0xff)/255f;
        args.setAll(r,g,b,args.get(3));
    }*/
}