package io.github.ThatRobin.ccpacks.dataDrivenTypes.Items;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class DDHoeItem extends HoeItem {

    private List<String> lore;

    public DDHoeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings, List<String> lore) {
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
