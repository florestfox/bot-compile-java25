package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region;

import java.util.Map;
import java.util.Optional;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.IWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.ISelection;
import org.bukkit.Location;

public interface IWrappedRegion {
   ISelection getSelection();

   String getId();

   Map<IWrappedFlag<?>, Object> getFlags();

   <T> Optional<T> getFlag(IWrappedFlag<T> var1);

   <T> void setFlag(IWrappedFlag<T> var1, T var2);

   int getPriority();

   void setPriority(int var1);

   IWrappedDomain getOwners();

   IWrappedDomain getMembers();

   boolean contains(Location var1);
}
