package io.github.ThatRobin.ccpacks.serializableData;

import com.google.gson.JsonParseException;
import io.github.ThatRobin.ccpacks.CCPacksMain;
import io.github.ThatRobin.ccpacks.util.AdvancedHudRender;
import io.github.ThatRobin.ccpacks.util.Choice;
import io.github.ThatRobin.ccpacks.util.ColourHolder;
import io.github.ThatRobin.ccpacks.util.Outcome;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.Calio;
import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.fabricmc.fabric.api.biome.v1.OverworldClimate;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricMaterialBuilder;
import net.fabricmc.fabric.mixin.object.builder.MaterialBuilderAccessor;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
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

    public static final SerializableDataType<List<Double>> DOUBLES =
            SerializableDataType.list(SerializableDataTypes.DOUBLE);

    public static final SerializableDataType<OverworldClimate> OVERWORLD_CLIMATE =
            SerializableDataType.enumValue(OverworldClimate.class);

    public static final SerializableDataType<List<OverworldClimate>> OVERWORLD_CLIMATES =
            SerializableDataType.list(CCPackDataTypes.OVERWORLD_CLIMATE);

    public static final SerializableDataType<AdvancedHudRender> HUD_RENDER = SerializableDataType.compound(AdvancedHudRender.class, new
                    SerializableData()
                    .add("should_render", SerializableDataTypes.BOOLEAN, true)
                    .add("bar_index", SerializableDataTypes.INT, 0)
                    .add("side", SerializableDataTypes.STRING, "right")
                    .add("sprite_location", SerializableDataTypes.IDENTIFIER, new Identifier("ccpacks", "textures/gui/icons.png"))
                    .add("condition", ApoliDataTypes.ENTITY_CONDITION, null),
            (dataInst) -> new AdvancedHudRender(
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
                    .add("red", SerializableDataTypes.INT, 1)
                    .add("green", SerializableDataTypes.INT, 1)
                    .add("blue", SerializableDataTypes.INT, 1),
            (dataInst) -> new ColourHolder(
                    dataInst.getInt("red"),
                    dataInst.getInt("green"),
                    dataInst.getInt("blue")),
            (data, inst) -> {
                SerializableData.Instance dataInst = data.new Instance();
                dataInst.set("red", inst.getRed());
                dataInst.set("green", inst.getGreen());
                dataInst.set("blue", inst.getBlue());
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
                    .add("allow_light", SerializableDataTypes.BOOLEAN, false),
            (dataInst) -> {
                FabricMaterialBuilder mat = new FabricMaterialBuilder(MapColor.BLACK);
                if(dataInst.getBoolean("allow_light")){
                    mat.lightPassesThrough();
                }
                if(dataInst.getBoolean("allow_movement")){
                    mat.allowsMovement();
                }
                if(dataInst.getBoolean("blocks_pistons")){
                    mat.blocksPistons();
                }
                if(dataInst.getBoolean("burnable")){
                    mat.burnable();
                }
                if(dataInst.getBoolean("destroyed_by_piston")){
                    mat.destroyedByPiston();
                }
                if(dataInst.getBoolean("liquid")){
                    mat.liquid();
                }
                if(dataInst.getBoolean("not_solid")){
                    mat.notSolid();
                }
                if(dataInst.getBoolean("replaceable")){
                    mat.replaceable();
                }
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
                boolean itemPresent = dataInstance.isPresent("entity");
                if(tagPresent == itemPresent) {
                    throw new JsonParseException("An ingredient entry is either a tag or an item, " + (tagPresent ? "not both" : "one has to be provided."));
                }
                if(tagPresent) {
                    Tag<EntityType> tag = (Tag<EntityType>)dataInstance.get("tag");
                    return List.copyOf(tag.values());
                } else {
                    return List.of((EntityType)dataInstance.get("entity"));
                }
            }, (data, entities) -> {
                SerializableData.Instance inst = data.new Instance();
                if(entities.size() == 1) {
                    inst.set("item", entities.get(0));
                } else {
                    TagManager tagManager = Calio.getTagManager();
                    TagGroup<EntityType<?>> tagGroup = tagManager.getOrCreateTagGroup(Registry.ENTITY_TYPE_KEY);
                    Collection<Identifier> possibleTags = tagGroup.getTagsFor(entities.get(0));
                    for(int i = 1; i < entities.size() && possibleTags.size() > 1; i++) {
                        possibleTags.removeAll(tagGroup.getTagsFor(entities.get(i)));
                    }
                    if(possibleTags.size() != 1) {
                        throw new IllegalStateException("Couldn't transform item list to a single tag");
                    }
                    inst.set("tag", tagGroup.getTag(possibleTags.stream().findFirst().get()));
                }
                return inst;
            });

    public static final SerializableDataType<List<Choice>> CHOICES =
            SerializableDataType.list(CCPackDataTypes.CHOICE);

    public static final SerializableDataType<Choice> CHOICE = SerializableDataType.compound(Choice.class, new SerializableData()
            .add("flavour_text", SerializableDataTypes.STRING, "")
            .add("outcome_list", CCPackDataTypes.OUTCOMES),
            dataInstance -> {
                if (dataInstance.get("outcome_list") != null) {
                    Choice choice = new Choice(dataInstance.getString("flavour_text"), (Outcome) dataInstance.get("outcome_list"));
                    return choice;
                }
                return new Choice("tester", new Outcome("tester2"));
            }, (data, choice) -> {
                SerializableData.Instance inst = data.new Instance();
                inst.set("flavour_text", choice.getText());
                inst.set("outcome_list", choice.getOutcomes());
                return inst;
            });

    public static final SerializableDataType<List<Outcome>> OUTCOMES =
            SerializableDataType.list(CCPackDataTypes.OUTCOME);


    public static final SerializableDataType<Outcome> OUTCOME = SerializableDataType.compound(Outcome.class, new SerializableData()
                    .add("power", ApoliDataTypes.POWER_TYPE, null)
                    .add("text", SerializableDataTypes.STRING, null),
            dataInstance -> {
                boolean powerPresent = dataInstance.isPresent("power");
                boolean textPresent = dataInstance.isPresent("text");
                if(powerPresent == textPresent) {
                    throw new JsonParseException("An ingredient entry is either a power or text, " + (powerPresent ? "not both" : "one has to be provided."));
                }
                if(textPresent) {
                    return new Outcome(dataInstance.getString("text"));
                } else {
                    return new Outcome((PowerTypeReference)dataInstance.get("power"));
                }
            }, (data, outcome) -> {
                SerializableData.Instance inst = data.new Instance();
                inst.set("text", outcome.getText());
                inst.set("power", outcome.getPower());
                return inst;
            });



}