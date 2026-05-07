package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.HasVehicle;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.event.Cancellable;

public class VehicleRegionLeaveEvent extends MTVEvent implements Cancellable, HasVehicle {
   private final String licensePlate;
   private final String regionName;

   public VehicleRegionLeaveEvent(String licensePlate, String regionName) {
      this.licensePlate = licensePlate;
      this.regionName = regionName;
   }

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public String getLicensePlate() {
      return this.licensePlate;
   }

   public String getRegionName() {
      return this.regionName;
   }
}
