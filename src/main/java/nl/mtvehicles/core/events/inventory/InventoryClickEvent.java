package nl.mtvehicles.core.events.inventory;

import nl.mtvehicles.core.events.interfaces.HasInventory;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;

public class InventoryClickEvent extends MTVEvent implements IsCancellable, HasInventory {
   private int clickedSlot;
   private final InventoryTitle title;

   public InventoryClickEvent(InventoryTitle title) {
      this.title = title;
   }

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public int getClickedSlot() {
      return this.clickedSlot;
   }

   public void setClickedSlot(int clickedSlot) {
      this.clickedSlot = clickedSlot;
   }

   public InventoryTitle getTitle() {
      return this.title;
   }
}
