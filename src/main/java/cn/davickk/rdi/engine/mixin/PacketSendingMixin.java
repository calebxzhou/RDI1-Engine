package cn.davickk.rdi.engine.mixin;

import com.google.gson.Gson;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CClickWindowPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CClickWindowPacket.class)
public class PacketSendingMixin{
    @Shadow
    private int containerId;
    @Shadow
    private int slotNum;
    @Shadow
    private int buttonNum;
    @Shadow
    private short uid;
    @Shadow
    private ItemStack itemStack;
    @Shadow
    private ClickType clickType;
    @Inject(method = "<init>*", at = @At("RETURN"))
    private void onConstructed(CallbackInfo ci) {
        System.out.println(toString());
        // do initialisation stuff
    }

    @Override
    public String toString() {
        return "PacketSendingMixin{" +
                "containerId=" + containerId +
                ", slotNum=" + slotNum +
                ", buttonNum=" + buttonNum +
                ", uid=" + uid +
                ", itemStack=" + itemStack +
                ", clickType=" + clickType +
                '}';
    }
}
