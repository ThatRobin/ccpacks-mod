package io.github.connor3001.ccpacks.middleManTypes;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.connor3001.ccpacks.dataDrivenTypes.DDVSlabBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Consumer;

public class MMVSlabBlock {

    public MMVSlabBlock(Identifier identifier, String material, String tool, String sound, Boolean collision, Integer hardness, Integer resistance, Float slipperiness, int luminance, int miningLevel, Consumer<Entity> action, ConditionFactory<LivingEntity>.Instance condition, Identifier loot){
        Material mat = getMat(material);
        BlockSoundGroup sounds = getSound(sound);
        Tag<Item> tools = getTool(tool);
        DDVSlabBlock EXAMPLE_BLOCK = new DDVSlabBlock(FabricBlockSettings.of(mat).collidable(collision).strength(hardness, resistance).slipperiness(slipperiness).luminance(luminance).sounds(sounds).breakByTool(tools).requiresTool().drops(loot), action, condition);
        Registry.register(Registry.BLOCK, identifier, EXAMPLE_BLOCK);
        Registry.register(Registry.ITEM, identifier, new BlockItem(EXAMPLE_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
    }

    private Tag<Item> getTool(String name){
        Tag<Item> tools = null;
        if(name.toLowerCase().equals("pickaxe")){
            tools = FabricToolTags.PICKAXES;
        } else if(name.toLowerCase().equals("axe")){
            tools = FabricToolTags.AXES;
        } else if(name.toLowerCase().equals("shovel")){
            tools = FabricToolTags.SHOVELS;
        } else if(name.toLowerCase().equals("hoe")){
            tools = FabricToolTags.HOES;
        } else if(name.toLowerCase().equals("shears")){
            tools = FabricToolTags.SHEARS;
        } else {
            tools = FabricToolTags.AXES;
        }
        return tools;
    }

    private BlockSoundGroup getSound(String name){
        BlockSoundGroup sound = null;
        if(name.toLowerCase().equals("bone")){
            sound = BlockSoundGroup.BONE;
        } else if(name.toLowerCase().equals("glass")){
            sound = BlockSoundGroup.GLASS;
        } else if(name.toLowerCase().equals("stone")){
            sound = BlockSoundGroup.STONE;
        } else if(name.toLowerCase().equals("amethyst")){
            sound = BlockSoundGroup.AMETHYST_BLOCK;
        } else if(name.toLowerCase().equals("leaves")){
            sound = BlockSoundGroup.WET_GRASS;
        } else if(name.toLowerCase().equals("wood")){
            sound = BlockSoundGroup.WOOD;
        } else if(name.toLowerCase().equals("snow_block")){
            sound = BlockSoundGroup.SNOW;
        } else if(name.toLowerCase().equals("anvil")){
            sound = BlockSoundGroup.ANVIL;
        } else {
            sound = BlockSoundGroup.BONE;
        }
        return sound;
    }

    private Material getMat(String name){
        Material mat = null;
        if(name.toLowerCase().equals("air")){
            mat = Material.AIR;
        } else if(name.toLowerCase().equals("ice")){
            mat = Material.ICE;
        } else if(name.toLowerCase().equals("stone")){
            mat = Material.STONE;
        } else if(name.toLowerCase().equals("amethyst")){
            mat = Material.AMETHYST;
        } else if(name.toLowerCase().equals("leaves")){
            mat = Material.LEAVES;
        } else if(name.toLowerCase().equals("wood")){
            mat = Material.WOOD;
        } else if(name.toLowerCase().equals("snow_block")){
            mat = Material.SNOW_BLOCK;
        } else if(name.toLowerCase().equals("tnt")){
            mat = Material.TNT;
        } else {
            mat = Material.STONE;
        }
        return mat;
    }
}
