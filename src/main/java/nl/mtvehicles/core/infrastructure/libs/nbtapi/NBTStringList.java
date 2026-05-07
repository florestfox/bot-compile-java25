package nl.mtvehicles.core.infrastructure.libs.nbtapi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ClassWrapper;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ReflectionMethod;

public class NBTStringList extends NBTList<String> {
   protected NBTStringList(NBTCompound owner, String name, NBTType type, Object list) {
      super(owner, name, type, list);
   }

   public String get(int index) {
      try {
         Object ret = ReflectionMethod.LIST_GET_STRING.run(this.listObject, index);
         return ret instanceof Optional ? (String)((Optional)ret).orElse("") : (String)ret;
      } catch (Exception ex) {
         throw new NbtApiException(ex);
      }
   }

   protected Object asTag(String object) {
      try {
         Constructor<?> con = ClassWrapper.NMS_NBTTAGSTRING.getClazz().getDeclaredConstructor(String.class);
         con.setAccessible(true);
         return con.newInstance(object);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException e) {
         throw new NbtApiException("Error while wrapping the Object " + object + " to it's NMS object!", e);
      }
   }
}
