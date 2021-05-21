package cn.davickk.rdi.engine.mixin;

import net.minecraft.network.NettyCompressionDecoder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(NettyCompressionDecoder.class)
public class PacketInflaterMixin {
    @ModifyConstant(method = "decode",constant = @Constant(intValue = 2097152))
    private int xlPackets(int old) {
        return 2147483646;
    }
    /*@Shadow
    private Inflater inflater;
    @Shadow
    private int threshold;
    *//*
    @Inject(method = "decode",at = @At("INVOKE"))
    private void decode(ChannelHandlerContext p_decode_1_, ByteBuf p_decode_2_, List<Object> p_decode_3_, CallbackInfo cbi) throws DataFormatException {
        if (p_decode_2_.readableBytes() != 0) {
            PacketBuffer packetbuffer = new PacketBuffer(p_decode_2_);
            int i = packetbuffer.readVarInt();
            if (i == 0) {
                p_decode_3_.add(packetbuffer.readBytes(packetbuffer.readableBytes()));
            } else {
                if (i < this.threshold) {
                    throw new DecoderException("Badly compressed packet - size of " + i + " is below server threshold of " + this.threshold);
                }

                if (i > 2097152*16) {
                    throw new DecoderException("Badly compressed packet - size of " + i + " is larger than protocol maximum of " + 2097152);
                }

                byte[] abyte = new byte[packetbuffer.readableBytes()];
                packetbuffer.readBytes(abyte);
                this.inflater.setInput(abyte);
                byte[] abyte1 = new byte[i];
                this.inflater.inflate(abyte1);
                p_decode_3_.add(Unpooled.wrappedBuffer(abyte1));
                this.inflater.reset();
            }

        }
    }*/
}
