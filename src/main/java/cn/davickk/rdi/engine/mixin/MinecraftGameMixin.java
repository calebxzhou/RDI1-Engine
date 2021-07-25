package cn.davickk.rdi.engine.mixin;

import cn.davickk.rdi.engine.gui.ServerConnectingScreen;
import com.mojang.bridge.game.GameSession;
import net.minecraft.client.ClientGameSession;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftGame;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(MinecraftGame.class)
public class MinecraftGameMixin {
    @Shadow private Minecraft minecraft;
    @Nullable
    @Overwrite
    public GameSession getCurrentSession() {
        ClientWorld clientworld = this.minecraft.level;
        this.minecraft.setScreen(ServerConnectingScreen.quickRenderText("94% 正在写入地图数据"));
        return clientworld == null ? null : new ClientGameSession(clientworld, this.minecraft.player, this.minecraft.player.connection);
    }
}
