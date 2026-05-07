package nl.mtvehicles.core.infrastructure.dependencies;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.enums.WGFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.WorldGuardWrapper;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.IWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.WrappedState;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegion;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGuardUtils {
   public static WorldGuardWrapper instance = WorldGuardWrapper.getInstance();
   public static HashMap<WGFlag, IWrappedFlag<WrappedState>> flags = new HashMap();

   public WorldGuardUtils() {
      this.registerFlags();
   }

   public boolean isInsideGasStation(Player player, Location loc) {
      return this.isInRegionWithFlag(player, loc, WGFlag.GAS_STATION, WrappedState.ALLOW);
   }

   public Set<String> getRegionNames(Location loc) {
      Set<String> returns = new HashSet();
      this.getRegions(loc).forEach((region) -> returns.add(region.getId()));
      return returns;
   }

   public boolean isInsideRegion(Location loc, String regionName) {
      return this.getRegionNames(loc).contains(regionName);
   }

   private Set<IWrappedRegion> getRegions(Location loc) {
      return instance.getRegions(loc);
   }

   public boolean isInRegionWithFlag(Player player, Location loc, WGFlag customFlag, WrappedState flagState) {
      Set<IWrappedRegion> regions = this.getRegions(loc);
      if (regions.size() == 0) {
         return false;
      } else {
         IWrappedFlag<WrappedState> flag = (IWrappedFlag)flags.get(customFlag);
         boolean returns = false;

         for(IWrappedRegion region : regions) {
            Optional<WrappedState> regionFlagState = region.<WrappedState>getFlag(flag);
            if (regionFlagState.isPresent()) {
               WrappedState state = (WrappedState)instance.queryFlag(player, loc, flag).orElse((Object)null);
               if (flagState.equals(state)) {
                  returns = true;
               }
            }
         }

         return returns;
      }
   }

   public boolean isInRegionWithFlag(Player player, Location loc, WGFlag customFlag, boolean flagState) {
      WrappedState state = flagState ? WrappedState.ALLOW : WrappedState.DENY;
      return this.isInRegionWithFlag(player, loc, customFlag, state);
   }

   private void registerFlags() {
      try {
         for(WGFlag flag : WGFlag.getFlagList()) {
            flags.put(flag, (IWrappedFlag)instance.registerFlag(flag.getKey(), WrappedState.class).orElse((Object)null));
         }
      } catch (IllegalStateException var4) {
         for(WGFlag flag : WGFlag.getFlagList()) {
            flags.put(flag, (IWrappedFlag)instance.getFlag(flag.getKey(), WrappedState.class).orElse((Object)null));
            if (flags.get(flag) == null) {
               Bukkit.getLogger().severe("[MTVehicles] Custom WorldGuard flags could not be registered for MTVehicles. Disabling soft-dependency... (If you've just reloaded the plugin with PlugMan, try restarting the server.)");
               DependencyModule.disableDependency(SoftDependency.WORLD_GUARD);
               return;
            }
         }
      }

   }
}
