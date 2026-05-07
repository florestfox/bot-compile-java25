package nl.mtvehicles.core.infrastructure.dependencies.skript.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("MTV Vehicle's vehicle health")
@Description({"Get the vehicle's vehicle fuel usage"})
@Examples({"set {_currentHealth} to {_car}'s vehicle health", "set {_health} to vehicle health of (player's driven mtv vehicle)", "set vehicle health of {_car} to 100", "remove 55.5 from {_car}'s vehicle health", "add 0.5 to {_car}'s vehicle health"})
@Since({"2.5.6"})
public class ExprHealth extends SimplePropertyExpression<Vehicle, Double> {
   protected String getPropertyName() {
      return "[mtv] vehicle health";
   }

   public Class<? extends Double> getReturnType() {
      return Double.class;
   }

   public @Nullable Double convert(Vehicle vehicle) {
      return vehicle == null ? null : vehicle.getHealth();
   }

   public @Nullable Class<?> @NotNull [] acceptChange(Changer.ChangeMode mode) {
      return mode != ChangeMode.SET && mode != ChangeMode.ADD && mode != ChangeMode.REMOVE ? null : new Class[]{Double.class, Number.class};
   }

   public void change(@NotNull Event event, @Nullable Object @NotNull [] delta, Changer.@NotNull ChangeMode changeMode) {
      Vehicle vehicle = (Vehicle)this.getExpr().getSingle(event);
      if (Main.isNotNull(delta, delta[0], vehicle)) {
         double changeValue = ((Number)delta[0]).doubleValue();
         double currentHealth = vehicle.getHealth();
         switch (changeMode) {
            case SET:
               this.setHealth(vehicle, changeValue);
               break;
            case ADD:
               this.setHealth(vehicle, currentHealth + changeValue);
               break;
            case REMOVE:
               this.setHealth(vehicle, currentHealth - changeValue);
         }

      }
   }

   private void setHealth(@NotNull Vehicle vehicle, double newHealth) {
      double health = newHealth < (double)0.0F ? (double)0.0F : newHealth;
      if (health > (double)0.0F) {
         VehicleData.markVehicleAsRepaired(vehicle.getLicensePlate());
      } else {
         VehicleData.markVehicleAsDestroyed(vehicle.getLicensePlate());
      }

      vehicle.setHealth(health);
      vehicle.save();
   }

   static {
      register(ExprHealth.class, Double.class, "[mtv] vehicle health", "mtvehicles");
   }
}
