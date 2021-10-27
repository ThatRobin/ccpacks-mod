package io.github.ThatRobin.ccpacks.Factories.ContentFactories;

import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistries;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.DDEnchantment;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class EnchantmentFactories {

    public static Identifier identifier(String string) {
        return new Identifier("enchantment", string);
    }

    public static void register() {
        register(new ContentFactory<>(identifier("generic"), Types.ENCHANTMENT,
                new SerializableData()
                        .add("treasure", SerializableDataTypes.BOOLEAN, false)
                        .add("target", SerializableDataType.enumValue(EnchantmentTarget.class), EnchantmentTarget.BREAKABLE)
                        .add("max_level", SerializableDataTypes.INT, 1)
                        .add("rarity", SerializableDataType.enumValue(Enchantment.Rarity.class), Enchantment.Rarity.VERY_RARE),
                data ->
                        (contentType, content) -> {
                            return (Supplier<Enchantment>) () -> new DDEnchantment((Enchantment.Rarity)data.get("rarity"), EnchantmentTarget.BREAKABLE, null, data.getInt("max_level"), false, data.getBoolean("treasure"));
                        }));

        register(new ContentFactory<>(identifier("curse"), Types.ENCHANTMENT,
                new SerializableData()
                        .add("treasure", SerializableDataTypes.BOOLEAN, false)
                        .add("target", SerializableDataType.enumValue(EnchantmentTarget.class), EnchantmentTarget.BREAKABLE)
                        .add("max_level", SerializableDataTypes.INT, 1)
                        .add("rarity", SerializableDataType.enumValue(Enchantment.Rarity.class), Enchantment.Rarity.VERY_RARE),
                data ->
                        (contentType, content) -> {
                            return (Supplier<Enchantment>) () -> new DDEnchantment((Enchantment.Rarity)data.get("rarity"), EnchantmentTarget.BREAKABLE, null, data.getInt("max_level"), true, data.getBoolean("treasure"));
                        }));
    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
