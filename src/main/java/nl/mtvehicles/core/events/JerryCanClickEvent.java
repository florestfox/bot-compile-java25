package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.HasJerryCan;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;

public class JerryCanClickEvent extends MTVEvent implements IsCancellable, HasJerryCan {
   private final int jerryCanFuel;
   private final int jerryCanSize;

   public JerryCanClickEvent(int jerryCanFuel, int jerryCanSize) {
      this.jerryCanFuel = jerryCanFuel;
      this.jerryCanSize = jerryCanSize;
   }

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public int getJerryCanFuel() {
      return this.jerryCanFuel;
   }

   public int getJerryCanSize() {
      return this.jerryCanSize;
   }
}
