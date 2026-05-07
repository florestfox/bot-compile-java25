package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.CanEditLicensePlate;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class VehicleRemoveMemberEvent extends MTVEvent implements IsCancellable, Cancellable, CanEditLicensePlate {
   private String licensePlate;
   private Player removedPlayer;

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public String getLicensePlate() {
      return this.licensePlate;
   }

   public void setLicensePlate(String licensePlate) {
      this.licensePlate = licensePlate;
   }

   public Player getRemover() {
      return super.getPlayer();
   }

   public Player getRemoved() {
      return this.removedPlayer;
   }

   public void setRemoved(Player player) {
      this.removedPlayer = player;
   }
}
