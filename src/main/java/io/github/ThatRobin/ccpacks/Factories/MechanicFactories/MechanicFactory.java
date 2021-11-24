package io.github.ThatRobin.ccpacks.Factories.MechanicFactories;

import com.google.gson.JsonObject;
import io.github.ThatRobin.ccpacks.Util.Mechanic;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;
import java.util.function.Function;

public class MechanicFactory<P extends Mechanic> {

        private final Identifier id;
        protected SerializableData data;
        protected Function<SerializableData.Instance, BiFunction<MechanicType<P>, BlockEntity, P>>  factoryConstructor;

        public MechanicFactory(Identifier id, SerializableData data, Function<SerializableData.Instance, BiFunction<MechanicType<P>, BlockEntity, P>> factoryConstructor) {
            this.id = id;
            this.data = data;
            this.factoryConstructor = factoryConstructor;
        }

        public Identifier getSerializerId() {
            return id;
        }

        public class Instance implements BiFunction<MechanicType<P>, BlockEntity, P> {

            private final SerializableData.Instance dataInstance;
            public Item item;

            private Instance(SerializableData.Instance data) {
                this.dataInstance = data;
            }

            public void write(PacketByteBuf buf) {
                buf.writeIdentifier(id);
                data.write(buf, dataInstance);
            }

            @Override
            public P apply(MechanicType<P> mechanicType, BlockEntity blockEntity) {
                BiFunction<MechanicType<P>, BlockEntity, P> mechanicFactory = factoryConstructor.apply(dataInstance);
                P p = mechanicFactory.apply(mechanicType, blockEntity);
                return p;
            }
        }

        public Instance read(JsonObject json) {
            return new Instance(data.read(json));
        }

        public Instance read(PacketByteBuf buffer) {
            return new Instance(data.read(buffer));
        }
}
