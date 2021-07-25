package cn.davickk.rdi.engine.music;

import cn.davickk.rdi.engine.utils.RandomUtils;
import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public class BgmTracks {
    public static String AF="menu-af";
    public static String EGL="menu-egl";
    public static String LIBZG="menu-libzg";
    public static String PLZINSP="menu-plzinsp";
    public static String RF="menu-rf";
    public static String RUSH="menu-rush";

    public static String getFullUrl(String bgmName){
        String jarUrl="assets/rdiengine/sounds/"+bgmName+".wav";
        return BgmTracks.class.getClassLoader().getResource(jarUrl)
                .getFile().replace("/","\\").replace("%20"," ");
    }
    public static String getRandomMenuBgmUrl(){
        int r= RandomUtils.generateRandomInt(1,6);
        if(r==1)
            return getFullUrl(AF);
        else if(r==2)
            return getFullUrl(EGL);
        else if(r==3)
            return getFullUrl(LIBZG);
        else if(r==4)
            return getFullUrl(PLZINSP);
        else if(r==5)
            return getFullUrl(RF);
        else
            return getFullUrl(RUSH);
    }
    public static BackgroundMusicSelector getRandomMenuBgm(){
        int r= RandomUtils.generateRandomInt(1,6);
        MusicEvents events=new MusicEvents();
        if(r==1)
            return new BackgroundMusicSelector(MusicEvents.MENU_AF,12000,24000,true);
        else if (r==2)
            return new BackgroundMusicSelector(MusicEvents.MENU_EGL,12000,24000,true);
        else if (r==3)
            return new BackgroundMusicSelector(MusicEvents.MENU_LIBZG,12000,24000,true);
        else if (r==4)
            return new BackgroundMusicSelector(MusicEvents.MENU_RF,12000,24000,true);
        else if (r==5)
            return new BackgroundMusicSelector(MusicEvents.MENU_PLZINSP,12000,24000,true);
        else
            return new BackgroundMusicSelector(MusicEvents.MENU_RUSH,12000,24000,true);

    }
}
