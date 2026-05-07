package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.scopedpool;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SoftValueHashMap<K, V> implements Map<K, V> {
   private Map<K, SoftValueRef<K, V>> hash;
   private ReferenceQueue<V> queue;

   public Set<Map.Entry<K, V>> entrySet() {
      this.processQueue();
      Set<Map.Entry<K, V>> ret = new HashSet();

      for(Map.Entry<K, SoftValueRef<K, V>> e : this.hash.entrySet()) {
         ret.add(new AbstractMap.SimpleImmutableEntry(e.getKey(), ((SoftValueRef)e.getValue()).get()));
      }

      return ret;
   }

   private void processQueue() {
      Object ref;
      if (!this.hash.isEmpty()) {
         while((ref = this.queue.poll()) != null) {
            if (ref instanceof SoftValueRef) {
               SoftValueRef que = (SoftValueRef)ref;
               if (ref == this.hash.get(que.key)) {
                  this.hash.remove(que.key);
               }
            }
         }
      }

   }

   public SoftValueHashMap(int initialCapacity, float loadFactor) {
      this.queue = new ReferenceQueue();
      this.hash = new ConcurrentHashMap(initialCapacity, loadFactor);
   }

   public SoftValueHashMap(int initialCapacity) {
      this.queue = new ReferenceQueue();
      this.hash = new ConcurrentHashMap(initialCapacity);
   }

   public SoftValueHashMap() {
      this.queue = new ReferenceQueue();
      this.hash = new ConcurrentHashMap();
   }

   public SoftValueHashMap(Map<K, V> t) {
      this(Math.max(2 * t.size(), 11), 0.75F);
      this.putAll(t);
   }

   public int size() {
      this.processQueue();
      return this.hash.size();
   }

   public boolean isEmpty() {
      this.processQueue();
      return this.hash.isEmpty();
   }

   public boolean containsKey(Object key) {
      this.processQueue();
      return this.hash.containsKey(key);
   }

   public V get(Object key) {
      this.processQueue();
      return (V)this.valueOrNull((SoftValueRef)this.hash.get(key));
   }

   public V put(K key, V value) {
      this.processQueue();
      return (V)this.valueOrNull((SoftValueRef)this.hash.put(key, SoftValueHashMap.SoftValueRef.create(key, value, this.queue)));
   }

   public V remove(Object key) {
      this.processQueue();
      return (V)this.valueOrNull((SoftValueRef)this.hash.remove(key));
   }

   public void clear() {
      this.processQueue();
      this.hash.clear();
   }

   public boolean containsValue(Object arg0) {
      this.processQueue();
      if (null == arg0) {
         return false;
      } else {
         for(SoftValueRef<K, V> e : this.hash.values()) {
            if (null != e && arg0.equals(e.get())) {
               return true;
            }
         }

         return false;
      }
   }

   public Set<K> keySet() {
      this.processQueue();
      return this.hash.keySet();
   }

   public void putAll(Map<? extends K, ? extends V> arg0) {
      this.processQueue();

      for(K key : arg0.keySet()) {
         this.put(key, arg0.get(key));
      }

   }

   public Collection<V> values() {
      this.processQueue();
      List<V> ret = new ArrayList();

      for(SoftValueRef<K, V> e : this.hash.values()) {
         ret.add(e.get());
      }

      return ret;
   }

   private V valueOrNull(SoftValueRef<K, V> rtn) {
      return (V)(null == rtn ? null : rtn.get());
   }

   private static class SoftValueRef<K, V> extends SoftReference<V> {
      public K key;

      private SoftValueRef(K key, V val, ReferenceQueue<V> q) {
         super(val, q);
         this.key = key;
      }

      private static <K, V> SoftValueRef<K, V> create(K key, V val, ReferenceQueue<V> q) {
         return val == null ? null : new SoftValueRef(key, val, q);
      }
   }
}
