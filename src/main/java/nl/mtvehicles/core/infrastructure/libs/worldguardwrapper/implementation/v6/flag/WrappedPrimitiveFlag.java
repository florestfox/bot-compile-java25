package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.flag;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.protection.flags.Flag;
import java.util.Optional;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.utility.WorldGuardFlagUtilities;

public class WrappedPrimitiveFlag<T> extends AbstractWrappedFlag<T> {
   public WrappedPrimitiveFlag(Flag<T> handle) {
      super(handle);
   }

   public Optional<T> fromWGValue(Object value) {
      if (value instanceof Location) {
         return Optional.of(WorldGuardFlagUtilities.adaptLocation((Location)value));
      } else {
         return value instanceof Vector ? Optional.of(WorldGuardFlagUtilities.adaptVector((Vector)value)) : Optional.ofNullable(value);
      }
   }

   public Optional<Object> fromWrapperValue(T value) {
      if (value instanceof org.bukkit.Location) {
         return Optional.of(WorldGuardFlagUtilities.adaptLocation((org.bukkit.Location)value));
      } else {
         return value instanceof org.bukkit.util.Vector ? Optional.of(WorldGuardFlagUtilities.adaptVector((org.bukkit.util.Vector)value)) : Optional.ofNullable(value);
      }
   }
}
