package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.HasVehicle;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.event.Cancellable;

public class TankShootEvent extends MTVEvent implements IsCancellable, Cancellable, HasVehicle {
   private final String licensePlate;

   public TankShootEvent(String licensePlate) {
      this.licensePlate = licensePlate;
   }

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public String getLicensePlate() {
      return this.licensePlate;
   }
}
