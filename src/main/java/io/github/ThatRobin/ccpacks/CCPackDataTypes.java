package io.github.ThatRobin.ccpacks;

import com.google.gson.JsonParseException;
import io.github.ThatRobin.ccpacks.Util.ColourHolder;
import io.github.ThatRobin.ccpacks.Util.ItemGroups;
import io.github.ThatRobin.ccpacks.Util.StatBarHudRender;
import io.github.ThatRobin.ccpacks.Util.ToolTypes;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.Calio;
import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricMaterialBuilder;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagGroup;
import net.minecraft.tag.TagManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Collection;
import java.util.List;

public class CCPackDataTypes {

    public static final SerializableDataType<List<String>> STRINGS =
            SerializableDataType.list(SerializableDataTypes.STRING);

    public static final SerializableDataType<ToolTypes> TOOL_TYPES = SerializableDataType.enumValue(ToolTypes.class);

    public static final SerializableDataType<ItemGroups> ITEM_GROUP = SerializableDataType.enumValue(ItemGroups.class);

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
                    .add("allow_light", SerializableDataTypes.BOOLEAN, false)
                    .add("allow_movement", SerializableDataTypes.BOOLEAN, false)
                    .add("blocks_pistons", SerializableDataTypes.BOOLEAN, false)
                    .add("burnable", SerializableDataTypes.BOOLEAN, false)
                    .add("destroyed_by_piston", SerializableDataTypes.BOOLEAN, false)
                    .add("liquid", SerializableDataTypes.BOOLEAN, false)
                    .add("not_solid", SerializableDataTypes.BOOLEAN, false)
                    .add("replaceable", SerializableDataTypes.BOOLEAN, false),
            (dataInst) -> {
                FabricMaterialBuilder mat = new FabricMaterialBuilder(MapColor.BLACK);
                dataInst.ifPresent("allow_light", (d) -> mat.lightPassesThrough());
                dataInst.ifPresent("allow_movement", (d) -> mat.allowsMovement());
                dataInst.ifPresent("blocks_pistons", (d) -> mat.blocksPistons());
                dataInst.ifPresent("burnable", (d) -> mat.burnable());
                dataInst.ifPresent("destroyed_by_piston", (d) -> mat.destroyedByPiston());
                dataInst.ifPresent("liquid", (d) -> mat.liquid());
                dataInst.ifPresent("not_solid", (d) -> mat.notSolid());
                dataInst.ifPresent("replaceable", (d) -> mat.replaceable());
                return mat.build();
            },
            (data, inst) -> {
                SerializableData.Instance dataInst = data.new Instance();
                dataInst.set("allow_light", !inst.blocksLight());
                dataInst.set("allow_movement", !inst.blocksMovement());
                dataInst.set("burnable", inst.isBurnable());
                dataInst.set("liquid", inst.isLiquid());
                dataInst.set("not_solid", !inst.isSolid());
                dataInst.set("replaceable", inst.isReplaceable());
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