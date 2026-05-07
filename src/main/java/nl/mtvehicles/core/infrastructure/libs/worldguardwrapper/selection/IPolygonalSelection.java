package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection;

import java.util.Set;
import org.bukkit.Location;

public interface IPolygonalSelection extends ISelection {
   Set<Location> getPoints();

   int getMinimumY();

   int getMaximumY();
}
