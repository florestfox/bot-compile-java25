package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.HasVehicle;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;

public class VehicleEnterEvent extends MTVEvent implements IsCancellable, Cancellable, HasVehicle {
   private final String licensePlate;
   private final Location location;

   public VehicleEnterEvent(String licensePlate, Location location) {
      this.licensePlate = licensePlate;
      this.location = location;
   }

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public String getLicensePlate() {
      return this.licensePlate;
   }

   public Location getLocation() {
      return this.location;
   }
}
