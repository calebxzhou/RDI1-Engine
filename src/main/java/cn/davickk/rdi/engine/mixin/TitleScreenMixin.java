package cn.davickk.rdi.engine.mixin;

import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
@Mixin(MainMenuScreen.class)
public class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(ITextComponent p_i51108_1_) {
        super(p_i51108_1_);
    }
    @ModifyConstant(method = "render",constant =
    @Constant(stringValue =                         "Copyright Mojang AB. Do not distribute!"))
    private String changeright(String old) {
        return "RDI Liberation Engine / Game by Mojang";
    }
    /*@Inject(method = "init", at = @At("HEAD"))
    private void onInit(CallbackInfo cbi){
        int j = this.height / 4 + 48;
        this.addButton(new Button(this.width / 2 - 20, j + 72 + 12,
                98, 20, new TranslationTextComponent("支持RDI维持营运"), (p_213096_1_) -> {
            this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options));
        }));


    }*/
}
