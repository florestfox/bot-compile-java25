package nl.mtvehicles.core.infrastructure.libs.nbtapi.iface;

import javax.annotation.Nonnull;

public interface NBTHandler<T> {
   default boolean fuzzyMatch(Object obj) {
      return false;
   }

   void set(@Nonnull ReadWriteNBT var1, @Nonnull String var2, @Nonnull T var3);

   T get(@Nonnull ReadableNBT var1, @Nonnull String var2);
}
