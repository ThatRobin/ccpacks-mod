package io.github.thatrobin.ccpacks.factories.content_factories;

import io.github.thatrobin.ccpacks.registries.CCPacksRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.CountPlacementModifier;
import net.minecraft.world.gen.decorator.HeightRangePlacementModifier;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.function.Supplier;


public class FeatureFactories {
    public static Identifier identifier(String string) {
        return new Identifier("feature", string);
    }

    public static void register() {
        register(new ContentFactory<>(identifier("ore_generation"), Types.FEATURE,
                new SerializableData()
                        .add("block", SerializableDataTypes.BLOCK)
                        .add("vein_size", SerializableDataTypes.INT, 9)
                        .add("veins_per_chunk", SerializableDataTypes.INT, 20)
                        .add("min_height", SerializableDataTypes.INT, 0)
                        .add("max_height", SerializableDataTypes.INT, 64),
                data ->
                        (contentType, content) -> {
                            PlacedFeature EXAMPLE_FEATURE = Feature.ORE
                                    .configure(new OreFeatureConfig(
                                            OreConfiguredFeatures.STONE_ORE_REPLACEABLES,
                                            ((Block)data.get("block")).getDefaultState(),
                                            data.getInt("vein_size")))
                                    .withPlacement(
                                            CountPlacementModifier.of(data.getInt("veins_per_chunk")),
                                            HeightRangePlacementModifier.uniform(YOffset.aboveBottom(data.getInt("min_height")), YOffset.belowTop(data.getInt("max_height")))
                                    );
                            return () -> EXAMPLE_FEATURE;
                        }));

        register(new ContentFactory<>(identifier("tree"), Types.FEATURE,
                new SerializableData()
                        .add("log_block", SerializableDataTypes.BLOCK)
                        .add("leaf_block", SerializableDataTypes.BLOCK)
                        .add("sapling_block", SerializableDataTypes.BLOCK)
                        .add("log_height", SerializableDataTypes.INT, 4)
                        .add("log_variance", SerializableDataTypes.INT, 2)
                        .add("leaf_radius", SerializableDataTypes.INT, 2)
                        .add("leaf_offset", SerializableDataTypes.INT, 0)
                        .add("leaf_height", SerializableDataTypes.INT, 3)
                        .add("chance_per_chunk", SerializableDataTypes.INT, 33),
                data ->
                        (contentType, content) -> {
                            ConfiguredFeature<?, ?> EXAMPLE_FEATURE = Feature.TREE
                                    .configure(new TreeFeatureConfig.Builder(
                                            BlockStateProvider.of(((Block)data.get("log_block")).getDefaultState()),
                                            new StraightTrunkPlacer(data.getInt("log_height"), data.getInt("log_variance"), 0),
                                            BlockStateProvider.of(((Block)data.get("leaf_block")).getDefaultState()),
                                            new BlobFoliagePlacer(ConstantIntProvider.create(data.getInt("leaf_radius")), ConstantIntProvider.create(data.getInt("leaf_offset")), data.getInt("leaf_height")),
                                            new TwoLayersFeatureSize(1, 0, 1)).build()
                                    );
                            return () -> EXAMPLE_FEATURE;
                        }));
    }

    private static void register(ContentFactory<Supplier<?>> serializer) {
        Registry.register(CCPacksRegistries.CONTENT_FACTORY, serializer.getSerializerId(), serializer);
    }
}
