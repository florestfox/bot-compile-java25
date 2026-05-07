package nl.mtvehicles.core.events.inventory;

import nl.mtvehicles.core.events.interfaces.HasInventory;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.entity.Player;

public class JerryCanMenuOpen extends MTVEvent implements IsCancellable, HasInventory {
   public JerryCanMenuOpen(Player player) {
      this.setPlayer(player);
   }

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public InventoryTitle getTitle() {
      return InventoryTitle.JERRYCAN_MENU;
   }
}
