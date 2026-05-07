package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.CanEditLicensePlate;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class VehicleAddMemberEvent extends MTVEvent implements IsCancellable, Cancellable, CanEditLicensePlate {
   private String licensePlate;
   private Player addedPlayer;

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public String getLicensePlate() {
      return this.licensePlate;
   }

   public void setLicensePlate(String licensePlate) {
      this.licensePlate = licensePlate;
   }

   public Player getAdder() {
      return super.getPlayer();
   }

   public Player getAdded() {
      return this.addedPlayer;
   }

   public void setAdded(Player player) {
      this.addedPlayer = player;
   }
}
