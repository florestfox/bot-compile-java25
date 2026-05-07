package nl.mtvehicles.core.infrastructure.libs.nbtapi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ClassWrapper;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ReflectionMethod;

public class NBTIntArrayList extends NBTList<int[]> {
   private final NBTContainer tmpContainer = new NBTContainer();

   protected NBTIntArrayList(NBTCompound owner, String name, NBTType type, Object list) {
      super(owner, name, type, list);
   }

   protected Object asTag(int[] object) {
      try {
         Constructor<?> con = ClassWrapper.NMS_NBTTAGINTARRAY.getClazz().getDeclaredConstructor(int[].class);
         con.setAccessible(true);
         return con.newInstance(object);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException e) {
         throw new NbtApiException("Error while wrapping the Object " + object + " to it's NMS object!", e);
      }
   }

   public int[] get(int index) {
      try {
         Object obj = ReflectionMethod.LIST_GET.run(this.listObject, index);
         ReflectionMethod.COMPOUND_SET.run(this.tmpContainer.getCompound(), "tmp", obj);
         int[] val = this.tmpContainer.getIntArray("tmp");
         this.tmpContainer.removeKey("tmp");
         return val;
      } catch (NumberFormatException var4) {
         return null;
      } catch (Exception ex) {
         throw new NbtApiException(ex);
      }
   }
}
