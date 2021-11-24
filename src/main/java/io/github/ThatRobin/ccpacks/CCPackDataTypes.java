package io.github.ThatRobin.ccpacks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicRegistry;
import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicType;
import io.github.ThatRobin.ccpacks.Factories.MechanicFactories.MechanicTypeReference;
import io.github.ThatRobin.ccpacks.Util.*;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.Calio;
import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricMaterialBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.enums.WallShape;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagGroup;
import net.minecraft.tag.TagManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CCPackDataTypes {

    public static final SerializableDataType<MechanicTypeReference> MECHANIC_TYPE = SerializableDataType.wrap(
            MechanicTypeReference.class, SerializableDataTypes.IDENTIFIER,
            MechanicType::getIdentifier, MechanicTypeReference::new);

    public static final SerializableDataType<List<MechanicTypeReference>> MECHANIC_TYPES =
            SerializableDataType.list(CCPackDataTypes.MECHANIC_TYPE);

    public static final SerializableDataType<List<String>> STRINGS =
            SerializableDataType.list(SerializableDataTypes.STRING);

    public static final SerializableDataType<ToolTypes> TOOL_TYPES = SerializableDataType.enumValue(ToolTypes.class);

    public static final SerializableDataType<ItemGroups> ITEM_GROUP = SerializableDataType.enumValue(ItemGroups.class);

    public static final SerializableDataType<RenderLayerTypes> RENDER_LAYER = SerializableDataType.enumValue(RenderLayerTypes.class);

    public static final SerializableDataType<VoxelInfo> BLOCK_STATE = SerializableDataType.compound(VoxelInfo.class,
            new SerializableData()
                    .add("name", SerializableDataTypes.STRING)
                    .add("base_value", SerializableDataTypes.BOOLEAN, false)
                    .add("from", SerializableDataType.list(SerializableDataTypes.FLOAT), null)
                    .add("to", SerializableDataType.list(SerializableDataTypes.FLOAT), null),
            (dataInst) -> {
                VoxelInfo info = new VoxelInfo();
                info.from = (List<Float>)dataInst.get("from");
                info.to = (List<Float>)dataInst.get("to");
                info.property = BooleanProperty.of(dataInst.getString("name"));
                info.base = dataInst.getBoolean("base_value");
                return info;
            },
            (data, inst) -> {
                SerializableData.Instance dataInst = data.new Instance();
                dataInst.set("from", inst.from);
                dataInst.set("to", inst.to);
                return dataInst;
            });

    public static final SerializableDataType<List<VoxelInfo>> BLOCK_STATES =
            SerializableDataType.list(CCPackDataTypes.BLOCK_STATE);

    public static final SerializableDataType<StatBarHudRender> STAT_BAR_HUD_RENDER = SerializableDataType.compound(StatBarHudRender.class, new
                    SerializableData()
                    .add("should_render", SerializableDataTypes.BOOLEAN, true)
                    .add("bar_index", SerializableDataTypes.INT, 0)
                    .add("side", SerializableDataTypes.STRING, "right")
                    .add("sprite_location", SerializableDataTypes.IDENTIFIER, new Identifier("ccpacks", "textures/gui/icons.png"))
                    .add("condition", ApoliDataTypes.ENTITY_CONDITION, null),
            (dataInst) -> new StatBarHudRender(
                    dataInst.getBoolean("should_render"),
                    dataInst.getInt("bar_index"),
                    dataInst.getId("sprite_location"),
                    (ConditionFactory<LivingEntity>.Instance)dataInst.get("condition"),
                    dataInst.getString("side")),
            (data, inst) -> {
                SerializableData.Instance dataInst = data.new Instance();
                dataInst.set("should_render", inst.shouldRender());
                dataInst.set("bar_index", inst.getBarIndex());
                dataInst.set("sprite_location", inst.getSpriteLocation());
                dataInst.set("condition", inst.getCondition());
                dataInst.set("side", inst.getSide());
                return dataInst;
            });

    public static final SerializableDataType<ColourHolder> COLOR = SerializableDataType.compound(ColourHolder.class, new
                    SerializableData()
                    .add("red", SerializableDataTypes.FLOAT, 1f)
                    .add("green", SerializableDataTypes.FLOAT, 1f)
                    .add("blue", SerializableDataTypes.FLOAT, 1f)
                    .add("alpha", SerializableDataTypes.FLOAT, 1f),
            (dataInst) -> new ColourHolder(
                    dataInst.getFloat("red"),
                    dataInst.getFloat("green"),
                    dataInst.getFloat("blue"),
                    dataInst.getFloat("alpha")),
            (data, inst) -> {
                SerializableData.Instance dataInst = data.new Instance();
                dataInst.set("red", inst.getRed());
                dataInst.set("green", inst.getGreen());
                dataInst.set("blue", inst.getBlue());
                dataInst.set("alpha", inst.getAlpha());
                return dataInst;
            });

    public static final SerializableDataType<DefaultAttributeContainer.Builder> ENTITY_ATTRIBUTES = SerializableDataType.compound(DefaultAttributeContainer.Builder.class,
            new SerializableData()
                    .add("generic.max_health", SerializableDataTypes.DOUBLE, 20.0D)
                    .add("generic.follow_range", SerializableDataTypes.DOUBLE, 32.0D)
                    .add("generic.knockback_resistance", SerializableDataTypes.DOUBLE, 0.0D)
                    .add("generic.movement_speed", SerializableDataTypes.DOUBLE, 0.7D)
                    .add("generic.attack_damage", SerializableDataTypes.DOUBLE, 0.4D)
                    .add("generic.armor", SerializableDataTypes.DOUBLE, 0.0D)
                    .add("generic.armor_toughness", SerializableDataTypes.DOUBLE, 0.0D)
                    .add("generic.attack_knockback", SerializableDataTypes.DOUBLE, 0.0D),
            (dataInst) -> {
                DefaultAttributeContainer.Builder builder = MobEntity.createLivingAttributes();
                dataInst.ifPresent("generic.max_health", (doubleVar) -> builder.add(EntityAttributes.GENERIC_MAX_HEALTH,(double)doubleVar));
                dataInst.ifPresent("generic.follow_range", (doubleVar) -> builder.add(EntityAttributes.GENERIC_FOLLOW_RANGE,(double)doubleVar));
                dataInst.ifPresent("generic.knockback_resistance", (doubleVar) -> builder.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,(double)doubleVar));
                dataInst.ifPresent("generic.movement_speed", (doubleVar) -> builder.add(EntityAttributes.GENERIC_MOVEMENT_SPEED,(double)doubleVar));
                dataInst.ifPresent("generic.attack_damage", (doubleVar) -> builder.add(EntityAttributes.GENERIC_ATTACK_DAMAGE,(double)doubleVar));
                dataInst.ifPresent("generic.armor", (doubleVar) -> builder.add(EntityAttributes.GENERIC_ARMOR,(double)doubleVar));
                dataInst.ifPresent("generic.armor_toughness", (doubleVar) -> builder.add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS,(double)doubleVar));
                dataInst.ifPresent("generic.attack_knockback", (doubleVar) -> builder.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK,(double)doubleVar));
                return builder;
            },
            (data, inst) -> {
                SerializableData.Instance dataInst = data.new Instance();
                if(inst.build().has(EntityAttributes.GENERIC_MAX_HEALTH))
                    dataInst.set("generic.max_health", inst.build().getBaseValue(EntityAttributes.GENERIC_MAX_HEALTH));
                if(inst.build().has(EntityAttributes.GENERIC_FOLLOW_RANGE))
                    dataInst.set("generic.follow_range", inst.build().getBaseValue(EntityAttributes.GENERIC_FOLLOW_RANGE));
                if(inst.build().has(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE))
                    dataInst.set("generic.knockback_resistance", inst.build().getBaseValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                if(inst.build().has(EntityAttributes.GENERIC_MOVEMENT_SPEED))
                    dataInst.set("generic.movement_speed", inst.build().getBaseValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
                if(inst.build().has(EntityAttributes.GENERIC_ATTACK_DAMAGE))
                    dataInst.set("generic.attack_damage", inst.build().getBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
                if(inst.build().has(EntityAttributes.GENERIC_ARMOR))
                    dataInst.set("generic.armor", inst.build().getBaseValue(EntityAttributes.GENERIC_ARMOR));
                if(inst.build().has(EntityAttributes.GENERIC_ARMOR_TOUGHNESS))
                    dataInst.set("generic.armor_toughness", inst.build().getBaseValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS));
                if(inst.build().has(EntityAttributes.GENERIC_ATTACK_KNOCKBACK))
                    dataInst.set("generic.attack_knockback", inst.build().getBaseValue(EntityAttributes.GENERIC_ATTACK_KNOCKBACK));
                return dataInst;
            });

    public static final SerializableDataType<SpawnGroup> SPAWN_GROUP = SerializableDataType.enumValue(SpawnGroup.class);

    public static final SerializableDataType<BlockSoundGroup> BLOCK_SOUND_GROUP = SerializableDataType.compound(BlockSoundGroup.class, new
                    SerializableData()
                    .add("pitch", SerializableDataTypes.INT, 1)
                    .add("volume", SerializableDataTypes.INT, 1)
                    .add("break_sound",SerializableDataTypes.SOUND_EVENT)
                    .add("step_sound",SerializableDataTypes.SOUND_EVENT)
                    .add("place_sound",SerializableDataTypes.SOUND_EVENT)
                    .add("hit_sound",SerializableDataTypes.SOUND_EVENT)
                    .add("fall_sound",SerializableDataTypes.SOUND_EVENT),
            (dataInst) -> new BlockSoundGroup(
                    dataInst.getInt("pitch"),
                    dataInst.getInt("volume"),
                    (SoundEvent) dataInst.get("break_sound"),
                    (SoundEvent) dataInst.get("step_sound"),
                    (SoundEvent) dataInst.get("place_sound"),
                    (SoundEvent) dataInst.get("hit_sound"),
                    (SoundEvent) dataInst.get("fall_sound")),
            (data, inst) -> {
                SerializableData.Instance dataInst = data.new Instance();
                dataInst.set("pitch", inst.getPitch());
                dataInst.set("volume", inst.getVolume());
                dataInst.set("break_sound", inst.getBreakSound());
                dataInst.set("step_sound", inst.getStepSound());
                dataInst.set("place_sound", inst.getPlaceSound());
                dataInst.set("hit_sound", inst.getHitSound());
                dataInst.set("fall_sound", inst.getFallSound());
                return dataInst;
            });

    public static final SerializableDataType<Material> MATERIAL = SerializableDataType.compound(Material.class,
            new SerializableData()
                    .add("liquid", SerializableDataTypes.BOOLEAN, false)
                    .add("solid", SerializableDataTypes.BOOLEAN, true)
                    .add("blocks_movement", SerializableDataTypes.BOOLEAN, true)
                    .add("blocks_light", SerializableDataTypes.BOOLEAN, true)
                    .add("burnable", SerializableDataTypes.BOOLEAN, false)
                    .add("piston_behaviour", SerializableDataType.enumValue(PistonBehavior.class), PistonBehavior.NORMAL),
            (dataInst) -> {
                Material mat = new Material(MapColor.BLACK,
                        dataInst.getBoolean("liquid"),
                        dataInst.getBoolean("solid"),
                        dataInst.getBoolean("blocks_movement"),
                        dataInst.getBoolean("blocks_light"),
                        false,
                        dataInst.getBoolean("burnable"),
                        (PistonBehavior) dataInst.get("piston_behaviour"));
                return mat;
            },
            (data, inst) -> {
                SerializableData.Instance dataInst = data.new Instance();
                dataInst.set("liquid", inst.isLiquid());
                dataInst.set("solid", inst.isSolid());
                dataInst.set("blocks_movement", inst.blocksMovement());
                dataInst.set("blocks_light", inst.blocksLight());
                dataInst.set("burnable", inst.isBurnable());
                dataInst.set("piston_behaviour", inst.getPistonBehavior());
                return dataInst;
            });

    public static final SerializableDataType<List<EntityType>> ENTITY_ENTRY = SerializableDataType.compound(ClassUtil.castClass(List.class),
            new SerializableData()
                    .add("entity", SerializableDataTypes.ENTITY_TYPE, null)
                    .add("tag", SerializableDataTypes.ENTITY_TAG, null),
            dataInstance -> {
                boolean tagPresent = dataInstance.isPresent("tag");
                boolean entityPresent = dataInstance.isPresent("entity");
                if(tagPresent == entityPresent) {
                    throw new JsonParseException("An ingredient entry is either a tag or an entity, " + (tagPresent ? "not both" : "one has to be provided."));
                }
                if(tagPresent) {
                    Tag<EntityType> tag = (Tag<EntityType>)dataInstance.get("tag");
                    return List.copyOf(tag.values());
                } else {
                    return List.of((EntityType<? extends net.minecraft.entity.Entity>)dataInstance.get("entity"));
                }
            }, (data, entities) -> {
                SerializableData.Instance inst = data.new Instance();
                if(entities.size() == 1) {
                    inst.set("entity", entities.get(0));
                } else {
                    TagManager tagManager = Calio.getTagManager();
                    TagGroup<EntityType<?>> tagGroup = tagManager.getOrCreateTagGroup(Registry.ENTITY_TYPE_KEY);
                    Collection<Identifier> possibleTags = tagGroup.getTagsFor(entities.get(0));
                    for(int i = 1; i < entities.size() && possibleTags.size() > 1; i++) {
                        possibleTags.removeAll(tagGroup.getTagsFor(entities.get(i)));
                    }
                    if(possibleTags.size() != 1) {
                        throw new IllegalStateException("Couldn't transform entity list to a single tag");
                    }
                    inst.set("tag", tagGroup.getTag(possibleTags.stream().findFirst().get()));
                }
                return inst;
            });

}