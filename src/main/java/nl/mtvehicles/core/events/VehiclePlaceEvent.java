package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.CanEditLicensePlate;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;

public class VehiclePlaceEvent extends MTVEvent implements IsCancellable, Cancellable, CanEditLicensePlate {
   private Location location;
   private String licensePlate;

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public String getLicensePlate() {
      return this.licensePlate;
   }

   public void setLicensePlate(String licensePlate) {
      this.licensePlate = licensePlate;
   }

   public Location getLocation() {
      return this.location;
   }

   public void setLocation(Location location) {
      this.location = location;
   }
}
