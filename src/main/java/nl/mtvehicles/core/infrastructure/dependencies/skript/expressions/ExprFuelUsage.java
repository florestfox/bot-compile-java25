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

@Name("MTV Vehicle's vehicle fuel usage")
@Description({"Get the vehicle's vehicle fuel usage"})
@Examples({"set {_fuel} to {_car}'s vehicle fuel usage", "set {_fuel} to vehicle fuel of (player's driven mtv vehicle)", "set vehicle fuel usage of {_car} to 0.5"})
@Since({"2.5.6"})
public class ExprFuelUsage extends SimplePropertyExpression<Vehicle, Double> {
   protected String getPropertyName() {
      return "[mtv] vehicle fuel usage";
   }

   public Class<? extends Double> getReturnType() {
      return Double.class;
   }

   public @Nullable Double convert(Vehicle vehicle) {
      return vehicle == null ? null : vehicle.getFuelUsage();
   }

   public @Nullable Class<?> @NotNull [] acceptChange(Changer.ChangeMode mode) {
      return mode == ChangeMode.SET ? new Class[]{Double.class, Number.class} : null;
   }

   public void change(@NotNull Event event, @Nullable Object @NotNull [] delta, Changer.@NotNull ChangeMode changeMode) {
      if (changeMode == ChangeMode.SET) {
         Vehicle vehicle = (Vehicle)this.getExpr().getSingle(event);
         if (Main.isNotNull(delta, delta[0], vehicle)) {
            double changeValue = ((Number)delta[0]).doubleValue();
            vehicle.setFuelUsage(Math.max((double)0.0F, changeValue));
            vehicle.save();
            if (VehicleData.fuelUsage.containsKey(vehicle.getLicensePlate())) {
               VehicleData.fuelUsage.put(vehicle.getLicensePlate(), Math.max((double)0.0F, changeValue));
            }

         }
      }
   }

   static {
      register(ExprFuelUsage.class, Double.class, "[mtv] vehicle fuel usage", "mtvehicles");
   }
}
