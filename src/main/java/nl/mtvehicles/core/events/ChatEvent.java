package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.event.Cancellable;

public class ChatEvent extends MTVEvent implements IsCancellable, Cancellable {
   private String message;

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String message) {
      this.message = message;
   }
}
