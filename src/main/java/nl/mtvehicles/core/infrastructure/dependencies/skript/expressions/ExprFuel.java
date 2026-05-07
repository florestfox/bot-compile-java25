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

@Name("MTV Vehicle's vehicle fuel")
@Description({"Get the vehicle's vehicle fuel (from VehicleData or VehicleData.yml – whatever is lower)"})
@Examples({"set {_fuel} to {_car}'s vehicle fuel level", "set {_fuel} to vehicle fuel level of (player's driven mtv vehicle)", "set vehicle fuel level of {_car} to 96", "remove 10 from {_car}'s vehicle fuel level"})
@Since({"2.5.6"})
public class ExprFuel extends SimplePropertyExpression<Vehicle, Double> {
   protected String getPropertyName() {
      return "[mtv] vehicle fuel level";
   }

   public Class<? extends Double> getReturnType() {
      return Double.class;
   }

   public @Nullable Double convert(Vehicle vehicle) {
      if (vehicle == null) {
         return null;
      } else {
         String license = vehicle.getLicensePlate();
         Double dataFuel = (Double)VehicleData.fuel.get(license);
         if (dataFuel == null) {
            dataFuel = (double)100.0F;
         }

         double configFuel = vehicle.getFuel();
         return Math.min(dataFuel, configFuel);
      }
   }

   public @Nullable Class<?> @NotNull [] acceptChange(Changer.ChangeMode mode) {
      return mode != ChangeMode.SET && mode != ChangeMode.ADD && mode != ChangeMode.REMOVE ? null : new Class[]{Double.class, Number.class};
   }

   public void change(@NotNull Event event, @Nullable Object @NotNull [] delta, Changer.@NotNull ChangeMode changeMode) {
      Vehicle vehicle = (Vehicle)this.getExpr().getSingle(event);
      if (Main.isNotNull(delta, delta[0], ((Number)delta[0]).doubleValue())) {
         if (Main.isNotNull(vehicle.getLicensePlate())) {
            double changeValue = ((Number)delta[0]).doubleValue();
            double currentFuel = Math.min((Double)VehicleData.fuel.get(vehicle.getLicensePlate()), vehicle.getFuel());
            switch (changeMode) {
               case SET:
                  this.setFuel(vehicle, currentFuel, changeValue);
                  break;
               case ADD:
                  this.setFuel(vehicle, currentFuel, currentFuel + changeValue);
                  break;
               case REMOVE:
                  this.setFuel(vehicle, currentFuel, currentFuel - changeValue);
            }

         }
      }
   }

   private void setFuel(Vehicle vehicle, double currentFuel, double newFuel) {
      String licensePlate = vehicle.getLicensePlate();
      if (licensePlate != null) {
         double finalFuel = Math.max((double)0.0F, Math.min((double)100.0F, newFuel));
         vehicle.setFuel(finalFuel);
         vehicle.save();
         VehicleData.fuel.put(licensePlate, finalFuel);
         if (VehicleData.fallDamage.get(vehicle.getLicensePlate()) != null && finalFuel > currentFuel) {
            VehicleData.fallDamage.remove(vehicle.getLicensePlate());
         }

      }
   }

   static {
      register(ExprFuel.class, Double.class, "[mtv] vehicle fuel level", "mtvehicles");
   }
}
