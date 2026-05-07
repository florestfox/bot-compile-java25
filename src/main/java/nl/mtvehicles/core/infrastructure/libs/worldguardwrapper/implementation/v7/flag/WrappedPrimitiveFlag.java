package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.flag;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.protection.flags.Flag;
import java.util.Optional;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.utility.WorldGuardFlagUtilities;
import org.bukkit.util.Vector;

public class WrappedPrimitiveFlag<T> extends AbstractWrappedFlag<T> {
   public WrappedPrimitiveFlag(Flag<T> handle) {
      super(handle);
   }

   public Optional<T> fromWGValue(Object value) {
      if (value instanceof Location) {
         return Optional.of(BukkitAdapter.adapt((Location)value));
      } else {
         return value instanceof Vector3 ? Optional.of(WorldGuardFlagUtilities.adaptVector((Vector3)value)) : Optional.ofNullable(value);
      }
   }

   public Optional<Object> fromWrapperValue(T value) {
      if (value instanceof org.bukkit.Location) {
         return Optional.of(BukkitAdapter.adapt((org.bukkit.Location)value));
      } else {
         return value instanceof Vector ? Optional.of(WorldGuardFlagUtilities.adaptVector((Vector)value)) : Optional.ofNullable(value);
      }
   }
}
