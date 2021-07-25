package cn.davickk.rdi.engine.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

@Mixin(MainWindow.class)
public class MainWindowMixin {
    @Shadow private static Logger LOGGER;
    @Shadow private long window;
    /**
     * @reason 更换游戏图标为rdi logo
     * @author
     */
    @Overwrite
    public void setIcon(InputStream x16icon, InputStream x32icon) {
        RenderSystem.assertThread(RenderSystem::isInInitPhase);
        InputStream new16icon =  getClass().getClassLoader().getResourceAsStream("assets/rdiengine/icon/rdi-icon16.png");
        InputStream new32icon =getClass().getClassLoader().getResourceAsStream("assets/rdiengine/icon/rdi-icon32.png");
        try (MemoryStack memorystack = MemoryStack.stackPush()) {
            if (new16icon == null) {
                throw new FileNotFoundException("icons/icon_16x16.png");
            }

            if (new32icon == null) {
                throw new FileNotFoundException("icons/icon_32x32.png");
            }

            IntBuffer intbuffer = memorystack.mallocInt(1);
            IntBuffer intbuffer1 = memorystack.mallocInt(1);
            IntBuffer intbuffer2 = memorystack.mallocInt(1);
            GLFWImage.Buffer buffer = GLFWImage.mallocStack(2, memorystack);
            ByteBuffer bytebuffer = this.readIconPixels(new16icon, intbuffer, intbuffer1, intbuffer2);
            if (bytebuffer == null) {
                throw new IllegalStateException("Could not load icon: " + STBImage.stbi_failure_reason());
            }

            buffer.position(0);
            buffer.width(intbuffer.get(0));
            buffer.height(intbuffer1.get(0));
            buffer.pixels(bytebuffer);
            ByteBuffer bytebuffer1 = this.readIconPixels(new32icon, intbuffer, intbuffer1, intbuffer2);
            if (bytebuffer1 == null) {
                throw new IllegalStateException("Could not load icon: " + STBImage.stbi_failure_reason());
            }

            buffer.position(1);
            buffer.width(intbuffer.get(0));
            buffer.height(intbuffer1.get(0));
            buffer.pixels(bytebuffer1);
            buffer.position(0);
            GLFW.glfwSetWindowIcon(this.window, buffer);
            STBImage.stbi_image_free(bytebuffer);
            STBImage.stbi_image_free(bytebuffer1);
        } catch (IOException ioexception) {
            LOGGER.error("Couldn't set icon", (Throwable)ioexception);
        }

    }
    private ByteBuffer readIconPixels(InputStream p_198111_1_, IntBuffer p_198111_2_, IntBuffer p_198111_3_, IntBuffer p_198111_4_) throws IOException {
        RenderSystem.assertThread(RenderSystem::isInInitPhase);
        ByteBuffer bytebuffer = null;

        ByteBuffer bytebuffer1;
        try {
            bytebuffer = TextureUtil.readResource(p_198111_1_);
            ((java.nio.Buffer)bytebuffer).rewind();
            bytebuffer1 = STBImage.stbi_load_from_memory(bytebuffer, p_198111_2_, p_198111_3_, p_198111_4_, 0);
        } finally {
            if (bytebuffer != null) {
                MemoryUtil.memFree(bytebuffer);
            }

        }

        return bytebuffer1;
    }
}
