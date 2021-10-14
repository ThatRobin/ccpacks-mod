package io.github.ThatRobin.ccpacks.Factories.ContentFactories;

import com.google.gson.JsonObject;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class ContentFactory<I extends Supplier> {

        private final Identifier id;
        private Types type;
        protected SerializableData data;
        protected Function<SerializableData.Instance, BiFunction<ContentType, Supplier<?>, I>>  factoryConstructor;

        public ContentFactory(Identifier id, Types type, SerializableData data, Function<SerializableData.Instance, BiFunction<ContentType, Supplier<?>, I>> factoryConstructor) {
            this.id = id;
            this.data = data;
            this.type = type;
            this.factoryConstructor = factoryConstructor;
        }

        public Identifier getSerializerId() {
            return id;
        }

        public Types getType() {
            return type;
        }

        public class Instance implements BiFunction<ContentType, Supplier<?>, I> {

            private final SerializableData.Instance dataInstance;
            public Item item;

            private Instance(SerializableData.Instance data) {
                this.dataInstance = data;
            }

            public void write(PacketByteBuf buf) {
                buf.writeIdentifier(id);
                data.write(buf, dataInstance);
            }

            public Item getItem() {
                return this.item;
            }

            @Override
            public I apply(ContentType itemType, Supplier<?> item) {
                BiFunction<ContentType, Supplier<?>, I> itemFactory = factoryConstructor.apply(dataInstance);
                I p = itemFactory.apply(itemType, item);
                return p;
            }
        }

        public ContentFactory.Instance read(JsonObject json) {
            return new ContentFactory.Instance(data.read(json));
        }

        public ContentFactory.Instance read(PacketByteBuf buffer) {
            return new ContentFactory.Instance(data.read(buffer));
        }
}
