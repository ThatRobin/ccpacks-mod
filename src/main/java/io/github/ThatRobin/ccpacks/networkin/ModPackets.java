package io.github.ThatRobin.ccpacks.networkin;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import net.minecraft.util.Identifier;

public class ModPackets {
    public static final Identifier HANDSHAKE = CCPacksMain.identifier("handshake");

    public static final Identifier OPEN_CHOICE_SCREEN = new Identifier(CCPacksMain.MODID, "open_choice_screen");
}
