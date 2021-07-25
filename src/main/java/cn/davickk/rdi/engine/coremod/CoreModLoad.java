package cn.davickk.rdi.engine.coremod;

import cpw.mods.modlauncher.api.ITransformer;
import net.minecraftforge.coremod.CoreModEngine;
import org.objectweb.asm.tree.ClassNode;

import java.util.List;
import java.util.stream.Collectors;

public class CoreModLoad {
    void testJSLoading() {
        final CoreModEngine coreModEngine = new CoreModEngine();
        //coreModEngine.loadCoreMod(new JSFileLoader("js/no_red_screen.js"));
        final List<ITransformer<?>> iTransformers = coreModEngine.initializeCoreMods();
        iTransformers.forEach(t -> {
            System.out.printf("targ: %s\n", t.targets().stream().map(ITransformer.Target::getClassName).collect(Collectors.joining(",")));
            ClassNode cn = new ClassNode();
            cn.name = "HelloWorld";
            ClassNode newcn = ((ITransformer<ClassNode>)t).transform(cn, null);
            System.out.println(newcn.methods.stream().map(m->m.name).collect(Collectors.joining(",")));
        });
    }
}
