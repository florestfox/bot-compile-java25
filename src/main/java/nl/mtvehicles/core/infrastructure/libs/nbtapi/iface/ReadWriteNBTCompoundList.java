package nl.mtvehicles.core.infrastructure.libs.nbtapi.iface;

import java.util.function.Predicate;

public interface ReadWriteNBTCompoundList extends ReadableNBTList<ReadWriteNBT> {
   ReadWriteNBT addCompound();

   ReadWriteNBT addCompound(ReadableNBT var1);

   ReadWriteNBT remove(int var1);

   void clear();

   boolean removeIf(Predicate<? super ReadWriteNBT> var1);
}
