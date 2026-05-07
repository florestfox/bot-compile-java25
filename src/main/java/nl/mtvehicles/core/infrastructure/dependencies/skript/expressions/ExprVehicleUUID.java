package nl.mtvehicles.core.infrastructure.dependencies.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("MTV Vehicle's UUID")
@Description({"Get the vehicle's UUID"})
@Examples({"set {_uuid} to {_car}'s vehicle uuid", "set {_uuid} to vehicle UUID of (player's driven mtv vehicle)"})
public class ExprVehicleUUID extends SimpleExpression<String> {
   private Expression<Vehicle> vehicle;

   public Class<? extends String> getReturnType() {
      return String.class;
   }

   public boolean isSingle() {
      return true;
   }

   public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
      this.vehicle = expressions[0];
      return true;
   }

   public String toString(@Nullable Event event, boolean debug) {
      return "MTVehicles vehicle UUID";
   }

   protected String[] get(Event event) {
      return this.vehicle.getSingle(event) == null ? null : new String[]{((Vehicle)this.vehicle.getSingle(event)).getUUID()};
   }

   static {
      Skript.registerExpression(ExprVehicleUUID.class, String.class, ExpressionType.PROPERTY, new String[]{"%mtvehicle%'s [mtv] vehicle (UUID|uuid)", "[mtv] vehicle (UUID|uuid) of %mtvehicle%"});
   }
}
