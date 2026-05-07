package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.IWrappedFlag;
import org.bukkit.OfflinePlayer;

public interface IWrappedRegionSet extends Iterable<IWrappedRegion> {
   boolean isVirtual();

   <V> Optional<V> queryValue(OfflinePlayer var1, IWrappedFlag<V> var2);

   <V> Collection<V> queryAllValues(OfflinePlayer var1, IWrappedFlag<V> var2);

   boolean isOwnerOfAll(OfflinePlayer var1);

   boolean isMemberOfAll(OfflinePlayer var1);

   int size();

   Set<IWrappedRegion> getRegions();
}
