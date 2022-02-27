package io.github.thatrobin.ccpacks.power;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.power.OverlayPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.thatrobin.ccpacks.CCPacksMain;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.block.PumpkinBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class GuiElementPower extends Power {

    public Identifier texture;
    public float opacity;
    public float maxx;
    public float maxy;
    public float minx;
    public float miny;

    public GuiElementPower(PowerType<?> type, LivingEntity entity, Identifier texture, float opacity, float maxx, float maxy, float minx, float miny) {
        super(type, entity);
        this.texture = texture;
        this.opacity = opacity;
        this.maxx = maxx;
        this.maxy = maxy;
        this.minx = minx;
        this.miny = miny;
    }

    public static PowerFactory createFactory() {
        return new PowerFactory<>(CCPacksMain.identifier("gui_element"),
                new SerializableData()
                        .add("texture", SerializableDataTypes.IDENTIFIER)
                        .add("opacity", SerializableDataTypes.FLOAT)
                        .add("max_x", SerializableDataTypes.FLOAT)
                        .add("max_y", SerializableDataTypes.FLOAT)
                        .add("min_x", SerializableDataTypes.FLOAT)
                        .add("min_y", SerializableDataTypes.FLOAT),
                data ->
                        (type, player) -> new GuiElementPower(type, player,
                                data.getId("texture"), data.getFloat("opacity"), data.getFloat("max_x"), data.getFloat("max_y"), data.getFloat("min_x"), data.getFloat("min_y")))
                .allowCondition();
    }

}