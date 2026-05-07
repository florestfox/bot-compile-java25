package nl.mtvehicles.core.infrastructure.libs.nbtapi.iface;

import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTType;
import org.bukkit.inventory.ItemStack;

public interface ReadableNBT {
   String getString(String var1);

   Integer getInteger(String var1);

   Double getDouble(String var1);

   Byte getByte(String var1);

   Short getShort(String var1);

   Long getLong(String var1);

   Float getFloat(String var1);

   @Nullable
   byte[] getByteArray(String var1);

   @Nullable
   int[] getIntArray(String var1);

   @Nullable
   long[] getLongArray(String var1);

   Boolean getBoolean(String var1);

   @Nullable
   ItemStack getItemStack(String var1);

   @Nullable
   ItemStack[] getItemStackArray(String var1);

   @Nullable
   UUID getUUID(String var1);

   boolean hasTag(String var1);

   default boolean hasTag(String key, NBTType type) {
      return this.hasTag(key) && this.getType(key) == type;
   }

   Set<String> getKeys();

   @Nullable
   ReadableNBT getCompound(String var1);

   ReadableNBTList<String> getStringList(String var1);

   ReadableNBTList<Integer> getIntegerList(String var1);

   ReadableNBTList<int[]> getIntArrayList(String var1);

   ReadableNBTList<UUID> getUUIDList(String var1);

   ReadableNBTList<Float> getFloatList(String var1);

   ReadableNBTList<Double> getDoubleList(String var1);

   ReadableNBTList<Long> getLongList(String var1);

   @Nullable
   NBTType getListType(String var1);

   ReadableNBTList<ReadWriteNBT> getCompoundList(String var1);

   <T> T getOrDefault(String var1, T var2);

   @Nullable
   <T> T getOrNull(String var1, Class<?> var2);

   @Nullable
   <T> T resolveOrNull(String var1, Class<?> var2);

   <T> T resolveOrDefault(String var1, T var2);

   @Nullable
   ReadableNBT resolveCompound(String var1);

   <T> T get(String var1, NBTHandler<T> var2);

   @Nullable
   <E extends Enum<E>> E getEnum(String var1, Class<E> var2);

   NBTType getType(String var1);

   void writeCompound(OutputStream var1);

   ReadWriteNBT extractDifference(ReadableNBT var1);

   String toString();
}
