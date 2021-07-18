package io.github.ThatRobin.ccpacks.dataDrivenTypes.Items;

import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;

public class DDMusicDiscItem extends MusicDiscItem {
    public DDMusicDiscItem(int comparatorOutput, SoundEvent sound, Settings settings) {
        super(comparatorOutput, sound, settings);
    }
}
