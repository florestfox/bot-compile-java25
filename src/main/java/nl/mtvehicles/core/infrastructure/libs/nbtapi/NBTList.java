package nl.mtvehicles.core.infrastructure.libs.nbtapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.iface.ReadWriteNBTList;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.MinecraftVersion;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ReflectionMethod;

public abstract class NBTList<T> implements List<T>, ReadWriteNBTList<T> {
   private String listName;
   private NBTCompound parent;
   private NBTType type;
   protected Object listObject;

   protected NBTList(NBTCompound owner, String name, NBTType type, Object list) {
      this.parent = owner;
      this.listName = name;
      this.type = type;
      this.listObject = list;
   }

   public String getName() {
      return this.listName;
   }

   public NBTCompound getParent() {
      return this.parent;
   }

   private void validateClosed() {
      if (this.parent.isClosed()) {
         throw new NbtApiException("Tried using closed NBT data!");
      }
   }

   private void validateWritable() {
      if (this.getParent().isReadOnly()) {
         throw new NbtApiException("Tried setting data in read only mode!");
      }
   }

   protected void save() {
      this.validateClosed();
      this.parent.set(this.listName, this.listObject);
   }

   protected abstract Object asTag(T var1);

   public boolean add(T element) {
      this.validateClosed();
      this.validateWritable();

      boolean var2;
      try {
         this.parent.getWriteLock().lock();
         if (MinecraftVersion.getVersion().getVersionId() >= MinecraftVersion.MC1_14_R1.getVersionId()) {
            ReflectionMethod.LIST_ADD.run(this.listObject, this.size(), this.asTag(element));
         } else {
            ReflectionMethod.LEGACY_LIST_ADD.run(this.listObject, this.asTag(element));
         }

         this.save();
         var2 = true;
      } catch (Exception ex) {
         throw new NbtApiException(ex);
      } finally {
         this.parent.getWriteLock().unlock();
      }

      return var2;
   }

   public void add(int index, T element) {
      this.validateClosed();
      this.validateWritable();

      try {
         this.parent.getWriteLock().lock();
         if (MinecraftVersion.getVersion().getVersionId() >= MinecraftVersion.MC1_14_R1.getVersionId()) {
            ReflectionMethod.LIST_ADD.run(this.listObject, index, this.asTag(element));
         } else {
            ReflectionMethod.LEGACY_LIST_ADD.run(this.listObject, this.asTag(element));
         }

         this.save();
      } catch (Exception ex) {
         throw new NbtApiException(ex);
      } finally {
         this.parent.getWriteLock().unlock();
      }

   }

   public T set(int index, T element) {
      this.validateClosed();
      this.validateWritable();

      Object var4;
      try {
         this.parent.getWriteLock().lock();
         T prev = (T)this.get(index);
         ReflectionMethod.LIST_SET.run(this.listObject, index, this.asTag(element));
         this.save();
         var4 = prev;
      } catch (Exception ex) {
         throw new NbtApiException(ex);
      } finally {
         this.parent.getWriteLock().unlock();
      }

      return (T)var4;
   }

   public T remove(int i) {
      this.validateClosed();
      this.validateWritable();

      Object var3;
      try {
         this.parent.getWriteLock().lock();
         T old = (T)this.get(i);
         ReflectionMethod.LIST_REMOVE_KEY.run(this.listObject, i);
         this.save();
         var3 = old;
      } catch (Exception ex) {
         throw new NbtApiException(ex);
      } finally {
         this.parent.getWriteLock().unlock();
      }

      return (T)var3;
   }

   public int size() {
      this.validateClosed();

      int var1;
      try {
         this.parent.getReadLock().lock();
         var1 = (Integer)ReflectionMethod.LIST_SIZE.run(this.listObject);
      } catch (Exception ex) {
         throw new NbtApiException(ex);
      } finally {
         this.parent.getReadLock().unlock();
      }

      return var1;
   }

   public NBTType getType() {
      return this.type;
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public void clear() {
      while(!this.isEmpty()) {
         this.remove(0);
      }

   }

   public boolean contains(Object o) {
      this.validateClosed();

      try {
         this.parent.getReadLock().lock();

         for(int i = 0; i < this.size(); ++i) {
            if (o.equals(this.get(i))) {
               boolean var3 = true;
               return var3;
            }
         }

         boolean var7 = false;
         return var7;
      } finally {
         this.parent.getReadLock().unlock();
      }
   }

   public int indexOf(Object o) {
      this.validateClosed();

      try {
         this.parent.getReadLock().lock();

         for(int i = 0; i < this.size(); ++i) {
            if (o.equals(this.get(i))) {
               int var3 = i;
               return var3;
            }
         }

         byte var7 = -1;
         return var7;
      } finally {
         this.parent.getReadLock().unlock();
      }
   }

   public boolean addAll(Collection<? extends T> c) {
      this.validateClosed();

      boolean var8;
      try {
         this.parent.getWriteLock().lock();
         int size = this.size();

         for(T ele : c) {
            this.add(ele);
         }

         var8 = size != this.size();
      } finally {
         this.parent.getWriteLock().unlock();
      }

      return var8;
   }

   public boolean addAll(int index, Collection<? extends T> c) {
      this.validateClosed();

      boolean var9;
      try {
         this.parent.getWriteLock().lock();
         int size = this.size();

         for(T ele : c) {
            this.add(index++, ele);
         }

         var9 = size != this.size();
      } finally {
         this.parent.getWriteLock().unlock();
      }

      return var9;
   }

   public boolean containsAll(Collection<?> c) {
      this.validateClosed();

      boolean var4;
      try {
         this.parent.getReadLock().lock();
         Iterator var2 = c.iterator();

         Object ele;
         do {
            if (!var2.hasNext()) {
               boolean var8 = true;
               return var8;
            }

            ele = var2.next();
         } while(this.contains(ele));

         var4 = false;
      } finally {
         this.parent.getReadLock().unlock();
      }

      return var4;
   }

   public int lastIndexOf(Object o) {
      this.validateClosed();

      int var7;
      try {
         this.parent.getReadLock().lock();
         int index = -1;

         for(int i = 0; i < this.size(); ++i) {
            if (o.equals(this.get(i))) {
               index = i;
            }
         }

         var7 = index;
      } finally {
         this.parent.getReadLock().unlock();
      }

      return var7;
   }

   public boolean removeAll(Collection<?> c) {
      this.validateClosed();

      boolean var8;
      try {
         this.parent.getWriteLock().lock();
         int size = this.size();

         for(Object obj : c) {
            this.remove(obj);
         }

         var8 = size != this.size();
      } finally {
         this.parent.getWriteLock().unlock();
      }

      return var8;
   }

   public boolean retainAll(Collection<?> c) {
      this.validateClosed();

      boolean var9;
      try {
         this.parent.getWriteLock().lock();
         int size = this.size();

         for(Object obj : c) {
            for(int i = 0; i < this.size(); ++i) {
               if (!obj.equals(this.get(i))) {
                  this.remove(i--);
               }
            }
         }

         var9 = size != this.size();
      } finally {
         this.parent.getWriteLock().unlock();
      }

      return var9;
   }

   public boolean remove(Object o) {
      this.validateClosed();

      boolean var4;
      try {
         this.parent.getWriteLock().lock();
         int size = this.size();
         int id = this.indexOf(o);
         if (id != -1) {
            this.remove(id);
         }

         var4 = size != this.size();
      } finally {
         this.parent.getWriteLock().unlock();
      }

      return var4;
   }

   public Iterator<T> iterator() {
      return new Iterator<T>() {
         private int index = -1;

         public boolean hasNext() {
            return NBTList.this.size() > this.index + 1;
         }

         public T next() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               return (T)NBTList.this.get(++this.index);
            }
         }

         public void remove() {
            NBTList.this.remove(this.index);
            --this.index;
         }
      };
   }

   public ListIterator<T> listIterator() {
      return this.listIterator(0);
   }

   public ListIterator<T> listIterator(final int startIndex) {
      return new ListIterator<T>() {
         int index = startIndex - 1;

         public void add(T e) {
            NBTList.this.add(this.index, e);
         }

         public boolean hasNext() {
            return NBTList.this.size() > this.index + 1;
         }

         public boolean hasPrevious() {
            return this.index >= 0 && this.index <= NBTList.this.size();
         }

         public T next() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               return (T)NBTList.this.get(++this.index);
            }
         }

         public int nextIndex() {
            return this.index + 1;
         }

         public T previous() {
            if (!this.hasPrevious()) {
               throw new NoSuchElementException("Id: " + (this.index - 1));
            } else {
               return (T)NBTList.this.get(this.index--);
            }
         }

         public int previousIndex() {
            return this.index - 1;
         }

         public void remove() {
            NBTList.this.remove(this.index);
            --this.index;
         }

         public void set(T e) {
            NBTList.this.set(this.index, e);
         }
      };
   }

   public Object[] toArray() {
      this.validateClosed();

      Object[] var6;
      try {
         this.parent.getReadLock().lock();
         Object[] ar = new Object[this.size()];

         for(int i = 0; i < this.size(); ++i) {
            ar[i] = this.get(i);
         }

         var6 = ar;
      } finally {
         this.parent.getReadLock().unlock();
      }

      return var6;
   }

   public <E> E[] toArray(E[] a) {
      this.validateClosed();

      Object[] var9;
      try {
         this.parent.getReadLock().lock();
         E[] ar = (E[])Arrays.copyOf(a, this.size());
         Arrays.fill(ar, (Object)null);
         Class<?> arrayclass = a.getClass().getComponentType();

         for(int i = 0; i < this.size(); ++i) {
            T obj = (T)this.get(i);
            if (!arrayclass.isInstance(obj)) {
               throw new ArrayStoreException("The array does not match the objects stored in the List.");
            }

            ar[i] = this.get(i);
         }

         var9 = ar;
      } finally {
         this.parent.getReadLock().unlock();
      }

      return (E[])var9;
   }

   public List<T> subList(int fromIndex, int toIndex) {
      this.validateClosed();

      ArrayList var8;
      try {
         this.parent.getReadLock().lock();
         ArrayList<T> list = new ArrayList();

         for(int i = fromIndex; i < toIndex; ++i) {
            list.add(this.get(i));
         }

         var8 = list;
      } finally {
         this.parent.getReadLock().unlock();
      }

      return var8;
   }

   public boolean removeIf(Predicate<? super T> filter) {
      return super.removeIf(filter);
   }

   public String toString() {
      this.validateClosed();

      String var1;
      try {
         this.parent.getReadLock().lock();
         var1 = this.listObject.toString();
      } finally {
         this.parent.getReadLock().unlock();
      }

      return var1;
   }
}
