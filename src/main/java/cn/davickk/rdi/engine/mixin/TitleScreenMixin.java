package cn.davickk.rdi.engine.mixin;

import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(ITextComponent p_i51108_1_) {
        super(p_i51108_1_);
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
