package io.github.ThatRobin.ccpacks.dataDrivenTypes.Items;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class DDShovelItem extends ShovelItem {

    private List<String> lore;

    public DDShovelItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings, List<String> lore) {
        super(material, attackDamage, attackSpeed, settings);
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

}
