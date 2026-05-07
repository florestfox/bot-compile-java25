package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.legacy;

import com.google.common.collect.Iterators;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Polygonal2DSelection;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.IWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.handler.IHandler;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.IWorldGuardImplementation;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.legacy.flag.AbstractWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.legacy.region.WrappedRegion;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.legacy.utility.WorldGuardFlagUtilities;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.legacy.utility.WorldGuardVectorUtilities;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegion;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegionSet;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.ICuboidSelection;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.IPolygonalSelection;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.ISelection;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldGuardImplementation implements IWorldGuardImplementation {
   private final WorldGuardPlugin worldGuardPlugin = WorldGuardPlugin.inst();
   private final WorldEditPlugin worldEditPlugin;

   public WorldGuardImplementation() {
      try {
         this.worldEditPlugin = this.worldGuardPlugin.getWorldEdit();
      } catch (CommandException e) {
         throw new RuntimeException(e);
      }
   }

   private Optional<LocalPlayer> wrapPlayer(OfflinePlayer player) {
      return Optional.ofNullable(player).map((bukkitPlayer) -> bukkitPlayer.isOnline() ? this.worldGuardPlugin.wrapPlayer((Player)bukkitPlayer) : this.worldGuardPlugin.wrapOfflinePlayer(bukkitPlayer));
   }

   private Optional<RegionManager> getWorldManager(@NonNull World world) {
      if (world == null) {
         throw new NullPointerException("world is marked non-null but is null");
      } else {
         return Optional.ofNullable(this.worldGuardPlugin.getRegionManager(world));
      }
   }

   private Optional<ApplicableRegionSet> getApplicableRegions(@NonNull Location location) {
      if (location == null) {
         throw new NullPointerException("location is marked non-null but is null");
      } else {
         return this.getWorldManager((World)Objects.requireNonNull(location.getWorld())).map((manager) -> manager.getApplicableRegions(location));
      }
   }

   private Optional<ApplicableRegionSet> getApplicableRegions(@NonNull Location minimum, @NonNull Location maximum) {
      if (minimum == null) {
         throw new NullPointerException("minimum is marked non-null but is null");
      } else if (maximum == null) {
         throw new NullPointerException("maximum is marked non-null but is null");
      } else {
         return this.getWorldManager((World)Objects.requireNonNull(minimum.getWorld())).map((manager) -> manager.getApplicableRegions(new ProtectedCuboidRegion("temp", WorldGuardVectorUtilities.toBlockVector(minimum), WorldGuardVectorUtilities.toBlockVector(maximum))));
      }
   }

   private <V> Optional<V> queryValue(Player player, @NonNull Location location, @NonNull Flag<V> flag) {
      if (location == null) {
         throw new NullPointerException("location is marked non-null but is null");
      } else if (flag == null) {
         throw new NullPointerException("flag is marked non-null but is null");
      } else {
         return this.getApplicableRegions(location).map((applicableRegions) -> applicableRegions.queryValue((RegionAssociable)this.wrapPlayer(player).orElse((Object)null), flag));
      }
   }

   private IWrappedRegionSet wrapRegionSet(final @NonNull World world, final @NonNull ApplicableRegionSet regionSet) {
      if (world == null) {
         throw new NullPointerException("world is marked non-null but is null");
      } else if (regionSet == null) {
         throw new NullPointerException("regionSet is marked non-null but is null");
      } else {
         return new IWrappedRegionSet() {
            public Iterator<IWrappedRegion> iterator() {
               return Iterators.transform(regionSet.iterator(), (region) -> new WrappedRegion(world, region));
            }

            public boolean isVirtual() {
               return regionSet.isVirtual();
            }

            public <V> Optional<V> queryValue(OfflinePlayer subject, IWrappedFlag<V> flag) {
               LocalPlayer subjectHandle = (LocalPlayer)WorldGuardImplementation.this.wrapPlayer(subject).orElse((Object)null);
               AbstractWrappedFlag<V> wrappedFlag = (AbstractWrappedFlag)flag;
               Optional var10000 = Optional.ofNullable(regionSet.queryValue(subjectHandle, wrappedFlag.getHandle()));
               Objects.requireNonNull(wrappedFlag);
               return var10000.flatMap(wrappedFlag::fromWGValue);
            }

            public <V> Collection<V> queryAllValues(OfflinePlayer subject, IWrappedFlag<V> flag) {
               LocalPlayer subjectHandle = (LocalPlayer)WorldGuardImplementation.this.wrapPlayer(subject).orElse((Object)null);
               AbstractWrappedFlag<V> wrappedFlag = (AbstractWrappedFlag)flag;
               Stream var10000 = regionSet.queryAllValues(subjectHandle, wrappedFlag.getHandle()).stream();
               Objects.requireNonNull(wrappedFlag);
               return (Collection)var10000.map(wrappedFlag::fromWGValue).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
            }

            public boolean isOwnerOfAll(OfflinePlayer player) {
               LocalPlayer playerHandle = (LocalPlayer)WorldGuardImplementation.this.wrapPlayer(player).orElse((Object)null);
               return regionSet.isOwnerOfAll(playerHandle);
            }

            public boolean isMemberOfAll(OfflinePlayer player) {
               LocalPlayer playerHandle = (LocalPlayer)WorldGuardImplementation.this.wrapPlayer(player).orElse((Object)null);
               return regionSet.isMemberOfAll(playerHandle);
            }

            public int size() {
               return regionSet.size();
            }

            public Set<IWrappedRegion> getRegions() {
               return (Set)regionSet.getRegions().stream().map((region) -> new WrappedRegion(world, region)).collect(Collectors.toSet());
            }
         };
      }
   }

   public JavaPlugin getWorldGuardPlugin() {
      return WorldGuardPlugin.inst();
   }

   public int getApiVersion() {
      return -6;
   }

   public void registerHandler(Supplier<IHandler> factory) {
      throw new UnsupportedOperationException("Custom flag handlers aren't supported in this version of WorldGuard!");
   }

   public <T> Optional<IWrappedFlag<T>> getFlag(String name, Class<T> type) {
      for(Flag<?> currentFlag : DefaultFlag.getFlags()) {
         if (currentFlag.getName().equalsIgnoreCase(name)) {
            return Optional.of(WorldGuardFlagUtilities.wrap(currentFlag, type));
         }
      }

      return Optional.empty();
   }

   public <T> Optional<T> queryFlag(Player player, @NonNull Location location, @NonNull IWrappedFlag<T> flag) {
      if (location == null) {
         throw new NullPointerException("location is marked non-null but is null");
      } else if (flag == null) {
         throw new NullPointerException("flag is marked non-null but is null");
      } else {
         AbstractWrappedFlag<T> wrappedFlag = (AbstractWrappedFlag)flag;
         Optional var10000 = this.queryValue(player, location, wrappedFlag.getHandle());
         Objects.requireNonNull(wrappedFlag);
         return var10000.flatMap(wrappedFlag::fromWGValue);
      }
   }

   public Map<IWrappedFlag<?>, Object> queryApplicableFlags(Player player, Location location) {
      ApplicableRegionSet applicableSet = (ApplicableRegionSet)this.getApplicableRegions(location).orElse((Object)null);
      if (applicableSet == null) {
         return Collections.emptyMap();
      } else {
         LocalPlayer localPlayer = (LocalPlayer)this.wrapPlayer(player).orElse((Object)null);
         Map<IWrappedFlag<?>, Object> flags = new HashMap();
         Set<String> seen = new HashSet();

         for(ProtectedRegion region : applicableSet.getRegions()) {
            for(Flag<?> flag : region.getFlags().keySet()) {
               if (seen.add(flag.getName())) {
                  Object value = applicableSet.queryValue(localPlayer, flag);
                  if (value != null) {
                     try {
                        Map.Entry<IWrappedFlag<?>, Object> wrapped = WorldGuardFlagUtilities.wrap(flag, value);
                        flags.put((IWrappedFlag)wrapped.getKey(), wrapped.getValue());
                     } catch (IllegalArgumentException var13) {
                     }
                  }
               }
            }
         }

         return flags;
      }
   }

   public <T> Optional<IWrappedFlag<T>> registerFlag(@NonNull String name, @NonNull Class<T> type, T defaultValue) {
      if (name == null) {
         throw new NullPointerException("name is marked non-null but is null");
      } else if (type == null) {
         throw new NullPointerException("type is marked non-null but is null");
      } else {
         throw new UnsupportedOperationException("Custom flags aren't supported in this version of WorldGuard!");
      }
   }

   public Optional<IWrappedRegion> getRegion(@NonNull World world, @NonNull String id) {
      if (world == null) {
         throw new NullPointerException("world is marked non-null but is null");
      } else if (id == null) {
         throw new NullPointerException("id is marked non-null but is null");
      } else {
         return this.getWorldManager(world).map((regionManager) -> regionManager.getRegion(id)).map((region) -> new WrappedRegion(world, region));
      }
   }

   public Map<String, IWrappedRegion> getRegions(@NonNull World world) {
      if (world == null) {
         throw new NullPointerException("world is marked non-null but is null");
      } else {
         RegionManager regionManager = this.worldGuardPlugin.getRegionManager(world);
         Map<String, ProtectedRegion> regions = regionManager.getRegions();
         Map<String, IWrappedRegion> map = new HashMap();
         regions.forEach((name, region) -> map.put(name, new WrappedRegion(world, region)));
         return map;
      }
   }

   public Set<IWrappedRegion> getRegions(@NonNull Location location) {
      if (location == null) {
         throw new NullPointerException("location is marked non-null but is null");
      } else {
         ApplicableRegionSet regionSet = (ApplicableRegionSet)this.getApplicableRegions(location).orElse((Object)null);
         Set<IWrappedRegion> set = new HashSet();
         if (regionSet == null) {
            return set;
         } else {
            regionSet.forEach((region) -> set.add(new WrappedRegion(location.getWorld(), region)));
            return set;
         }
      }
   }

   public Set<IWrappedRegion> getRegions(@NonNull Location minimum, @NonNull Location maximum) {
      if (minimum == null) {
         throw new NullPointerException("minimum is marked non-null but is null");
      } else if (maximum == null) {
         throw new NullPointerException("maximum is marked non-null but is null");
      } else {
         ApplicableRegionSet regionSet = (ApplicableRegionSet)this.getApplicableRegions(minimum, maximum).orElse((Object)null);
         Set<IWrappedRegion> set = new HashSet();
         if (regionSet == null) {
            return set;
         } else {
            regionSet.forEach((region) -> set.add(new WrappedRegion(minimum.getWorld(), region)));
            return set;
         }
      }
   }

   public Optional<IWrappedRegionSet> getRegionSet(@NonNull Location location) {
      if (location == null) {
         throw new NullPointerException("location is marked non-null but is null");
      } else {
         return this.getApplicableRegions(location).map((regionSet) -> this.wrapRegionSet((World)Objects.requireNonNull(location.getWorld()), regionSet));
      }
   }

   public Optional<IWrappedRegion> addRegion(@NonNull String id, @NonNull List<Location> points, int minY, int maxY) {
      if (id == null) {
         throw new NullPointerException("id is marked non-null but is null");
      } else if (points == null) {
         throw new NullPointerException("points is marked non-null but is null");
      } else {
         World world = (World)Objects.requireNonNull(((Location)points.get(0)).getWorld());
         ProtectedRegion region;
         if (points.size() == 2) {
            region = new ProtectedCuboidRegion(id, WorldGuardVectorUtilities.toBlockVector((Location)points.get(0)), WorldGuardVectorUtilities.toBlockVector((Location)points.get(1)));
         } else {
            region = new ProtectedPolygonalRegion(id, WorldGuardVectorUtilities.toBlockVector2DList(points), minY, maxY);
         }

         Optional<RegionManager> manager = this.getWorldManager(world);
         if (manager.isPresent()) {
            ((RegionManager)manager.get()).addRegion(region);
            return Optional.of(new WrappedRegion(world, region));
         } else {
            return Optional.empty();
         }
      }
   }

   public Optional<Set<IWrappedRegion>> removeRegion(@NonNull World world, @NonNull String id) {
      if (world == null) {
         throw new NullPointerException("world is marked non-null but is null");
      } else if (id == null) {
         throw new NullPointerException("id is marked non-null but is null");
      } else {
         Optional<Set<ProtectedRegion>> set = this.getWorldManager(world).map((manager) -> manager.removeRegion(id));
         return set.map((protectedRegions) -> (Set)protectedRegions.stream().map((region) -> new WrappedRegion(world, region)).collect(Collectors.toSet()));
      }
   }

   public Optional<ISelection> getPlayerSelection(@NonNull Player player) {
      if (player == null) {
         throw new NullPointerException("player is marked non-null but is null");
      } else {
         return Optional.ofNullable(this.worldEditPlugin.getSelection(player)).map((selection) -> {
            if (selection instanceof CuboidSelection) {
               return new ICuboidSelection() {
                  public Location getMinimumPoint() {
                     return selection.getMinimumPoint();
                  }

                  public Location getMaximumPoint() {
                     return selection.getMaximumPoint();
                  }
               };
            } else if (selection instanceof Polygonal2DSelection) {
               return new IPolygonalSelection() {
                  public Set<Location> getPoints() {
                     return (Set)((Polygonal2DSelection)selection).getNativePoints().stream().map((vector) -> new BlockVector(vector.toVector())).map((vector) -> WorldGuardVectorUtilities.fromBlockVector(selection.getWorld(), vector)).collect(Collectors.toSet());
                  }

                  public int getMinimumY() {
                     return selection.getMinimumPoint().getBlockY();
                  }

                  public int getMaximumY() {
                     return selection.getMaximumPoint().getBlockY();
                  }
               };
            } else {
               throw new UnsupportedOperationException("Unsupported " + selection.getClass().getSimpleName() + " selection!");
            }
         });
      }
   }
}
