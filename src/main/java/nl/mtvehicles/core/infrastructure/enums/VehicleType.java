package nl.mtvehicles.core.infrastructure.enums;

import java.util.Locale;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public enum VehicleType {
   CAR,
   HOVER,
   TANK,
   HELICOPTER,
   AIRPLANE,
   BOAT;

   public String getName() {
      return this.toString().substring(0, 1).toUpperCase() + this.toString().substring(1).toLowerCase(Locale.ROOT);
   }

   public boolean isCar() {
      return this.equals(CAR);
   }

   public boolean isHover() {
      return this.equals(HOVER);
   }

   public boolean isTank() {
      return this.equals(TANK);
   }

   public boolean isHelicopter() {
      return this.equals(HELICOPTER);
   }

   public boolean isAirplane() {
      return this.equals(AIRPLANE);
   }

   public boolean isBoat() {
      return this.equals(BOAT);
   }

   public boolean canFly() {
      return this.equals(AIRPLANE) || this.equals(HELICOPTER);
   }

   public boolean isUsageDisabled(Player player, Location loc) {
      return !DependencyModule.isDependencyEnabled(SoftDependency.WORLD_GUARD) ? false : DependencyModule.worldGuard.isInRegionWithFlag(player, loc, WGFlag.valueOf("USE_" + this.toString()), false);
   }

   // $FF: synthetic method
   private static VehicleType[] $values() {
      return new VehicleType[]{CAR, HOVER, TANK, HELICOPTER, AIRPLANE, BOAT};
   }
}
