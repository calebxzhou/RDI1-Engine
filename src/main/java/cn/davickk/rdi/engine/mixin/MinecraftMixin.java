package cn.davickk.rdi.engine.mixin;

import cn.davickk.rdi.engine.music.BgmTracks;
import cn.davickk.rdi.engine.utils.MusicPlayUtils;
import cn.davickk.rdi.engine.utils.ThreadUtils;
import net.minecraft.client.GameConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.client.audio.BackgroundMusicTracks;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.DirtMessageScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WinGameScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow private static Logger LOGGER;
    @Shadow
    private Screen screen;
    @Shadow
    private ClientPlayerEntity player;
    @Shadow
    private IngameGui gui;
    @Shadow
    private MusicTicker musicManager;
    @Shadow private ClientWorld level;

    /**
     * @author
     * @reason 替换mc原版bgm
     */
    //@Overwrite
    public BackgroundMusicSelector getSituationalMusic() {
        if (this.screen instanceof WinGameScreen) {
            return BgmTracks.getRandomMenuBgm();
        } else if (this.player != null) {
            if (this.player.level.dimension() == World.END) {
                return this.gui.getBossOverlay().shouldPlayMusic() ? BackgroundMusicTracks.END_BOSS : BackgroundMusicTracks.END;
            } else {
                Biome.Category biome$category = this.player.level.getBiome(this.player.blockPosition()).getBiomeCategory();
                if (!this.musicManager.isPlayingMusic(BackgroundMusicTracks.UNDER_WATER)
                        && (!this.player.isUnderWater() || biome$category != Biome.Category.OCEAN && biome$category != Biome.Category.RIVER)) {
                    return this.player.level.dimension() != World.NETHER && this.player.abilities.instabuild && this.player.abilities.mayfly ? BackgroundMusicTracks.CREATIVE : this.level.getBiomeManager().getNoiseBiomeAtPosition(this.player.blockPosition()).getBackgroundMusic().orElse(BackgroundMusicTracks.GAME);
                } else {
                    return BackgroundMusicTracks.UNDER_WATER;
                }
            }
        } else {
            return BgmTracks.getRandomMenuBgm();
        }
    }

    /**
     * @reason  崩溃不退游戏
     * @author
     */
    @Overwrite
    public void destroy() {
            LOGGER.info("Stopping!");
    }
    /**
     * @reason 同上
     */
    @Overwrite
    public void close() {


    }
    /**
     * @reason 同上
     */
    @Overwrite
    public void emergencySave() {
    }
/**
 * @reason 播放音乐
 * */
    @Inject(
            method = "setScreen(Lnet/minecraft/client/gui/screen/Screen;)V",  // the jvm bytecode signature for the constructor
            at = @At("HEAD")  // signal that this void should be run at the method HEAD, meaning the first opcode
    )
    private void init(Screen screen,CallbackInfo cir){
        System.out.println("Sound Mixin Worked");
        ThreadUtils.startThread(() ->{
            String url=BgmTracks.getRandomMenuBgmUrl();
            System.out.println(url);
            File inputStream=
                    new File(url);
            try {
                new MusicPlayUtils().play(inputStream);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            }
        });


    }
}
