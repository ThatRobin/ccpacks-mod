package io.github.connor3001.ccpacks.middleManTypes;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MMFoodItem {

    public MMFoodItem(Identifier identifier, Integer maxCount, Integer hunger, Float saturation, Boolean meat, Boolean snack, Boolean alwaysEdible) {
        FoodComponent.Builder food = new FoodComponent.Builder().hunger(hunger).saturationModifier(saturation);
        if(meat) {
            food.meat();
        }
        if(snack){
            food.snack();
        }
        if(alwaysEdible){
            food.alwaysEdible();
        }
        FoodComponent foodComp = food.build();
        Item EXAMPLE_ITEM = new Item(new FabricItemSettings().group(ItemGroup.FOOD).maxCount(maxCount).food(foodComp));
        Registry.register(Registry.ITEM, identifier, EXAMPLE_ITEM);
    }
}