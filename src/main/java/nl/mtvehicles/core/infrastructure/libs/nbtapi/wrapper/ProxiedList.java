package nl.mtvehicles.core.infrastructure.libs.nbtapi.wrapper;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.iface.ReadWriteNBT;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.iface.ReadWriteNBTCompoundList;

class ProxiedList<E extends NBTProxy> implements ProxyList<E> {
   private final ReadWriteNBTCompoundList nbt;
   private final Class<E> proxy;

   public ProxiedList(ReadWriteNBTCompoundList nbt, Class<E> proxyClass) {
      this.nbt = nbt;
      this.proxy = proxyClass;
   }

   public E get(int index) {
      ReadWriteNBT tag = (ReadWriteNBT)this.nbt.get(index);
      return (E)(new ProxyBuilder(tag, this.proxy)).build();
   }

   public int size() {
      return this.nbt.size();
   }

   public void remove(int index) {
      this.nbt.remove(index);
   }

   public Iterator<E> iterator() {
      return new Itr();
   }

   public E addCompound() {
      ReadWriteNBT tag = this.nbt.addCompound();
      return (E)(new ProxyBuilder(tag, this.proxy)).build();
   }

   public boolean isEmpty() {
      return this.nbt.isEmpty();
   }

   private class Itr implements Iterator<E> {
      int cursor;
      int lastRet;

      private Itr() {
         this.cursor = 0;
         this.lastRet = -1;
      }

      public boolean hasNext() {
         return this.cursor != ProxiedList.this.size();
      }

      public E next() {
         try {
            int i = this.cursor;
            E next = (E)ProxiedList.this.get(i);
            this.lastRet = i;
            this.cursor = i + 1;
            return next;
         } catch (IndexOutOfBoundsException var3) {
            throw new NoSuchElementException();
         }
      }

      public void remove() {
         if (this.lastRet < 0) {
            throw new IllegalStateException();
         } else {
            try {
               ProxiedList.this.remove(this.lastRet);
               if (this.lastRet < this.cursor) {
                  --this.cursor;
               }

               this.lastRet = -1;
            } catch (IndexOutOfBoundsException var2) {
               throw new ConcurrentModificationException();
            }
         }
      }
   }
}
