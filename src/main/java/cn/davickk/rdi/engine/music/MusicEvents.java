package cn.davickk.rdi.engine.music;

import cn.davickk.rdi.engine.utils.ResourceLocationUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

public class MusicEvents {
    static public final SoundEvent MENU_AF ;
    public  static final SoundEvent MENU_EGL;
    public static final SoundEvent MENU_LIBZG ;
    public  static final SoundEvent MENU_PLZINSP ;
    public  static final SoundEvent MENU_RF ;
    public  static final SoundEvent MENU_RUSH ;
    static{
        MENU_AF = makeEvent("music.menu-af");
        MENU_EGL = makeEvent("music.menu-egl");
        MENU_LIBZG = makeEvent("music.menu-libzg");
        MENU_PLZINSP = makeEvent("music.menu-plzinsp");
        MENU_RF = makeEvent("music.menu-rf");
        MENU_RUSH = makeEvent("music.menu-rush");
        
    }
    private static SoundEvent makeEvent(String name){
        ResourceLocation loc= ResourceLocationUtils.prefix(name);
        return new SoundEvent(loc).setRegistryName(loc);
    }
    public static void register(RegistryEvent.Register<SoundEvent> evt) {
        IForgeRegistry<SoundEvent> r = evt.getRegistry();
        r.register(MENU_AF);
        r.register(MENU_EGL);
        r.register(MENU_LIBZG);
        r.register(MENU_PLZINSP);
        r.register(MENU_RF);
        r.register(MENU_RUSH);
    }
}
