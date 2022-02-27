package io.github.thatrobin.ccpacks.mixins;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.thatrobin.ccpacks.CCPackDataTypes;
import net.minecraft.network.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SerializableData.class)
public class SerializableDataMixin {


    @Inject(method = "read(Lcom/google/gson/JsonObject;)Lio/github/apace100/calio/data/SerializableData$Instance;", at = @At("RETURN"), remap = false, cancellable = true)
    public void read(JsonObject jsonObject, CallbackInfoReturnable<SerializableData.Instance> cir){
        SerializableData.Instance instance = cir.getReturnValue();
        instance.set("ccpacksJsonTextGetter", jsonObject);
        cir.setReturnValue(instance);
    }

}


