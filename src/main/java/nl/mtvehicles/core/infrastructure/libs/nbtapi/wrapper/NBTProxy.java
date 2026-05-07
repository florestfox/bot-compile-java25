package nl.mtvehicles.core.infrastructure.libs.nbtapi.wrapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.iface.NBTHandler;

public interface NBTProxy {
   Map<Class<?>, NBTHandler<Object>> handlers = new HashMap();

   default void init() {
   }

   default Casing getCasing() {
      return Casing.PascalCase;
   }

   default <T> NBTHandler<T> getHandler(Class<T> clazz) {
      return (NBTHandler)handlers.get(clazz);
   }

   default Collection<NBTHandler<Object>> getHandlers() {
      return handlers.values();
   }

   default <T> void registerHandler(Class<T> clazz, NBTHandler<T> handler) {
      handlers.put(clazz, handler);
   }
}
