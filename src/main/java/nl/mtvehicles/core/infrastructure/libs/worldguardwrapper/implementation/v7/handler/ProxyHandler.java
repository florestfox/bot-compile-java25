package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.handler;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.WrappedState;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.handler.IHandler;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.WorldGuardImplementation;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.region.WrappedRegion;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegion;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ProxyHandler extends Handler {
   private final WorldGuardImplementation implementation;
   private final IHandler handler;

   public ProxyHandler(WorldGuardImplementation implementation, IHandler handler, Session session) {
      super(session);
      this.implementation = implementation;
      this.handler = handler;
   }

   public void initialize(LocalPlayer player, Location current, ApplicableRegionSet set) {
      Player bukkitPlayer = BukkitAdapter.adapt(player);
      org.bukkit.Location bukkitLocation = BukkitAdapter.adapt(current);
      this.handler.initialize(bukkitPlayer, bukkitLocation, this.implementation.wrapRegionSet((World)Objects.requireNonNull(bukkitLocation.getWorld()), set));
   }

   public boolean testMoveTo(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, MoveType moveType) {
      Player bukkitPlayer = BukkitAdapter.adapt(player);
      org.bukkit.Location bukkitFrom = BukkitAdapter.adapt(from);
      org.bukkit.Location bukkitTo = BukkitAdapter.adapt(to);
      return this.handler.testMoveTo(bukkitPlayer, bukkitFrom, bukkitTo, this.implementation.wrapRegionSet((World)Objects.requireNonNull(bukkitTo.getWorld()), toSet), moveType.name());
   }

   public boolean onCrossBoundary(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
      Player bukkitPlayer = BukkitAdapter.adapt(player);
      org.bukkit.Location bukkitFrom = BukkitAdapter.adapt(from);
      org.bukkit.Location bukkitTo = BukkitAdapter.adapt(to);
      Set<IWrappedRegion> mappedEntered = ImmutableSet.copyOf(Collections2.transform(entered, (region) -> new WrappedRegion(bukkitTo.getWorld(), region)));
      Set<IWrappedRegion> mappedExited = ImmutableSet.copyOf(Collections2.transform(exited, (region) -> new WrappedRegion(bukkitFrom.getWorld(), region)));
      return this.handler.onCrossBoundary(bukkitPlayer, bukkitFrom, bukkitTo, this.implementation.wrapRegionSet((World)Objects.requireNonNull(bukkitTo.getWorld()), toSet), mappedEntered, mappedExited, moveType.name());
   }

   public void tick(LocalPlayer player, ApplicableRegionSet set) {
      Player bukkitPlayer = BukkitAdapter.adapt(player);
      this.handler.tick(bukkitPlayer, this.implementation.wrapRegionSet(bukkitPlayer.getWorld(), set));
   }

   @Nullable
   public StateFlag.State getInvincibility(LocalPlayer player) {
      Player bukkitPlayer = BukkitAdapter.adapt(player);
      WrappedState state = this.handler.getInvincibility(bukkitPlayer);
      if (state == null) {
         return null;
      } else {
         return state == WrappedState.ALLOW ? State.ALLOW : State.DENY;
      }
   }
}
