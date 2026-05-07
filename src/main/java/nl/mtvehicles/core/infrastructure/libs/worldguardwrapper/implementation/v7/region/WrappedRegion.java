package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.region;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.IWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.flag.AbstractWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.utility.WorldGuardFlagUtilities;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedDomain;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegion;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.ICuboidSelection;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.IPolygonalSelection;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.ISelection;
import org.bukkit.Location;
import org.bukkit.World;

public class WrappedRegion implements IWrappedRegion {
   private final World world;
   private final ProtectedRegion handle;

   public ISelection getSelection() {
      if (this.handle instanceof ProtectedCuboidRegion) {
         return new ICuboidSelection() {
            public Location getMinimumPoint() {
               return BukkitAdapter.adapt(WrappedRegion.this.world, WrappedRegion.this.handle.getMinimumPoint());
            }

            public Location getMaximumPoint() {
               return BukkitAdapter.adapt(WrappedRegion.this.world, WrappedRegion.this.handle.getMaximumPoint());
            }
         };
      } else if (this.handle instanceof ProtectedPolygonalRegion) {
         return new IPolygonalSelection() {
            public Set<Location> getPoints() {
               return (Set)WrappedRegion.this.handle.getPoints().stream().map(BlockVector2::toBlockVector3).map((vector) -> BukkitAdapter.adapt(WrappedRegion.this.world, vector)).collect(Collectors.toSet());
            }

            public int getMinimumY() {
               return WrappedRegion.this.handle.getMinimumPoint().getBlockY();
            }

            public int getMaximumY() {
               return WrappedRegion.this.handle.getMaximumPoint().getBlockY();
            }
         };
      } else {
         throw new UnsupportedOperationException("Unsupported " + this.handle.getClass().getSimpleName() + " region!");
      }
   }

   public String getId() {
      return this.handle.getId();
   }

   public Map<IWrappedFlag<?>, Object> getFlags() {
      Map<IWrappedFlag<?>, Object> result = new HashMap();
      this.handle.getFlags().forEach((flag, value) -> {
         if (value != null) {
            try {
               Map.Entry<IWrappedFlag<?>, Object> wrapped = WorldGuardFlagUtilities.wrap(flag, value);
               result.put((IWrappedFlag)wrapped.getKey(), wrapped.getValue());
            } catch (IllegalArgumentException var4) {
            }
         }

      });
      return result;
   }

   public <T> Optional<T> getFlag(IWrappedFlag<T> flag) {
      AbstractWrappedFlag<T> wrappedFlag = (AbstractWrappedFlag)flag;
      return Optional.ofNullable(this.handle.getFlag(wrappedFlag.getHandle())).map((value) -> wrappedFlag.fromWGValue(value));
   }

   public <T> void setFlag(IWrappedFlag<T> flag, T value) {
      AbstractWrappedFlag<T> wrappedFlag = (AbstractWrappedFlag)flag;
      this.handle.setFlag(wrappedFlag.getHandle(), wrappedFlag.fromWrapperValue(value).orElse((Object)null));
   }

   public int getPriority() {
      return this.handle.getPriority();
   }

   public void setPriority(int priority) {
      this.handle.setPriority(priority);
   }

   public IWrappedDomain getOwners() {
      return new IWrappedDomain() {
         public Set<UUID> getPlayers() {
            return WrappedRegion.this.handle.getOwners().getUniqueIds();
         }

         public void addPlayer(UUID uuid) {
            WrappedRegion.this.handle.getOwners().addPlayer(uuid);
         }

         public void removePlayer(UUID uuid) {
            WrappedRegion.this.handle.getOwners().removePlayer(uuid);
         }

         public Set<String> getGroups() {
            return WrappedRegion.this.handle.getOwners().getGroups();
         }

         public void addGroup(String name) {
            WrappedRegion.this.handle.getOwners().addGroup(name);
         }

         public void removeGroup(String name) {
            WrappedRegion.this.handle.getOwners().removeGroup(name);
         }
      };
   }

   public IWrappedDomain getMembers() {
      return new IWrappedDomain() {
         public Set<UUID> getPlayers() {
            return WrappedRegion.this.handle.getMembers().getUniqueIds();
         }

         public void addPlayer(UUID uuid) {
            WrappedRegion.this.handle.getMembers().addPlayer(uuid);
         }

         public void removePlayer(UUID uuid) {
            WrappedRegion.this.handle.getMembers().removePlayer(uuid);
         }

         public Set<String> getGroups() {
            return WrappedRegion.this.handle.getMembers().getGroups();
         }

         public void addGroup(String name) {
            WrappedRegion.this.handle.getMembers().addGroup(name);
         }

         public void removeGroup(String name) {
            WrappedRegion.this.handle.getMembers().removeGroup(name);
         }
      };
   }

   public boolean contains(Location location) {
      return this.handle.contains(BukkitAdapter.asBlockVector(location));
   }

   public WrappedRegion(World world, ProtectedRegion handle) {
      this.world = world;
      this.handle = handle;
   }

   public World getWorld() {
      return this.world;
   }

   public ProtectedRegion getHandle() {
      return this.handle;
   }
}
