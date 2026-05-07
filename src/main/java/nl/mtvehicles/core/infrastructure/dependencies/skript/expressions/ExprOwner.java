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
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("MTV Vehicle's owner")
@Description({"Get/Set the vehicle's owner (as OfflinePlayer)"})
@Examples({"set {_owner} to {_car}'s vehicle owner", "set {_owner} to vehicle owner of (mtv vehicle with license plate \"DF-4J-2R\")"})
@Since({"2.5.5"})
public class ExprOwner extends SimplePropertyExpression<Vehicle, OfflinePlayer> {
   protected String getPropertyName() {
      return "[mtv] vehicle owner";
   }

   public Class<? extends OfflinePlayer> getReturnType() {
      return OfflinePlayer.class;
   }

   public @Nullable OfflinePlayer convert(Vehicle vehicle) {
      return vehicle == null ? null : Bukkit.getOfflinePlayer(vehicle.getOwnerUUID());
   }

   public @Nullable Class<?> @NotNull [] acceptChange(Changer.ChangeMode mode) {
      return mode == ChangeMode.SET ? new Class[]{OfflinePlayer.class} : null;
   }

   public void change(@NotNull Event event, @Nullable Object @NotNull [] delta, Changer.@NotNull ChangeMode changeMode) {
      if (changeMode == ChangeMode.SET) {
         Vehicle vehicle = (Vehicle)this.getExpr().getSingle(event);
         if (Main.isNotNull(delta, delta[0], vehicle)) {
            if (delta[0] instanceof OfflinePlayer) {
               OfflinePlayer newOwner = (OfflinePlayer)delta[0];
               vehicle.setOwner(newOwner.getUniqueId());
               vehicle.save();
            }
         }
      }
   }

   static {
      register(ExprOwner.class, OfflinePlayer.class, "[mtv] vehicle owner", "mtvehicles");
   }
}
