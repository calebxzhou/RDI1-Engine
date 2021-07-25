package cn.davickk.rdi.engine.thread;

import io.netty.buffer.Unpooled;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CCustomPayloadPacket;

public class RegistryPacketsSendThread extends Thread{
    NetworkManager connection;
    Minecraft minecraft;
    public RegistryPacketsSendThread(NetworkManager connection,Minecraft mc){
        this.connection=connection;
        this.minecraft=mc;
    }
    @Override
    public void run(){
        net.minecraftforge.fml.network.NetworkHooks.sendMCRegistryPackets(connection, "PLAY_TO_SERVER");
        this.connection.send(new CCustomPayloadPacket(CCustomPayloadPacket.BRAND, (new PacketBuffer(Unpooled.buffer())).writeUtf(ClientBrandRetriever.getClientModName())));
        this.minecraft.options.broadcastOptions();
    }
}
