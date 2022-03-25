package io.github.thatrobin.ccpacks.data_driven_classes.items;

import com.google.common.collect.Lists;
import io.github.apace100.apoli.util.PowerGrantingItem;
import io.github.apace100.apoli.util.StackPowerUtil;
import io.github.thatrobin.ccpacks.util.ColourHolder;
import io.github.thatrobin.ccpacks.util.StackPowerExpansion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import software.bernie.example.item.JackInTheBoxItem;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.awt.*;
import java.util.Collection;
import java.util.List;

public class DDShieldItem extends ShieldItem implements IAnimatable, ISyncable, PowerGrantingItem {

    private final List<StackPowerExpansion> item_powers;
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final String controllerName = "popupController";
    private static final int ANIM_OPEN = 0;
    private final int cooldown;
    private final ToolMaterial toolMaterial;
    private LiteralText name;
    private final List<String> lore;
    private final ColourHolder startColours;
    private final ColourHolder endColours;

    public DDShieldItem(Settings settings, int cooldown, ToolMaterial toolMaterial, List<StackPowerExpansion> item_powers, LiteralText name, List<String> lore, ColourHolder startColours, ColourHolder endColours) {
        super(settings);
        this.name = name;
        this.lore = lore;
        this.cooldown = cooldown;
        this.toolMaterial = toolMaterial;
        this.item_powers = item_powers;
        this.startColours = startColours;
        this.endColours = endColours;
    }

    @Override
    public Text getName() {
        if(name != null) {
            return name;
        }
        return new TranslatableText(this.getTranslationKey());
    }

    @Override
    public Text getName(ItemStack stack) {
        if(name != null) {
            return name;
        }
        return new TranslatableText(this.getTranslationKey(stack));
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

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return this.cooldown;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return toolMaterial.getRepairIngredient().test(ingredient);
    }

    @Override
    public void registerControllers(AnimationData data) {
        @SuppressWarnings({ "unchecked", "rawtypes" })
        AnimationController<JackInTheBoxItem> controller = new AnimationController(this, controllerName, 20,
                this::predicate);

        // Registering a sound listener just makes it so when any sound keyframe is hit
        // the method will be called.
        // To register a particle listener or custom event listener you do the exact
        // same thing, just with registerParticleListener and
        // registerCustomInstructionListener, respectively.
        controller.registerSoundListener(this::soundListener);
        data.addAnimationController(controller);
    }

    @SuppressWarnings("resource")
    private <ENTITY extends IAnimatable> void soundListener(SoundKeyframeEvent<ENTITY> event) {
    }

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.CONTINUE;
    }


    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @SuppressWarnings("resource")
    @Override
    public void onAnimationSync(int id, int state) {
        if (state == ANIM_OPEN) {
            // Always use GeckoLibUtil to get AnimationControllers when you don't have
            // access to an AnimationEvent
            @SuppressWarnings("rawtypes")
            final AnimationController controller = GeckoLibUtil.getControllerForID(this.factory, id, controllerName);

            if (controller.getAnimationState() == AnimationState.Stopped) {
                final ClientPlayerEntity player = MinecraftClient.getInstance().player;
                if (player != null) {
                    player.sendMessage(new LiteralText("Opening the jack in the box!"), true);
                }
                // If you don't do this, the popup animation will only play once because the
                // animation will be cached.
                controller.markNeedsReload();
                // Set the animation to open the JackInTheBoxItem which will start playing music
                // and
                // eventually do the actual animation. Also sets it to not loop
                controller.setAnimation(new AnimationBuilder().addAnimation("Soaryn_chest_popup", false));
            }
        }
    }

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

    @Override
    public Collection<StackPowerUtil.StackPower> getPowers(ItemStack stack, EquipmentSlot slot) {
        List<StackPowerUtil.StackPower> stackPowerList = Lists.newArrayList();
        if(this.item_powers != null) {
            this.item_powers.forEach(item_power -> {
                if (item_power.slot == slot) {
                    stackPowerList.add(item_power);
                }
            });
        }
        return stackPowerList;
    }
}
