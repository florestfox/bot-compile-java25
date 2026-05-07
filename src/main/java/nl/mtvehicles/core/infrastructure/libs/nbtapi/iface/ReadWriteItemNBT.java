package nl.mtvehicles.core.infrastructure.libs.nbtapi.iface;

import java.util.function.BiConsumer;
import org.bukkit.inventory.meta.ItemMeta;

public interface ReadWriteItemNBT extends ReadWriteNBT, ReadableItemNBT {
   boolean hasCustomNbtData();

   void clearCustomNBT();

   void modifyMeta(BiConsumer<ReadableNBT, ItemMeta> var1);

   <T extends ItemMeta> void modifyMeta(Class<T> var1, BiConsumer<ReadableNBT, T> var2);
}
