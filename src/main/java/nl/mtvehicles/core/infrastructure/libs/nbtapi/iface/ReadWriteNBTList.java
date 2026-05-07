package nl.mtvehicles.core.infrastructure.libs.nbtapi.iface;

import java.util.Collection;
import java.util.ListIterator;
import java.util.function.Predicate;

public interface ReadWriteNBTList<T> extends ReadableNBTList<T> {
   boolean add(T var1);

   void add(int var1, T var2);

   T set(int var1, T var2);

   T remove(int var1);

   void clear();

   boolean addAll(Collection<? extends T> var1);

   boolean addAll(int var1, Collection<? extends T> var2);

   boolean removeAll(Collection<?> var1);

   boolean retainAll(Collection<?> var1);

   boolean removeIf(Predicate<? super T> var1);

   boolean remove(Object var1);

   ListIterator<T> listIterator(int var1);
}
