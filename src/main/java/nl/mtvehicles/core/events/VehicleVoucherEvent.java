package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.event.Cancellable;

public class VehicleVoucherEvent extends MTVEvent implements IsCancellable, Cancellable {
   private String voucherUUID;

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public String getVoucherUUID() {
      return this.voucherUUID;
   }

   public void setVoucherUUID(String voucherUUID) {
      this.voucherUUID = voucherUUID;
   }
}
