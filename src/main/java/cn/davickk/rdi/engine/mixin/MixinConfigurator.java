package cn.davickk.rdi.engine.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class MixinConfigurator implements IMixinConfigPlugin {
    public static final String basepath="cn.davickk.rdi.engine.mixin.";
    Map<String, Supplier<Boolean>> configClasses = new HashMap<>();
    @Override
    public void onLoad(String s) {
        this.configClasses.put(basepath+"PacketInflaterMixin", () -> (Boolean)true);

    }


    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String s, String mixinClassName) {
        if (this.configClasses.containsKey(mixinClassName)) {
            if (((Boolean)((Supplier<Boolean>)this.configClasses.get(mixinClassName)).get()).booleanValue())
                return true;
            System.out.println("Not enabling mixin for" + mixinClassName + " as config disables it.");
            return false;
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }
}
