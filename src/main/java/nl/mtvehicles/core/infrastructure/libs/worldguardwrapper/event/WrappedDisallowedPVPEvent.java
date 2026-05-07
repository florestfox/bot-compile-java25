package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.event;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WrappedDisallowedPVPEvent extends AbstractWrappedEvent {
   private static final HandlerList handlers = new HandlerList();
   private final Player attacker;
   private final Player defender;
   private final Event cause;

   public @NonNull HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public WrappedDisallowedPVPEvent(Player attacker, Player defender, Event cause) {
      this.attacker = attacker;
      this.defender = defender;
      this.cause = cause;
   }

   public Player getAttacker() {
      return this.attacker;
   }

   public Player getDefender() {
      return this.defender;
   }

   public Event getCause() {
      return this.cause;
   }
}
