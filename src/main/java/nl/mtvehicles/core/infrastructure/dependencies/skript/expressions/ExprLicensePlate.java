package nl.mtvehicles.core.infrastructure.dependencies.skript.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("MTV Vehicle's license plate")
@Description({"Get the vehicle's license plate"})
@Examples({"set {_licensePlate} to {_car}'s vehicle license plate", "set {_licensePlate} to vehicle license plate of (player's driven mtv vehicle)", "set {_car}'s vehicle license plate to \"RW-2K-7I\""})
public class ExprLicensePlate extends SimplePropertyExpression<Vehicle, String> {
   protected String getPropertyName() {
      return "[mtv] vehicle license plate";
   }

   public Class<? extends String> getReturnType() {
      return String.class;
   }

   public @Nullable String convert(Vehicle vehicle) {
      return vehicle == null ? null : vehicle.getLicensePlate();
   }

   public @Nullable Class<?> @NotNull [] acceptChange(Changer.ChangeMode mode) {
      return mode == ChangeMode.SET ? new Class[]{String.class} : null;
   }

   public void change(@NotNull Event event, @Nullable Object @NotNull [] delta, Changer.@NotNull ChangeMode changeMode) {
      if (changeMode == ChangeMode.SET) {
         Vehicle vehicle = (Vehicle)this.getExpr().getSingle(event);
         if (Main.isNotNull(delta, delta[0], vehicle)) {
            String newLicense = delta[0].toString();
            vehicle.setLicensePlate(newLicense);
            vehicle.save();
         }
      }
   }

   static {
      register(ExprLicensePlate.class, String.class, "[mtv] vehicle license [plate]", "mtvehicles");
   }
}
