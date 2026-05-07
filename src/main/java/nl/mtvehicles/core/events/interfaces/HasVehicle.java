package nl.mtvehicles.core.events.interfaces;

import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;

public interface HasVehicle {
   String getLicensePlate();

   default Vehicle getVehicle() {
      return VehicleUtils.getVehicle(this.getLicensePlate());
   }
}
