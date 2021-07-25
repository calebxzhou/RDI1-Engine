package cn.davickk.rdi.engine.utils;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.fonts.FontResourceManager;
import net.minecraft.client.gui.fonts.providers.DefaultGlyphProvider;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.Util;

import static net.minecraft.client.gui.fonts.FontResourceManager.MISSING_FONT;

public class FontUtils {
    private Font missingFontSet;
    private IReloadableResourceManager resourceManager;
    private TextureManager textureManager;
    public FontUtils(){
        resourceManager = new SimpleReloadableResourceManager(ResourcePackType.CLIENT_RESOURCES);
        textureManager = new TextureManager(resourceManager);
        missingFontSet = Util.make(new Font(textureManager, MISSING_FONT), (font) -> {
            font.reload(Lists.newArrayList(new DefaultGlyphProvider()));
        });
    }
    public FontRenderer createFont() {
        return new FontRenderer((resourceLocation) -> {
            return missingFontSet;//new FontResourceManager(textureManager).createFont();
        });
    }
}
