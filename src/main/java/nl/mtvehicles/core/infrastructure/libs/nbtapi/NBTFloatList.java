package nl.mtvehicles.core.infrastructure.libs.nbtapi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ClassWrapper;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ReflectionMethod;

public class NBTFloatList extends NBTList<Float> {
   protected NBTFloatList(NBTCompound owner, String name, NBTType type, Object list) {
      super(owner, name, type, list);
   }

   protected Object asTag(Float object) {
      try {
         Constructor<?> con = ClassWrapper.NMS_NBTTAGFLOAT.getClazz().getDeclaredConstructor(Float.TYPE);
         con.setAccessible(true);
         return con.newInstance(object);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException e) {
         throw new NbtApiException("Error while wrapping the Object " + object + " to it's NMS object!", e);
      }
   }

   public Float get(int index) {
      try {
         Object obj = ReflectionMethod.LIST_GET.run(this.listObject, index);
         return Float.valueOf(obj.toString());
      } catch (NumberFormatException var3) {
         return 0.0F;
      } catch (Exception ex) {
         throw new NbtApiException(ex);
      }
   }
}
