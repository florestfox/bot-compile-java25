package nl.mtvehicles.core.listeners;

import java.util.Collection;
import nl.mtvehicles.core.events.VehicleDamageEvent;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class VehicleOtherDamageListener extends MTVListener {
   public VehicleOtherDamageListener() {
      super(new VehicleDamageEvent());
   }

   @EventHandler
   public void onVehicleDamage(EntityDamageEvent event) {
      this.event = event;
      Entity victim = event.getEntity();
      if (VehicleUtils.isVehicle(victim)) {
         String license = VehicleUtils.getLicensePlate(victim);
         if (license != null) {
            EntityDamageEvent.DamageCause damageCause = event.getCause();
            if (damageCause == DamageCause.FIRE || damageCause == DamageCause.LAVA || damageCause == DamageCause.PROJECTILE) {
               VehicleDamageEvent api = (VehicleDamageEvent)this.getAPI();
               api.setDamage(event.getDamage());
               api.setDamageCause(damageCause);
               api.setLicensePlate(license);
               this.callAPI();
               if (!this.isCancelled()) {
                  VehicleEntityListener.damage(api.getLicensePlate(), api.getDamage());
               }
            }
         }
      }
   }

   @EventHandler
   public void onBlockExplode(BlockExplodeEvent event) {
      this.event = event;
      Collection<Entity> nearbyEntities = event.getBlock().getLocation().getWorld().getNearbyEntities(event.getBlock().getLocation(), (double)5.0F, (double)5.0F, (double)5.0F);
      this.damageNearbyVehicles(nearbyEntities, (double)40.0F, DamageCause.BLOCK_EXPLOSION);
   }

   @EventHandler
   public void onEntityExplode(EntityExplodeEvent event) {
      this.event = event;
      Collection<Entity> nearbyEntities = event.getEntity().getNearbyEntities((double)5.0F, (double)5.0F, (double)5.0F);
      if (event.getEntity().getType().equals(EntityType.WITHER)) {
         this.damageNearbyVehicles(nearbyEntities, (double)80.0F, DamageCause.ENTITY_EXPLOSION);
      } else {
         this.damageNearbyVehicles(nearbyEntities, (double)40.0F, DamageCause.ENTITY_EXPLOSION);
      }

   }

   private void damageNearbyVehicles(Collection<Entity> entities, double damage, EntityDamageEvent.DamageCause cause) {
      for(Entity entity : entities) {
         if (VehicleUtils.isVehicle(entity)) {
            String license = VehicleUtils.getLicensePlate(entity);
            if (license != null) {
               VehicleDamageEvent api = (VehicleDamageEvent)this.getAPI();
               api.setDamage(damage);
               api.setDamageCause(cause);
               api.setLicensePlate(license);
               this.callAPI();
               if (!this.isCancelled()) {
                  VehicleEntityListener.damage(api.getLicensePlate(), api.getDamage());
               }
            }
         }
      }

   }
}
