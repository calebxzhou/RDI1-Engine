package cn.davickk.rdi.engine.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

public class PlayerUtils {
    public static BlockPos lookingAtBlock(PlayerEntity player, boolean isFluid){
        BlockRayTraceResult rays=(BlockRayTraceResult) player.pick(20.0D,0.0f,isFluid);
        return rays.getBlockPos();
    }
}
