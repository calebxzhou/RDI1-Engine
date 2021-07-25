package cn.davickk.rdi.engine.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InsufficientPrivilegesException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.login.ClientLoginNetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.client.CEncryptionResponsePacket;
import net.minecraft.network.login.server.SEncryptionRequestPacket;
import net.minecraft.util.CryptException;
import net.minecraft.util.CryptManager;
import net.minecraft.util.HTTPUtil;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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
import java.util.function.Consumer;

@Mixin(ClientLoginNetHandler.class)
public class ClientLoginNetHandlerMixin {
    @Shadow
    private static Logger LOGGER;
    @Shadow
    private Minecraft minecraft;
    @Shadow
    private Screen parent;
    @Shadow
    private Consumer<ITextComponent> updateStatus;
    @Shadow
    private NetworkManager connection;
    @Shadow
    private GameProfile localGameProfile;
    /**
     * @author
     */
    @Overwrite
    public void handleHello(SEncryptionRequestPacket p_147389_1_) {
        Cipher cipher;
        Cipher cipher1;
        String s;
        CEncryptionResponsePacket cencryptionresponsepacket;
        try {
            SecretKey secretkey = CryptManager.generateSecretKey();
            PublicKey publickey = p_147389_1_.getPublicKey();
            s = (new BigInteger(CryptManager.digestData(p_147389_1_.getServerId(), publickey, secretkey))).toString(16);
            cipher = CryptManager.getCipher(2, secretkey);
            cipher1 = CryptManager.getCipher(1, secretkey);
            cencryptionresponsepacket = new CEncryptionResponsePacket(secretkey, publickey, p_147389_1_.getNonce());
        } catch (CryptException cryptexception) {
            throw new IllegalStateException("Protocol error", cryptexception);
        }

        this.updateStatus.accept(new StringTextComponent("发送连接请求.."));
        HTTPUtil.DOWNLOAD_EXECUTOR.submit(() -> {
            ITextComponent itextcomponent = this.authenticateServer(s);
            this.updateStatus.accept(new StringTextComponent("验证您的正版账号.."));
            if (itextcomponent != null) {
                if (this.minecraft.getCurrentServer() == null || !this.minecraft.getCurrentServer().isLan()) {
                    this.connection.disconnect(itextcomponent);
                    return;
                }

                LOGGER.warn(itextcomponent.getString());
           }

            this.updateStatus.accept(new TranslationTextComponent("验证成功。加密连接中..."));
            this.connection.send(cencryptionresponsepacket, (p_244776_3_) -> {
                this.updateStatus.accept(new StringTextComponent("发送密钥中...."));
                this.connection.setEncryptionKey(cipher, cipher1);
            });
        });
    }
    /**
     * @author
     */
    @Nullable
    @Overwrite
    private ITextComponent authenticateServer(String p_209522_1_) {
        try {
            this.getMinecraftSessionService().joinServer(this.minecraft.getUser().getGameProfile(), this.minecraft.getUser().getAccessToken(), p_209522_1_);
            return null;
        } catch (AuthenticationUnavailableException authenticationunavailableexception) {
            return new TranslationTextComponent("disconnect.loginFailedInfo", new TranslationTextComponent("disconnect.loginFailedInfo.serversUnavailable"));
        } catch (InvalidCredentialsException invalidcredentialsexception) {
            return new TranslationTextComponent("disconnect.loginFailedInfo", new StringTextComponent("服务器无法获取您的正版UUID，请尝试重启您的客户端\n或重新登录启动器中的正版账号。"));
        } catch (InsufficientPrivilegesException insufficientprivilegesexception) {
            return new TranslationTextComponent("disconnect.loginFailedInfo", new TranslationTextComponent("disconnect.loginFailedInfo.insufficientPrivileges"));
        } catch (AuthenticationException authenticationexception) {
            return new TranslationTextComponent("disconnect.loginFailedInfo", authenticationexception.getMessage());
        }
    }
    private MinecraftSessionService getMinecraftSessionService() {
        return this.minecraft.getMinecraftSessionService();
    }
}
