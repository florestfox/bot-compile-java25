package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.handler;

import java.util.Set;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.WrappedState;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegion;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegionSet;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface IHandler {
   default void initialize(Player player, Location current, IWrappedRegionSet regionSet) {
   }

   default boolean testMoveTo(Player player, Location from, Location to, IWrappedRegionSet regionSet, String moveType) {
      return true;
   }

   default boolean onCrossBoundary(Player player, Location from, Location to, IWrappedRegionSet toSet, Set<IWrappedRegion> entered, Set<IWrappedRegion> exited, String moveType) {
      return true;
   }

   default void tick(Player player, IWrappedRegionSet regionSet) {
   }

   default WrappedState getInvincibility(Player player) {
      return null;
   }
}
