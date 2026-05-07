package nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings;

import java.util.Objects;
import java.util.Optional;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTReflectionUtil;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NbtApiException;

public class CodecHelper {
   public static Object convertItemStackToNbt(Object itemStack) {
      Object result = null;

      try {
         Object var4 = NBTReflectionUtil.itemstack_codec.encodeStart(NBTReflectionUtil.nbtRegistryOps, itemStack);
         Objects.requireNonNull(var4);
         return ((Optional)var4.getClass().getMethod("result").invoke(var4)).get();
      } catch (Exception e) {
         throw new NbtApiException("Failed to convert ItemStack to NBT. " + result + " " + itemStack, e);
      }
   }

   public static Object convertNbtToItemStack(Object nbt) {
      Object result = null;

      try {
         Object var4 = NBTReflectionUtil.itemstack_codec.parse(NBTReflectionUtil.nbtRegistryOps, nbt);
         Objects.requireNonNull(var4);
         return ((Optional)var4.getClass().getMethod("result").invoke(var4)).get();
      } catch (Exception e) {
         throw new NbtApiException("Failed to convert NBT to ItemStack. " + result + " " + nbt, e);
      }
   }
}
