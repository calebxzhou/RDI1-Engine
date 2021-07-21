package cn.davickk.rdi.engine.mixin;

import cn.davickk.rdi.engine.model.ContainerInteractRecord;
import cn.davickk.rdi.engine.utils.HttpUtils;
import cn.davickk.rdi.engine.utils.PlayerUtils;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HTTPUtil;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.sql.Date;
import java.sql.Timestamp;

@Mixin(PlayerController.class)
public class PlayerControllerMixin {
    @Shadow
    private ClientPlayNetHandler connection;

    private boolean isServerError=false;
    @Inject(method = "handleInventoryMouseClick",at=@At(value = "RETURN"))
    private void handleInventoryMouseClick(int containerId, int slotNum, int buttonNum, ClickType clickType,
                                           PlayerEntity playerEntity, CallbackInfoReturnable<ItemStack> info){
        //if(playerEntity.level.isClientSide());
        if(clickType.equals(ClickType.THROW))
            return;
        /**
         * QUICK_MOVE：按下shift键的快速移动
         * THROW:点击容器UI界面外的空白区域
         * PICKUP：点击鼠标拾取物品
         */
        String clickTyping=clickType.toString();

        //点击物品的对象
        ItemStack clickedItemStack=info.getReturnValue();
        int itemAmount=clickedItemStack.getCount();
        if(itemAmount<1)
            return;
        //光标指向方块坐标
        BlockPos lookingBlockPos=PlayerUtils.lookingAtBlock(playerEntity,false);
        //具体方块
        BlockState lookingBlockState=playerEntity.level.getBlockState(lookingBlockPos);



        //点击玩家名称
        String playerName=playerEntity.getDisplayName().getString();
        //点击物品的名称
        String clickedItemName=clickedItemStack.getItem().getRegistryName().toString();
        //点击操作类型



        //获取点击物品的nbt值，如果为null就返回字符串null
        String clickedItemNbt=
                clickedItemStack.serializeNBT()==null  ?
                clickedItemStack.serializeNBT().toString() : "NO_NBT";

        //点击的方块名称
        String blockName=lookingBlockState.getBlock().getRegistryName().toString();
        //方块坐标

        int posX=lookingBlockPos.getX();
        int posY=lookingBlockPos.getY();
        int posZ=lookingBlockPos.getZ();
        //操作时间
        Timestamp timestamp=new Timestamp(new Date(System.currentTimeMillis()).getTime());
        ContainerInteractRecord record=new ContainerInteractRecord(playerName,clickedItemName,clickTyping,clickedItemNbt,blockName,itemAmount,posX,posY,posZ,timestamp);
        System.out.println(record.toString());
        if(isServerError){
            return;
        }
        try {
            String res=HttpUtils.doPost("http://localhost:8080/RDI_CloudRest_war_exploded/container-io",record.toString());
        } catch (Exception e) {
            this.isServerError=true;
            e.printStackTrace();
        }

    }
}
