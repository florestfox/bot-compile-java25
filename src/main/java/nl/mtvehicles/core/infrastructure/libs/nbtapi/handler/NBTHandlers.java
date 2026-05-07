package nl.mtvehicles.core.infrastructure.libs.nbtapi.handler;

import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBT;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.iface.NBTHandler;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.iface.ReadWriteNBT;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.iface.ReadableNBT;
import org.bukkit.inventory.ItemStack;

public class NBTHandlers {
   public static final NBTHandler<ItemStack> ITEM_STACK = new NBTHandler<ItemStack>() {
      public boolean fuzzyMatch(Object obj) {
         return obj instanceof ItemStack;
      }

      public void set(ReadWriteNBT nbt, String key, ItemStack value) {
         nbt.removeKey(key);
         ReadWriteNBT tag = nbt.getOrCreateCompound(key);
         tag.mergeCompound(NBT.itemStackToNBT(value));
      }

      public ItemStack get(ReadableNBT nbt, String key) {
         ReadableNBT tag = nbt.getCompound(key);
         return tag != null ? NBT.itemStackFromNBT(tag) : null;
      }
   };
   public static final NBTHandler<ReadableNBT> STORE_READABLE_TAG = new NBTHandler<ReadableNBT>() {
      public boolean fuzzyMatch(Object obj) {
         return obj instanceof ReadableNBT;
      }

      public void set(ReadWriteNBT nbt, String key, ReadableNBT value) {
         nbt.removeKey(key);
         nbt.getOrCreateCompound(key).mergeCompound(value);
      }

      public ReadableNBT get(ReadableNBT nbt, String key) {
         ReadableNBT tag = nbt.getCompound(key);
         if (tag != null) {
            ReadWriteNBT value = NBT.createNBTObject();
            value.mergeCompound(tag);
            return value;
         } else {
            return null;
         }
      }
   };
   public static final NBTHandler<ReadWriteNBT> STORE_READWRITE_TAG = new NBTHandler<ReadWriteNBT>() {
      public boolean fuzzyMatch(Object obj) {
         return obj instanceof ReadWriteNBT;
      }

      public void set(ReadWriteNBT nbt, String key, ReadWriteNBT value) {
         nbt.removeKey(key);
         nbt.getOrCreateCompound(key).mergeCompound(value);
      }

      public ReadWriteNBT get(ReadableNBT nbt, String key) {
         ReadableNBT tag = nbt.getCompound(key);
         if (tag != null) {
            ReadWriteNBT value = NBT.createNBTObject();
            value.mergeCompound(tag);
            return value;
         } else {
            return null;
         }
      }
   };
}
