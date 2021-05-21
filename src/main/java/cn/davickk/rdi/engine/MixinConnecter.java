package cn.davickk.rdi.engine;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MixinConnecter implements IMixinConnector {
    @Override
    public void connect() {
        Mixins.addConfiguration("mixins.rdiengine.json");
    }
}
