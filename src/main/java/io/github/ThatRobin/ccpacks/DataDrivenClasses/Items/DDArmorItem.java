package io.github.ThatRobin.ccpacks.DataDrivenClasses.Items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class DDArmorItem extends ArmorItem {

    private List<String> lore;
    private EquipmentSlot slot;

    public DDArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings, List<String> lore) {
        super(material, slot, settings);
        this.lore = lore;
        this.slot = slot;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        if (lore != null) {
            if (lore.size() > 0) {
                for (String s : lore) {
                    tooltip.add(new LiteralText(s).formatted(Formatting.GRAY));
                }
            }
        }
    }
}