package cn.davickk.rdi.engine.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ServerConnectingScreen extends Screen {
    private ITextComponent iTextComponent;
        public ServerConnectingScreen(ITextComponent p_i51108_1_) {
            super(p_i51108_1_);
            this.iTextComponent=p_i51108_1_;
        }

        @Override
        public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
            this.renderDirtBackground(0);
            renderText(p_230430_1_,p_230430_2_,p_230430_3_,p_230430_4_,iTextComponent.getString());
        }
        public void renderText(MatrixStack matrixStack, int x, int y, float f, String text){
            drawCenteredString(matrixStack, this.font, text, this.width / 2, this.height / 2 - 50, 16777215);
            super.render(matrixStack, x, y, f);
        }
        public static Screen quickRenderText(String text){
            return new ServerConnectingScreen(new StringTextComponent(text));
        }

}
