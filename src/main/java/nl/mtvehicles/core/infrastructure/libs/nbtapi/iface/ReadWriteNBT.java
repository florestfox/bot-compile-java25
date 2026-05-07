package nl.mtvehicles.core.infrastructure.libs.nbtapi.iface;

import java.util.UUID;
import javax.annotation.Nullable;
import org.bukkit.inventory.ItemStack;

public interface ReadWriteNBT extends ReadableNBT {
   void mergeCompound(ReadableNBT var1);

   void setString(String var1, String var2);

   void setInteger(String var1, Integer var2);

   void setDouble(String var1, Double var2);

   void setByte(String var1, Byte var2);

   void setShort(String var1, Short var2);

   void setLong(String var1, Long var2);

   void setFloat(String var1, Float var2);

   void setByteArray(String var1, byte[] var2);

   void setIntArray(String var1, int[] var2);

   void setLongArray(String var1, long[] var2);

   void setBoolean(String var1, Boolean var2);

   void setItemStack(String var1, ItemStack var2);

   void setItemStackArray(String var1, ItemStack[] var2);

   void setUUID(String var1, UUID var2);

   void removeKey(String var1);

   ReadWriteNBT getOrCreateCompound(String var1);

   @Nullable
   ReadWriteNBT getCompound(String var1);

   ReadWriteNBT resolveOrCreateCompound(String var1);

   <T> void set(String var1, T var2, NBTHandler<T> var3);

   <E extends Enum<?>> void setEnum(String var1, E var2);

   ReadWriteNBTList<String> getStringList(String var1);

   ReadWriteNBTList<Integer> getIntegerList(String var1);

   ReadWriteNBTList<int[]> getIntArrayList(String var1);

   ReadWriteNBTList<UUID> getUUIDList(String var1);

   ReadWriteNBTList<Float> getFloatList(String var1);

   ReadWriteNBTList<Double> getDoubleList(String var1);

   ReadWriteNBTList<Long> getLongList(String var1);

   ReadWriteNBTCompoundList getCompoundList(String var1);

   @Nullable
   ReadWriteNBT resolveCompound(String var1);

   void clearNBT();
}
