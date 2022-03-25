package io.github.thatrobin.ccpacks.networking;

import io.github.thatrobin.ccpacks.CCPacksMain;
import net.minecraft.util.Identifier;

public class CCPacksModPackets {
    public static final Identifier HANDSHAKE = CCPacksMain.identifier("handshake");
    public static final Identifier OPEN_CHOICE_SCREEN = CCPacksMain.identifier("choice_screen");
    public static final Identifier CHOOSE_CHOICE = CCPacksMain.identifier("choose_choice");
    public static final Identifier CONFIRM_CHOICE = CCPacksMain.identifier("confirm_powers");
    public static final Identifier CHOICE_LIST = CCPacksMain.identifier("choice_list");
    public static final Identifier LAYER_LIST = CCPacksMain.identifier("layer_list");
    public static final Identifier MECHANIC_LIST = CCPacksMain.identifier("mechanic_list");
}
