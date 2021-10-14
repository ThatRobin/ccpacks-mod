package io.github.ThatRobin.ccpacks.dataDrivenTypes.Items;

import io.github.ThatRobin.ccpacks.CCPacksMain;
import net.minecraft.item.Item;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;

public class DDMusicDiscItem extends MusicDiscItem {
    public DDMusicDiscItem(int comparatorOutput, SoundEvent sound, Settings settings) {
        super(comparatorOutput, sound, settings);
    }

}
