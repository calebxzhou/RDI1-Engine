package cn.davickk.rdi.engine.screen;

import com.mojang.text2speech.Narrator;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

public class SupportUsScreen extends Screen {
    public SupportUsScreen() {
        super(NarratorChatListener.NO_TITLE);
    }

}
