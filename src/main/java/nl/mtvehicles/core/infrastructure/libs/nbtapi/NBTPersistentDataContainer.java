package nl.mtvehicles.core.infrastructure.libs.nbtapi;

import java.util.Map;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
import org.bukkit.persistence.PersistentDataContainer;

public class NBTPersistentDataContainer extends NBTCompound {
   private final PersistentDataContainer container;

   public NBTPersistentDataContainer(PersistentDataContainer container) {
      super((NBTCompound)null, (String)null);
      this.container = container;
   }

   public Object getCompound() {
      return ReflectionMethod.CRAFT_PERSISTENT_DATA_CONTAINER_TO_TAG.run(this.container);
   }

   protected void setCompound(Object compound) {
      Map<Object, Object> map = (Map)ReflectionMethod.CRAFT_PERSISTENT_DATA_CONTAINER_GET_MAP.run(this.container);
      map.clear();
      ReflectionMethod.CRAFT_PERSISTENT_DATA_CONTAINER_PUT_ALL.run(this.container, compound);
   }
}
