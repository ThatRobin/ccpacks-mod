package io.github.connor3001.ccpacks.dataDrivenTypes;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class DDItem extends Item {

    private List<String> lore;
    private Identifier source;
    private List<PowerTypeReference> power;

    public DDItem(Settings settings, Identifier source, List<PowerTypeReference> power, List<String> lore) {
        super(settings);
        this.power = power;
        this.source = source;
        this.lore = lore;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        if (lore != null) {
            if (lore.size() > 0) {
                for (int i = 0; i < lore.size(); i++) {
                    tooltip.add(new LiteralText(lore.get(i)).formatted(Formatting.GRAY));
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(!world.isClient) {
            if (entity instanceof PlayerEntity) {
                if (this.power != null && this.power.size() > 0) {
                    for (int i = 0; i < this.power.size(); i++) {
                        PlayerEntity player = (PlayerEntity) entity;
                        PowerType<?> type = PowerTypeRegistry.get(this.power.get(i).getIdentifier());
                        PowerHolderComponent component = PowerHolderComponent.KEY.get(player);
                        if (player.getMainHandStack().getItem() == stack.getItem() || player.getOffHandStack().getItem() == stack.getItem()) {
                            if (!component.hasPower(type, this.source)) {
                                boolean give = component.addPower(type, this.source);
                                if (give) {
                                    component.sync();
                                }
                            }
                        } else {
                            if (component.hasPower(type, this.source)) {
                                component.removePower(type, this.source);
                                component.sync();
                            }
                        }
                    }
                }
            }
        }
    }
}