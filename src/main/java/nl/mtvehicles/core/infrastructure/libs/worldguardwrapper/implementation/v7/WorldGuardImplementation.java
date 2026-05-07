package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7;

import com.google.common.collect.Iterators;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.DoubleFlag;
import com.sk89q.worldguard.protection.flags.EnumFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.IntegerFlag;
import com.sk89q.worldguard.protection.flags.LocationFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.VectorFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.WrappedState;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.handler.IHandler;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.IWorldGuardImplementation;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.flag.AbstractWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.handler.ProxyHandler;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.region.WrappedRegion;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.utility.WorldGuardFlagUtilities;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegion;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegionSet;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.ICuboidSelection;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.IPolygonalSelection;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.selection.ISelection;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.util.proxy.ProxyFactory;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class WorldGuardImplementation implements IWorldGuardImplementation {
   private final WorldGuard core = WorldGuard.getInstance();
   private final FlagRegistry flagRegistry;

   public WorldGuardImplementation() {
      this.flagRegistry = this.core.getFlagRegistry();
   }

   private Optional<LocalPlayer> wrapPlayer(OfflinePlayer player) {
      return Optional.ofNullable(player).map((bukkitPlayer) -> bukkitPlayer.isOnline() ? WorldGuardPlugin.inst().wrapPlayer((Player)bukkitPlayer) : WorldGuardPlugin.inst().wrapOfflinePlayer(bukkitPlayer));
   }

   private Optional<RegionManager> getWorldManager(@NonNull World world) {
      if (world == null) {
         throw new NullPointerException("world is marked non-null but is null");
      } else {
         return Optional.ofNullable(this.core.getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world)));
      }
   }

   private Optional<ApplicableRegionSet> getApplicableRegions(@NonNull Location location) {
      if (location == null) {
         throw new NullPointerException("location is marked non-null but is null");
      } else {
         return this.getWorldManager((World)Objects.requireNonNull(location.getWorld())).map((manager) -> manager.getApplicableRegions(BukkitAdapter.asBlockVector(location)));
      }
   }

   private Optional<ApplicableRegionSet> getApplicableRegions(@NonNull Location minimum, @NonNull Location maximum) {
      if (minimum == null) {
         throw new NullPointerException("minimum is marked non-null but is null");
      } else if (maximum == null) {
         throw new NullPointerException("maximum is marked non-null but is null");
      } else {
         return this.getWorldManager((World)Objects.requireNonNull(minimum.getWorld())).map((manager) -> manager.getApplicableRegions(new ProtectedCuboidRegion("temp", BukkitAdapter.asBlockVector(minimum), BukkitAdapter.asBlockVector(maximum))));
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

   public IWrappedRegionSet wrapRegionSet(final @NonNull World world, final @NonNull ApplicableRegionSet regionSet) {
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
      return 7;
   }

   public void registerHandler(final Supplier<IHandler> factory) {
      ProxyFactory proxyFactory = new ProxyFactory();
      proxyFactory.setUseCache(false);
      proxyFactory.setSuperclass(ProxyHandler.class);

      final Constructor<? extends ProxyHandler> handlerConstructor;
      try {
         Class<? extends ProxyHandler> handlerClass = proxyFactory.createClass();
         handlerConstructor = handlerClass.getDeclaredConstructor(WorldGuardImplementation.class, IHandler.class, Session.class);
      } catch (NoSuchMethodException e) {
         throw new RuntimeException(e);
      }

      this.core.getPlatform().getSessionManager().registerHandler(new Handler.Factory<Handler>() {
         public Handler create(Session session) {
            IHandler handler = (IHandler)factory.get();

            try {
               return (Handler)handlerConstructor.newInstance(WorldGuardImplementation.this, handler, session);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
               throw new RuntimeException(e);
            }
         }
      }, (Handler.Factory)null);
   }

   public <T> Optional<IWrappedFlag<T>> getFlag(String name, Class<T> type) {
      return Optional.ofNullable(this.flagRegistry.get(name)).map((flag) -> WorldGuardFlagUtilities.wrap(flag, type));
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
         Flag<?> flag;
         if (type.equals(WrappedState.class)) {
            flag = new StateFlag(name, defaultValue == WrappedState.ALLOW);
         } else if (!type.equals(Boolean.class) && !type.equals(Boolean.TYPE)) {
            if (!type.equals(Double.class) && !type.equals(Double.TYPE)) {
               if (type.equals(Enum.class)) {
                  flag = new EnumFlag(name, type);
               } else if (!type.equals(Integer.class) && !type.equals(Integer.TYPE)) {
                  if (type.equals(Location.class)) {
                     flag = new LocationFlag(name);
                  } else if (type.equals(String.class)) {
                     flag = new StringFlag(name, (String)defaultValue);
                  } else {
                     if (!type.equals(Vector.class)) {
                        throw new IllegalArgumentException("Unsupported flag type " + type.getName());
                     }

                     flag = new VectorFlag(name);
                  }
               } else {
                  flag = new IntegerFlag(name);
               }
            } else {
               flag = new DoubleFlag(name);
            }
         } else {
            flag = new BooleanFlag(name);
         }

         try {
            this.flagRegistry.register(flag);
            return Optional.of(WorldGuardFlagUtilities.wrap(flag, type));
         } catch (FlagConflictException var6) {
            return Optional.empty();
         }
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
         RegionManager regionManager = this.core.getPlatform().getRegionContainer().get(new BukkitWorld(world));
         if (regionManager == null) {
            return Collections.emptyMap();
         } else {
            Map<String, ProtectedRegion> regions = regionManager.getRegions();
            Map<String, IWrappedRegion> map = new HashMap();
            regions.forEach((name, region) -> map.put(name, new WrappedRegion(world, region)));
            return map;
         }
      }
   }

   public Set<IWrappedRegion> getRegions(@NonNull Location location) {
      if (location == null) {
         throw new NullPointerException("location is marked non-null but is null");
      } else {
         ApplicableRegionSet regionSet = (ApplicableRegionSet)this.getApplicableRegions(location).orElse((Object)null);
         return regionSet == null ? Collections.emptySet() : (Set)regionSet.getRegions().stream().map((region) -> new WrappedRegion(location.getWorld(), region)).collect(Collectors.toSet());
      }
   }

   public Set<IWrappedRegion> getRegions(@NonNull Location minimum, @NonNull Location maximum) {
      if (minimum == null) {
         throw new NullPointerException("minimum is marked non-null but is null");
      } else if (maximum == null) {
         throw new NullPointerException("maximum is marked non-null but is null");
      } else {
         ApplicableRegionSet regionSet = (ApplicableRegionSet)this.getApplicableRegions(minimum, maximum).orElse((Object)null);
         return regionSet == null ? Collections.emptySet() : (Set)regionSet.getRegions().stream().map((region) -> new WrappedRegion(minimum.getWorld(), region)).collect(Collectors.toSet());
      }
   }

   public Optional<IWrappedRegionSet> getRegionSet(@NonNull Location location) {
      if (location == null) {
         throw new NullPointerException("location is marked non-null but is null");
      } else {
         return Optional.empty();
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
            region = new ProtectedCuboidRegion(id, BukkitAdapter.asBlockVector((Location)points.get(0)), BukkitAdapter.asBlockVector((Location)points.get(1)));
         } else {
            List<BlockVector2> vectorPoints = (List)points.stream().map((location) -> BukkitAdapter.asBlockVector(location).toBlockVector2()).collect(Collectors.toList());
            region = new ProtectedPolygonalRegion(id, vectorPoints, minY, maxY);
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
         Region region;
         try {
            region = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(player)).getSelection(BukkitAdapter.adapt(player.getWorld()));
         } catch (IncompleteRegionException var4) {
            region = null;
         }

         return Optional.ofNullable(region).map((selection) -> {
            final World world = (World)Optional.ofNullable(selection.getWorld()).map(BukkitAdapter::adapt).orElse((Object)null);
            if (world == null) {
               return null;
            } else if (selection instanceof CuboidRegion) {
               return new ICuboidSelection() {
                  public Location getMinimumPoint() {
                     return BukkitAdapter.adapt(world, selection.getMinimumPoint());
                  }

                  public Location getMaximumPoint() {
                     return BukkitAdapter.adapt(world, selection.getMaximumPoint());
                  }
               };
            } else if (selection instanceof Polygonal2DRegion) {
               return new IPolygonalSelection() {
                  public Set<Location> getPoints() {
                     return (Set)((Polygonal2DRegion)selection).getPoints().stream().map(BlockVector2::toBlockVector3).map((vector) -> BukkitAdapter.adapt(world, vector)).collect(Collectors.toSet());
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
