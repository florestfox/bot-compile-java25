package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.event;

import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WrappedUseEntityEvent extends AbstractWrappedEvent {
   private static final HandlerList handlers = new HandlerList();
   private final Event originalEvent;
   private final Player player;
   private final Location target;
   private final Entity entity;

   public @NonNull HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public WrappedUseEntityEvent(Event originalEvent, Player player, Location target, Entity entity) {
      this.originalEvent = originalEvent;
      this.player = player;
      this.target = target;
      this.entity = entity;
   }

   public Event getOriginalEvent() {
      return this.originalEvent;
   }

   public Player getPlayer() {
      return this.player;
   }

   public Location getTarget() {
      return this.target;
   }

   public Entity getEntity() {
      return this.entity;
   }
}
