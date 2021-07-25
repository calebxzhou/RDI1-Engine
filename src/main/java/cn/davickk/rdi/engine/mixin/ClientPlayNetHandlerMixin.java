package cn.davickk.rdi.engine.mixin;

import cn.davickk.rdi.engine.gui.ServerConnectingScreen;
import cn.davickk.rdi.engine.thread.RegistryPacketsSendThread;
import cn.davickk.rdi.engine.utils.ThreadUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InsufficientPrivilegesException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.brigadier.CommandDispatcher;
import io.netty.buffer.Unpooled;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DownloadTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.client.util.NBTQueryManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.login.client.CEncryptionResponsePacket;
import net.minecraft.network.login.server.SEncryptionRequestPacket;
import net.minecraft.network.play.client.CCustomPayloadPacket;
import net.minecraft.network.play.server.SJoinGamePacket;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.tags.ITagCollectionSupplier;
import net.minecraft.tags.TagRegistryManager;
import net.minecraft.util.*;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.security.PublicKey;
import java.util.*;

@Mixin(ClientPlayNetHandler.class)
public class ClientPlayNetHandlerMixin {
    @Shadow
    private static  Logger LOGGER;
    private static  ITextComponent GENERIC_DISCONNECT_MESSAGE = new TranslationTextComponent("disconnect.lost");
    @Shadow
    private NetworkManager connection;
    @Shadow
    private Screen callbackScreen;
    @Shadow private Minecraft minecraft;
    @Shadow private ClientWorld level;
    @Shadow private ClientWorld.ClientWorldInfo levelData;
    @Shadow private boolean started;
    @Shadow private Map<UUID, NetworkPlayerInfo> playerInfoMap;
    @Shadow private int serverChunkRadius;
    @Shadow private Random random;
    @Shadow private RecipeManager recipeManager;
    @Shadow private UUID id;
    @Shadow private Set<RegistryKey<World>> levels;
    @Shadow private DynamicRegistries registryAccess;

    /**
     * @author
     */
    @Overwrite
    public void handleLogin(SJoinGamePacket p_147282_1_) {
        this.minecraft.setScreen(ServerConnectingScreen.quickRenderText("同步线程中"));
        PacketThreadUtil.ensureRunningOnSameThread(p_147282_1_, (ClientPlayNetHandler) (Object)this, this.minecraft);
        this.minecraft.gameMode = new PlayerController(this.minecraft, (ClientPlayNetHandler) (Object)this);
        if (!this.connection.isMemoryConnection()) {
            TagRegistryManager.resetAllToEmpty();
        }
        this.minecraft.setScreen(ServerConnectingScreen.quickRenderText("获取所有世界中"));
        ArrayList<RegistryKey<World>> arraylist = Lists.newArrayList(p_147282_1_.levels());
        this.minecraft.setScreen(ServerConnectingScreen.quickRenderText("获取所有世界成功:"+arraylist.toString()));
        Collections.shuffle(arraylist);
        this.levels = Sets.newLinkedHashSet(arraylist);
        this.registryAccess = p_147282_1_.registryAccess();
        RegistryKey<World> registrykey = p_147282_1_.getDimension();
        DimensionType dimensiontype = p_147282_1_.getDimensionType();
        this.serverChunkRadius = p_147282_1_.getChunkRadius();
        boolean flag = p_147282_1_.isDebug();
        boolean flag1 = p_147282_1_.isFlat();
        this.minecraft.setScreen(ServerConnectingScreen.quickRenderText("获取世界基本数据中"));
        ClientWorld.ClientWorldInfo clientworld$clientworldinfo = new ClientWorld.ClientWorldInfo(Difficulty.NORMAL, p_147282_1_.isHardcore(), flag1);
        this.levelData = clientworld$clientworldinfo;
        this.level = new ClientWorld((ClientPlayNetHandler) (Object)this, clientworld$clientworldinfo, registrykey, dimensiontype, this.serverChunkRadius, this.minecraft::getProfiler, this.minecraft.levelRenderer, flag, p_147282_1_.getSeed());
        this.minecraft.setLevel(this.level);
        this.minecraft.setScreen(ServerConnectingScreen.quickRenderText("加入世界中"));
        if (this.minecraft.player == null) {
            this.minecraft.player = this.minecraft.gameMode.createPlayer(this.level, new StatisticsManager(), new ClientRecipeBook());
            this.minecraft.player.yRot = -180.0F;
            if (this.minecraft.getSingleplayerServer() != null) {
                this.minecraft.getSingleplayerServer().setUUID(this.minecraft.player.getUUID());
            }
        }

        this.minecraft.debugRenderer.clear();
        this.minecraft.player.resetPos();
        this.minecraft.setScreen(ServerConnectingScreen.quickRenderText("初始化玩家位置 20%-载入基本模型"));
        net.minecraftforge.fml.client.ClientHooks.firePlayerLogin(this.minecraft.gameMode, this.minecraft.player, this.minecraft.getConnection().getConnection());
        int i = p_147282_1_.getPlayerId();
        this.level.addPlayer(i, this.minecraft.player);
        this.minecraft.setScreen(ServerConnectingScreen.quickRenderText("初始化玩家位置 50%"));
        this.minecraft.player.input = new MovementInputFromOptions(this.minecraft.options);
        this.minecraft.gameMode.adjustPlayer(this.minecraft.player);
        this.minecraft.cameraEntity = this.minecraft.player;
        this.minecraft.setScreen(ServerConnectingScreen.quickRenderText("初始化玩家位置 70%"));
        this.minecraft.player.setId(i);
        this.minecraft.player.setReducedDebugInfo(p_147282_1_.isReducedDebugInfo());
        this.minecraft.player.setShowDeathScreen(p_147282_1_.shouldShowDeathScreen());
        this.minecraft.setScreen(ServerConnectingScreen.quickRenderText("初始化玩家位置 90%"));
        this.minecraft.gameMode.setLocalMode(p_147282_1_.getGameType());
        this.minecraft.gameMode.setPreviousLocalMode(p_147282_1_.getPreviousGameType());
        ThreadUtils.startThread(new RegistryPacketsSendThread(connection,this.minecraft));
        this.minecraft.setScreen(ServerConnectingScreen.quickRenderText("初始化玩家位置 90%"));
        this.minecraft.getGame().onStartGameSession();
        this.minecraft.setScreen(ServerConnectingScreen.quickRenderText("初始化玩家位置 91%"));
    }
}