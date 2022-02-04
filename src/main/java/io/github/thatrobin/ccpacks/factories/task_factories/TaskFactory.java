package io.github.thatrobin.ccpacks.factories.task_factories;

import com.google.gson.JsonObject;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;
import java.util.function.Function;

public class TaskFactory<G extends TaskType> {

        private final Identifier id;
        protected SerializableData data;
        protected Function<SerializableData.Instance, BiFunction<Identifier, TaskFactory<TaskType>.Instance, G>>  factoryConstructor;

        public TaskFactory(Identifier id, SerializableData data, Function<SerializableData.Instance, BiFunction<Identifier, TaskFactory<TaskType>.Instance, G>> factoryConstructor) {
            this.id = id;
            this.data = data;
            this.factoryConstructor = factoryConstructor;
        }

        public Identifier getSerializerId() {
            return id;
        }

        public class Instance implements BiFunction<Identifier, TaskFactory<TaskType>.Instance, G> {

            private final SerializableData.Instance dataInstance;
            public Item item;

            private Instance(SerializableData.Instance data) {
                this.dataInstance = data;
            }

            public Item getItem() {
                return this.item;
            }

            @Override
            public G apply(Identifier identifier, TaskFactory<TaskType>.Instance factory) {
                BiFunction<Identifier, TaskFactory<TaskType>.Instance, G> itemFactory = factoryConstructor.apply(dataInstance);
                return itemFactory.apply(identifier, factory);
            }
        }

        public Instance read(JsonObject json) {
            return new Instance(data.read(json));
        }

        public Instance read(PacketByteBuf buffer) {
            return new Instance(data.read(buffer));
        }
}
