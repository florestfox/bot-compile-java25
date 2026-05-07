package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.utility;

import com.google.common.collect.Maps;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import java.util.Map;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.IWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.WrappedState;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.flag.AbstractWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.flag.WrappedPrimitiveFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.flag.WrappedStatusFlag;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public final class WorldGuardFlagUtilities {
   public static <T> IWrappedFlag<T> wrap(Flag<?> flag, Class<T> type) {
      IWrappedFlag<T> wrappedFlag;
      if (type.equals(WrappedState.class)) {
         wrappedFlag = new WrappedStatusFlag(flag);
      } else if (!type.equals(Boolean.class) && !type.equals(Boolean.TYPE)) {
         if (!type.equals(Double.class) && !type.equals(Double.TYPE)) {
            if (type.equals(Enum.class)) {
               wrappedFlag = new WrappedPrimitiveFlag<T>(flag);
            } else if (!type.equals(Integer.class) && !type.equals(Integer.TYPE)) {
               if (type.equals(Location.class)) {
                  wrappedFlag = new WrappedPrimitiveFlag<T>(flag);
               } else if (type.equals(String.class)) {
                  wrappedFlag = new WrappedPrimitiveFlag<T>(flag);
               } else if (type.equals(Vector.class)) {
                  wrappedFlag = new WrappedPrimitiveFlag<T>(flag);
               } else {
                  if (!type.equals(Object.class)) {
                     throw new IllegalArgumentException("Unsupported flag type " + type.getName());
                  }

                  wrappedFlag = new WrappedPrimitiveFlag<T>(flag);
               }
            } else {
               wrappedFlag = new WrappedPrimitiveFlag<T>(flag);
            }
         } else {
            wrappedFlag = new WrappedPrimitiveFlag<T>(flag);
         }
      } else {
         wrappedFlag = new WrappedPrimitiveFlag<T>(flag);
      }

      return wrappedFlag;
   }

   public static IWrappedFlag<?> wrapFixType(Flag<?> flag, Class<?> type) {
      if (StateFlag.State.class.isAssignableFrom(type)) {
         type = WrappedState.class;
      } else if (com.sk89q.worldedit.util.Location.class.isAssignableFrom(type)) {
         type = Location.class;
      } else if (com.sk89q.worldedit.Vector.class.isAssignableFrom(type)) {
         type = Vector.class;
      }

      return wrap(flag, type);
   }

   public static Map.Entry<IWrappedFlag<?>, Object> wrap(Flag<?> flag, Object value) {
      IWrappedFlag<?> wrappedFlag = wrapFixType(flag, value.getClass());
      Object wrappedValue = ((AbstractWrappedFlag)wrappedFlag).fromWGValue(value).get();
      return Maps.immutableEntry(wrappedFlag, wrappedValue);
   }

   public static Vector adaptVector(com.sk89q.worldedit.Vector vector) {
      return new Vector(vector.getX(), vector.getY(), vector.getZ());
   }

   public static com.sk89q.worldedit.Vector adaptVector(Vector vector) {
      return new com.sk89q.worldedit.Vector(vector.getX(), vector.getY(), vector.getZ());
   }

   public static Location adaptLocation(com.sk89q.worldedit.util.Location location) {
      World world = location.getExtent() instanceof BukkitWorld ? ((BukkitWorld)location.getExtent()).getWorld() : null;
      return new Location(world, location.getX(), location.getY(), location.getZ());
   }

   public static com.sk89q.worldedit.util.Location adaptLocation(Location location) {
      return new com.sk89q.worldedit.util.Location(new BukkitWorld(location.getWorld()), location.getX(), location.getY(), location.getZ());
   }

   private WorldGuardFlagUtilities() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
