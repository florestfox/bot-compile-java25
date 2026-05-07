package nl.mtvehicles.core.events;

import javax.annotation.Nullable;
import lombok.Generated;
import nl.mtvehicles.core.events.interfaces.CanEditLicensePlate;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityDamageEvent;

public class VehicleDamageEvent extends MTVEvent implements IsCancellable, Cancellable, CanEditLicensePlate {
   @Nullable
   private Entity damager;
   private double damage;
   private String licensePlate;
   private EntityDamageEvent.DamageCause damageCause;

   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }

   public String getLicensePlate() {
      return this.licensePlate;
   }

   public void setLicensePlate(String licensePlate) {
      this.licensePlate = licensePlate;
   }

   @Nullable
   public Entity getDamager() {
      return this.damager;
   }

   public void setDamager(@Nullable Entity damager) {
      this.damager = damager;
   }

   @Generated
   public void setDamage(double damage) {
      this.damage = damage;
   }

   @Generated
   public double getDamage() {
      return this.damage;
   }

   @Generated
   public void setDamageCause(EntityDamageEvent.DamageCause damageCause) {
      this.damageCause = damageCause;
   }

   @Generated
   public EntityDamageEvent.DamageCause getDamageCause() {
      return this.damageCause;
   }
}
