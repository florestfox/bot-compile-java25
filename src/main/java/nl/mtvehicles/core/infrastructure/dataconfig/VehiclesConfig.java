package nl.mtvehicles.core.infrastructure.dataconfig;

import java.util.List;
import java.util.Map;
import nl.mtvehicles.core.infrastructure.enums.ConfigType;
import nl.mtvehicles.core.infrastructure.models.MTVConfig;

public class VehiclesConfig extends MTVConfig {
   public VehiclesConfig() {
      super(ConfigType.VEHICLES);
   }

   public List<Map<?, ?>> getVehicles() {
      return this.getConfiguration().getMapList("voertuigen");
   }
}
