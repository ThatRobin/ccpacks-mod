package io.github.thatrobin.ccpacks.data_driven_classes.items;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.LiteralText;
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

public class DDShieldItem extends ShieldItem implements IAnimatable, ISyncable {

    private final AnimationFactory factory = new AnimationFactory(this);
    private static final String controllerName = "popupController";
    private static final int ANIM_OPEN = 0;
    private final int cooldown;
    private final ToolMaterial toolMaterial;

    public DDShieldItem(Settings settings, int cooldown, ToolMaterial toolMaterial) {
        super(settings);
        this.cooldown = cooldown;
        this.toolMaterial = toolMaterial;
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
}
