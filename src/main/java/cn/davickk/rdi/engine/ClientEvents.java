package cn.davickk.rdi.engine;

import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RDIEngine.MOD_ID,bus=Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {
    @SubscribeEvent
    public static void onPressKey(GuiScreenEvent.KeyboardKeyPressedEvent event){

    }
}
