package io.github.thatrobin.ccpacks;

import com.google.gson.JsonParseException;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.calio.Calio;
import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicType;
import io.github.thatrobin.ccpacks.factories.mechanic_factories.MechanicTypeReference;
import io.github.thatrobin.ccpacks.util.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import org.apache.commons.compress.utils.Lists;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CCPackDataTypes {

    public static final SerializableDataType<MechanicTypeReference> MECHANIC_TYPE = SerializableDataType.wrap(
            MechanicTypeReference.class, SerializableDataTypes.IDENTIFIER,
            MechanicType::getIdentifier, MechanicTypeReference::new);

    public static final SerializableDataType<List<MechanicTypeReference>> MECHANIC_TYPES =
            SerializableDataType.list(CCPackDataTypes.MECHANIC_TYPE);

    public static final SerializableDataType<StackPowerExpansion> TRINKET_POWER = SerializableDataType.compound(StackPowerExpansion.class,
            new SerializableData()
                    .add("power", SerializableDataTypes.IDENTIFIER)
                    .add("hidden", SerializableDataTypes.BOOLEAN, false)
                    .add("negative", SerializableDataTypes.BOOLEAN, false),
            (dataInst) -> new StackPowerExpansion(dataInst.getId("power"), dataInst.getBoolean("hidden"), dataInst.getBoolean("negative"), null),
            (data, inst) -> {
                SerializableData.Instance dataInst = data.new Instance();
                dataInst.set("power", inst.powerId);
                dataInst.set("hidden", inst.isHidden);
                dataInst.set("negative", inst.isNegative);
                return dataInst;
            });

    public static final SerializableDataType<List<StackPowerExpansion>> TRINKET_POWERS =
            SerializableDataType.list(CCPackDataTypes.TRINKET_POWER);

    public static final SerializableDataType<StackPowerExpansion> STACK_POWER = SerializableDataType.compound(StackPowerExpansion.class,
            new SerializableData()
                    .add("power", SerializableDataTypes.IDENTIFIER)
                    .add("slot", SerializableDataTypes.EQUIPMENT_SLOT, EquipmentSlot.MAINHAND)
                    .add("hidden", SerializableDataTypes.BOOLEAN, false)
                    .add("negative", SerializableDataTypes.BOOLEAN, false),
            (dataInst) -> new StackPowerExpansion(dataInst.getId("power"), dataInst.getBoolean("hidden"), dataInst.getBoolean("negative"), dataInst.get("slot")),
            (data, inst) -> {
                SerializableData.Instance dataInst = data.new Instance();
                dataInst.set("power", inst.powerId);
                dataInst.set("slot", inst.slot);
                dataInst.set("hidden", inst.isHidden);
                dataInst.set("negative", inst.isNegative);
                return dataInst;
            });

    public static final SerializableDataType<List<StackPowerExpansion>> STACK_POWERS =
            SerializableDataType.list(CCPackDataTypes.STACK_POWER);

    public static final SerializableDataType<List<String>> STRINGS =
            SerializableDataType.list(SerializableDataTypes.STRING);

    public static final SerializableDataType<ItemGroups> ITEM_GROUP = SerializableDataType.enumValue(ItemGroups.class);

    public static final SerializableDataType<RenderLayerTypes> RENDER_LAYER = SerializableDataType.enumValue(RenderLayerTypes.class);

    public static final SerializableDataType<BlockItemHolder> ITEM = SerializableDataType.compound(BlockItemHolder.class,
            new SerializableData()
                    .add("item_group", CCPackDataTypes.ITEM_GROUP, ItemGroups.MISC)
                    .add("name", SerializableDataTypes.TEXT, null)
                    .add("lore", CCPackDataTypes.STRINGS, null)
                    .add("item_modifiers", SerializableDataTypes.IDENTIFIERS, null)
                    .add("fuel_tick", SerializableDataTypes.INT, 0)
                    .add("item_powers", CCPackDataTypes.STACK_POWERS, null)
                    .add("max_count", SerializableDataTypes.INT, 64),
            (data) -> {
                FabricItemSettings settings = new FabricItemSettings();
                ItemGroups group = data.get("item_group");
                settings.group(group.group);
                return new BlockItemHolder(settings.maxCount(data.getInt("max_count")), data.get("name"), data.get("lore"), data.get("item_modifiers"), data.getInt("fuel_tick"));
            },
            (data, inst) -> data.new Instance());



    public static final SerializableDataType<ColourHolder> COLOUR = SerializableDataType.compound(ColourHolder.class, new
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
                    dataInst.get("break_sound"),
                    dataInst.get("step_sound"),
                    dataInst.get("place_sound"),
                    dataInst.get("hit_sound"),
                    dataInst.get("fall_sound")),
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
            (dataInst) -> new Material(MapColor.BLACK,
                    dataInst.getBoolean("liquid"),
                    dataInst.getBoolean("solid"),
                    dataInst.getBoolean("blocks_movement"),
                    dataInst.getBoolean("blocks_light"),
                    false,
                    dataInst.getBoolean("burnable"),
                    dataInst.get("piston_behaviour")),
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

    public static final SerializableDataType<List<EntityType<?>>> ENTITY_ENTRY = SerializableDataType.compound(ClassUtil.castClass(List.class),
            new SerializableData()
                    .add("entity", SerializableDataTypes.ENTITY_TYPE, null)
                    .add("tag", SerializableDataTypes.ENTITY_TAG, null),
            dataInstance -> {
                boolean tagPresent = dataInstance.isPresent("tag");
                boolean itemPresent = dataInstance.isPresent("entity");
                if(tagPresent == itemPresent) {
                    throw new JsonParseException("An entity entry is either a tag or an entity, " + (tagPresent ? "not both" : "one has to be provided."));
                }
                if(tagPresent) {
                    TagKey<EntityType<?>> tag = dataInstance.get("tag");
                    var entryList = Registry.ENTITY_TYPE.getEntryList(tag);
                    if (entryList.isPresent()) {
                        return entryList.get().stream().map(RegistryEntry::value).collect(Collectors.toList());
                    } else {
                        return List.of();
                    }
                } else {
                    return List.of((EntityType<?>)dataInstance.get("entity"));
                }
            }, (data, entities) -> {
                SerializableData.Instance inst = data.new Instance();
                if(entities.size() == 1) {
                    inst.set("entity", entities.get(0));
                } else {
                    var itemTags = Registry.ITEM.streamTags();
                    //filter out any tags where the entries do not completely match the items list
                    itemTags = itemTags.filter(tag -> {
                        var entryList = Registry.ITEM.getEntryList(tag);
                        if (entryList.isPresent()) {
                            var tagItems = entryList.get().stream().map(RegistryEntry::value).toList();
                            return entities.equals(tagItems);
                        }
                        return false;
                    });
                    //2 tags contain the same items. panic.
                    if(itemTags.count() != 1) {
                        throw new IllegalStateException("Couldn't transform entity list to a single tag");
                    }
                    inst.set("tag", itemTags.findFirst().get().id());
                }
                return inst;
            });

}