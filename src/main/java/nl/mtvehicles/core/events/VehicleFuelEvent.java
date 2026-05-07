package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.CanEditLicensePlate;
import nl.mtvehicles.core.events.interfaces.HasJerryCan;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.event.Cancellable;

public class VehicleFuelEvent extends MTVEvent implements IsCancellable, Cancellable, CanEditLicensePlate, HasJerryCan {
   private final double vehicleFuel;
   private final int jerryCanFuel;
   private final int jerryCanSize;
   private String licensePlate;

   public VehicleFuelEvent(double vehicleFuel, int jerryCanFuel, int jerryCanSize) {
      this.vehicleFuel = vehicleFuel;
      this.jerryCanFuel = jerryCanFuel;
      this.jerryCanSize = jerryCanSize;
   }

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public String getLicensePlate() {
      return this.licensePlate;
   }

   public void setLicensePlate(String licensePlate) {
      this.licensePlate = licensePlate;
   }

   public int getJerryCanFuel() {
      return this.jerryCanFuel;
   }

   public int getJerryCanSize() {
      return this.jerryCanSize;
   }

   public double getVehicleFuel() {
      return this.vehicleFuel;
   }
}
