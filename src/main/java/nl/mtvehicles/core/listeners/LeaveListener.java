package nl.mtvehicles.core.listeners;

import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener extends MTVListener {
   @EventHandler
   public void onPlayerLeave(PlayerQuitEvent pq) {
      Player p = pq.getPlayer();
      if (p.isInsideVehicle()) {
         Entity vehicle = p.getVehicle();
         if (VehicleUtils.isVehicle(vehicle)) {
            VehicleUtils.kickOut(p);
         }
      }
   }
}
