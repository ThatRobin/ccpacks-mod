package io.github.ThatRobin.ccpacks.Factories.TaskFactories;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.ThatRobin.ccpacks.DataDrivenClasses.DDSound;
import io.github.ThatRobin.ccpacks.Factories.ContentFactories.ContentFactory;
import io.github.ThatRobin.ccpacks.Factories.ContentFactories.ContentRegistry;
import io.github.ThatRobin.ccpacks.Factories.ContentFactories.ContentType;
import io.github.ThatRobin.ccpacks.Registries.CCPacksRegistries;
import io.github.ThatRobin.ccpacks.Util.GameruleHolder;
import io.github.ThatRobin.ccpacks.Util.Portal;
import io.github.ThatRobin.ccpacks.Util.TypeAttributeHolder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class TaskTypes {

    public TaskTypes(Identifier id, JsonElement je) {
        readPower(id, je, TaskType::new);
    }

    private void readPower(Identifier id, JsonElement je, BiFunction<Identifier, TaskFactory<TaskType>.Instance, TaskType> taskFactory) {
        JsonObject jo = je.getAsJsonObject();
        Identifier factoryId = Identifier.tryParse(JsonHelper.getString(jo, "type"));
        Optional<TaskFactory> contentFactory = CCPacksRegistries.TASK_FACTORY.getOrEmpty(factoryId);
        if (contentFactory.isPresent()) {
            if (CCPacksRegistries.TASK_FACTORY.containsId(factoryId)) {
                contentFactory = CCPacksRegistries.TASK_FACTORY.getOrEmpty(factoryId);
                TaskFactory.Instance factoryInstance = contentFactory.get().read(jo);
                TaskType type = taskFactory.apply(id, factoryInstance);
                if (!TaskRegistry.contains(id)) {
                    TaskRegistry.register(id, type);
                }
            }
        }
    }

}
