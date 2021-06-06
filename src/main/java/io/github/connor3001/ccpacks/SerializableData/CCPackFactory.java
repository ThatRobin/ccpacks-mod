package io.github.connor3001.ccpacks.SerializableData;

import com.mojang.datafixers.types.Func;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.connor3001.ccpacks.CCPacksMain;
import io.github.connor3001.ccpacks.Util.CustomCraftingTable;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.*;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class CCPackFactory {

    public static void register() {
        register(new ActionFactory<>(CCPacksMain.identifier("crafting_gui"), new SerializableData(),
                (data, entity) -> {
                    if (entity instanceof PlayerEntity) {
                        PlayerEntity player = (PlayerEntity)entity;
                        if (player.world.isClient) {

                        } else {
                            player.openHandledScreen(craftingTable(player.world, player.getBlockPos()));
                            player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
                        }
                    }
                }));
    }

    private static NamedScreenHandlerFactory craftingTable(World world_1, BlockPos blockPos_1) {
        return new SimpleNamedScreenHandlerFactory(
                (int_1, playerInventory_1, playerEntity_1)
                        ->
                        new CustomCraftingTable(int_1, playerInventory_1,
                                ScreenHandlerContext.create(world_1, blockPos_1)),
                new TranslatableText("container.crafting", new Object[0])
        );
    }

    private static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }

}
