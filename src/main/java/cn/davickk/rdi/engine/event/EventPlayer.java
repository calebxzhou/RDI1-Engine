package cn.davickk.rdi.engine.event;

import cn.davickk.rdi.engine.RDIEngine;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RDIEngine.MOD_ID,bus= Mod.EventBusSubscriber.Bus.FORGE)
public class EventPlayer {
    public static void onSendingPacket(){

    }
}
