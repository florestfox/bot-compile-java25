package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.utility;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.ICuboidSelection;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.IPolygonalSelection;
import org.bukkit.Location;

public final class SelectionUtilities {
   public static ICuboidSelection createCuboidSelection(Location first, Location second) {
      final Location minimum;
      final Location maximum;
      if (first.getBlockY() > second.getBlockY()) {
         maximum = first;
         minimum = second;
      } else {
         maximum = second;
         minimum = first;
      }

      return new ICuboidSelection() {
         public Location getMinimumPoint() {
            return minimum;
         }

         public Location getMaximumPoint() {
            return maximum;
         }
      };
   }

   public static IPolygonalSelection createPolygonalSelection(final Collection<Location> points, final int minY, final int maxY) {
      return new IPolygonalSelection() {
         public Set<Location> getPoints() {
            return new HashSet(points);
         }

         public int getMinimumY() {
            return minY;
         }

         public int getMaximumY() {
            return maxY;
         }
      };
   }

   private SelectionUtilities() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
