package io.github.thatrobin.ccpacks.data_driven_classes.items;

import com.google.common.collect.Lists;
import io.github.apace100.apoli.mixin.EyeHeightAccess;
import io.github.apace100.apoli.util.PowerGrantingItem;
import io.github.apace100.apoli.util.StackPowerUtil;
import io.github.thatrobin.ccpacks.util.ColourHolder;
import io.github.thatrobin.ccpacks.util.StackPowerExpansion;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class DDPullbackItem extends BowItem implements PowerGrantingItem {

    public EntityType entityType;
    private final List<StackPowerExpansion> item_powers;
    public List<String> lore;
    private final ColourHolder startColours;
    private final ColourHolder endColours;
    private final LiteralText name;

    public DDPullbackItem(Settings settings, LiteralText name, List<String> lore, List<StackPowerExpansion> item_powers, EntityType entityType) {
        this(settings, name, lore, null, null, item_powers, entityType);
    }


    public DDPullbackItem(Settings settings, LiteralText name, List<String> lore, ColourHolder startColours, ColourHolder endColours, List<StackPowerExpansion> item_powers, EntityType entityType) {
        super(settings);
        this.name = name;
        this.lore = lore;
        this.startColours = startColours;
        this.endColours = endColours;
        this.item_powers = item_powers;
        this.entityType = entityType;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity)user;

            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            float f = getPullProgress(i);
            if (f > 0.9f) {
                if(entityType != null) {
                    fireProjectile(playerEntity);
                }
            }
        }
    }

    private void fireProjectile(PlayerEntity playerEntity) {
        Entity entity = entityType.create(playerEntity.world);
        if (entity == null) {
            return;
        }
        Vec3d rotationVector = playerEntity.getRotationVector();
        float yaw = playerEntity.getYaw();
        float pitch = playerEntity.getPitch();
        Vec3d spawnPos = playerEntity.getPos().add(0, ((EyeHeightAccess) playerEntity).callGetEyeHeight(playerEntity.getPose(), playerEntity.getDimensions(playerEntity.getPose())), 0).add(rotationVector);
        entity.refreshPositionAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), pitch, yaw);
        if (entity instanceof ProjectileEntity) {
            if (entity instanceof ExplosiveProjectileEntity) {
                ExplosiveProjectileEntity explosiveProjectileEntity = (ExplosiveProjectileEntity) entity;
                explosiveProjectileEntity.powerX = rotationVector.x * 1.5F;
                explosiveProjectileEntity.powerY = rotationVector.y * 1.5F;
                explosiveProjectileEntity.powerZ = rotationVector.z * 1.5F;
            }
            ProjectileEntity projectile = (ProjectileEntity) entity;
            projectile.setOwner(playerEntity);
            projectile.setVelocity(playerEntity, pitch, yaw, 0F, 1.5F, 0);
        } else {
            float f = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
            float g = -MathHelper.sin(pitch * 0.017453292F);
            float h = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
            Vec3d vec3d = (new Vec3d(f, g, h)).normalize().add(playerEntity.getRandom().nextGaussian() * 0.007499999832361937D, playerEntity.getRandom().nextGaussian() * 0.007499999832361937D, playerEntity.getRandom().nextGaussian() * 0.007499999832361937D).multiply((double) 1.5F);
            entity.setVelocity(vec3d);
            Vec3d entityVelo = playerEntity.getVelocity();
            entity.setVelocity(entity.getVelocity().add(entityVelo.x, playerEntity.isOnGround() ? 0.0D : entityVelo.y, entityVelo.z));
        }
        playerEntity.world.spawnEntity(entity);
    }


    public static float getPullProgress(int useTicks) {
        float f = (float)useTicks / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }


    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.fail(stack);
    }

    public Predicate<ItemStack> getProjectiles() {
        return null;
    }

    public int getRange() {
        return 15;
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
