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

@Name("MTV Vehicle's vehicle rotation speed")
@Description({"Get the vehicle's vehicle rotation speed"})
@Examples({"set {_licensePlate} to {_car}'s vehicle rotation speed", "add 1 to vehicle rotation speed of (player's driven mtv vehicle)", "set mtv vehicle rotation speed of {_helicopter} to 3"})
@Since({"2.5.6"})
public class ExprRotationSpeed extends SimplePropertyExpression<Vehicle, Integer> {
   protected String getPropertyName() {
      return "[mtv] vehicle rotation speed";
   }

   public @Nullable Integer convert(Vehicle vehicle) {
      return vehicle == null ? null : VehicleData.getRotationSpeed(vehicle.getLicensePlate());
   }

   public Class<? extends Integer> getReturnType() {
      return Integer.class;
   }

   public @Nullable Class<?> @NotNull [] acceptChange(Changer.ChangeMode mode) {
      return mode != ChangeMode.SET && mode != ChangeMode.ADD && mode != ChangeMode.REMOVE ? null : new Class[]{Integer.class, Number.class};
   }

   public void change(@NotNull Event event, @Nullable Object @NotNull [] delta, Changer.@NotNull ChangeMode changeMode) {
      Vehicle vehicle = (Vehicle)this.getExpr().getSingle(event);
      if (Main.isNotNull(delta, delta[0], vehicle)) {
         String licensePlate = vehicle.getLicensePlate();
         int changeValue = ((Number)delta[0]).intValue();
         switch (changeMode) {
            case SET:
               VehicleData.setRotationSpeed(licensePlate, changeValue);
               break;
            case ADD:
               VehicleData.setRotationSpeed(licensePlate, VehicleData.getRotationSpeed(licensePlate) + changeValue);
               break;
            case REMOVE:
               VehicleData.setRotationSpeed(licensePlate, Math.max(0, VehicleData.getRotationSpeed(licensePlate) - changeValue));
         }

      }
   }

   static {
      register(ExprRotationSpeed.class, Integer.class, "[mtv] vehicle rotation speed", "mtvehicles");
   }
}
