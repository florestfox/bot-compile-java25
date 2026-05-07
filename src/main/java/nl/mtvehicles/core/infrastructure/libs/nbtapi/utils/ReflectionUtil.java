package nl.mtvehicles.core.infrastructure.libs.nbtapi.utils;

import java.lang.reflect.Field;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NbtApiException;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.MojangToMapping;

public final class ReflectionUtil {
   public static Field getMappedField(Class<?> clazz, String mapping) {
      String mojmapName = mapping.split("#")[1];

      try {
         return clazz.getField(mojmapName);
      } catch (SecurityException | NoSuchFieldException var5) {
         try {
            return clazz.getDeclaredField((String)MojangToMapping.getMapping().get(mapping));
         } catch (Exception e) {
            throw new NbtApiException("Unable to find field " + mapping + " in class " + clazz.getName(), e);
         }
      }
   }
}
