package io.github.thatrobin.ccpacks.factories.mechanic_factories;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.thatrobin.ccpacks.util.Mechanic;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.function.BiFunction;

public class MechanicTypes {

    public MechanicTypes(Identifier id, JsonElement je) {
        readPower(id, je, MechanicType::new);
    }

    private void readPower(Identifier id, JsonElement je, BiFunction<Identifier, MechanicFactory<Mechanic>.Instance, MechanicType> taskFactory) {
        JsonObject jo = je.getAsJsonObject();
        Identifier factoryId = Identifier.tryParse(JsonHelper.getString(jo, "type"));
        //Optional<MechanicFactory> contentFactory = CCPacksRegistries.MECHANIC_FACTORY.getOrEmpty(factoryId);
        //if (contentFactory.isPresent()) {
        //    if (CCPacksRegistries.MECHANIC_FACTORY.containsId(factoryId)) {
        //        contentFactory = CCPacksRegistries.MECHANIC_FACTORY.getOrEmpty(factoryId);
        //        MechanicFactory.Instance factoryInstance = contentFactory.get().read(jo);
        //        MechanicType type = taskFactory.apply(id, factoryInstance);
        //        if (!MechanicRegistry.contains(id)) {
        //            MechanicRegistry.register(id, type);



    }

}
