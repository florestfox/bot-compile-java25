package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Result;

public abstract class AbstractWrappedEvent extends Event implements Cancellable {
   private Event.Result result;

   public AbstractWrappedEvent() {
      this.result = Result.DEFAULT;
   }

   public boolean isCancelled() {
      return this.result == Result.DENY;
   }

   public void setCancelled(boolean cancel) {
      if (cancel) {
         this.setResult(Result.DENY);
      }

   }

   public void setResult(Event.Result result) {
      this.result = result;
   }

   public Event.Result getResult() {
      return this.result;
   }
}
