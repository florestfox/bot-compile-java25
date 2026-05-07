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

@Name("MTV Vehicle's type")
@Description({"Get the vehicle's type as String"})
@Examples({"set {_type} to {_car}'s vehicle type", "set {_type} to vehicle type of (player's driven mtv vehicle)"})
public class ExprVehicleType extends SimpleExpression<String> {
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
      return "MTVehicles vehicle type";
   }

   protected String[] get(Event event) {
      return this.vehicle.getSingle(event) == null ? null : new String[]{((Vehicle)this.vehicle.getSingle(event)).getVehicleType().toString()};
   }

   static {
      Skript.registerExpression(ExprVehicleType.class, String.class, ExpressionType.PROPERTY, new String[]{"%mtvehicle%'s [mtv] vehicle type", "[mtv] vehicle type of %mtvehicle%"});
   }
}
