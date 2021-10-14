package io.github.ThatRobin.ccpacks.dataDrivenTypes.Items;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.Util.ColourHolder;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.awt.*;
import java.util.List;

public class DDItem extends Item {

    private List<String> lore;
    private ColourHolder startColours;
    private ColourHolder endColours;

    public DDItem(Settings settings, List<String> lore) {
        super(settings);
        this.lore = lore;
        this.startColours = null;
        this.endColours = null;
    }

    public DDItem(Settings settings, List<String> lore, ColourHolder startColours, ColourHolder endColours) {
        super(settings);
        this.lore = lore;
        this.startColours = startColours;
        this.endColours = endColours;
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
    public int getItemBarColor(ItemStack stack) {
        if(startColours != null && endColours != null) {
            int[] scolor = new int[]{(int)(startColours.getRed() * 255), (int)(startColours.getGreen() * 255), (int)(startColours.getBlue() * 255)};
            int[] ecolor = new int[]{(int)(endColours.getRed() * 255), (int)(endColours.getGreen() * 255), (int)(endColours.getBlue() * 255)};

            float dp = ((float) this.getMaxDamage() - (float) stack.getDamage()) / (float) this.getMaxDamage();

            float[] hsvs = Color.RGBtoHSB(scolor[0],scolor[1],scolor[2], null);
            float[] hsve = Color.RGBtoHSB(ecolor[0],ecolor[1],ecolor[2], null);

            float lerpr = lerp(hsve[0], hsvs[0], dp);
            float lerpg = lerp(hsve[1], hsvs[1], dp);
            float lerpb = lerp(hsve[2], hsvs[2], dp);

            return MathHelper.hsvToRgb(lerpr, lerpg, lerpb);
        } else {
            float f = Math.max(0.0F, ((float)this.getMaxDamage() - (float)stack.getDamage()) / (float)this.getMaxDamage());
            return MathHelper.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
        }
    }

    private float lerp(float a, float b, float f)
    {
        return (float)(a * (1.0 - f)) + (b * f);
    }
}