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

@Name("MTV Vehicle's vehicle max speed")
@Description({"Get/set the vehicle's vehicle max speed"})
@Examples({"set {_licensePlate} to {_car}'s vehicle max speed", "set mtv vehicle maximum speed of {_helicopter} to 3"})
@Since({"2.5.8"})
public class ExprMaxSpeed extends SimplePropertyExpression<Vehicle, Double> {
   protected String getPropertyName() {
      return "[mtv] vehicle max speed";
   }

   public @Nullable Double convert(Vehicle vehicle) {
      return vehicle == null ? null : VehicleData.getSpeed(VehicleData.DataSpeed.MAXSPEED, vehicle.getLicensePlate());
   }

   public Class<? extends Double> getReturnType() {
      return Double.class;
   }

   public @Nullable Class<?> @NotNull [] acceptChange(Changer.ChangeMode mode) {
      return mode != ChangeMode.SET && mode != ChangeMode.ADD && mode != ChangeMode.REMOVE ? null : new Class[]{Double.class, Number.class};
   }

   public void change(@NotNull Event event, @Nullable Object @NotNull [] delta, Changer.@NotNull ChangeMode changeMode) {
      Vehicle vehicle = (Vehicle)this.getExpr().getSingle(event);
      if (Main.isNotNull(delta, delta[0], vehicle)) {
         String licensePlate = vehicle.getLicensePlate();
         double changeValue = ((Number)delta[0]).doubleValue();
         switch (changeMode) {
            case SET:
               VehicleData.setSpeed(VehicleData.DataSpeed.MAXSPEED, licensePlate, changeValue);
               break;
            case ADD:
               VehicleData.setSpeed(VehicleData.DataSpeed.MAXSPEED, licensePlate, VehicleData.getSpeed(VehicleData.DataSpeed.MAXSPEED, licensePlate) + changeValue);
               break;
            case REMOVE:
               VehicleData.setSpeed(VehicleData.DataSpeed.MAXSPEED, licensePlate, Math.max((double)0.0F, VehicleData.getSpeed(VehicleData.DataSpeed.MAXSPEED, licensePlate) - changeValue));
         }

      }
   }

   static {
      register(ExprMaxSpeed.class, Double.class, "[mtv] vehicle (max|maximum) speed", "mtvehicles");
   }
}
